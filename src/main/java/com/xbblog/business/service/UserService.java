package com.xbblog.business.service;

import com.github.hiwepy.ip2region.spring.boot.IP2regionTemplate;
import com.xbblog.base.service.EmailService;
import com.xbblog.business.dto.*;
import com.xbblog.business.mapping.UserMapping;
import com.xbblog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapping userMapping;

    @Autowired
    private EmailService emailService;


    @Autowired
    private CacheManager cacheManager;


    @Autowired
    private IP2regionTemplate ip2regionTemplate;

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
            emailService.sendOne(name + "@qq.com" ,"validateforget.ftl", mailMap, "密码重置");
        } catch (IOException e) {
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

    public void saveSubLog(SubLog subLog)
    {
        String country = "";
        String region = "";
        String province = "";
        String city = "";
        try
        {
            String ipdesc = ip2regionTemplate.binarySearch(subLog.getIp()).getRegion();
            String regex = "(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)";
            List<List<String>> lists = RegexUtils.patternFindStr(regex, ipdesc);
            country = RegexUtils.getListItemValue(lists, 0, 0);
            region = RegexUtils.getListItemValue(lists, 0, 1);
            province = RegexUtils.getListItemValue(lists, 0, 2);
            city = RegexUtils.getListItemValue(lists, 0, 3);
        }
        catch (Exception e)
        {
            e.fillInStackTrace();
            country = IPUtils.UNKOWN_ADDRESS;
            region = IPUtils.UNKOWN_ADDRESS;
            province = IPUtils.UNKOWN_ADDRESS;
            city = IPUtils.UNKOWN_ADDRESS;
        }
        subLog.setRegion(region);
        subLog.setCountry(country);
        subLog.setCity(city);
        subLog.setProvince(province);
        userMapping.saveSubLog(subLog);
        if(riskControl(subLog))
        {
            //        被风控的重置当前订阅信息
            GetUserByIdReqDto getUserByIdReqDto = new GetUserByIdReqDto();
            getUserByIdReqDto.setId(subLog.getUserId());
            User user = userMapping.getUserById(getUserByIdReqDto);
            resetToken(user.getName());
            // 插入风控行为表
            RiskAction riskAction = new RiskAction();
            riskAction.setTime(new Date());
            riskAction.setUserId(subLog.getUserId());
            userMapping.insertRiskAction(riskAction);
        }

    }

    /**
     * 风控
     * @param subLog
     * @return
     */
    private Boolean riskControl(SubLog subLog)  {
        //查询今日的城市
        QrySubLogReqDto qryParam = new QrySubLogReqDto();
        qryParam.setUserId(subLog.getUserId());
        try {
            qryParam.setBeginDate(DateUtils.getDateDate(new Date()));
            qryParam.setEndDate(DateUtils.getDateNextDate(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        List<SubLog> subLogs = userMapping.qrySubLog(qryParam);
        //如果有，把这些城市提取出来，并去掉当前订阅城市
        if(CollectionUtils.isEmpty(subLogs))
        {
            return false;
        }
        //判断出来今天排除传入城市还在哪些城市订阅过
        Map<String, String> citys = new HashMap<>();
        for(SubLog log : subLogs)
        {
            if(!subLog.equals(log.getCity()))
            {
                citys.put(log.getCity(), log.getCity());
            }
        }
        return citys.keySet().size() > 1;
    }


}