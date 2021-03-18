package com.lhamster.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class OrgNewsListInfoResponse implements Serializable {
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
     * 新闻浏览量
     */
    private Integer newViews;

    /**
     * 社团id
     */
    private Long newOrganizationId;

    /**
     * 社团名
     */
    private String newOrganizationName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
