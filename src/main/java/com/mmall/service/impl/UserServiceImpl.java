package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.aspectj.lang.annotation.DeclareError;
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
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,password);
        if(user == null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang.StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功",user);
    }

    public ServiceResponse<String> register (User user){
        ServiceResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount > 0 ) {
//            return ServiceResponse.createByErrorMessage("用户名已存在");
//        }
//        resultCount = userMapper.checkUsername(user.getEmail());
//        if(resultCount > 0 ) {
//            return ServiceResponse.createByErrorMessage("email已存在");
//        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccessMessage("注册成功");
    }

    public ServiceResponse<String> checkValid (String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(type)){
            // 开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServiceResponse.createByErrorMessage("用户名已经存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(type);
                if(resultCount > 0){
                    return ServiceResponse.createByErrorMessage("email已经存在");
                }
            }
        }else{
            return ServiceResponse.createBySuccessMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
    }
}
