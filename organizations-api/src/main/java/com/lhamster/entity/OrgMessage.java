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
public class OrgMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "mes_id", type = IdType.AUTO)
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
     * 发布通知者id
     */
    private Long mesUserId;

    /**
     * 被通知者id
     */
    private Long mesTargetId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
