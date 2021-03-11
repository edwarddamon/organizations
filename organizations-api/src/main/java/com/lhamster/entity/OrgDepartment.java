package com.lhamster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrgDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dep_id", type = IdType.AUTO)
    private Long depId;

    /**
     * 部门名
     */
    private String depName;

    /**
     * 部长id
     */
    private Long depMinisterId;

    /**
     * 副部长id
     */
    private Long depViceMinisterId;

    /**
     * 社团id
     */
    private Long depOrganizationId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
