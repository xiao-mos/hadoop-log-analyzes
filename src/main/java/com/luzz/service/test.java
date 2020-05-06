package com.luzz.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luzz.entity.Product;
import com.luzz.util.HttpUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public class test {
    private static String nameNode = "hdfs://192.168.1.238:9000";

    public static void main(String[] args) {

        try {
//            download("product1.txt", "E:\\test2.txt");
            write();

            /*Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "ubuntu");

            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.1.238:9000/");
            FileSystem fs = FileSystem.get(conf);
            Path src = new Path("E:\\luzz\\test3.txt");
            Path dest = new Path("/");
            fs.copyFromLocalFile(src, dest);
            Path srcPath = new Path("hdfs://192.168.1.238:9000/test3.txt");
            InputStream in = null;
            in = fs.open(srcPath);
            //复制到标准输出流
            IOUtils.copyBytes(in, System.out, 4096, false);
            IOUtils.closeStream(in);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void write() {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "ubuntu");

            Configuration conf=new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.1.238:9000");

            Path inFile = new Path("hdfs:/product2.txt");
            FileSystem hdfs = FileSystem.get(conf);
            FSDataOutputStream outputStream=hdfs.create(inFile);

            JSONArray array = new JSONArray();
            array.add("2");
            JSONObject object = new JSONObject();
            object.put("query", "");
            object.put("chief_org", new JSONArray());
            object.put("classify", new JSONArray());
            object.put("selling_type", new JSONArray());
            object.put("status", array);
            object.put("appid", 10000);
            String result = HttpUtils.sendPostUrl("https://next.xuetangx.com/api/v1/lms/get_product_list/?page=1", object.toJSONString());
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
     * 下载 hdfs上的文件
     * @param local  //本地路径
     * @throws IOException
     */
    public static void download(String fileName , String local) throws IOException {
        String remote = nameNode + "/" + fileName;
        Configuration conf = new Configuration();

        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(nameNode), conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }
}
