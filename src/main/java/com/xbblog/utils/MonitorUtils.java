package com.xbblog.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MonitorUtils {


    public static Boolean testTCPActive(String host, int port){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("host", host);
            map.put("port", port);
            String json = HttpUtils.sendPost("http://ipcheck.xbblog.com/api_v1", map);
            JSONObject obj = JSONObject.parseObject(json);
            if(obj.getBoolean("success"))
            {
                return obj.getJSONObject("data").getJSONObject("inside").getBoolean("tcp");
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}