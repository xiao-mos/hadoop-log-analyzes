package com.luzz.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 课程数据实体类
 * @author luzz
 * @date 2020-04-30
 */
@Data
public class Product {
    private long weight_intl;
    private JSONObject val4sort;
    private JSONArray sku_class_time;
    private int selling_status;
    private String sign;
    private JSONArray live_status;
    private long course_id;
    private int weight_multi;
    private int university_id;
    private int selling_type;
    private JSONArray tag_titles;
    private long id;
    private JSONArray classroom_id;
    private boolean sale;
    private JSONArray tag_ids;
    private int creator_source;
    private int enroll_num_v4;
    private String sell_type_name;
    private int branch;
    private JSONArray lecture;
    private String short_intro;
    private JSONArray classify;
    private int status;
    private boolean halt;
    private JSONArray tags;
    private int deleted;
    private JSONArray org_ids;
    private JSONArray classify_name;
    private JSONObject org;
    private String _ut;
    private JSONArray teacher;
    private int count;
    private double weight_plus;
    private String name;
    private String created;
    private String cover;
    private String modified;
    private String ip;
    private JSONArray term_ids;
    private String ext_main_name;
    private JSONArray selling_platform_id;
    private int property;
    private String trailer;
}
