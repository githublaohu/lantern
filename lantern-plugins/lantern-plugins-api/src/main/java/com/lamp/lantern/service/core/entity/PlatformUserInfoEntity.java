package com.lamp.lantern.service.core.entity;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper= true)
public class PlatformUserInfoEntity extends PlatformUserInfo implements Serializable {
    private static final long serialVersionUID = -1081806036041678567L;
}
