package com.lamp.lantern.plugins.api.mode;

import lombok.Data;

@Data
public class AuthResultObject {

	public static AuthResultObject create() {
		return new AuthResultObject();
	}

	private UserInfo userInfo;

	private EnterpriseAttestationInfo enterpriseAttestationInfo;

	private OrganizationInfo organizationInfo;

	private String errorMessage;
}
