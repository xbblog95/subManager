package com.xbblog.business.service;

import com.xbblog.business.dto.User;
import com.xbblog.business.mapping.UserMapping;
import com.xbblog.utils.MD5;
import com.xbblog.utils.StringUtil;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapping userMapping;

//    @Autowired
    private MailService mailService;


//    @Autowired
    private CacheManager cacheManager;

    public User getUser(String name) {
        Map<String, Object> map  = new HashMap<String, Object>();
        map.put("name", name);
        return this.userMapping.getUser(map);
    }

    public User getUserByToken(String token) {
        return this.userMapping.getUserByToken(token);
    }

    public Map<String, Object> login(String name, String password) {
        Map<String, Object> map  = new HashMap<String, Object>();
        User user = getUser(name);
        if(user == null)
        {
            map.put("success", false);
            map.put("msg", "用户名密码错误");
            return map;
        }
        if(StringUtil.isEmpty(user.getPassword()))
        {
            if("123456".equals(password))
            {
                map.put("success", true);
                map.put("resetPass", true);
                return map;
            }
            map.put("success", false);
            map.put("msg", "用户名密码错误");
            return map;
        }
        if(user.getPassword().equals(MD5.MD5(password)))
        {
            map.put("success", true);
            return map;
        }
        map.put("success", false);
        map.put("msg", "用户名密码错误");
        return map;
    }

    public Map<String, Object> modifyPassword(User user, String oldPassword, String newPassword) {
        Map<String, Object> map  = new HashMap<String, Object>();
        user = getUser(user.getName());
        if(user == null)
        {
            map.put("success", false);
            map.put("msg", "用户名密码错误");
            return map;
        }
        //未设置密码的
        if(StringUtil.isEmpty(user.getPassword()))
        {
            //判断旧密码是否是123456
            if("123456".equals(oldPassword))
            {
                return modifyPassword(user.getName(), newPassword);
            }
            map.put("success", false);
            map.put("msg", "用户名密码错误");
            return map;
        }
        //设置过密码的
        if( user.getPassword().equals(MD5.MD5(oldPassword)))
        {
            return modifyPassword(user.getName(), newPassword);
        }
        map.put("success", false);
        map.put("msg", "用户名密码错误");
        return map;
    }


    private Map<String, Object>modifyPassword(String name, String password)
    {
        Map<String, Object> map  = new HashMap<String, Object>();
        Map<String, Object> paramMap  = new HashMap<String, Object>();
        paramMap.put("name", name);
        paramMap.put("password", MD5.MD5(password));
        userMapping.modifyPassword(paramMap);
        map.put("success", true);
        return map;
    }

    public Map<String, Object> resetToken(String name) {
        Map<String, Object> map  = new HashMap<String, Object>();
        String token = StringUtil.getRandomString(15);
        Map<String, Object> paramMap  = new HashMap<String, Object>();
        paramMap.put("name", name);
        paramMap.put("token", token);
        userMapping.modifyToken(paramMap);
        map.put("success", true);
        return map;
    }

    public Map<String, Object> forgetValidUser(String name, String basePath) {
        Map<String, Object> map  = new HashMap<String, Object>();
        User user = getUser(name);
        if(user == null)
        {
            map.put("success", false);
            map.put("msg", "该用户不存在");
            return map;
        }
        Map<String, Object> mailMap  = new HashMap<String, Object>();
        String code = StringUtil.getRandomString(6);
        mailMap.put("host", basePath);
        mailMap.put("code", code);
        mailMap.put("date", new Date());
        try {
//            mailService.sendOne(name + "@qq.com" ,"validateforget.ftl", mailMap, "密码重置");
            mailService.sendOne();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        cacheManager.getCache("forgetValidCode").put(name, code);
        map.put("success", true);
        map.put("email", name + "@qq.com");
        return map;
    }

    public Map<String, Object> resetPass(String code, String name) {
        Map<String, Object> map  = new HashMap<String, Object>();
        //判断code是否正确
        String cacheCode = cacheManager.getCache("forgetValidCode").get(name, String.class);
        if(StringUtil.isEmpty(cacheCode))
        {
            map.put("success", false);
            map.put("msg", "验证码无效，请重新获取");
            return map;
        }
        //将用户的密码字段置空
        if(cacheCode.toUpperCase().equals(code.toUpperCase()))
        {
            Map<String, Object> paramMap  = new HashMap<String, Object>();
            paramMap.put("name", name);
            paramMap.put("password", "");
            userMapping.modifyPassword(paramMap);
            map.put("success", true);
            return map;
        }
        map.put("success", false);
        map.put("msg", "验证码无效，请重新获取");
        return map;
    }
}