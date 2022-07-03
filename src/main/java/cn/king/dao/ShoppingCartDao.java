package cn.king.dao;

import cn.king.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
