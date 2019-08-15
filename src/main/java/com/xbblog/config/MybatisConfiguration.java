package com.xbblog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.xbblog.business.mapping"})
public class MybatisConfiguration {
}
