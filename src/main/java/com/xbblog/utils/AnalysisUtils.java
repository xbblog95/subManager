package com.xbblog.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbblog.base.utils.YamlProPertyUtils;
import com.xbblog.business.dto.*;
import com.xbblog.business.dto.clash.ClashConfigDto;
import com.xbblog.business.dto.clash.ClashNodeConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AnalysisUtils {


    private static String COMM_OBFSPARAM = "www.download.windowsupdate.com";

    public static NodeDetail analysisShadowsocks(String ssStr) {
        if(ssStr == null || "".equals(ssStr))
        {
            return null;
        }
        try
        {
            String ss = ssStr.substring(ssStr.indexOf("//") + 2);
            String decode = URLDecoder.decode(ss, "UTF-8");
            String ssRegex = "(.*)@(.*):([0-9]*)#(.*)";
            List<List<String>> lists = RegexUtils.patternFindStr(ssRegex, decode);
            String remark = RegexUtils.getListItemValue(lists, 0, 3);
            String host =  RegexUtils.getListItemValue(lists, 0, 1);
            String port = RegexUtils.getListItemValue(lists, 0, 2);
            String nodeInfoBase64 = RegexUtils.getListItemValue(lists, 0, 0);
            String nodeInfo = Base64Util.decode(nodeInfoBase64);
            String nodeInfoRegex = "(.*):(.*)";
            List<List<String>> nodeInfoList = RegexUtils.patternFindStr(nodeInfoRegex, nodeInfo);
            String security =  RegexUtils.getListItemValue(nodeInfoList, 0, 0);
            String password = RegexUtils.getListItemValue(nodeInfoList, 0, 1);
            ShadowsocksNode node = new ShadowsocksNode(host, Integer.parseInt(port), remark, security, password, 1);
            return node;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public static List<NodeDetail> analysisShadowsocksD(String ssdStr) throws IOException {
        List<NodeDetail> nodeList = new ArrayList<NodeDetail>();
        if(ssdStr == null || "".equals(ssdStr))
        {
            return nodeList;
        }
        String ssd = ssdStr.substring(ssdStr.indexOf("//") + 2);
        String origin = Base64Util.decode(ssd);
        JSONObject object = null;
        try
        {
            object = JSONObject.parseObject(origin);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return nodeList;
        }
        int port = object.getInteger("port");
        String encryption = object.getString("encryption");
        String password = object.getString("password");
        JSONArray array = object.getJSONArray("servers");
        for(int i = 0; i < array.size(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            String host = obj.getString("server");
            String remark = obj.getString("remarks");
            ShadowsocksNode shadowsocksNode = new ShadowsocksNode(host, port, remark, encryption, password, 1);
            nodeList.add(shadowsocksNode);
        }
        return nodeList;
    }


    public static NodeDetail analysisVmess(String vmess)  {
        if(vmess == null || "".equals(vmess))
        {
            return null;
        }
        if(vmess.startsWith("vmess"))
        {
            try
            {
                String vmessStr = vmess.substring(vmess.indexOf("//") + 2);
                JSONObject obj = JSONObject.parseObject(Base64Util.decode(vmessStr));
                String host = obj.getString("add");
                int port = obj.getInteger("port");
                String uuid = obj.getString("id");
                int alterId = obj.getInteger("aid");
                String network = obj.getString("net");
                String camouflageType = obj.getString("type");
                String camouflageHost = obj.getString("host");
                String camouflagePath = obj.getString("path");
                String camouflageTls = obj.getString("tls");
                String remark  = obj.getString("ps");
                V2rayNodeDetail node = new V2rayNodeDetail(host, port, remark, uuid,alterId,network, camouflageType, camouflageHost, camouflagePath, camouflageTls, 1);
                return node;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        //todo 是否ss
        else
        {
            return null;
        }
    }

    public static List<NodeDetail> analysisV2raySubscribe(String text) {
        List<NodeDetail> nodeList = new ArrayList<NodeDetail>();
        if(text == null || "".equals(text))
        {
            return nodeList;
        }
        String v2rayNode = Base64Util.decode(text);
        String[] arr = v2rayNode.split("\n");
        for(String str : arr)
        {
            if(str.startsWith("vmess"))
            {
                NodeDetail node = analysisVmess(str);
                if(node != null)
                {
                    nodeList.add(node);
                }
            }
            else if(str.startsWith("ss"))
            {
                NodeDetail node = analysisShadowsocks(str);
                if(node != null)
                {
                    nodeList.add(node);
                }
            }

        }
        return nodeList;
    }

    public static List<NodeDetail> analysisSSRSubscribe(String text) {
        List<NodeDetail> nodeList = new ArrayList<NodeDetail>();
        if(text == null || "".equals(text))
        {
            return nodeList;
        }
        String ssrNodes = Base64Util.decode(text);
        String[] arr = ssrNodes.split("\n");
        for(String ssr : arr)
        {
            NodeDetail node = analysisShadowsocksR(ssr);
            if(node != null)
            {
                nodeList.add(node);
            }
        }
        return nodeList;
    }

    public static List<NodeDetail> analysisClashSubscribe(String text) {
        List<NodeDetail> nodeList = new ArrayList<NodeDetail>();
        if(text == null || "".equals(text))
        {
            return nodeList;
        }
        Constructor constructor = new Constructor(ClashConfigDto.class);
        constructor.setPropertyUtils(new YamlProPertyUtils());
        Yaml yaml = new Yaml(constructor);
        ClashConfigDto clashConfigDto = yaml.loadAs(text, ClashConfigDto.class);
        List<ClashNodeConfigDto> proxies = clashConfigDto.getProxies();
        for(ClashNodeConfigDto clashNodeConfigDto : proxies)
        {
            NodeDetail node = analysisClash(clashNodeConfigDto);
            nodeList.add(node);
        }
        return nodeList;
    }

    private static NodeDetail analysisClash(ClashNodeConfigDto clashNodeConfigDto) {
        String type = clashNodeConfigDto.getType();
        switch (type)
        {
            case "ss":
            {
                if(StringUtils.isEmpty(clashNodeConfigDto.getPlugin()))
                {
                    return new ShadowsocksNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                            clashNodeConfigDto.getCipher(), clashNodeConfigDto.getPassword(), clashNodeConfigDto.getUdp() ? 1 : 0);
                }
                if("obfs".equals(clashNodeConfigDto.getPlugin()))
                {
                    if("tls".equals(clashNodeConfigDto.getPluginOpts().getMode()))
                    {
                        return new ShadowsocksRNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                                clashNodeConfigDto.getCipher(), clashNodeConfigDto.getPassword(), "origin", "",
                                "tls1.2_ticket_auth", StringUtils.isEmpty(clashNodeConfigDto.getPluginOpts().getHost()) ? "" : clashNodeConfigDto.getPluginOpts().getHost(),
                                clashNodeConfigDto.getUdp() ? 1 : 0);
                    }
                    else if("http".equals(clashNodeConfigDto.getPluginOpts().getMode()))
                    {
                        return new ShadowsocksRNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                                clashNodeConfigDto.getCipher(), clashNodeConfigDto.getPassword(), "origin", "",
                                "http_simple", StringUtils.isEmpty(clashNodeConfigDto.getPluginOpts().getHost()) ? "" : clashNodeConfigDto.getPluginOpts().getHost(),
                                clashNodeConfigDto.getUdp() ? 1 : 0);
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            case "vmess":
            {
                if(StringUtils.isEmpty(clashNodeConfigDto.getNetwork()))
                {
                    return new V2rayNodeDetail(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),clashNodeConfigDto.getUuid(),
                            clashNodeConfigDto.getAlterId(), "tcp", "", "",
                            clashNodeConfigDto.getHttpOpts() == null ? "" :
                                    CollectionUtils.isEmpty(clashNodeConfigDto.getHttpOpts().getPath()) ? "" : clashNodeConfigDto.getHttpOpts().getPath().get(0), "",
                            clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
                }
                switch (clashNodeConfigDto.getNetwork())
                {
                    case "ws":
                    {
                        if(clashNodeConfigDto.getWsOpt() != null)//新版本
                        {
                            return new V2rayNodeDetail(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),clashNodeConfigDto.getUuid(),
                                    clashNodeConfigDto.getAlterId(), "ws", "none", clashNodeConfigDto.getWsOpt().getHeaders() == null ? "" : clashNodeConfigDto.getWsOpt().getHeaders().getHost(),
                                    clashNodeConfigDto.getWsOpt().getPath(), clashNodeConfigDto.getTls() == null ? "" : clashNodeConfigDto.getTls() ? "tls": "",
                                    clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
                        }
                        else
                        {
                            return new V2rayNodeDetail(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),clashNodeConfigDto.getUuid(),
                                    clashNodeConfigDto.getAlterId(), "ws", "none", clashNodeConfigDto.getWsHeaders() == null ? "" : clashNodeConfigDto.getWsHeaders().getHost(),
                                    clashNodeConfigDto.getWsPath(), clashNodeConfigDto.getTls() == null ? "" : clashNodeConfigDto.getTls() ? "tls": "",
                                    clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
                        }
                    }
                    case "h2":
                    {
                        return new V2rayNodeDetail(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),clashNodeConfigDto.getUuid(),
                                clashNodeConfigDto.getAlterId(), "h2", "none", clashNodeConfigDto.getH2Opts().getHost().get(0),
                                clashNodeConfigDto.getH2Opts() == null ? "" : clashNodeConfigDto.getH2Opts().getPath(), clashNodeConfigDto.getTls() ? "tls": "",
                                clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
                    }
                    case "http":
                    {
                        return new V2rayNodeDetail(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),clashNodeConfigDto.getUuid(),
                                clashNodeConfigDto.getAlterId(), "tcp", "http", "",
                                clashNodeConfigDto.getHttpOpts() == null ? "" :
                                        CollectionUtils.isEmpty(clashNodeConfigDto.getHttpOpts().getPath()) ? "" : clashNodeConfigDto.getHttpOpts().getPath().get(0), "",
                                clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
                    }
                    default:
                    {
                        return null;
                    }
                }
            }
            case "ssr":
            {
                return new ShadowsocksRNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                        clashNodeConfigDto.getCipher(), clashNodeConfigDto.getPassword(), clashNodeConfigDto.getObfs(), clashNodeConfigDto.getProtocolParam(),
                        clashNodeConfigDto.getObfs(), clashNodeConfigDto.getObfsParam(), clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
            }
            case "snell":
            {
                return new SnellNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                        clashNodeConfigDto.getObfsOpts().getMode(), clashNodeConfigDto.getVersion(), clashNodeConfigDto.getObfsOpts().getHost(),
                        clashNodeConfigDto.getPsk(),clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
            }
            case "trojan":
            {
                return new TrojanNode(clashNodeConfigDto.getServer(), clashNodeConfigDto.getPort(), clashNodeConfigDto.getName(),
                        clashNodeConfigDto.getPassword(), clashNodeConfigDto.getSni(),
                        CollectionUtils.isEmpty(clashNodeConfigDto.getAlpn()) ? "" : clashNodeConfigDto.getAlpn().get(0),
                        clashNodeConfigDto.getUdp() == null ? 0 : clashNodeConfigDto.getUdp() ? 1 : 0);
            }
        }
        return null;
    }

    public static ShadowsocksRNode analysisShadowsocksR(String ssr) {
        if(ssr == null || "".equals(ssr))
        {
            return null;
        }
        if(ssr.startsWith("ssr"))
        {
            try
            {
                String ssStr = ssr.substring(ssr.indexOf("//") + 2);
                String origin = Base64Util.decode(ssStr);
                //以/?分割，前面是基础信息
                String[] arr = origin.split("/\\?");
                //解析基础信息
//                所有信息以":"分割
                String base = arr[0];
                String obfs = arr[1];
                String[] basearr = base.split(":");
                String ip = basearr[0];
                int port = Integer.parseInt(basearr[1]);
                String proto = basearr[2];
                String security = basearr[3];
                String obf = basearr[4];
                String password = Base64Util.decode(basearr[5]);
                //解析混淆部分
                String[] obfsArr = obfs.split("&");
                String obfsparam = Base64Util.decode(obfsArr[0].substring(obfsArr[0].indexOf("=") + 1));
                String protoparam = Base64Util.decode(obfsArr[1].substring(obfsArr[1].indexOf("=") + 1));
                String remarks = Base64Util.decode(obfsArr[2].substring(obfsArr[2].indexOf("=") + 1));
//                if("http_simple".equals(obf))
//                {
//                    obfsparam = COMM_OBFSPARAM;
//                }
                ShadowsocksRNode shadowsocksRNode = new ShadowsocksRNode(ip, port,remarks, security, password,proto,protoparam, obf, obfsparam, 1);
                return shadowsocksRNode;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}