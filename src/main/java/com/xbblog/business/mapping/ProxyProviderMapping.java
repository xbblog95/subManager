package com.xbblog.business.mapping;

import com.xbblog.business.dto.ProxyProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProxyProviderMapping {

    public List<ProxyProvider> getProxyProvider(ProxyProvider proxyProvider);
}
