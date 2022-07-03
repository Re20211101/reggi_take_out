package cn.king.service;

import cn.king.dto.DishDto;
import cn.king.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getFlavorById(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(Long[] ids);

    void updateStatus(Long[] ids,int status);
}
