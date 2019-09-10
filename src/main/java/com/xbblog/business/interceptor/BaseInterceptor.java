package com.xbblog.business.interceptor;

import com.xbblog.utils.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String path = request.getContextPath();
        String scheme = StringUtil.isEmpty(request.getHeader("X-Forwarded-Proto"))  ? request.getScheme() : request.getHeader("X-Forwarded-Proto");
        String port = StringUtil.isEmpty(request.getHeader("X-Forwarded-Port")) ? String.valueOf(request.getServerPort()) : request.getHeader("X-Forwarded-Port");
        String basePath = scheme
                + "://"+request.getServerName() + ":"
                + port
                + path + "/";
        request.setAttribute("BASE_PATH", basePath);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
