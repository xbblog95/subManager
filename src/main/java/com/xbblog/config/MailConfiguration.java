package com.xbblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource({"file:${server.config.localtion}\\mail.properties", "file:${server.config.localtion}\\web.properties"})
public class MailConfiguration {


}
