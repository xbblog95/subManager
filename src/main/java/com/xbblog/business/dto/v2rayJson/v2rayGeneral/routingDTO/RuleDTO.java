package com.xbblog.business.dto.v2rayJson.v2rayGeneral.routingDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleDTO {

    private String type;
    private List<String> domain;
    private List<String> ip;
    private String port;
    private String network;
    private List<String> source;
    private List<String> user;
    private List<String> inboundTag;
    private List<String> protocol;
    private String attrs;
    private String outboundTag;
    private String balancerTag;


    public static RuleDTO getDirectRule(List<String> inboundTags)
    {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setType("field");
        List<String> domains = new ArrayList<String>();
        domains.add("geosite:cn");
        ruleDTO.setDomain(domains);
        List<String> ips = new ArrayList<String>();
        ips.add("geoip:cn");
        ips.add("geoip:private");
        ruleDTO.setIp(ips);
        ruleDTO.setNetwork("tcp,udp");
        ruleDTO.setInboundTag(inboundTags);
        ruleDTO.setOutboundTag("direct");
        return ruleDTO;
    }

    public static RuleDTO getProxyRule(List<String> inboundTags, String outboundTag)
    {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setType("field");
        List<String> domains = new ArrayList<String>();
        ruleDTO.setDomain(domains);
        List<String> ips = new ArrayList<String>();
        ruleDTO.setIp(ips);
        ruleDTO.setNetwork("tcp,udp");
        ruleDTO.setInboundTag(inboundTags);
        ruleDTO.setOutboundTag(outboundTag);
        return ruleDTO;
    }
}
