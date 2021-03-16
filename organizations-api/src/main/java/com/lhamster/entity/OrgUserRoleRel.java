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
public class OrgUserRoleRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系表id
     */
    @TableId(value = "rel_id", type = IdType.AUTO)
    private Long relId;

    private Long relUserId;

    private Long relRoleId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
