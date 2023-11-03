package com.lamp.lantern.service.core.entity;

import java.io.Serializable;

import com.lamp.lantern.plugins.api.mode.UserInfo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper= true)
@ApiModel(value = "UserInfoEntity", description = "用户信息实体")
public class UserInfoEntity extends UserInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1081806036041678567L;


}
