package cn.king.service.impl;

import cn.king.dao.OrderDetailDao;
import cn.king.entity.OrderDetail;
import cn.king.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao,OrderDetail> implements OrderDetailService{
}
