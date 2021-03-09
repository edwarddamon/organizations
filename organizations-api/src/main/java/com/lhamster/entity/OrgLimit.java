package com.lhamster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class OrgLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "lim_id", type = IdType.AUTO)
    private Long limId;

    /**
     * 限制名称
     */
    private String limName;


}
