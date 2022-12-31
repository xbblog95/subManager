package com.xbblog.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;


/**
 * HttpClient发送GET、POST请求
 * @Author libin
 * @CreateDate 2018.5.28 16:56
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static String sendGet(String url, Map<String, Object> param) throws Exception
    {
       return sendGet(url, param, null);
    }

    public static String sendGet(String url, Map<String, Object> param, Header[] headersReq, Map<String, String> handerResultMap) throws Exception
    {
        LOGGER.info("发送get请求,url为 {} 参数为 {}", url , param);
        CloseableHttpClient closeableHttpClient = getHttpClient(url);
        URIBuilder uriBuilder = new URIBuilder(url);
        if(param != null)
        {
            List<NameValuePair> paramList = getParams(param);
            uriBuilder.addParameters(paramList);
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        if(headersReq != null && headersReq.length != 0)
        {
            httpGet.setHeaders(headersReq);
        }
        else
        {
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        }
        CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
        Header[] headers = response.getHeaders("content-type");
        if(handerResultMap != null)
        {
            Set<String> keys = handerResultMap.keySet();
            for(String key : keys)
            {
                Header[] hd = response.getHeaders(key);
                if(hd != null && hd.length != 0)
                {
                    handerResultMap.put(key, hd[0].getValue());
                }
            }
        }
        HttpEntity entity = response.getEntity();
        if(headers.length != 0 && "application/octet-stream".equals(headers[0].getElements()[0].getName()))
        {
            //文件下载
            if(entity.getContentLength() ==0)
            {
                return "";
            }
            InputStream in  = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    if(!line.endsWith("\n"))
                    {
                        sb.append("\n");
                    }
                }
            } catch (IOException e) {
                return "";
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    return "";
                }
            }
            return sb.toString().trim();
        }
        else
        {
            return EntityUtils.toString(entity, "UTF-8");
        }
    }
    /**
     * 发送GET请求
     * @param url   请求url
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendGet(String url, Map<String, Object> param, Header[] headersReq) throws Exception{
        return sendGet(url, param, headersReq, null);
    }

    /**
     * 发送post请求
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, Map<String, Object> param) throws Exception {
        LOGGER.info("发送post请求,url为" + url + "参数为" + param);
        CloseableHttpClient closeableHttpClient = getHttpClient(url);
        HttpPost httpPost = new HttpPost(url);
        if(param != null)
        {
            List<NameValuePair> paramList = getParams(param);
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));
        }
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }

    /**
     * 构建httpClient对象
     * @param url
     * @return
     */
    private static CloseableHttpClient getHttpClient(String url){
        CloseableHttpClient closeableHttpClient;
        //ssl判断
        if(url.startsWith("https"))
        {
            SSLConnectionSocketFactory socketFactory = SSLConnectionSocketFactory.getSocketFactory();
            closeableHttpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        }
        else
        {
            closeableHttpClient = HttpClients.createDefault();
        }
        return closeableHttpClient;
    }

    /**
     * 构建参数对象
     * @param map
     * @return
     */
    private static List<NameValuePair> getParams(Map<String, Object> map)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Set<String> keySet = map.keySet();
        for(String key : keySet)
        {
            NameValuePair pair = new BasicNameValuePair(key, String.valueOf(map.get(key)));
            list.add(pair);
        }
        return list;
    }

}