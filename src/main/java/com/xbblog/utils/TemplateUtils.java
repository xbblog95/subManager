package com.xbblog.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class TemplateUtils {

    public static void format(String templateName, Map<String, Object> objectMap, OutputStream os)
    {
        Template template = null;
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        try {

            template = cfg.getTemplate(templateName);
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            template.process(objectMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
