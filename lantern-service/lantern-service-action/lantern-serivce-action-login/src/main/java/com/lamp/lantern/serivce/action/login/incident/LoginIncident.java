package com.lamp.lantern.serivce.action.login.incident;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.serivce.action.login.LoginPatternEnums;
import com.lamp.lantern.serivce.action.login.security.SessionFactory;
import com.lamp.lantern.serivce.action.login.support.platform.PlatformTransfer;
import com.lamp.lantern.serivce.action.login.support.tripartite.TripartiteTransfer;
import com.lamp.lantern.serivce.action.login.utils.JWTUtils;
import com.lamp.lantern.serivce.action.login.utils.LoginUtils;
import com.lamp.lantern.serivce.action.login.utils.IpUtils;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.enums.*;

import com.lamp.lantern.service.core.service.LoginRecordService;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.lamp.lantern.service.core.service.UserInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 依赖比较多，如果使用ioc比较简单方便
 * 如果使用spring，ioc的性能消耗大
 * @author laohu
 *
 */
@Slf4j
@Builder
public  class LoginIncident {


	UserInfoService userInfoService;

	LoginRecordService loginRecordService;

	LoginPatternEnums loginPattern;

	UserInfoEntity userInfoEntity;

	ResultObject resultObject;

	HttpServletRequest request;

	// 用户平台登录 认证 信息获取
	PlatformTransfer platformTransfer;

	// 用户第三方登录 将会引起的操作
	TripartiteTransfer tripartiteTransfer;

	StringRedisTemplate stringRedisTemplate;

	HttpServletResponse response;


	public ResultObject getResultObject(){
		return this.resultObject;
	}



	// 一方登录暂不实现
	public void getLoginAppInfo(){

	}

	// 一方登录暂不实现
	public void getTripartiteAuthorInfo(){

	}

	public void partyTokenLogin(){


	}

	public void partyAccountLogin(){

		UserInfoEntity queryUserInfoEntity = userInfoService.queryUserInfoByLogin(userInfoEntity);
		if (Objects.isNull(queryUserInfoEntity)) {
			resultObject = new ResultObject(20000,"用户或者密码输入错误");
			return;
		}

		String uiSaltPassword = DigestUtils.md5Hex(queryUserInfoEntity.getUiSalt() + userInfoEntity.getUiPassword());

		if(!Objects.equals(uiSaltPassword, queryUserInfoEntity.getUiSaltPassword())){
			resultObject = new ResultObject(20000, "用户或者密码输入错误");
			return;
		}

	}

	public void partyPhoneVerifyCodeLogin(){

		verficationCodeCheck();



	}

	public void invokePatternLogin() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String party = loginPattern.getParty();
		String channel = loginPattern.getChannel();
		String pattern = party + channel.substring(0,1).toUpperCase() + channel.substring(1) + "Login";
		Method loginMethod = this.getClass().getMethod(pattern);
		loginMethod.invoke(this);
	}

	public void partyLogin(){

		// 改成 硬编码
		// 通过LoggingPattern 和 完成登录认证
		try{
			invokePatternLogin();
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return;
		}


		// 登录错误次数校验
		loginTimesCheck();

		// 黑名单校验
		blacklistCheck();

		// 登录地址校验 现在是按照IP地址来校验 (要修改不能按照Ip验证)
		loginAddressCheck();

		// 新增登录日志
		insertUserLoginRecord();

		generateToken();

		// 缓存用户信息 (将用户信息从sql放入到Redis中)
		cacheUserInfo();

		//

	}


	// 一方登录暂时不实现
	public void secondPartyLogin(){


	}

	// 一方登录暂时不实现
	public void tripartiteLogin(){


	}


	public void getPlatformInfo(){


	}

	/**
	 * 1. 平台登录一定传AppId
	 * 2. 如果是网站登录，读取地址
	 * 3. 如果app，终端，可以把唯一表达式写到代码里面
	 */
	public void getTripartiteInfo() {

	}

	public void  blacklistCheck(){

		// 黑名单校验要走表来查

		if(resultObject.getCode() == 200 && userInfoEntity.getAllowLogin() == StatusEnum.INACTIVE) {
			resultObject = new ResultObject(20000, "单日登录错误次数已达5次，请完成安全认证在登录");
		}
	}

	public void loginTimesCheck(){

		// 查询用户当日登录失败次数，如果大于5次则加入黑名单
		// 如果完成认证登录， 则计算完成认证后的登录错误次数 （需补充）
		int errorTimes = loginRecordService.queryUserValidLoginRecordToday(userInfoEntity);
		if(errorTimes >=5){
			userInfoEntity.setAllowLogin(StatusEnum.INACTIVE);
			userInfoService.updateUserInfoByUiId(userInfoEntity);
		}

	}

	public void verficationCodeCheck(){

		// 验证码 和 二维码登录

		String verifyCode = request.getParameter("verifyCode");
		String authCode = stringRedisTemplate.opsForValue().get(userInfoEntity.getUiPhone());
		UserInfoEntity queryUserInfoEntity = userInfoService.queryUserInfoByPhone(userInfoEntity);

		if(StringUtils.isEmpty(authCode)){
			resultObject = new ResultObject(20000, "验证码已经过期");
		}else if(! "".equals(authCode) && !authCode.equals(verifyCode)){
			resultObject = new ResultObject(20000, "验证码错误");
		}else{
			resultObject = new ResultObject(200, "success");
			userInfoEntity = queryUserInfoEntity;
		}


	}

	// 登录地址校验不需要
	public void loginAddressCheck(){
		List<String> loginedIps = userInfoService.queryLoginedIpByUiID(userInfoEntity);
		String loginIp = request.getRemoteAddr();
		if(! loginedIps.contains(loginIp)){
			resultObject = new ResultObject(20000, "你在新设备上登录，请验证！");
		}
	}


	public void getPartyUserInfo(){


	}

	public void getTripartiteUserInfo(){


	}

	public void generateToken(){
		if(resultObject.getCode() != 200){
			return;
		}

		String token = JWTUtils.createToken(userInfoEntity.getUiId(),
				userInfoEntity.getUiName(), "jaycase", 7 * 24 * 20);

		userInfoEntity.setUiToken(token);

		userInfoService.updateUserInfo(userInfoEntity);

		response.addHeader("Authorization", token);

	}


	public void insertUser(){

		if(userInfoService.queryUserInfoByLogin(userInfoEntity) == null){
			userInfoService.insertUserInfoEntity(userInfoEntity);
		}


	}

	public void insertTripartiteInfo(){


	}

	public void queryUser(){


	}

	public void updateUserToken(){
		generateToken();
	}

	public void cacheUserInfo(){

		SessionFactory.getInstance().setCache(userInfoEntity.getUiToken(), userInfoEntity);

	}

	public void getUserRoles(){


	}

	public void getUserPermissions(){


	}

	// 确定登录事件 / 注册事件是一个方式使用一个还是公用一套


	public void userLoginIncident(){


	}

	public void userRegisterIncident(){


	}

	public void insertUserLoginRecord(){

		// 写入登录日志 分为登录成功
		LoginRecordEntity loginRecord = new LoginRecordEntity();
		loginRecord.setUiId(userInfoEntity.getUiId());
		loginRecord.setUlLoginTime(new Date());
		loginRecord.setUlLoginIp(IpUtils.getIpAddr(request));
		loginRecord.setUlLoginAddress(IpUtils.getCityInfo(request));
		loginRecord.setUlLoginDevice(LoginUtils.getDeviceType(request));
		loginRecord.setUlLoginDeviceModel(LoginUtils.getDeviceModel(request));
		loginRecord.setUlLoginSystem(LoginUtils.getOperateSystemType(request));
		loginRecord.setUlLoginWay(LoginUtils.getLoginWayType(loginPattern));
		loginRecord.setUlLoginTerminal(LoginUtils.getTerminalType(request));
		// 退出时间 退出方式 退出时修改

		// 第三方Id 需要补充

		if(resultObject.getCode() == 200){

			// 将成功登录写入日志
			loginRecord.setUlLoginStatus(StatusEnum.ACTIVE);

		}else{

			// 将登录日志写入日志
			loginRecord.setUlLoginStatus(StatusEnum.INACTIVE);
		}

		loginRecordService.insertLoginRecord(loginRecord);
	}

	public void getUserInfo() {

	}

	public void getPhone() {

	}

	public void identityCard() {

	}

	public void getRedirectUrl() {
		// 跳转登录界面 或者 扫码界面

	}


	public void checkoutToken(){
		String token = request.getAuthType();

		Claims claim = JWTUtils.parseToken(token, "jaycase");
		if(Objects.isNull(claim)){
			resultObject = new ResultObject(20000,"Token已经过期，请重新登录");

		}else{
			String uiId = claim.getId();
			resultObject = new ResultObject(200, "login success");
			userInfoEntity = userInfoService.queryUserInfoByLogin(userInfoEntity);
		}

	}

	public void tokenLogin(){
		checkoutToken();

		// 登录错误次数校验
		loginTimesCheck();

		// 黑名单校验
		blacklistCheck();

		// 登录地址校验 现在是按照IP地址来校验 (要修改不能按照Ip验证)
		loginAddressCheck();

		// 新增登录日志
		insertUserLoginRecord();

		generateToken();

		// 缓存用户信息 (将用户信息从sql放入到Redis中)
		cacheUserInfo();



	}

}
