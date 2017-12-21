package com.mmall.service.impl;

import com.mmall.common.ServiceResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

/**
 * Create by ACE on 2017/12/21
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount ==0 ){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        // tode 密码登录MD5

        User user = userMapper.selectLogin(username,password);
        if(user == null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang.StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功",user);
    }
}
