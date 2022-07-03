package cn.king.controller;


import cn.king.common.R;
import cn.king.dto.DishDto;
import cn.king.entity.Category;
import cn.king.entity.Dish;
import cn.king.entity.DishFlavor;
import cn.king.service.CategoryService;
import cn.king.service.DishFlavorService;
import cn.king.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //1.构造分页构造器
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(null != name, Dish::getName, name);
        //3.添加排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //4.执行查询
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<DishDto> records = new ArrayList<>();
        for (Dish record : pageInfo.getRecords()) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);
            if (record.getCategoryId() != null) {
                String name1 = categoryService.getById(record.getCategoryId()).getName();
                dishDto.setCategoryName(name1);
            }
            records.add(dishDto);
        }
        dishDtoPage.setRecords(records);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getFlavorById(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(Long[] ids) {
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(Long[] ids, @PathVariable int status) {
        dishService.updateStatus(ids, status);
        return R.success("修改状态成功");
    }

    @GetMapping("/list")
/*    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getSort).orderByAsc(Dish::getPrice);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByDesc(Dish::getSort).orderByAsc(Dish::getPrice);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = new ArrayList<>();
        for (Dish dish1 : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            List<DishFlavor> flavors = dishService.getFlavorById(dishDto.getId()).getFlavors();
            dishDto.setFlavors(flavors);
            dtoList.add(dishDto);
        }
        return R.success(dtoList);
    }
}
