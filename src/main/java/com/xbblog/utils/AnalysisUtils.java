package com.xbblog.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbblog.business.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
            ShadowsocksNode node = new ShadowsocksNode(host, Integer.parseInt(port), remark, security, password);
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
            ShadowsocksNode shadowsocksNode = new ShadowsocksNode(host, port, remark, encryption, password);
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
                V2rayNodeDetail node = new V2rayNodeDetail(host, port, remark, uuid,alterId,network, camouflageType, camouflageHost, camouflagePath, camouflageTls);
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
                ShadowsocksRNode shadowsocksRNode = new ShadowsocksRNode(ip, port,remarks, security, password,proto,protoparam, obf, obfsparam );
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