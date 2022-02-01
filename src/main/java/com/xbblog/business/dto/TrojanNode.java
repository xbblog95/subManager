package com.xbblog.business.dto;


import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Data
public class TrojanNode extends NodeDetail{

    private String password;
    private String sni;
    private String alpn;

    public TrojanNode(String ip, int port, String remarks, String password, String sni, String alpn, int udp) {
        super(ip, port, remarks, "trojan", "trojan", udp);
        this.password = password;
        this.sni = sni;
        this.alpn = alpn;
    }

    public static String parseToV2rayNgString(NodeDto nodeDto) {
        try {
            return "trojan://" + nodeDto.getPassword() + "@" + nodeDto.getIp() + ":" + nodeDto.getPort() + "?" + "sni=" + nodeDto.getObfsparam() + "#" + URLEncoder.encode(nodeDto.getRemarks(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseToShadowrocketString(NodeDto nodeDto) {
        String templateInfo = "trajon://${password}@${ip}:${port}?sni=${host}#${remark}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", nodeDto.getIp());
        templateMap.put("port", String.valueOf(nodeDto.getPort()));
        templateMap.put("password",nodeDto.getPassword());
        templateMap.put("host", nodeDto.getObfsparam());
        templateMap.put("remark", nodeDto.getRemarks());
        return StringUtil.format(templateInfo, templateMap);
    }

    public static String parseToQuantumultXString(NodeDto nodeDto) {
        String templateInfo = "trajon=${ip}:${port}, password=${password}, tls-host=${host},over-tls=true, tls-verification=true, tag=${remark}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", nodeDto.getIp());
        templateMap.put("port", String.valueOf(nodeDto.getPort()));
        templateMap.put("password",nodeDto.getPassword());
        templateMap.put("host", nodeDto.getObfsparam());
        templateMap.put("remark", nodeDto.getRemarks());
        return StringUtil.format(templateInfo, templateMap);
    }
}
