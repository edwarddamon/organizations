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
 * @date 2021/3/16
 */
@Data
@Builder
public class OrgBoardLIstInfoResponse implements Serializable {
    private Long boaId;
    /**
     * 公告内容
     */
    private String boaContent;

    /**
     * 已读人数
     */
    private Integer boaViews;

    /**
     * 发布者id
     */
    private Long boaUserId;

    /**
     * 发布者头像地址
     */
    private String boaUserAvatar;

    /**
     * 发布者昵称
     */
    private String boaUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
