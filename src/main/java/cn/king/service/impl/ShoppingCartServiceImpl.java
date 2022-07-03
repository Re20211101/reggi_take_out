package cn.king.service.impl;

import cn.king.dao.ShoppingCartDao;
import cn.king.entity.ShoppingCart;
import cn.king.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
}
