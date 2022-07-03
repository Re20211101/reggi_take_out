package cn.king.controller;

import cn.king.common.R;
import cn.king.entity.Category;
import cn.king.entity.Employee;
import cn.king.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //1.构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //3.添加排序
        queryWrapper.orderByAsc(Category::getSort);
        //4.执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete( Long ids){
        //  categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public  R<String> update(@RequestBody Category category){

        categoryService.updateById(category);
        return R.success("编辑成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list( Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }
}
