package com.luzz.enums;

/**
 * 返回结果枚举类
 */
public enum ResultsCode {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(0, "未知错误"),
    /**
     * 成功
     */
    SUCCESS(200, "返回正常"),
    /**
     * 无数据返回
     */
    NOT_DATA(201, "无数据返回"),
    /**
     * 超时
     */
    OUT_TIME(500, "超时"),
    ;

    private int code;
    private String description;

    ResultsCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
