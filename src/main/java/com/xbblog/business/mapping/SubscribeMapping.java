package com.xbblog.business.mapping;

import com.xbblog.business.dto.Subscribe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface SubscribeMapping {

    public List<Subscribe> getAllSubscribe();
}
