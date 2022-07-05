package cn.king.controller;


import cn.king.common.R;
import cn.king.dto.DishDto;
import cn.king.dto.SetmealDto;
import cn.king.entity.Employee;
import cn.king.entity.Setmeal;
import cn.king.entity.SetmealDish;
import cn.king.service.CategoryService;
import cn.king.service.SetMealDishService;
import cn.king.service.SetMealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/setmeal")

public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //1.构造分页构造器
        Page<Setmeal> pageInfo = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page(page, pageSize);

        //2.构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(null != name, Setmeal::getName, name);
        //3.添加排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //4.执行查询
        setMealService.page(pageInfo, queryWrapper);
        //拷贝对象
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> setmeals = pageInfo.getRecords();
        ArrayList<SetmealDto> records = new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            String categoryName = categoryService.getById(setmeal.getCategoryId()).getName();
            setmealDto.setCategoryName(categoryName);
            records.add(setmealDto);
        }
        setmealDtoPage.setRecords(records);

        return R.success(setmealDtoPage);
    }


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setMealService.saveWithDish(setmealDto);
        return R.success("新增成功");
    }

    @DeleteMapping
    public R<String> delete(Long[] ids) {
        setMealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(Long[] ids, @PathVariable Integer status) {
        setMealService.updateStatus(ids, status);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //1.从Redis中获取缓存数据
        String key = "dish_" + setmeal.getCategoryId() + "_" + setmeal.getStatus();
        List<Setmeal> setmealList = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        //2.如果redis中存在
        if (setmealList != null) {
            return R.success(setmealList);
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId()).eq(Setmeal::getStatus,1);
        List<Setmeal> list = setMealService.list(queryWrapper);
        //如果redis没有数据，则加入数据
        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
        return R.success(list);
    }


    @GetMapping("/{id}")
    public R<Setmeal> get(@PathVariable Long id) {
        Setmeal setmeal = setMealService.getById(id);
        return R.success(setmeal);
    }
}
