package com.lhamster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Edward
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrgNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新闻id
     */
    @TableId(value = "new_id", type = IdType.AUTO)
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

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
