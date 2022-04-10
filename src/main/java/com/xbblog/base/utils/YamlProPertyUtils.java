package com.xbblog.base.utils;

import com.xbblog.base.annotation.YamlProperty;
import org.springframework.beans.BeanUtils;
import org.yaml.snakeyaml.introspector.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * yaml属性处理类
 */
public class YamlProPertyUtils extends PropertyUtils {

    private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap<Class<?>, Map<String, Property>>();

    @Override
    protected synchronized Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
        if(propertiesCache.get(type) != null)
        {
            return propertiesCache.get(type);
        }

        Map<String, Property> properties = new LinkedHashMap<String, Property>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    YamlProperty annotation = field.getAnnotation(YamlProperty.class);
                    if (Modifier.isPublic(modifiers)) {
                        if(annotation != null)
                        {
                            properties.put(annotation.value(), new YamlFieldProperty(annotation.value(), field));
                        }
                        else
                        {
                            properties.put(field.getName(), new YamlFieldProperty(field.getName(), field));
                        }
                    }
                    else
                    {
                        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(type, field.getName());
                        Method readMethod = propertyDescriptor.getReadMethod();
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        if(annotation != null)
                        {
                            properties.put(annotation.value(), new YamlFieldProperty(annotation.value(), readMethod, writeMethod));
                        }
                        else
                        {
                            properties.put(field.getName(), new YamlFieldProperty(field.getName(), readMethod, writeMethod));
                        }
                    }
                }
            }
        }
        propertiesCache.put(type, properties);
        return properties;
    }
}
