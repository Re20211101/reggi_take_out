package cn.king.service;

import cn.king.dto.SetmealDto;
import cn.king.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void deleteWithDish(Long[] ids);
    void updateStatus(Long[] ids,int status);
    void updateStatus(Long id,int status);
}
