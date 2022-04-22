package com.lamp.lantern.serivce.action.logon.incident;

import com.lamp.lantern.serivce.action.logon.LoginPatternEnums;
import com.lamp.lantern.serivce.action.logon.support.platform.PlatformTransfer;
import com.lamp.lantern.serivce.action.logon.support.tripartite.TripartiteTransfer;

import lombok.Builder;

/**
 * 依赖比较多，如果使用ioc比较简单方便
 * 如果使用spring，ioc的性能消耗大
 * @author laohu
 *
 */
@Builder
public  class LogonIncident {

	
	LoginPatternEnums loginPattern;
	
	PlatformTransfer platformTransfer;
	
	TripartiteTransfer tripartiteTransfer;
	
	
	public void login() {
		
	}
	
	/**
	 * 1. 平台登录一定传AppId
	 * 2. 如果是网站登录，读取地址
	 * 3. 如果app，终端，可以把唯一表达式写到代码里面
	 */
	public void getTripartiteInfo() {
		
	}
	
	
	public void authentication() {
		
	}
	
	public void getTripartiteLoginInfo() {
		
	}
	
	public void createUserInfo() {
		
	}
	
	public void createTripartiteLoginInfo() {
		
	}
	
	public void getUserInfo() {
		
	}
	
	public void getPhone() {
		
	}
	
	public void identityCard() {
		
	}
	
	public void getRedirectUrl() {
		
	}
	
}
