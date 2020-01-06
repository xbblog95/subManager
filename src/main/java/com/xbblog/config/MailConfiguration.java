package com.xbblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource({"file:/usr/local/subManager/web/conf/mail.properties", "file:/usr/local/subManager/web/conf/web.properties"})
public class MailConfiguration {


}
