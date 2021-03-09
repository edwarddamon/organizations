package com.lhamster.response.result;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@Data
@NoArgsConstructor
public class Response<T> {
    private Boolean result;
    private String msg;
    private Integer count; // 返回的总数据数目
    private T data; // 存放返回的数据

    public Response(Boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public Response(Boolean result, String msg, Integer count) {
        this.result = result;
        this.msg = msg;
        this.count = count;
    }

    public Response(Boolean result, String msg, T data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
