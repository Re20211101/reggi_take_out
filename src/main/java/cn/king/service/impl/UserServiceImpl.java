package cn.king.service.impl;

import cn.king.dao.UserDao;
import cn.king.entity.User;
import cn.king.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User>implements UserService {
}
