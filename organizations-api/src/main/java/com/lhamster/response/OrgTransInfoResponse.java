package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/16
 */
@ApiModel(value = "用户信息透出")
@Data
@Builder
public class OrgTransInfoResponse implements Serializable {
    private Long traId;

    /**
     * 变动金额
     */
    private Long traChangeAmount;

    /**
     * 变动原因
     */
    private String traReason;

    /**
     * 当前余额
     */
    private Long traAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
