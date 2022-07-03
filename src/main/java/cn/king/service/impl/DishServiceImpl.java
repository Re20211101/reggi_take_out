package cn.king.service.impl;

import cn.king.common.CustomException;
import cn.king.dao.DishDao;
import cn.king.dto.DishDto;
import cn.king.entity.Category;
import cn.king.entity.Dish;
import cn.king.entity.DishFlavor;
import cn.king.entity.SetmealDish;
import cn.king.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到dish表
        this.save(dishDto);
        Long id = dishDto.getId();
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(id);
        }
        //保存菜品的口味信息到dishflavor表中
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getFlavorById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        //删除原先的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加口味
        Long id = dishDto.getId();
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(id);
        }
        //保存菜品的口味信息到dishflavor表中
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    @Transactional
    public void deleteWithFlavor(Long[] ids) {

        for (Long id : ids) {
            if (this.getById(id).getStatus()==0){
            this.removeById(id);
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);

            dishFlavorService.remove(queryWrapper);
        }else {
                 throw new CustomException("该菜品售卖中，无法删除");
            }
    }
    }

    @Override
    public void updateStatus(Long[] ids, int status) {
        for (Long id : ids) {
            Dish dish = this.getById(id);
            dish.setStatus(status);
            if (status == 0) {
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getDishId, id);
                Long setmealId = setMealDishService.getOne(queryWrapper).getSetmealId();
                setMealService.updateStatus(id, 0);
            }
            this.updateById(dish);

        }
    }

}


