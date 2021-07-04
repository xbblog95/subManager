package com.xbblog.business.dto.v2rayJson.v2rayGeneral;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.routingDTO.RuleDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoutingDTO {

    private String domainStrategy;
    private List<RuleDTO> rules;


    public static RoutingDTO getInstance(List<String> inboundTags, String outboundTag)
    {
        RoutingDTO routingDTO = new RoutingDTO();
        routingDTO.setDomainStrategy("AsIs");
        List<RuleDTO> rules = new ArrayList<>();
        rules.add(RuleDTO.getDirectRule(inboundTags));
        rules.add(RuleDTO.getProxyRule(inboundTags, outboundTag));
        routingDTO.setRules(rules);
        return routingDTO;
    }
}
