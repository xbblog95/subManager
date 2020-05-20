package com.xbblog.business.dto;

import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShadowsocksRNode extends ShadowsocksNode {

    //混淆参数
    private String obfsparam;

    //协议参数
    private String protoparam;

    //协议
    private String protocol;

    //混淆
    private String obfs;

    public ShadowsocksRNode(String ip, int port, String remarks, String security, String password, String protocol, String protoparam, String obfs, String obfsparam, String group) {
        super(ip, port, remarks, security, password, group);
        this.obfs = obfs;
        this.protocol = protocol;
        this.protoparam = protoparam;
        this.obfsparam = obfsparam;
        this.setType("ssr");
    }


    public ShadowsocksRNode(String ip, int port, String remarks, String security, String password, String protocol, String protoparam, String obfs, String obfsparam) {
        super(ip, port, remarks, security, password);
        this.obfs = obfs;
        this.protocol = protocol;
        this.protoparam = protoparam;
        this.obfsparam = obfsparam;
        this.setType("ssr");
    }

    public static List<ShadowsocksRNode> toShadowsocksRNodes(List<NodeDto> nodes) {
        List<ShadowsocksRNode> list = new ArrayList<ShadowsocksRNode>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return list;
        }
        for(NodeDto node : nodes)
        {
            list.add(toShadowsocksNodeR(node));
        }
        return list;
    }

    public static ShadowsocksRNode toShadowsocksNodeR(NodeDto nodeDto) {
        if(nodeDto == null)
        {
            return null;
        }
        ShadowsocksRNode shadowsocksrNode = new ShadowsocksRNode(nodeDto.getIp(), nodeDto.getPort(), nodeDto.getRemarks(),
                nodeDto.getSecurity(), nodeDto.getPassword(), nodeDto.getProtocol(), nodeDto.getProtoparam(), nodeDto.getObfs(),
                nodeDto.getObfsparam(), nodeDto.getGroup());
        return shadowsocksrNode;
    }

    public static String parseToShadowsocksRString(ShadowsocksRNode node)
    {
        if(node == null)
        {
            return "";
        }
        String template = "${ip}:${port}:${protocol}:${security}:${obfs}:${password}/?obfsparam=${obfsparam}&protoparam=${protoparam}&remarks=${remarks}&group=${group}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("security", node.getSecurity());
        templateMap.put("password", Base64Util.encode(node.getPassword()));
        templateMap.put("remarks", Base64Util.encodeURLSafe(node.getRemarks()));
        templateMap.put("protocol", node.getProtocol());
        templateMap.put("obfs", node.getObfs());
        templateMap.put("protoparam", Base64Util.encodeURLSafe(node.getProtoparam()));
        templateMap.put("obfsparam", Base64Util.encodeURLSafe(node.getObfsparam()));
        templateMap.put("group",  Base64Util.encodeURLSafe(node.getGroup()));
        String ssStr = StringUtil.format(template, templateMap);
        return "ssr://" + Base64Util.encodeURLSafe(ssStr);
    }

   public static List<Map<String,String>> parseShadowsocksRToClashMap(List<ShadowsocksRNode> list)
   {
       List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
       if(CollectionUtils.isEmpty(list))
       {
           return mapList;
       }
       for(ShadowsocksRNode shadowsocksRNode : list)
       {
           Map<String, String> tempMap = new HashMap<String, String>();
           tempMap.put("type","ssr");
           tempMap.put("ip", shadowsocksRNode.getIp());
           tempMap.put("protocol", shadowsocksRNode.getProtocol());
           tempMap.put("protocolparam", shadowsocksRNode.getProtoparam());
           tempMap.put("obfs", shadowsocksRNode.getObfs());
           tempMap.put("obfsparam", shadowsocksRNode.getObfsparam());
           tempMap.put("port", String.valueOf(shadowsocksRNode.getPort()));
           tempMap.put("security", shadowsocksRNode.getSecurity());
           tempMap.put("password",shadowsocksRNode.getPassword());
           tempMap.put("remarks", shadowsocksRNode.getRemarks());
           mapList.add(tempMap);
       }
       return mapList;
   }

    public static String parseToShadowrocketString(ShadowsocksRNode node)
    {
        return parseToShadowsocksRString(node);
    }

    public static String parseToQuantumultString(ShadowsocksRNode node)
    {
        return parseToShadowsocksRString(node);
    }

    public static String parseToQuantumultXString(ShadowsocksRNode node)
    {
        String template = "shadowsocks=${ip}:${port}, method=${security}, password=${password}, ssr-protocol=${protocol}," +
                " ssr-protocol-param=${protoparam}, obfs=${obfs}, obfs-host=${obfsparam}, tag=${remarks}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("security", node.getSecurity());
        templateMap.put("password", node.getPassword());
        templateMap.put("remarks", node.getRemarks());
        templateMap.put("protocol", node.getProtocol());
        templateMap.put("obfs", node.getObfs());
        templateMap.put("protoparam", node.getProtoparam());
        templateMap.put("obfsparam", node.getObfsparam());
        return StringUtil.format(template, templateMap);
    }

    public String getObfsparam() {
        return obfsparam;
    }

    public void setObfsparam(String obfsparam) {
        this.obfsparam = obfsparam;
    }

    public String getProtoparam() {
        return protoparam;
    }

    public void setProtoparam(String protoparam) {
        this.protoparam = protoparam;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getObfs() {
        return obfs;
    }

    public void setObfs(String obfs) {
        this.obfs = obfs;
    }
}
