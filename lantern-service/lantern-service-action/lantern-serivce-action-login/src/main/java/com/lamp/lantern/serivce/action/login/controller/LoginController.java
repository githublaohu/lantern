//package com.lamp.lantern.serivce.action.logon.controller;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.allitetech.vaast.common.thirdParty.WeChatEmpower;
//import com.allitetech.vaast.common.thirdParty.response.EmpowerResponse;
//import com.allitetech.vaast.entity.ThirdpartyInformation;
//import com.allitetech.vaast.entity.UserInfo;
//import com.allitetech.vaast.entity.UserLoginRecord;
//import com.allitetech.vaast.entity.model.SoftwareInfo;
//import com.allitetech.vaast.service.ThirdpartyInformationService;
//import com.allitetech.vaast.service.UserInfoService;
//import com.allitetech.vaast.service.UserLoginRecordService;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@RestController
//@RequestMapping("userInfo") // 接口注解
//@ConfigurationProperties(value = "vaast.software")
//public class LoginController {
//
//	@Reference(url = "127.0.0.1:22131")
//	private ThirdpartyInformationService thirdpartyInformationService;
//
//	@Reference(url = "127.0.0.1:22131")
//	private UserInfoService userInfoService;
//
//	@Reference(url = "127.0.0.1:22131")
//	private UserLoginRecordService userLoginRecordService;
//
//	private WeChatEmpower weChatEmpower;
//
//	private Map<String, SoftwareInfo> softwareInfoMap = new HashMap<>();
//
//	private Map<String,Map<String,String>> map = new HashMap<>();
//
//	@PostConstruct
//	private void init() {
//		String data = map.get("").get("url");
//		//RetrofitManager retrofitManager = new RetrofitManager("https://api.weixin.qq.com");
//		//weChatEmpower = retrofitManager.create(WeChatEmpower.class);
//	}
//
//	@PostMapping("/thirdPartyLogin/{appId}")
//	public String thirdPartyLogin(@RequestBody UserInfo userInfo,
//			@Validated @NotNull(message = "appId 不能为空") @PathVariable String appId,
//			@RequestHeader("x-forwarded-for") String requesIP) throws IOException {
//		// 获得app信息
//		SoftwareInfo softwareId = softwareInfoMap.get(appId);
//		// 微信调用？
//		EmpowerResponse empowerResponse = weChatEmpower.empower(appId, softwareId.getSiSecret(), userInfo.getUiExtend()).execute().body();
//		// 判断用户是否重复登录，判断？
//
//		// 查询第三方登录
//		ThirdpartyInformation thirdpartyInformation = new ThirdpartyInformation();
//		thirdpartyInformation.setTiSoftwareUserId(empowerResponse.getOpenid());
//		thirdpartyInformation.setTiSoftwareId(softwareId.getSiId());
//		thirdpartyInformation.setTiSoftwareName(softwareId.getSiName());
//
//		ThirdpartyInformation newTthirdpartyInformation = thirdpartyInformationService
//				.queryThirdpartyInformationBySoftwareUserId(thirdpartyInformation);
//
//		// 是否保证只有一个用户登录？
//
//		// 是否有登录
//		UserInfo newUserInfo = null;
//		if (Objects.isNull(newTthirdpartyInformation)) {
//			// 创建用户信息
//			newUserInfo = new UserInfo();
//			newUserInfo.setUiName("打杂");
//			//
//			newUserInfo.setUiNickname("打杂");
//			newUserInfo.setUiHeadPortrait("11111");
//			newUserInfo.setUiSex("男");
//			newUserInfo.setUiAddress("1");
//			newUserInfo.setUiAge(1);
//			newUserInfo.setUiType("用户");
//			newUserInfo.setUiIdCard("123111111");
//			newUserInfo.setUiPhone("1111");
//			newUserInfo.setUiEmail("11");
//			newUserInfo.setUiBirth("1");
//			newUserInfo.setUiPhone(System.currentTimeMillis() + "");
//			userInfoService.insertUserInfo(newUserInfo);
//			// 添加第三方信息
//			thirdpartyInformation.setUiId(newUserInfo.getUiId());
//			thirdpartyInformation.setTiPlatformId(1L);
//			thirdpartyInformation.setTiPlatformName("wechat");
//			thirdpartyInformation.setTiSoftwareId(softwareId.getSiId());
//			thirdpartyInformation.setTiSoftwareName(softwareId.getSiName());
//			thirdpartyInformation.setTiPlatformUserId(empowerResponse.getUnionid());
//			thirdpartyInformation.setTiSoftwareUserId(empowerResponse.getOpenid());
//			thirdpartyInformationService.insertThirdpartyInformation(thirdpartyInformation);
//
//			// 添加账户?
//		} else {
//			// 查询用户信息
//			userInfo = new UserInfo();
//			thirdpartyInformation.setUiId(userInfo.getUiId());
//			newUserInfo = userInfoService.queryUserInfoById(userInfo);
//		}
//		// 创建token？
//
//		// 写入缓存？
//		UserLoginRecord userLoginRecord = new UserLoginRecord();
//		userLoginRecord.setUiId(newUserInfo.getUiId());
//		userLoginRecord.setUlIp(requesIP);
//		userLoginRecord.setUlLoginLocal("");
//		userLoginRecord.setUlLoginTerminal("微信小程序");
//		userLoginRecord.setUlLoginWay("微信小程序登录");
//		userLoginRecord.setUlLoginSystem(softwareId.getSiId() + "");
//		// 登录日志
//		userLoginRecordService.insertUserLoginRecordAsync(userLoginRecord);
//
//		return null;
//	}
//
//	@PostMapping("login")
//	public String login() {
//		// 判断用户是否重复登录，判断？
//
//		// 查询第三方登录
//
//		// 是否保证只有一个用户登录？
//
//		// 是否有登录
//		if (Objects.isNull(null)) {
//			// 创建用户信息
//
//			// 添加第三方信息
//
//			// 添加账户
//		} else {
//
//		}
//		// 创建token？
//
//		// 写入缓存？
//
//		// 登录日志
//
//		return null;
//	}
//
//}
package com.lamp.lantern.serivce.action.login.controller;

public class LoginController{

}
