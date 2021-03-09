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
public class OrgUserOrganizationRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rel_id", type = IdType.AUTO)
    private Long relId;

    /**
     * 用户id
     */
    private Long relUserId;

    /**
     * 社团id
     */
    private Long relOrganizationId;

    /**
     * 角色id
     */
    private Long relRoleId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
