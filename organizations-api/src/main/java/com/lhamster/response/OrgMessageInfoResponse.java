package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
@Data
@Builder
public class OrgMessageInfoResponse implements Serializable {
    private Long mesId;

    /**
     * 消息内容
     */
    private String mesContent;

    /**
     * 消息状态（UNREAD、READ、DELETED）
     */
    private String mesStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
