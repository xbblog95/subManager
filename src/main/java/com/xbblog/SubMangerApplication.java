package com.xbblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@EnableAutoConfiguration
@Controller
@ComponentScan({"com.xbblog.config"})
@EnableCaching
public class SubMangerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubMangerApplication.class, args);
    }


    @RequestMapping("/")
    public String index()
    {
        return "redirect:/user/toIndex";
    }

    @RequestMapping("/tologin")
    public String tologin()
    {
        return "login/login";
    }
}
