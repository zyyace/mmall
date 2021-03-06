package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController
{
    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param usernmae
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value ="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String usernmae, String password , HttpSession session){
        ServiceResponse<User> response = iUserService.login(usernmae,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
        // service-->mybatis->dao
    }

    @RequestMapping(value ="login.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }

    @RequestMapping(value ="register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> register(User user){

        return iUserService.register(user);
    }

    @RequestMapping(value ="check_valid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> checkValid (String str,String type){
        return iUserService.checkValid(str,type);
    }
}
