package com.xbblog.business.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CherryDream on 2016/11/14.
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        Object obj = request.getSession().getAttribute("user");
        if(obj == null)
        {
            log.info("当前用户未登录");
            String path = request.getContextPath();
            String basePath =  request.getScheme() + "://"+request.getServerName() + ":"  +  request.getServerPort()+path+"/";
            response.sendRedirect(basePath + "tologin");
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String path = request.getContextPath();
        String basePath =  request.getScheme() + "://"+request.getServerName() + ":"  +  request.getServerPort()+path+"/";
        request.setAttribute("BASE_PATH", basePath);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
