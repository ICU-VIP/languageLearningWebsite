package org.cdtu.website.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpResult<T> {
    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static final Integer INFO_CODE = 2;
    public static final Integer SUCCESS_CODE = 1;
    public static final Integer ERROR_CODE = 0;
    public static final Integer UNAUTHORIZED = 401;
    public static <T> HttpResult<T> success(T object) {
        return HttpResult.<T>builder().code(SUCCESS_CODE).msg("操作成功").data(object).build();
    }

    public static <T> HttpResult<T> error(String msg) {
        return HttpResult.<T>builder().code(ERROR_CODE).msg(msg).build();
    }
}
