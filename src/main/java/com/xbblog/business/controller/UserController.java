package com.xbblog.business.controller;

import com.xbblog.business.dto.User;
import com.xbblog.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController  {

    @Autowired
    private UserService userService;


    @PostMapping("login")
    @ResponseBody
    public Map<String, Object> login(String name, String password)
    {
        Map<String, Object> map = userService.login(name, password);
        if(Boolean.parseBoolean(String.valueOf(map.get("success"))))
        {
            setLoginUser(userService.getUser(name));
        }
        return map;
    }

    //修改密码
    @RequestMapping("modifyPassword")
    @ResponseBody
    public Map<String, Object> modifyPassword(String oldPassword, String newPassword)
    {
        Map<String, Object> map = new HashMap();
        User user = getLoginUser();
        if(user == null)
        {
            map.put("success", false);
            map.put("msg", "系统未登录");
            return map;
        }
        return userService.modifyPassword(user, oldPassword, newPassword);
    }

    //发送邮件忘记密码邮件确认身份
    @RequestMapping("forgetValidUser")
    @ResponseBody
    public Map<String, Object> forgetValidUser(String name)
    {
        String path = request.getContextPath();
        String basePath =  request.getScheme() + "://"+request.getServerName() + ":"  +  request.getServerPort()+path+"/";
        return userService.forgetValidUser(name, basePath);
    }

    //将用户密码置空，下次登录进来将自动重置密码
    @RequestMapping("resetPass")
    @ResponseBody
    public Map<String, Object> resetPass(String code, String name)
    {
        return userService.resetPass(code, name);
    }

    //重置用户令牌
    @RequestMapping("resetToken")
    @ResponseBody
    public Map<String, Object> resetToken()
    {
        Map<String, Object> map = new HashMap();
        User user = getLoginUser();
        if(user == null)
        {
            map.put("success", false);
            map.put("msg", "系统未登录");
            return map;
        }
        return userService.resetToken(user.getName());
    }

    @RequestMapping("resetPassForm")
    public String resetPassForm()
    {
        return "modPassForm/modPassForm";
    }

    @RequestMapping("toIndex")
    public String toIndex()
    {
        User user = getLoginUser();
        if(user == null)
        {
            return "/";
        }
        User newUser = userService.getUser(user.getName());
        setLoginUser(newUser);
        request.setAttribute("user", newUser);

        return "/index/index";
    }

    @RequestMapping("loginOut")
    public String loginOut()
    {
        setLoginUser(null);
        String path = request.getContextPath();
        String basePath =  request.getScheme() + "://"+request.getServerName() + ":"  +  request.getServerPort()+path+"/";
        return "redirect:" + basePath;
    }
}
