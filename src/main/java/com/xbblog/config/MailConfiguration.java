package com.xbblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource({"file:D://subManager/web/conf/mail.properties", "file:D://subManager/web/conf/web.properties"})
public class MailConfiguration {


}
