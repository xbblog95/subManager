package com.xbblog.business.mapping;

import com.xbblog.business.dto.Subscribe;
import com.xbblog.business.dto.SubscribeHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface SubscribeMapping {

    public List<Subscribe> getAllSubscribe();

    SubscribeHandler getNodeHandler(Map<String, Object> id);
}
