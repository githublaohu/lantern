/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.api.mode;

import lombok.Data;

@Data
public class AuthResultObject {

	public static AuthResultObject create() {
		return new AuthResultObject();
	}

	private UserInfo userInfo;

	private PlatformUserInfo platformUserInfo;

	private EnterpriseAttestationInfo enterpriseAttestationInfo;

	private OrganizationInfo organizationInfo;

	private String errorMessage;
}
