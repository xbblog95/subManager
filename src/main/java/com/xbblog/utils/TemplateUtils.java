package com.xbblog.utils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class TemplateUtils {

    private static Configuration configuration;

    static
    {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.setCacheStorage(new freemarker.cache.MruCacheStorage(100, 2500));
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(TemplateUtils.class, "/freemarkerTemplate");
    }

    public static void format(String templateName, Map<String, Object> objectMap, OutputStream os)
    {
        Template template = null;
        try {
            template = configuration.getTemplate(templateName);
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            template.process(objectMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public static void format(String templateName, Map<String, Object> objectMap, Writer writer)
    {
        Template template = null;
        try {
            template = configuration.getTemplate(templateName);
            template.process(objectMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
