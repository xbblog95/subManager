package com.xbblog.business.controller;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.dto.SubLog;
import com.xbblog.business.dto.User;
import com.xbblog.business.service.MonitorService;
import com.xbblog.business.service.NodeService;
import com.xbblog.business.service.UserService;
import com.xbblog.config.NormalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.xbblog.utils.StringUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("link")
public class LinkController {

    private static Logger logger = LoggerFactory.getLogger(LinkController.class);
    @Autowired
    private UserService userService;


    @Autowired
    private NodeService nodeService;

    @Autowired
    private MonitorService monitorService;


    @RequestMapping("{token}/{platform}/getLink")
    public void getLink(@PathVariable("token") String token,@PathVariable("platform") String platform,HttpServletRequest request, HttpServletResponse response) throws IOException {
        getLinkISP(token, platform, null, request, response);
    }

    @RequestMapping("{token}/{platform}/getLink/{isp}")
    public void getLinkISP(@PathVariable("token") String token,@PathVariable("platform") String platform, @PathVariable("isp")String isp,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (token == null || token.equals("")) {
            response.sendError(403);
            return;
        }

        User user = this.userService.getUserByToken(token);
        if (user == null) {
            response.sendError(403);
            return;
        }
        if(StringUtil.isEmpty(platform))
        {
            platform = "v2rayNg";
        }
        String ip = StringUtil.isEmpty(request.getHeader("X-real-ip")) ? request.getRemoteAddr() : request.getHeader("X-real-ip");
        logger.info(String.format("用户QQ：%s获取订阅", user.getName()));
        SubLog subLog = new SubLog(ip, platform, user.getId());
        switch (platform)
        {
            case "v2rayNg":
                String v2rayNgStr = nodeService.getV2rayNgSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(v2rayNgStr);
                break;
            case "shadowrocket":
                String shadowrocketStr = nodeService.getShadowrocketSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(shadowrocketStr);
                break;
            case "quantumult":
                String quantumultStr = nodeService.getQuantumultSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(quantumultStr);
                break;
            case "shadowsocksR":
                String shadowsocksRStr = nodeService.getShadowsocksRSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(shadowsocksRStr);
                break;
            case "clash":
                response.setHeader("content-disposition", "attachment;filename=" + NormalConfiguration.webGroup +".yml");
                response.setCharacterEncoding("UTF-8");
                userService.saveSubLog(subLog);
                nodeService.getClashSubscribe(response.getOutputStream(), isp);
                break;
            case "potatso":
                String potatsoStr = nodeService.getShadowsocksRSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(potatsoStr);
                break;
            case "pharosPro":
                String pharosProString = nodeService.getPharosProSubscribe(isp);
                userService.saveSubLog(subLog);
                response.getWriter().println(pharosProString);
                break;
            default:{
                response.sendError(404);
                return;
            }
        }
    }



    @RequestMapping("refreshList")
    @ResponseBody
    public Map<String, Object> refreshList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<NodeBo> list = nodeService.getAllssLink();
        nodeService.insertAll(list);
        map.put("success", true);
        return map;
    }

    @RequestMapping("testActive")
    @ResponseBody
    public Map<String, Object> testActive() throws Exception
    {
        List<NodeBo> list = nodeService.getAllssLink();
        nodeService.insertAll(list);
        Map<String, Object> map = new HashMap<String, Object>();
        monitorService.testActive();
        map.put("success", true);
        return map;
    }
}