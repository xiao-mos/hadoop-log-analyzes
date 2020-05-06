package com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luzz.common.HadoopContext;
import com.luzz.entity.Product;
import com.luzz.util.HttpUtils;
import io.netty.handler.timeout.TimeoutException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.*;
//import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Test1 {
    public static final String CHARSET_UTF8 = "UTF-8";
    /***
     040
     * 加载配置文件
     041
     * **/
    //static Configuration conf = new Configuration();

    public static void main(String[] args) {
        try {
            /*Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "root");

            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.1.238:9000/");
            FileSystem fs = FileSystem.get(conf);
            Path src = new Path("E:\\luzz\\test3.txt");
            Path dest = new Path("/");
            fs.copyFromLocalFile(src, dest);*/
//            System.out.println(getRedirectUrl("https://next.xuetangx.com/search?query=&org=&classify=&type=&status=&page=2"));
//            doPost("https://next.xuetangx.com/search?query=&org=&classify=&type=&status=&page=2", null);
            //Configuration conf = new Configuration();
            /*Path srcPath = new Path("hdfs://192.168.1.238:9000/test1.txt");
            InputStream in = null;
            in = fs.open(srcPath);
            IOUtils.copyBytes(in, System.out, 4096, false); //复制到标准输出流
            IOUtils.closeStream(in);*/

            JSONArray array = new JSONArray();
            array.add("2");
            JSONObject object = new JSONObject();
            object.put("query", "");
            object.put("chief_org", new JSONArray());
            object.put("classify", new JSONArray());
            object.put("selling_type", new JSONArray());
            object.put("status", array);
            object.put("appid", 10000);
            String result = sendPostUrl("https://next.xuetangx.com/api/v1/lms/get_product_list/?page=1", object.toJSONString());
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray product_list = data.getJSONArray("product_list");
            product_list.forEach(json -> {
                Product product = JSONObject.parseObject(json.toString(), Product.class);
                System.out.println("product：" + product);
                write("test1.txt", array, 10000, 1);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void write(String fileName, JSONArray status, int appId, int page) {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "ubuntu");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.1.239:9000/");

            Path inFile = new Path("hdfs://192.168.1.238:9000/test2.txt");
            FileSystem hdfs = FileSystem.get(conf);
            try {
                if (hdfs.getStatus() == null) {
                    System.out.println("宕机啦=========");
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
                System.out.println("宕机啦=========");
                return;
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
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray product_list = data.getJSONArray("product_list");
            product_list.forEach(json -> {
                try {
                    Product product = JSONObject.parseObject(json.toString(), Product.class);
                    System.out.println("product：" + product);
                    outputStream.write(product.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * http post请求
     *
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
