package com.luzz.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luzz.entity.Product;
import com.luzz.enums.ResultsCode;
import com.luzz.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * 定时器上下文
 * @author luzz
 * @date 2020-03-09
 */
@Slf4j
public class TimerContext {

    /**
     * 初始化定时任务
     */
    public static void init () {
        ScheduledExecutorService ses = newScheduledThreadPool(3);
        // 即将开课
        ses.scheduleAtFixedRate(new CrawlerOne(), 1, 3600*3, TimeUnit.SECONDS);
        // 开课中
        ses.scheduleAtFixedRate(new CrawlerTwo(), 5, 3600*3, TimeUnit.SECONDS);
        // 已结课
        ses.scheduleAtFixedRate(new CrawlerThree(), 10, 3600*3, TimeUnit.SECONDS);
    }

    /**
     * 爬虫任务-1
     */
    static class CrawlerOne extends TimerTask {
        @Override
        public void run() {
            try {
                int scope = 0;
                int appId = 10000;
                JSONArray status = new JSONArray();
                // 即将开课
                status.add("1");
                for (int i = 1; i < 1000; i++) {
                    long startTime = System.currentTimeMillis();
                    log.info("爬虫任务-1，即将开课，第：{}，页，任务开始", i);
                    String fileName = "no_classes_product_";
                    Configuration conf = HadoopContext.getConfiguration(scope);
                    int write = write(conf, HadoopContext.getUrl(scope), fileName + i, status, appId, i);
                    if (ResultsCode.NOT_DATA.getCode() == write) {
                        log.info("爬虫任务-1，任务结束，暂停12小时");
                        break;
                    }
                    if (ResultsCode.OUT_TIME.getCode() == write) {
                        log.info("服务器宕机啦");
                        break;
                    }
                    long endTime = System.currentTimeMillis();
                    log.info("爬虫任务-1，即将开课，第：{}，页，任务结束，耗时：{}毫秒", i, endTime - startTime);
                }
            } catch (Throwable e) {
                log.error("【定时任务】异常，异常信息为：{}", e.getMessage());
            }
        }
    }

    /**
     * 爬虫任务-2
     */
    static class CrawlerTwo extends TimerTask {
        @Override
        public void run() {
            try {
                int scope = 1;
                int appId = 10000;
                JSONArray status = new JSONArray();
                // 开课中
                status.add("2");
                for (int i = 1; i < 1000; i++) {
                    long startTime = System.currentTimeMillis();
                    log.info("爬虫任务-2，开课中，第：{}，页，任务开始", i);
                    String fileName = "in_the_course_product_";
                    Configuration conf = HadoopContext.getConfiguration(scope);
                    int write = write(conf, HadoopContext.getUrl(scope), fileName + i, status, appId, i);
                    if (ResultsCode.NOT_DATA.getCode() == write) {
                        log.info("爬虫任务-2，任务结束，暂停3小时");
                        break;
                    }
                    if (ResultsCode.OUT_TIME.getCode() == write) {
                        log.info("服务器宕机啦");
                        break;
                    }
                    long endTime = System.currentTimeMillis();
                    log.info("爬虫任务-2，开课中，第：{}，页，任务结束，耗时：{}毫秒", i, endTime - startTime);
                }
            } catch (Throwable e) {
                log.error("【定时任务】异常，异常信息为：{}", e.getMessage());
            }
        }
    }

    /**
     * 爬虫任务-3
     */
    static class CrawlerThree extends TimerTask {
        @Override
        public void run() {
            try {
                int scope = 2;
                int appId = 10000;
                JSONArray status = new JSONArray();
                // 已结课
                status.add("3");
                for (int i = 1; i < 1000; i++) {
                    long startTime = System.currentTimeMillis();
                    log.info("爬虫任务-3，已结课，第：{}，页，任务开始", i);
                    String fileName = "in_the_course_product_";
                    Configuration conf = HadoopContext.getConfiguration(scope);
                    int write = write(conf, HadoopContext.getUrl(scope), fileName + i, status, appId, i);
                    if (ResultsCode.NOT_DATA.getCode() == write) {
                        log.info(" 爬虫任务-3，任务结束，暂停3小时");
                        break;
                    }
                    if (ResultsCode.OUT_TIME.getCode() == write) {
                        log.info("服务器宕机啦");
                        break;
                    }
                    long endTime = System.currentTimeMillis();
                    log.info("爬虫任务-3，已结课，第：{}，页，任务结束，耗时：{}毫秒", i, endTime - startTime);
                }
            } catch (Throwable e) {
                log.error("【定时任务】异常，异常信息为：{}", e.getMessage());
            }
        }
    }

    /**
     * 创建文件并写入内容
     * @param fileName
     * @param status
     * @param appId
     * @param page
     */
    private static int write(Configuration conf, String url, String fileName, JSONArray status, int appId, int page) {
        try {
            Path inFile = new Path("hdfs://" + url + ":9000/" + fileName + ".txt");
            FileSystem hdfs = FileSystem.get(conf);
            try {
                FsStatus fsStatus = hdfs.getStatus();
                if (fsStatus == null) {
                    log.info("服务器宕机啦");
                    return ResultsCode.OUT_TIME.getCode();
                }
            } catch (Exception e) {
                log.error("服务器宕机啦", e);
                return ResultsCode.OUT_TIME.getCode();
            }
            FSDataOutputStream outputStream = hdfs.create(inFile);
            JSONObject object = new JSONObject();
            object.put("query", "");
            object.put("chief_org", new JSONArray());
            object.put("classify", new JSONArray());
            object.put("selling_type", new JSONArray());
            object.put("status", status);
            object.put("appid", appId);
            String result = HttpUtils.sendPostUrl("https://next.xuetangx.com/api/v1/lms/get_product_list/?page=" + page, object.toJSONString());
            if (StringUtils.isEmpty(result)) {
                log.info("暂时无任务");
                return ResultsCode.NOT_DATA.getCode();
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null || data.size() <= 0) {
                log.info("暂时无任务");
                return ResultsCode.NOT_DATA.getCode();
            }
            JSONArray productList = data.getJSONArray("product_list");
            if (productList == null || productList.size() <= 0) {
                log.info("暂时无任务");
                return ResultsCode.NOT_DATA.getCode();
            }
            productList.forEach(json -> {
                try {
                    Product product = JSONObject.parseObject(json.toString(), Product.class);
//                    log.info("product：" + product);
                    outputStream.write(product.toString().getBytes());
                } catch (IOException e) {
                    log.error("", e);
                }
            });
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("", e);
        }
        return ResultsCode.SUCCESS.getCode();
    }

    public static void main(String[] args) {
        init();
    }
}
