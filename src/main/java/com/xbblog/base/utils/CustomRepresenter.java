package com.xbblog.base.utils;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class CustomRepresenter extends Representer {

    private Boolean isAllowWriteNull = false;

    public Boolean getAllowWriteNull() {
        return isAllowWriteNull;
    }

    public void setAllowWriteNull(Boolean allowWriteNull) {
        isAllowWriteNull = allowWriteNull;
    }

    @Override
    protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                                                  Object propertyValue, Tag customTag) {
        if(propertyValue != null || isAllowWriteNull)
        {
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
        return null;
    }
}
