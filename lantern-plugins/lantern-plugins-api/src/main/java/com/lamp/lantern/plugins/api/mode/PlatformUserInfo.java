package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import lombok.Data;

@Data
public class PlatformUserInfo extends OperateInfoInjection implements java.io.Serializable{
    private Long puiId;

    private Long puiUserId;

    private Long puiOpenId;

    private Long puiUnionId;

    private LoginType puiType;

    private String puiAuthchannel;
}
