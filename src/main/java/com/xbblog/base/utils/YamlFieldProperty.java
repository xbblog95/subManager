package com.xbblog.base.utils;

import com.xbblog.business.dto.clash.ClashConfigDto;
import org.yaml.snakeyaml.introspector.GenericProperty;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class YamlFieldProperty extends GenericProperty {

    private Field field;

    private Method readMethod;

    private Method writeMethod;

    public YamlFieldProperty(String name, Field field) {
        super(name, field.getType(), field.getGenericType());
        this.field = field;
    }

    public YamlFieldProperty(String name, Method readMethod, Method writeMethod) {
        super(name, readMethod.getReturnType(), readMethod.getGenericReturnType());
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    /**
     * 设置值
     * @param object
     * @param value
     * @throws Exception
     */
    @Override
    public void set(Object object, Object value) throws Exception {
        if(field != null)
        {
            field.set(object, value);
        }
        else
        {
            if(object.getClass().equals(ClashConfigDto.class))
            {
                int i = 0;
            }
            this.writeMethod.invoke(object, value);
        }
    }

    /**
     * 获取值
     * @param object
     * @return
     */
    @Override
    public Object get(Object object) {
        if(field != null)
        {
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
        {
            try {
                return readMethod.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public List<Annotation> getAnnotations() {
        if(field != null)
        {
           return Arrays.asList(field.getAnnotations());
        }
        else
        {
            return ArrayUtils.toUnmodifiableCompositeList(this.readMethod.getAnnotations(), this.writeMethod.getAnnotations());
        }
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        if(field != null)
        {
            return field.getAnnotation(annotationType);
        }
        else
        {
            if(this.readMethod.getAnnotation(annotationType) != null)
            {
                return this.readMethod.getAnnotation(annotationType);
            }
            else
            {
                return this.writeMethod.getAnnotation(annotationType);
            }
        }
    }
}
