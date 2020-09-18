package com.xbblog.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;


public class MonitorUtils {


    public static Boolean testTCPActive(String host, int port){
        try {
            StringBuffer stringBuffer = new StringBuffer("https://www.toolsdaquan.com/toolapi/public/ipchecking/");
            stringBuffer.append(host);
            stringBuffer.append("/");
            stringBuffer.append(port);
            Header header = new BasicHeader("referer", "https://www.toolsdaquan.com/ipcheck/");
            String json = HttpUtils.sendGet(stringBuffer.toString(), null, new Header[]{header});
            JSONObject obj = JSONObject.parseObject(json);
            if("success".equals(obj.getString("tcp")))
            {
                return true;
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