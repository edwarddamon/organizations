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
public class OrgUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户头像地址
     */
    private String userAvatar;

    /**
     * 用户名
     */
    private String userUsername;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * QQ号
     */
    private String userQq;

    /**
     * 微信
     */
    private String userVx;

    /**
     * 用户状态
     */
    private String userStatus;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;


}
