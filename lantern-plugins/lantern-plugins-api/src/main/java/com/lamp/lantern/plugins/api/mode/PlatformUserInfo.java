package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PlatformUserInfo extends OperateInfoInjection implements java.io.Serializable{
    private Long puiId;

    private Long systemId;

    private Long userId;

    private String corporationId;

    private String appId;

    private String puiOpenId;

    private String puiUnionId;

    /**
     * LoginType(Enum)
     */
    private LoginType puiType;

    /**
     * Github, Alipay Wechat Taobao QQ
     */
    private String puiAuthChannel;

    /**
     * 是否禁止使用该信息登录，默认0不禁止
     */
    private Integer puiStatus;

    private LocalDateTime puiCreateTime;

    private Long puiCreateUserId;

    private LocalDateTime puiUpdateTime;

    private Long puiUpdateUserId;

    private Integer isDelete;

}
