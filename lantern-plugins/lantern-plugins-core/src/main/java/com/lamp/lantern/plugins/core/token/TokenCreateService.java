package com.lamp.lantern.plugins.core.token;

public interface TokenCreateService {
	
	public default void config(TokenConfig tokenConfig ) {
		
	}

    public String createToken(TokenConstructData tokenConstructData);

    public default long verifyToken(String token) {
    	return 0;
    }
}
