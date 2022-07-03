package cn.king.service.impl;

import cn.king.common.CustomException;
import cn.king.dao.CategoryDao;
import cn.king.entity.Category;
import cn.king.entity.Dish;
import cn.king.entity.Setmeal;
import cn.king.service.CategoryService;
import cn.king.service.DishService;
import cn.king.service.SetMealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        long count1 = dishService.count(queryWrapper);
        if (count1>0){
            //抛出异常
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long count2 = setMealService.count(lambdaQueryWrapper);
        if (count2>0){
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }
        super.removeById(id);
    }
}
