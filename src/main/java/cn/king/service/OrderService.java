package cn.king.service;

import cn.king.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
