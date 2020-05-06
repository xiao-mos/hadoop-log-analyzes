package com.luzz;

import com.luzz.common.HadoopContext;
import com.luzz.common.TimerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

/**
 * 项目启动类
 * @author luzz
 * @date 2020-03-09
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class HadoopLogAnalyzesApplication {
	public static void main(String[] args) {
		try {
			Properties properties = System.getProperties();
			properties.setProperty("HADOOP_USER_NAME", "ubuntu");
			SpringApplication.run(HadoopLogAnalyzesApplication.class, args);
			// 初始化hadoop 集群配置
			HadoopContext.init();
			// 初始化定时任务
			TimerContext.init();
			log.info("启动成功======");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
