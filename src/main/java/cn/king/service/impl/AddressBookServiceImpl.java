package cn.king.service.impl;

import cn.king.dao.AddressBookDao;
import cn.king.entity.AddressBook;
import cn.king.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao,AddressBook> implements AddressBookService {
}
