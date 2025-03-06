package com.rpc.example.service;

import com.rpc.example.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author 周贵龙
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description TODO
 * @createTime 2022年04月23日 19:58:00
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        System.out.println("begin save user:" + name);
        return "save user success：" + name;
    }
}
