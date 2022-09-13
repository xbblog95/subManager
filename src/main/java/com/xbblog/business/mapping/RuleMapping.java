package com.xbblog.business.mapping;

import com.xbblog.business.dto.ClashCustomRule;
import com.xbblog.business.dto.ClashRuleProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RuleMapping {

    List<ClashRuleProvider> queryAllClashRuleProviders();

    List<ClashCustomRule> queryAllCustomRules();

}
