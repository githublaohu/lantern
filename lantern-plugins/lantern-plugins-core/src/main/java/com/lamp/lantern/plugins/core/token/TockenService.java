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
package com.lamp.lantern.plugins.core.token;

import com.lamp.lantern.plugins.core.token.create.EncryptionTokenService;
import com.lamp.lantern.plugins.core.token.create.RandomTokenService;
import com.lamp.lantern.plugins.core.token.create.UUidTokenService;

public class TockenService {
	
	public TockenService() {
		
	}
	
	public TokenCreateService  createToken(TokenConfig tokenConfig) {
		if(tokenConfig.getTokenCreateMode() == TokenCreateMode.UUID) {
			return new UUidTokenService();
		}else if(tokenConfig.getTokenCreateMode() == TokenCreateMode.RANDOMSTRING) {
			return new RandomTokenService();
		}else if(tokenConfig.getTokenCreateMode() == TokenCreateMode.ENCRYPTION) {
			EncryptionTokenService tockenCreateService = new EncryptionTokenService();
			tockenCreateService.config(tokenConfig);
			return tockenCreateService;
		}
		return new UUidTokenService();
		
	}

}
