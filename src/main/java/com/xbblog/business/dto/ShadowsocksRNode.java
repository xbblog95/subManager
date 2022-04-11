package com.xbblog.business.dto;

import com.xbblog.business.dto.clash.ClashNodeConfigDto;
import com.xbblog.business.dto.clash.ClashNodePluginConfigDto;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShadowsocksRNode extends ShadowsocksNode {

    //混淆参数
    private String obfsparam;

    //协议参数
    private String protoparam;

    //协议
    private String protocol;

    //混淆
    private String obfs;

    public ShadowsocksRNode(String ip, int port, String remarks, String security, String password, String protocol, String protoparam, String obfs, String obfsparam, String group, int udp) {
        super(ip, port, remarks, security, password, group, udp);
        this.obfs = obfs;
        this.protocol = protocol;
        this.protoparam = protoparam;
        this.obfsparam = obfsparam;
        this.setType("ssr");
    }


    public ShadowsocksRNode(String ip, int port, String remarks, String security, String password, String protocol, String protoparam, String obfs, String obfsparam, int udp) {
        super(ip, port, remarks, security, password, udp);
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
                nodeDto.getObfsparam(), nodeDto.getGroup(), nodeDto.getUdp());
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
        templateMap.put("group",  Base64Util.encodeURLSafe(NormalConfiguration.webGroup));
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

    public static List<Map<String, String>> shadowsocksRNodeparseToClashMap(List<ShadowsocksRNode> list)
    {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isEmpty(list))
        {
            return mapList;
        }

        for(ShadowsocksRNode shadowsocksRNode : list)
        {
            //排除所有chacha20的加密
            if("chacha20".equals(shadowsocksRNode.getSecurity()))
            {
                continue;
            }
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("ip", shadowsocksRNode.getIp());
            tempMap.put("port", String.valueOf(shadowsocksRNode.getPort()));
            tempMap.put("security", shadowsocksRNode.getSecurity());
            tempMap.put("password",shadowsocksRNode.getPassword());
            tempMap.put("remarks", shadowsocksRNode.getRemarks());
            tempMap.put("protocol", shadowsocksRNode.getProtocol());
            tempMap.put("protocolParam", shadowsocksRNode.getProtoparam());
            tempMap.put("obfs", shadowsocksRNode.getObfs());
            tempMap.put("obfsParam", shadowsocksRNode.getObfsparam());
            mapList.add(tempMap);
        }
        return mapList;
    }

    public static ClashNodeConfigDto shadowsocksRNodeparseToClashNode(ShadowsocksRNode node)
    {
        //排除所有chacha20的加密
        if("chacha20".equals(node.getSecurity()))
        {
            return null;
        }
        ClashNodeConfigDto clashNodeConfigDto = new ClashNodeConfigDto();
        clashNodeConfigDto.setType("ssr");
        clashNodeConfigDto.setServer(node.getIp());
        clashNodeConfigDto.setPort(node.getPort());
        clashNodeConfigDto.setName(StringUtils.isEmpty(node.getRemarks()) ? "": node.getRemarks().replaceAll("'", "").replaceAll("\"", ""));
        if(node.getUdp() == 1)
        {
            clashNodeConfigDto.setUdp(true);
        }
        clashNodeConfigDto.setCipher(node.getSecurity());
        clashNodeConfigDto.setPassword(node.getPassword());
        if(StringUtils.isNotEmpty(node.getProtocol()) && "origin".equals(node.getProtocol()) && StringUtils.isEmpty(node.getProtoparam()))
        {
            clashNodeConfigDto.setPlugin("obfs");
            ClashNodePluginConfigDto clashNodePluginConfigDto = new ClashNodePluginConfigDto();
            switch (node.getObfs())
            {
                case "http_simple":
                {
                    clashNodePluginConfigDto.setMode("http");
                    clashNodePluginConfigDto.setHost(node.getObfsparam());
                    break;
                }
                case "tls1.2_ticket_auth":
                {
                    clashNodePluginConfigDto.setMode("tls");
                    clashNodePluginConfigDto.setHost(node.getObfsparam());
                    break;
                }
                default:
                {
                    break;
                }
            }
            clashNodeConfigDto.setPluginOpts(clashNodePluginConfigDto);
        }
        else
        {
            clashNodeConfigDto.setProtocol(node.getProtocol());
            clashNodeConfigDto.setProtocolParam(node.getProtoparam());
            clashNodeConfigDto.setObfs(node.getObfs());
            clashNodeConfigDto.setObfsParam(node.getObfsparam());
        }
        return clashNodeConfigDto;
    }
}
