package com.luzz.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title HttpUtils
 * @Description http请求代理工具
 * @Author Craig
 * @Version 1.0.0
 * @Date 2019/9/30 14:18
 */
@Slf4j
public class HttpUtils {
    /**
     * 默认的超时时间
     */
    public static final int COMMON_TIMEOUT = 10000;
    /**
     * 心跳超时时间
     */
    public static final int HEARTBEAT_TIMEOUT = 1000;
    /**
     * 默认重试次数
     */
    public static final int RETRY_TIMES = 5;


    /**
     * http post请求
     * @param urlStr 请求url
     * @param headers 头
     * @param param 加密的内容
     * @param timeout 超时时间
     * @return
     */
    private static String sendPostUrl(String urlStr, Map<String, String> headers, String param, int timeout) {
        HttpURLConnection conn = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            URL url= new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置链接超时时间
            conn.setConnectTimeout(timeout);
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(conn::setRequestProperty);
            }
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            out.write(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (Exception e) {
            log.error("发送url失败， url = " + urlStr + "; header = " + headers + "; param = " + param, e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
    /**
     * http get请求
     * @param urlStr 请求url
     * @param param 加密的内容
     * @return
     */
    public static String sendGetUrl(String urlStr, String param) {
        HttpURLConnection conn = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            URL url= new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置链接超时时间
            conn.setConnectTimeout(COMMON_TIMEOUT);
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            out.write(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (Exception e) {
            log.error("发送url失败， url = " + urlStr + "; param = " + param, e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * http post请求
     * @param urlStr  请求url
     * @param param   加密的内容
     * @return
     */
    public static String sendPostUrl(String urlStr, String param) {
        HttpURLConnection conn = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("content-type", "application/json;charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置链接超时时间
            conn.setConnectTimeout(60000);
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            out.write(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
