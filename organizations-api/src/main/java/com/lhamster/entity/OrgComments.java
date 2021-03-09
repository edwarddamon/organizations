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
public class OrgComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "com_id", type = IdType.AUTO)
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
     * 目标用户id
     */
    private Long comTargetId;

    /**
     * 新闻id
     */
    private Long comNewsId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
