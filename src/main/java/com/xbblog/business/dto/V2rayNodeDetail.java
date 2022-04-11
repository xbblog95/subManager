package com.xbblog.business.dto;

import com.alibaba.fastjson.JSONObject;
import com.xbblog.business.dto.clash.ClashNodeConfigDto;
import com.xbblog.business.dto.clash.ClashNodeWsHeadersConfigDto;
import com.xbblog.business.dto.clash.ClashNodoWsConfigDto;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class V2rayNodeDetail extends NodeDetail {

    //v2ray uuid
    private String uuid;

    //v2ray 额外id
    private int alterId;

    //v2ray 网络传输
    private String network;

    //v2ray 伪装类型
    private String camouflageType;

    //v2ray 伪装域名
    private String camouflageHost;

    //v2ray 伪装路径
    private String camouflagePath;

    //v2ray 底层传输协议
    private String camouflageTls;

    public V2rayNodeDetail(String ip, int port, String remarks, String uuid, int alterId,
                           String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls, int udp) {
        super(ip, port, remarks, "auto", "v2ray", udp);
        this.uuid = uuid;
        this.alterId = alterId;
        this.network = network == null ? "tcp" : network;
        this.camouflageType = camouflageType == null ? "" : camouflageType;
        this.camouflageHost = camouflageHost == null ? "" : camouflageHost;
        this.camouflagePath = camouflagePath == null ? "" : camouflagePath;
        this.camouflageTls = camouflageTls == null ? "" : camouflageTls;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAlterId() {
        return alterId;
    }

    public void setAlterId(int alterId) {
        this.alterId = alterId;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCamouflageType() {
        return camouflageType;
    }

    public void setCamouflageType(String camouflageType) {
        this.camouflageType = camouflageType;
    }

    public String getCamouflageHost() {
        return camouflageHost;
    }

    public void setCamouflageHost(String camouflageHost) {
        this.camouflageHost = camouflageHost;
    }

    public String getCamouflagePath() {
        return camouflagePath;
    }

    public void setCamouflagePath(String camouflagePath) {
        this.camouflagePath = camouflagePath;
    }

    public String getCamouflageTls() {
        return camouflageTls;
    }

    public void setCamouflageTls(String camouflageTls) {
        this.camouflageTls = camouflageTls;
    }

    public static String parseToV2rayNgString(V2rayNodeDetail node)
    {
        if(node == null)
        {
            return "";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("v", "2");
        map.put("ps", node.getRemarks() == null ? "" : node.getRemarks());
        map.put("add", node.getIp());
        map.put("port", String.valueOf(node.getPort()));
        map.put("id", node.getUuid());
        map.put("aid", String.valueOf(node.getAlterId()));
        map.put("net", node.getNetwork() == null ? "auto" : node.getNetwork());
        map.put("type", node.getCamouflageType() == null ? "" : node.getCamouflageType());
        map.put("host", node.getCamouflageHost() == null ? "" : node.getCamouflageHost());
        map.put("path", node.getCamouflagePath() == null ? "" : node.getCamouflagePath());
        map.put("tls", node.getCamouflageTls() == null ? "" : node.getCamouflageTls());
        String json = JSONObject.toJSONString(map);
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("str", Base64Util.encode(json));
        String template = "vmess://${str}";
        return StringUtil.format(template,templateMap);
    }

    public static String parseToShadowrocketString(V2rayNodeDetail node)
    {
        if(node == null)
        {
            return "";
        }
        //        Shadowrocket不支持kcp
        if("kcp".equals(node.getNetwork()))
        {
            return "";
        }
        String template = "none:${uuid}@${ip}:${port}";
        Map<String, String> nodeMap = new HashMap<String, String>();
        nodeMap.put("uuid", node.getUuid());
        nodeMap.put("ip", node.getIp());
        nodeMap.put("port", String.valueOf(node.getPort()));
        String base64Origin = StringUtil.format(template, nodeMap);
        String base64 = Base64Util.encodeURLSafe(base64Origin);
        try {
            String templateOther = "vmess://${base}?remarks=${remarks}&obfs=${network}&obfsParam=${host}&path=${path}&tls=${tls}";
            Map<String, String> templateMap = new HashMap<String, String>();
            templateMap.put("base", base64);
            templateMap.put("remarks", URLEncoder.encode(node.getRemarks(), "UTF-8").replaceAll("\\+", "%20"));
            templateMap.put("network", "ws".equals(node.getNetwork()) ? node.getNetwork() : node.getCamouflageType());
            templateMap.put("host", node.getCamouflageHost());
            templateMap.put("path", node.getCamouflagePath());
            templateMap.put("tls", String.valueOf("tls".equals(node.getCamouflageTls())));
            return StringUtil.format(templateOther, templateMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseToQuantumultString(V2rayNodeDetail node)
    {
        if(node == null)
        {
            return "";
        }
//        Quantumult不支持kcp
        if("kcp".equals(node.getNetwork()))
        {
            return "";
        }
        String template = "${remarks} = vmess,${ip},${port},chacha20-ietf-poly1305,\"${uuid}\",group=${group},over-tls=${tls},certificate=1,obfs=${network},obfs-path=\"${path}\",obfs-header=\"Host:${host}[Rr][Nn]User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\"";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("remarks", node.getRemarks());
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("uuid", node.getUuid());
        templateMap.put("group", NormalConfiguration.webGroup);
        templateMap.put("tls", "".equals(node.getCamouflageTls()) ? "false" : "true");
        templateMap.put("network", "tcp".equals(node.getNetwork()) ? "none" : node.getNetwork());
        templateMap.put("path", "".equals(node.getCamouflagePath()) ? "/" : node.getCamouflagePath());
        templateMap.put("host", "".equals(node.getCamouflageHost()) ? node.getIp() :node.getCamouflageHost());
        String base64Origin = StringUtil.format(template, templateMap);
        String base64 = Base64Util.encodeURLSafe(base64Origin);
        return String.format("vmess://%s", base64);
    }

    public static String parseToQuantumultXString(V2rayNodeDetail node) {
        if(node == null)
        {
            return "";
        }
//        QuantumultX不支持kcp
        if("kcp".equals(node.getNetwork()))
        {
            return "";
        }
        String template = "vmess=${ip}:${port}, method=aes-128-gcm, password=${uuid}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("remarks", node.getRemarks());
        templateMap.put("ip", node.getIp());
        templateMap.put("port", String.valueOf(node.getPort()));
        templateMap.put("uuid", node.getUuid());
        templateMap.put("group", NormalConfiguration.webGroup);
        templateMap.put("tls", "".equals(node.getCamouflageTls()) ? "false" : "true");
        templateMap.put("network", "".equals(node.getCamouflageTls())
                ? ("http".equals(node.getNetwork()) ? "http" : "")
                : ("ws".equals(node.getNetwork()) ? "wss" : "over-tls"));
        templateMap.put("path", "".equals(node.getCamouflagePath()) ? "/" : node.getCamouflagePath());
        templateMap.put("host", "".equals(node.getCamouflageHost()) ? node.getIp() :node.getCamouflageHost());
        StringBuffer basic = new StringBuffer(StringUtil.format(template, templateMap));
        if("over-tls".equals(templateMap.get("network")))
        {
            basic.append(", obfs=" + templateMap.get("network"));
        }
        else
        {
            if(!"".equals(templateMap.get("network")))
            {
                basic.append(", obfs=" + templateMap.get("network") + ", obfs-uri=" + node.camouflagePath );
            }
        }
        basic.append(", fast-open=false, udp-relay=false, tag=" + node.getRemarks());
        return basic.toString();
    }
    public static V2rayNodeDetail toV2rayDetail(NodeDto nodeDto) {
        if(nodeDto == null)
        {
            return null;
        }
        V2rayNodeDetail v2rayNodeDetail = new V2rayNodeDetail(nodeDto.getIp(), nodeDto.getPort(), nodeDto.getRemarks(), nodeDto.getUuid(),
                nodeDto.getAlterId(), nodeDto.getNetwork(), nodeDto.getCamouflageType(), nodeDto.getCamouflageHost(), nodeDto.getCamouflagePath(), nodeDto.getCamouflageTls(), nodeDto.getUdp());
        return v2rayNodeDetail;
    }

    public static List<V2rayNodeDetail> toV2rayDetails(List<NodeDto> nodes) {
        List<V2rayNodeDetail> list = new ArrayList<V2rayNodeDetail>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return list;
        }
        for(NodeDto node : nodes)
        {
            list.add(toV2rayDetail(node));
        }
        return list;
    }

    public static List<Map<String, String>> parseToClashMap(List<V2rayNodeDetail> list)
    {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isEmpty(list))
        {
            return mapList;
        }
        for(V2rayNodeDetail v2rayNodeDetail : list)
        {
            if("kcp".equals(v2rayNodeDetail.getNetwork()))
            {
                continue;
            }
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("remarks", v2rayNodeDetail.getRemarks());
            tempMap.put("ip", v2rayNodeDetail.getIp());
            tempMap.put("port", String.valueOf(v2rayNodeDetail.getPort()));
            tempMap.put("uuid", v2rayNodeDetail.getUuid());
            tempMap.put("alterId", String.valueOf(v2rayNodeDetail.getAlterId()));
            tempMap.put("network", v2rayNodeDetail.getNetwork());
            tempMap.put("camouflageTls", v2rayNodeDetail.getCamouflageTls() == null ? "" : v2rayNodeDetail.getCamouflageTls());
            tempMap.put("camouflagePath", v2rayNodeDetail.getCamouflagePath() == null ? "" : v2rayNodeDetail.getCamouflagePath());
            tempMap.put("camouflageHost", v2rayNodeDetail.getCamouflageHost() == null ? "" :v2rayNodeDetail.getCamouflageHost());
            tempMap.put("camouflageType", v2rayNodeDetail.getCamouflageType() == null ? "" :v2rayNodeDetail.getCamouflageType());
            tempMap.put("security", v2rayNodeDetail.getSecurity() == null ? "auto" : v2rayNodeDetail.getSecurity());
            tempMap.put("type", "v2ray");
            mapList.add(tempMap);
        }
        return mapList;
    }

    public static ClashNodeConfigDto parseToClashNode(V2rayNodeDetail node)
    {
        if("kcp".equals(node.getNetwork()))
        {
            return null;
        }
        ClashNodeConfigDto clashNodeConfigDto = new ClashNodeConfigDto();
        clashNodeConfigDto.setType("vmess");
        clashNodeConfigDto.setName(StringUtil.isEmpty(node.getRemarks()) ? "": node.getRemarks().replaceAll("'", "").replaceAll("\"", ""));
        clashNodeConfigDto.setServer(node.getIp());
        clashNodeConfigDto.setPort(node.getPort());
        clashNodeConfigDto.setUuid(node.getUuid());
        clashNodeConfigDto.setAlterId(node.getAlterId());
        clashNodeConfigDto.setCipher("auto");
        String network = "";
        if(StringUtils.isEmpty(node.getNetwork()) )
        {
            network = "tcp";
        }
        else
        {
            network = node.getNetwork();
        }
        clashNodeConfigDto.setNetwork(network);
        if(node.getUdp() == 1)
        {
            clashNodeConfigDto.setUdp(true);
        }
        if(StringUtils.isNotEmpty(node.getCamouflageTls()))
        {
            clashNodeConfigDto.setTls(true);
            clashNodeConfigDto.setSkipCertVerify(true);
        }
        if("ws".equals(network))
        {
            ClashNodoWsConfigDto clashNodoWsConfigDto = new ClashNodoWsConfigDto();
            if(StringUtils.isNotEmpty(node.getCamouflageHost()))
            {
                ClashNodeWsHeadersConfigDto clashNodeWsHeadersConfigDto = new ClashNodeWsHeadersConfigDto();
                clashNodeWsHeadersConfigDto.setHost(node.getCamouflageHost());
                clashNodeConfigDto.setWsHeaders(clashNodeWsHeadersConfigDto);
                clashNodoWsConfigDto.setHeaders(clashNodeWsHeadersConfigDto);
            }
            if(StringUtils.isNotEmpty(node.getCamouflagePath()))
            {
                clashNodeConfigDto.setWsPath(node.getCamouflagePath());
                clashNodoWsConfigDto.setPath(node.getCamouflagePath());
            }
            clashNodeConfigDto.setWsOpt(clashNodoWsConfigDto);
        }
        return clashNodeConfigDto;
    }
}
