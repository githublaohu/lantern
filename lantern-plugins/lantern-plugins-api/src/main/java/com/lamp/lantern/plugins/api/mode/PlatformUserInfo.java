package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.config.LoginType;
import lombok.Data;

@Data
public class PlatformUserInfo implements java.io.Serializable{
    private Long puiId;

    private Long puiUserId;

    private Long puiOpenId;

    private Long puiUnionId;

    private LoginType puiType;

    private String puiAuthchannel;
}
