package com.xbblog.business.controller;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.dto.SubLog;
import com.xbblog.business.dto.TestActiveReqDto;
import com.xbblog.business.dto.User;
import com.xbblog.business.service.MonitorService;
import com.xbblog.business.service.NodeService;
import com.xbblog.business.service.UserService;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.DateUtils;
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
import java.util.Date;
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
        logger.info("用户QQ：{} 获取订阅", user.getName());
        SubLog subLog = new SubLog(ip, platform, user.getId());
        response.setContentType("text/plain;charset=UTF-8");
        switch (platform)
        {
            case "v2rayNg":
                String v2rayNgStr = nodeService.getV2rayNgSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(v2rayNgStr);
                break;
            case "shadowrocket":
                String shadowrocketStr = nodeService.getShadowrocketSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(shadowrocketStr);
                break;
            case "quantumult":
                String quantumultStr = nodeService.getQuantumultSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(quantumultStr);
                break;
            case "quantumultx":
                response.setHeader("content-disposition", "attachment;filename=" + DateUtils.dateFormat(new Date(), DateUtils.DATE_FORMAT) + ".txt");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream");
                String quantumultXStr = nodeService.getQuantumultXSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(quantumultXStr);
                break;
            case "shadowsocksR":
                String shadowsocksRStr = nodeService.getShadowsocksRSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(shadowsocksRStr);
                break;
            case "clash":
                response.setHeader("content-disposition", "attachment;filename=" + DateUtils.dateFormat(new Date(), DateUtils.DATE_FORMAT) + ".yaml");
                response.setCharacterEncoding("UTF-8");
                userService.saveSubLog(subLog);
                nodeService.getClashSubscribe(response.getWriter(), isp, user.getGroup());
                break;
            case "potatso":
                String potatsoStr = nodeService.getShadowsocksRSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(potatsoStr);
                break;
            case "pharosPro":
                String pharosProString = nodeService.getPharosProSubscribe(isp, user.getGroup());
                userService.saveSubLog(subLog);
                response.getWriter().println(pharosProString);
                break;
            default:{
                response.sendError(404);
                return;
            }
        }
    }


    @RequestMapping("testActive")
    @ResponseBody
    public Map<String, Object> testActive(TestActiveReqDto reqDto) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        monitorService.testActiveActive(reqDto);
        map.put("success", true);
        return map;
    }

    @RequestMapping("testSpeed")
    @ResponseBody
    public Map<String, Object> testSpeed(TestActiveReqDto reqDto) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        monitorService.testActiveSpeed(reqDto);
        map.put("success", true);
        return map;
    }

    @RequestMapping("refreshLink")
    @ResponseBody
    public Map<String, Object> refreshLink() throws Exception
    {
        logger.info("开始更新订阅");
        List<NodeBo> list = nodeService.getAllssLink();
        nodeService.insertAll(list);
        logger.info("更新订阅完成");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        return map;
    }
}