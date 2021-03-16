package com.lhamster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Builder;
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
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrgBoards implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "boa_id", type = IdType.AUTO)
    private Long boaId;

    /**
     * 封面地址
     */
    private String boaAvatar;

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
     * 社团id
     */
    private Long boaOrgId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
