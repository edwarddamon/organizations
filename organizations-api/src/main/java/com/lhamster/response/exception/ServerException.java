package com.lhamster.response.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ServerException extends RuntimeException {
    private Boolean result;
    private String msg;
}
