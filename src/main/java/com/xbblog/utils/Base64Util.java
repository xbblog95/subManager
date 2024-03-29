
package com.xbblog.utils;


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Base64Util {

    public static String decode(String text)
    {
        if(text == null || text.length() == 0)
        {
            return "";
        }
        try {
            return new String(Base64.decodeBase64(text.getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encodeURLSafe(String text)
    {
        if(text == null || text.length() == 0)
        {
            return "";
        }
        return new String(Base64.encodeBase64URLSafe(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String encode(String text)
    {
        if(text == null || text.length() == 0)
        {
            return "";
        }
        return new String(Base64.encodeBase64(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

}
