package com.xbblog.base.utils;

import com.xbblog.base.annotation.YamlProperty;
import org.yaml.snakeyaml.introspector.MissingProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;

/**
 * yaml属性处理类
 */
public class YamlProPertyUtils extends PropertyUtils {

    @Override
    public Property getProperty(Class<?> type, String name) {
        Field[] declaredFields = type.getDeclaredFields();
        for(Field field : declaredFields)
        {
            if(field.getName().equals(name))
            {
                return super.getProperty(type, name);
            }
            else
            {
                YamlProperty annotation = field.getAnnotation(YamlProperty.class);
                if(annotation != null)
                {
                    if(annotation.value().equals(name))
                    {
                        return super.getProperty(type, field.getName());
                    }
                }
            }
        }
        return new MissingProperty(name);
    }

}
