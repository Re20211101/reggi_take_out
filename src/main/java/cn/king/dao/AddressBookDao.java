package cn.king.dao;

import cn.king.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {
}
