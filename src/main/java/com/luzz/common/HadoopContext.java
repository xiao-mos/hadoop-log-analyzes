package com.luzz.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hadoop 上下文
 * @author luzz
 * @date 2020-03-09
 */

@Slf4j
@Component
public class HadoopContext {
    private static Map<Integer, Configuration> configurationMap = new ConcurrentHashMap<>();
    private static String [] urls = {"192.168.1.238", "192.168.1.216", "192.168.1.229"};

    /**
     * 初始化hadoop
     */
    public static void init () {
        try {
            for (int i = 0; i < urls.length; i++) {
                Configuration conf = new Configuration();
                conf.set("fs.defaultFS", "hdfs://" + urls[i] + ":9000/");
                configurationMap.put(i, conf);
            }
            log.info("hadoop 初始化结果：{}", configurationMap);
        } catch (Exception e) {
            log.error("【初始化hadoop】错误，错误信息为：{}", e.getMessage());
        }
    }

    /**
     * 获取hadoop 配置
     * @param scope
     * @return
     */
    public static Configuration getConfiguration(int scope) {
        return configurationMap.get(scope);
    }

    /**
     * 获取hadoop url
     * @param scope
     * @return
     */
    public static String getUrl(int scope) {
        return urls[scope];
    }
}
