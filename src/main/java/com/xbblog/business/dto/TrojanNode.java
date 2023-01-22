package com.xbblog.business.dto;


import com.xbblog.business.dto.clash.ClashNodeConfigDto;
import com.xbblog.utils.Base64Util;
import com.xbblog.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return "trojan://" + nodeDto.getPassword() + "@" + nodeDto.getIp() + ":" + nodeDto.getPort() + "?" + "security=tls&sni=" + nodeDto.getObfsparam() + "#" + nodeDto.getRemarks();
    }

    public static String parseToPharosProString(NodeDto nodeDto) {
        String templateInfo = "trojan://${password}@${ip}:${port}?sni=${host}#${remark}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", nodeDto.getIp());
        templateMap.put("port", String.valueOf(nodeDto.getPort()));
        templateMap.put("password",nodeDto.getPassword());
        templateMap.put("host", nodeDto.getObfsparam());
        try {
            templateMap.put("remark", URLEncoder.encode(nodeDto.getRemarks(), "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtil.format(templateInfo, templateMap);
    }

    public static String parseToShadowrocketString(NodeDto nodeDto) {
        return parseToV2rayNgString(nodeDto);
    }

    public static String parseToQuantumultXString(NodeDto nodeDto) {
        String templateInfo = "trojan=${ip}:${port}, password=${password}, tls-host=${host},over-tls=true, tls-verification=true, tag=${remark}";
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("ip", nodeDto.getIp());
        templateMap.put("port", String.valueOf(nodeDto.getPort()));
        templateMap.put("password",nodeDto.getPassword());
        templateMap.put("host", nodeDto.getObfsparam());
        templateMap.put("remark", nodeDto.getRemarks());
        return StringUtil.format(templateInfo, templateMap);
    }

    public static TrojanNode toTrojanNode(NodeDto nodeDto)
    {
        if(nodeDto == null)
        {
            return null;
        }
        TrojanNode trojanNode = new TrojanNode(nodeDto.getIp(), nodeDto.getPort(), nodeDto.getRemarks(),
                nodeDto.getPassword(), nodeDto.getObfsparam(), nodeDto.getObfs(), nodeDto.getUdp());
        return trojanNode;
    }

    public static ClashNodeConfigDto trojanNodeparseToClash(TrojanNode node) {
        ClashNodeConfigDto clashNodeConfigDto = new ClashNodeConfigDto();
        clashNodeConfigDto.setName(StringUtils.isEmpty(node.getRemarks()) ? "": node.getRemarks().replaceAll("'", "").replaceAll("\"", ""));
        clashNodeConfigDto.setType("trojan");
        clashNodeConfigDto.setServer(node.getIp());
        clashNodeConfigDto.setPort(node.getPort());
        clashNodeConfigDto.setSkipCertVerify(true);
        clashNodeConfigDto.setPassword(node.getPassword());
        if(node.getUdp() == 1)
        {
            clashNodeConfigDto.setUdp(true);
        }
        if(StringUtils.isNotEmpty(node.getSni()))
        {
            clashNodeConfigDto.setSni(node.getSni());
        }
        if(StringUtils.isNotEmpty(node.getAlpn()))
        {
            List<String> alpns = new ArrayList<>();
            alpns.add(node.getAlpn());
            clashNodeConfigDto.setAlpn(alpns);
        }
        return clashNodeConfigDto;
    }
}
