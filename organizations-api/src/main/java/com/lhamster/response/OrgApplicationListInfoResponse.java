package com.lhamster.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
@Data
@Builder
public class OrgApplicationListInfoResponse implements Serializable {
    /**
     * 入社申请id
     */
    private Long appId;

    /**
     * 申请状态（undetermined、AGREE、REFUSE）
     */
    private String appStatus;

    /**
     * 申请理由
     */
    private String appApplicationReason;

    /**
     * 拒绝理由
     */
    private String appRefuseReason;

    /**
     * 申请的社团id
     */
    private Long appOrgId;

    /**
     * 申请的社团名
     */
    private String appOrgName;

    /**
     * 申请的社团封面地址
     */
    private String appOrgAvatar;

    /**
     * 申请用户id
     */
    private Long appUserId;

    /**
     * 申请用户名
     */
    private String appUserName;

    /**
     * 申请用户头像
     */
    private String appUserAvatar;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
