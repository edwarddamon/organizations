package com.lhamster.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/18
 */
@Data
@Builder
public class OrgNewsInfoResponse implements Serializable {
    /**
     * 新闻id
     */
    private Long newId;

    /**
     * 新闻标题
     */
    private String newTitle;

    /**
     * 新闻封面地址
     */
    private String newAvatar;

    /**
     * 新闻内容
     */
    private String newContent;

    /**
     * 新闻浏览量
     */
    private Integer newViews;

    /**
     * 社团id
     */
    private Long newOrganizationId;

    /**
     * 社团封面
     */
    private String newOrganizationAvatar;

    /**
     * 社团名
     */
    private String newOrganizationName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    /**
     * 评论response
     */
    private List<OrgComInfoResponse> comInfoResponse;
}
