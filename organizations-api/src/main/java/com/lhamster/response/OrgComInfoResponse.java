package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/18
 */
@Data
@Builder
public class OrgComInfoResponse implements Serializable {
    private Long comId;

    /**
     * 评论内容
     */
    private String comContent;

    /**
     * 点赞数
     */
    private Integer comGood;

    /**
     * 发布者id
     */
    private Long comUserId;

    /**
     * 发布者昵称
     */
    private String comUserName;

    /**
     * 发布者头像地址
     */
    private String comUserAvatar;


    /**
     * 目标用户id
     */
    private Long comTargetId;

    /**
     * 目标用户昵称
     */
    private String comTargetName;

    /**
     * 目标用户头像地址
     */
    private String comTargetAvatar;

    /**
     * 新闻id
     */
    private Long comNewsId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
