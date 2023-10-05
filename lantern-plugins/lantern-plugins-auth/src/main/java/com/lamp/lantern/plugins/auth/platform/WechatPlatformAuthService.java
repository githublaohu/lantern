//package com.lamp.lantern.plugins.auth.platform;
//
//import java.security.AlgorithmParameters;
//import java.security.Security;
//import java.util.Arrays;
//import java.util.Base64;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import com.lamp.lantern.plugins.api.mode.AuthResultObject;
//import com.lamp.lantern.plugins.api.mode.UserInfo;
//import com.lamp.lantern.plugins.api.service.AbstractAuthService;
//import com.lamp.light.Light;
//import com.lamp.light.api.http.annotation.method.GET;
//import com.lamp.light.api.http.annotation.parameter.Path;
//
//import lombok.Data;
//
//public class WechatPlatformAuthService extends AbstractAuthService {
//
//	private WechatAuthAPI wechatAuthAPI;
//
//	public void doInitialization() throws Exception {
//		// api.weixin.qq.com
//		Light light = Light.Builder().tls(true).host(this.config.getServiceAddress()).build();
//		this.wechatAuthAPI = light.create(WechatAuthAPI.class);
//	}
//
//	@Override
//	public AuthResultObject auth(UserInfo userInfo) {
//		// https://www.it610.com/article/1294173597299974144.htm
//		wechatAuthAPI.empower(this.config.getAppId(), this.config.getSecretAccessKey(), userInfo.getToken());
//		return null;
//	}
//
//	@Override
//	public AuthResultObject getUserInfo(UserInfo userInfo) {
//		// https://www.it610.com/article/1294173597299974144.htm
//		// {"nickName":"Band","gender":1,"language":"zh_CN","city":"Guangzhou","province":"Guangdong","country":"CN","avatarUrl":"http://wx.qlogo.cn/mmopen/vi_32/1vZvI39NWFQ9XM4LtQpFrQJ1xlgZxx3w7bQxKARol6503Iuswjjn6nIGBiaycAjAtpujxyzYsrztuuICqIM5ibXQ/0"}HyVFkGl5F5OQWJZZaNzBBg==
//		return null;
//	}
//
//	public void getUserInfo(String encryptedData, String sessionkey, String iv) {
//
//		// 被加密的数据
//		byte[] dataByte = Base64.getDecoder().decode(encryptedData);
//
//		// 加密秘钥
//		byte[] keyByte = Base64.getDecoder().decode(sessionkey);
//
//		// 偏移量
//		byte[] ivByte = Base64.getDecoder().decode(iv);
//		try {
//			// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
//			int base = 16;
//			if (keyByte.length % base != 0) {
//				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
//				byte[] temp = new byte[groups * base];
//				Arrays.fill(temp, (byte) 0);
//				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
//				keyByte = temp;
//
//			}
//			Security.addProvider(new BouncyCastleProvider());
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
//			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
//			parameters.init(new IvParameterSpec(ivByte));
//			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
//			byte[] resultByte = cipher.doFinal(dataByte);
//			if (null != resultByte && resultByte.length > 0) {
//				String result = new String(resultByte, "UTF-8");
//				return JSONObject.parseObject(result);
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//	}
//
//	interface WechatAuthAPI {
//
//		/**
//		 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
//		 *
//		 * @param appId
//		 * @param secret
//		 * @param code
//		 * @return
//		 */
//		@GET("/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type=authorization_code")
//		public EmpowerResponse empower(@Path("appId") String appId, @Path("secret") String secret,
//				@Path("code") String code);
//	}
//
//	@Data
//	public class EmpowerResponse {
//
//		private String openid;
//
//		private String unionid;
//
//		private String session_key;
//
//		private Integer errcode;
//
//		private String errmsg;
//	}
//}
