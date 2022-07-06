package cn.king.controller;

import cn.king.common.R;
import cn.king.entity.Employee;
import cn.king.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * @return 登录
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.对密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //2.根据页面提交的用户名查询数据
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到则返回失败结果
        if (null == emp) {
            return R.error("用户不存在！");
        }
        //4.比对用户密码
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //5.查看用户状态是否禁用
        if (emp.getStatus() == 0) {
            return R.error("用户已禁用");
        }
        //6.登录成功，将用户id放入Session中
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("成功退出");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());
        //1.设置员工初始密码123456，并加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //2.将未设置的信息补全
//        Long emp_id = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(emp_id);
//        employee.setUpdateUser(emp_id);
        //3.新增员工
        employeeService.save(employee);
        return R.success("新增成功！");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
       //1.构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(null!=name,Employee::getName,name);
        //3.添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //4.执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 一开始修改员工信息不成功，由于客户端传过来的json数据long型id精度丢失而造成与实际数据库id不匹配
     * 解决方法：在服务端将json数据进行处理（将long型数据统一转换为String类型）
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long emp_id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(emp_id);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (null!=employee){
            return R.success(employee);
        }else {
            return R.error("没有找到数据");

        }
    }
}
