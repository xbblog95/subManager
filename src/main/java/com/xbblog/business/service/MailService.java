package com.xbblog.business.service;

import com.xbblog.SubMangerApplication;
import com.xbblog.business.dto.NodeDetail;
import com.xbblog.config.MailConfiguration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class MailService {

//    @Autowired
    private JavaMailSender mailSender;

//    @Autowired
    private MailConfiguration mailConfiguration;

    private static Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendOne() throws IOException, TemplateException, MessagingException {

//        Template template = configuration.getTemplate(templeteName);
//        StringWriter writer = new StringWriter();
//        template.process(map, writer);
//        mailSender.send(getMessage(writer.toString(), subject));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailConfiguration.getFrom());
        mailMessage.setTo("550529870@qq.com");

        mailMessage.setSubject("主题");
        mailMessage.setText("文本");
        mailSender.send(mailMessage);
    }





//    private MimeMessage getMessage(String html, String subject) throws IOException, TemplateException, MessagingException {
//        MimeMessage msg = mailSender.createMimeMessage();
//        msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
//        msg.setContent(html,"text/html;charset=utf-8");
//        msg.setFrom(new InternetAddress(fromMail, fromUserName, "UTF-8"));
//        return msg;
//    }

}
