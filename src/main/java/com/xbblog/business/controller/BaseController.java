package com.xbblog.business.controller;

import com.xbblog.business.dto.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {


    @Autowired
    protected HttpServletRequest request;


    protected User getLoginUser()
    {
        return (User) request.getSession().getAttribute("user");
    }


    protected void setLoginUser(User user)
    {
        if(user == null)
        {
            request.getSession().removeAttribute("user");
        }
        request.getSession().setAttribute("user", user);
    }
}
