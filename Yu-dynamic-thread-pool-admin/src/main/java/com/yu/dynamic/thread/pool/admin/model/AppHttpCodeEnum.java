package com.yu.dynamic.thread.pool.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author yu
 * @description 响应枚举类
 * @date 2024-05-24
 */
@AllArgsConstructor
@Getter
public enum AppHttpCodeEnum {

    // 成功段固定为200
    SUCCESS(200,"操作成功"),
    // 登录段1~50
    NEED_LOGIN(1,"需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2,"密码错误"),

    // TOKEN50~100
    TOKEN_INVALID(50,"无效的TOKEN"),
    TOKEN_EXPIRE(51,"TOKEN已过期"),
    TOKEN_REQUIRE(52,"TOKEN是必须的"),

    // 参数错误 500~1000
    PARAM_REQUIRE(500,"缺少参数"),
    PARAM_INVALID(501,"无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502,"图片格式有误"),
    SERVER_ERROR(503,"服务器内部错误"),


    // 数据错误 1000~2000
    DATA_EXIST(1000,"数据已经存在"),
    OVER_SIZE(1001,"文件大小不能超过3M"),
    UNSELECTED_FILE(1002,"未选择文件"),
    READ_EXCEL_ERROR(1003,"读取Excel文件失败"),
    CHECK_PASSWORD_ERROR(1004,"密码校验异常"),
    LOGIN_URL_ERROR(1004,"Url登陆请求异常"),
    UNZIP_ERROR(1004,"解压文件异常"),
    DATE_ERROR(1004, "日期处理日常"),
    PICTURE_IO_FILE_ERROR(1004, "图片上传读写流异常"),
    DOWNLOAD_ZIP_ERROR(1004,"下载zip文件失败"),
    ZIP_NOT_EXISTS(1004,"压缩文件不存在"),
    SEARCH_ZIP_ERROR(1004,"搜索zip文件失败"),
    NOT_FOUND_DATA(1004,"未找到文件数据"),
    DATA_NOT_EXIST(1002,"数据不存在"),

    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000,"无权限操作"),
    NEED_ADMIND(3001,"需要管理员权限"),

    FILE_TYPE_ERROR(3002,"文件类型错误");

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

}