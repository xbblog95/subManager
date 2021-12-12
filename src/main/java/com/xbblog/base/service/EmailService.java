package com.xbblog.base.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.xbblog.utils.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;


@Service
public class EmailService {


    @Value("${monitor.email.fromAddress}")
    private String userName;
    @Value("${sendgrid.apikey}")
    private String apiKey;

    public void sendOne(String address, String templeteName, Map<String, Object> map, String subject) throws IOException {
        StringWriter writer = new StringWriter();
        TemplateUtils.format(templeteName, map, writer);
        Email from = new Email(userName);
        Email to = new Email(address);
        Content content = new Content("text/html", writer.toString());
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if(response.getStatusCode() != 202)
            {
                throw new RuntimeException("邮件服务器发送失败！" + response.getBody());
            }
        } catch (IOException ex) {
            throw ex;
        }
    }

}
