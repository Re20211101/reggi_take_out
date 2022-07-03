package cn.king.service.impl;

import cn.king.common.CustomException;
import cn.king.dao.SetMealDao;
import cn.king.dto.SetmealDto;
import cn.king.entity.Setmeal;
import cn.king.entity.SetmealDish;
import cn.king.service.SetMealDishService;
import cn.king.service.SetMealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealDao, Setmeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setMealDishService.save(setmealDish);
        }
    }

    @Override
    @Transactional
    public void deleteWithDish(Long[] ids) {
        for (Long id : ids) {
            if (this.getById(id).getStatus()==0){
                this.removeById(id);
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId,id);
                setMealDishService.remove(queryWrapper);
            }else {
              throw  new CustomException("套餐正在售卖，无法删除");
            }
        }
    }

    @Override
    public void updateStatus(Long[] ids, int status) {
        for (Long id : ids) {
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(status);
            this.updateById(setmeal);
        }

    }

    @Override
    public void updateStatus(Long ids, int status) {
        Setmeal setmeal = this.getById(ids);
        if (setmeal!=null){
            setmeal.setStatus(status);
            this.updateById(setmeal);
        }
    }
}
