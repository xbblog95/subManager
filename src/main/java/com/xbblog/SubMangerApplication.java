package com.xbblog;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@Controller
@ComponentScan({"com.xbblog.config", "com.github.hiwepy.ip2region.spring.boot"})
@EnableCaching
public class SubMangerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubMangerApplication.class, args);
    }


    @RequestMapping("/")
    public String index()
    {
        return "redirect:user/toIndex";
    }

    @RequestMapping("/tologin")
    public String tologin()
    {
        return "login/login";
    }

    /**
     * 注入Bean : HttpMessageConverters 解析，使用FastJson解析JSON数据
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {

        // 配置 FastJson
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.QuoteFieldNames, SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);

        // 添加 FastJsonHttpMessageConverter
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);

        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
