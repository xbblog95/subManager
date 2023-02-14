package com.xbblog.business.controller;

import com.xbblog.business.dto.NodeStatusVo;
import com.xbblog.business.dto.User;
import com.xbblog.business.service.NodeService;
import com.xbblog.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("node")
public class NodeController extends BaseController{


    @Autowired
    private NodeService nodeService;

    @Autowired
    private UserService userService;
    @RequestMapping("toNodeList")
    public String toNodeList()
    {
        User user = getLoginUser();
        if(user == null)
        {
            return "/";
        }
        User newUser = userService.getUser(user.getName());
        setLoginUser(newUser);
        request.setAttribute("user", newUser);
        return "node/node";
    }
    @ResponseBody
    @RequestMapping("queryNodeStatus")
    public List<NodeStatusVo> queryNodeStatus()
    {
        return nodeService.queryNodeStatusPage();
    }
}
