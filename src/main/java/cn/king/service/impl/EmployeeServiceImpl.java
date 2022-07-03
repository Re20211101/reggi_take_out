package cn.king.service.impl;

import cn.king.dao.EmployeeDao;
import cn.king.entity.Employee;
import cn.king.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee>implements EmployeeService {
}
