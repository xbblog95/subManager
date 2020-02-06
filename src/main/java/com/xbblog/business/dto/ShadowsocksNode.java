package com.xbblog.business.dto;

import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShadowsocksNode extends NodeDetail{

    //ss/ssr密码
    private String password;

    public ShadowsocksNode(String ip, int port, String remarks, String security, String password) {
        super(ip, port, remarks, security, "ss");
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String parseToV2rayNgString(ShadowsocksNode node)
    {

        if(node == null)
        {
            return "";
        }
        String templateBase = "${security}:${password}@${ip}:${port}";
        Map<String, String> nodeMap = new HashMap<String, String>();
        nodeMap.put("security", node.getSecurity());
        nodeMap.put("password", node.getPassword());
        nodeMap.put("ip", node.getIp());
        nodeMap.put("port", String.valueOf(node.getPort()));
        String ssStr = StringUtil.format(templateBase, nodeMap);
        try {
            String templateInfo = "ss://${base}#${remarks}";
            Map<String, String> templateMap = new HashMap<String, String>();
            templateMap.put("base", Base64Util.encodeURLSafe(ssStr));
            templateMap.put("remarks",URLEncoder.encode(node.getRemarks(), "UTF-8"));
            return StringUtil.format(templateInfo, templateMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String parseToShadowrocketString(ShadowsocksNode node)
    {
        return parseToShadowsocksRString(node);
    }

    public static String parseToQuantumultString(ShadowsocksNode node)
    {
        return parseToShadowsocksRString(node);
    }

    public static String parseToQuantumultXString(ShadowsocksNode node) {
        if(node == null)
        {
            return "";
        }
        String template = "shadowsocks=${ip}:${port}, method=${security}, password=${password}, fast-open=false, udp-relay=false, tag=${remarks}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("security", node.getSecurity());
        templateMap.put("password", node.getPassword());
        templateMap.put("remarks", node.getRemarks());
        templateMap.put("group", Base64Util.encodeURLSafe(NormalConfiguration.webGroup));
        String ssStr = StringUtil.format(template, templateMap);
        return ssStr;
    }
    public static String parseToShadowsocksRString(ShadowsocksNode node)
    {
        if(node == null)
        {
            return "";
        }
        String template = "${ip}:${port}:origin:${security}:plain:${password}/?obfsparam=&protoparam=&remarks=${remarks}&group=${group}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("security", node.getSecurity());
        templateMap.put("password", Base64Util.encodeURLSafe(node.getPassword()));
        templateMap.put("remarks", Base64Util.encodeURLSafe(node.getRemarks()));
        templateMap.put("group", Base64Util.encodeURLSafe(NormalConfiguration.webGroup));
        String ssStr = StringUtil.format(template, templateMap);
        return "ssr://" + Base64Util.encodeURLSafe(ssStr);
    }

    public static ShadowsocksNode toShadowsocksNode(NodeDto nodeDto)
    {
        if(nodeDto == null)
        {
            return null;
        }
        ShadowsocksNode shadowsocksNode = new ShadowsocksNode(nodeDto.getIp(), nodeDto.getPort(), nodeDto.getRemarks(), nodeDto.getSecurity(), nodeDto.getPassword());
        return shadowsocksNode;
    }

    public static List<ShadowsocksNode> toShadowsocksNodes(List<NodeDto> nodes)
    {
        List<ShadowsocksNode> list = new ArrayList<ShadowsocksNode>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return list;
        }
        for(NodeDto node : nodes)
        {
            list.add(toShadowsocksNode(node));
        }
        return list;
    }

    public static List<Map<String, String>> parseToClashMap(List<ShadowsocksNode> list)
    {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isEmpty(list))
        {
            return mapList;
        }
        for(ShadowsocksNode shadowsocksNode : list)
        {
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("ip", shadowsocksNode.getIp());
            tempMap.put("port", String.valueOf(shadowsocksNode.getPort()));
            tempMap.put("security", shadowsocksNode.getSecurity());
            tempMap.put("password",shadowsocksNode.getPassword());
            tempMap.put("remarks", shadowsocksNode.getRemarks());
            mapList.add(tempMap);
        }
        return mapList;
    }

}