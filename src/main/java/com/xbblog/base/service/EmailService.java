package com.xbblog.base.service;

import com.xbblog.utils.TemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;


@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;


    public void sendOne(String address, String templeteName, Map<String, Object> map, String subject) throws IOException, TemplateException, MessagingException {
        StringWriter writer = new StringWriter();
        TemplateUtils.format(templeteName, map, writer);
        mailSender.send(getMessage(writer.toString(), subject, address));
    }

    private MimeMessage getMessage(String html, String subject, String to) throws IOException, TemplateException, MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
        helper.setTo(to);
        helper.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        helper.setText(html, true);
        helper.setFrom(mailProperties.getUsername());
        return msg;
    }


}
