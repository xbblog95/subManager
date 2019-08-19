package com.xbblog.config;

import com.xbblog.business.interceptor.BaseInterceptor;
import com.xbblog.business.interceptor.LoginInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@ComponentScan({"com.xbblog.business", "com.xbblog.base"})
public class WebConfiguration extends WebMvcConfigurationSupport {



    public void addInterceptors(InterceptorRegistry registry) {
        //所有页面的拦截器
        HandlerInterceptor baseInterceptor = new BaseInterceptor();
        registry.addInterceptor(baseInterceptor).addPathPatterns("/**");
        HandlerInterceptor interceptor = new LoginInterceptor();
        //添加登录拦截器
        registry.addInterceptor(interceptor).excludePathPatterns("/user/login")
                .excludePathPatterns("/user/resetPass")
                .excludePathPatterns("/user/forgetValidUser")
                .excludePathPatterns("/link/**/getLink")
                .excludePathPatterns("/link/**/getLink.do")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/user/forgetValidUser")
                .excludePathPatterns("/user/resetPass")
                .excludePathPatterns("/*").addPathPatterns("/*/**");

    }

    /**
     * 静态文件过滤
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

}
