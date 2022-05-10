package com.lamp.lantern.service.action.login.incident;

import com.lamp.lantern.service.action.login.security.HttpSessionService;
import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;
import com.lamp.lantern.service.action.login.security.JwtTokenService;
import com.lamp.lantern.service.action.login.security.LoginErrorCountService;
import com.lamp.lantern.service.action.login.utils.ParseHttpRequest;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.enums.StatusEnum;
import com.lamp.lantern.service.core.service.LoginRecordEntityService;
import org.apache.commons.lang3.StringUtils;
import com.lamp.lantern.service.action.login.security.RedisService;
import com.lamp.lantern.service.action.login.utils.GenerateRandomVerifyCode;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

/*
* 基本一方登录已经完成 接口基本自测完毕 2022/04/24
* 还存在以下问题：（1）插入日志 主键返回为0 可能造成后面无法对登录记录的ExitTime 和 QuitWay 无法修改
* */


@Slf4j
@Builder
public class LoginIncident {

    private UserInfoEntityService userInfoEntityService;

    private LoginRecordEntityService loginRecordEntityService;

    private RedisService redisService;

    private JwtTokenService jwtTokenService;

    private LoginErrorCountService loginErrorCountService;

    private UserInfoEntity userInfoEntity;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String inputVerifyCode;

    private ResultObjectEnums resultObjectEnums;

    private LoginPatternEnum loginPatternEnum;

    private HttpSessionService httpSessionService;

    private HttpSession httpSession;

    public ResultObjectEnums getResultObjectEnums(){
        return resultObjectEnums;
    }

    public void loginSucessAndSetSession(){
        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        httpSessionService.loginSuccessAndAddSession(loginPatternEnum, userInfoEntity.getUiId(), httpSession);
    }

    public void partyLoginByUserName(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByUserName(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.USERNAME_ERROR;
            return;
        }
        String uiSalt = queryUserInfoEntity.getUiSalt();
        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
        if(Objects.equals(queryUserInfoEntity.getUiSaltPassword(), uiSaltPassword)){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
            userInfoEntity = queryUserInfoEntity;
        }else{
            resultObjectEnums = ResultObjectEnums.PASSWORD_ERROR;
            increaseUserLoginError();
        }
    }

    public void partyLoginByPhone(){

        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByPhone(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.PHONE_ERROR;
            return;
        }
        String uiSalt = queryUserInfoEntity.getUiSalt();
        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
        if(Objects.equals(queryUserInfoEntity.getUiSaltPassword(), uiSaltPassword)){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
            userInfoEntity = queryUserInfoEntity;
        }else{
            resultObjectEnums = ResultObjectEnums.PASSWORD_ERROR;
            increaseUserLoginError();
        }

    }

    public void partyLoginByEmail(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByEmail(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.EMAIL_ERROR;
            return;
        }
        String uiSalt = queryUserInfoEntity.getUiSalt();
        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
        if(Objects.equals(queryUserInfoEntity.getUiSaltPassword(), uiSaltPassword)){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
            userInfoEntity = queryUserInfoEntity;
        }else{
            resultObjectEnums = ResultObjectEnums.PASSWORD_ERROR;
            increaseUserLoginError();
        }
    }

    public void checkUserNameExist(){
        UserInfoEntity userInfoEntity = userInfoEntityService.checkUserExistByUserName(this.userInfoEntity);

        if(userInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.USERNAME_ERROR;
        }
    }

    public void checkEmailExist(){
        UserInfoEntity userInfoEntity = userInfoEntityService.checkUserExistByEmail(this.userInfoEntity);

        if(userInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.EMAIL_ERROR;
        }
    }

    public void checkPhoneExist(){
        UserInfoEntity userInfoEntity = userInfoEntityService.checkUserExistByPhone(this.userInfoEntity);

        if(userInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.PHONE_ERROR;
        }
    }

    public void verifyCodeLogin(){

        verifyCodeCheck();




    }

    public void verifyCodeCheck(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        String cachedVerifyCode = redisService.get(userInfoEntity.getUiPhone());

        if(StringUtils.isEmpty(cachedVerifyCode)){
            resultObjectEnums = ResultObjectEnums.VERIFY_CODE_EXPIRE;
        }else if(! "".equals(cachedVerifyCode) && !inputVerifyCode.equals(cachedVerifyCode)){
            resultObjectEnums = ResultObjectEnums.VERIFY_CODE_WRONG;
        }else{
            resultObjectEnums = ResultObjectEnums.SUCCESS;
            userInfoEntity = userInfoEntityService.checkUserExistByPhone(userInfoEntity);
        }

    }

    public void generateVerifyCode(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        String cachedVerifyCode = GenerateRandomVerifyCode.generateVerifyCode(6);
        redisService.set(userInfoEntity.getUiPhone(), cachedVerifyCode);
        redisService.expire(userInfoEntity.getUiPhone(), 300);
        inputVerifyCode = cachedVerifyCode;

    }

    public void partyLoginByToken(){
        long user_id = jwtTokenService.verifyToken(userInfoEntity.getUiToken());
        userInfoEntity.setUiId(user_id);
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserId(this.userInfoEntity);

        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.TOKEN_ERROR;
            return;
        }else{
            resultObjectEnums = ResultObjectEnums.SUCCESS;
            userInfoEntity = queryUserInfoEntity;
        }



    }

    public void createToken(){
        // 已经登录成功 userInfoEntity 是指向正确的对象
        if(! Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }
        String token = jwtTokenService.createToken(userInfoEntity.getUiName(),
                                                    userInfoEntity.getUiId());
        response.addHeader("token", token);
        userInfoEntity.setUiToken(token);
    }

    public void blackListCheck(){
        // 现在的逻辑是 账号密码是对的 但是错误次数已经被超限
        if(!Objects.equals(ResultObjectEnums.SUCCESS, resultObjectEnums)){
            return;
        }
        if(userInfoEntity.getAllowLogin() == StatusEnum.INACTIVE){
            resultObjectEnums = ResultObjectEnums.LOGIN_ERROR_OVERRUN;
        }
    }

    public void blockUserToBlackList(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserIdOrPhoneOrEmail(this.userInfoEntity);
        queryUserInfoEntity.setAllowLogin(StatusEnum.INACTIVE);
        userInfoEntityService.updateUserAllowLoginField(queryUserInfoEntity);
    }

    public void loginTimesCheck(){
        if(!Objects.equals(ResultObjectEnums.SUCCESS, resultObjectEnums)){
            return;
        }
        int userLoginTimes = loginErrorCountService.countUserLoginError(userInfoEntity);

        if(userLoginTimes >= 5){
            resultObjectEnums = ResultObjectEnums.LOGIN_ERROR_OVERRUN;
            blockUserToBlackList();
        }

    }

    public void increaseUserLoginError(){
        System.out.println(userInfoEntity);
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserIdOrPhoneOrEmail(this.userInfoEntity);
        loginErrorCountService.insertUserLoginError(queryUserInfoEntity);
    }

    public void countLoginErrorTimes(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserIdOrPhoneOrEmail(this.userInfoEntity);
        int i = loginErrorCountService.countUserLoginError(queryUserInfoEntity);
        System.out.println(i);

    }

    public void insertUserLoginRecord(){
        loginRecordEntity.setUiId(userInfoEntity.getUiId());
        loginRecordEntity.setTriId(userInfoEntity.getTriId());
        loginRecordEntity.setUlLoginAddress(ParseHttpRequest.getRemoteAddress(request));
        loginRecordEntity.setUlLoginIp(ParseHttpRequest.getRometeIp(request));
        loginRecordEntity.setUlLoginDevice(ParseHttpRequest.getDeviceEnum(request));
        loginRecordEntity.setUlLoginTerminal(ParseHttpRequest.getTerminalEnum(request));
        loginRecordEntity.setUlLoginDeviceModel(ParseHttpRequest.getDeviceModel(request));
        loginRecordEntity.setUlLoginSystem(ParseHttpRequest.getLoginSystem(request));
        loginRecordEntity.setUlLoginWay(loginPatternEnum);

        loginRecordEntityService.insertLoginRecord(loginRecordEntity);
    }

    public void sureUserNameNotRegister(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByUserName(this.userInfoEntity);
        if(!Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.USERNAME_HAVE_BEEN_REGISTER;
        }

    }

    public void surePhoneNotRegister(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByPhone(this.userInfoEntity);
        if(!Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.PHONE_HAVE_BEEN_REGISTER;
        }
    }

    public void sureEmailNotRegister(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByEmail(this.userInfoEntity);
        if(!Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.EMAIL_HAVE_BEEN_REGISTER;
        }

    }

    public void checkUserNameFilled(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        if(userInfoEntity.getUiName() == null || userInfoEntity.getUiName() == ""){
            String random_username = GenerateRandomVerifyCode.generateRandomPlaceholder(14);
            UserInfoEntity tempUserInfoEntity = new UserInfoEntity();
            tempUserInfoEntity.setUiName(random_username);

            while(!Objects.equals(userInfoEntityService.queryUserByUserName(tempUserInfoEntity), null)){
                random_username = GenerateRandomVerifyCode.generateRandomPlaceholder(14);
                tempUserInfoEntity.setUiName(random_username);
            }
            userInfoEntity.setUiName(random_username);
            userInfoEntity.setUiLackFlag(userInfoEntity.getUiLackFlag() + 1);
        }
    }

    public void checkPhoneFilled(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        if(userInfoEntity.getUiPhone() == null || userInfoEntity.getUiPhone() == ""){
            String random_phone = GenerateRandomVerifyCode.generateRandomPlaceholder(15);
            UserInfoEntity tempUserInfoEntity = new UserInfoEntity();
            tempUserInfoEntity.setUiPhone(random_phone);

            while(!Objects.equals(userInfoEntityService.checkUserExistByPhone(tempUserInfoEntity), null)){
                random_phone = GenerateRandomVerifyCode.generateRandomPlaceholder(15);
                tempUserInfoEntity.setUiPhone(random_phone);
            }
            userInfoEntity.setUiPhone(random_phone);
            userInfoEntity.setUiLackFlag(userInfoEntity.getUiLackFlag() + 2);
        }
    }

    public void checkEmailFilled(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        if(userInfoEntity.getUiEmail() == null || userInfoEntity.getUiEmail() == ""){
            String random_email = GenerateRandomVerifyCode.generateRandomPlaceholder(16);
            UserInfoEntity tempUserInfoEntity = new UserInfoEntity();
            tempUserInfoEntity.setUiEmail(random_email);

            while(!Objects.equals(userInfoEntityService.checkUserExistByEmail(tempUserInfoEntity), null)){
                random_email = GenerateRandomVerifyCode.generateRandomPlaceholder(16);
                tempUserInfoEntity.setUiEmail(random_email);
            }
            userInfoEntity.setUiEmail(random_email);
            userInfoEntity.setUiLackFlag(userInfoEntity.getUiLackFlag() + 4);
        }
    }

    public void checkIdcardFilled(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        if(userInfoEntity.getUiIdcard() == null || userInfoEntity.getUiIdcard() == ""){
            String random_id_card = GenerateRandomVerifyCode.generateRandomPlaceholder(19);
            UserInfoEntity tempUserInfoEntity = new UserInfoEntity();
            tempUserInfoEntity.setUiIdcard(random_id_card);

            while(!Objects.equals(userInfoEntityService.checkUserExistByIdcard(tempUserInfoEntity), null)){
                random_id_card = GenerateRandomVerifyCode.generateRandomPlaceholder(19);
                tempUserInfoEntity.setUiIdcard(random_id_card);
            }
            userInfoEntity.setUiIdcard(random_id_card);
            userInfoEntity.setUiLackFlag(userInfoEntity.getUiLackFlag() + 8);
        }
    }

    public void registerUserInfoEntityToDatabase(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        userInfoEntityService.registerUserInfoEntity(userInfoEntity);


    }

    public void registerUserInfoEntityByUserName(){

        // 一方登录的注册方式  支持用户名 / 手机 / 邮箱 注册
        if(userInfoEntity.getUiName() == "" || userInfoEntity.getUiName() == null){
            resultObjectEnums = ResultObjectEnums.USERNAME_MUST_NOT_BLANK;
            return;
        }

        sureUserNameNotRegister();

        checkPhoneFilled();
        checkEmailFilled();
        checkIdcardFilled();

        registerUserInfoEntityToDatabase();

    }

    public void registerUserInfoEntityByPhone(){

        // 一方登录的注册方式  支持用户名 / 手机 / 邮箱 注册
        if(userInfoEntity.getUiPhone() == "" || userInfoEntity.getUiPhone() == null){
            resultObjectEnums = ResultObjectEnums.PHONE_MUST_NOT_BLANK;
            return;
        }

        surePhoneNotRegister();

        checkUserNameFilled();
        checkEmailFilled();
        checkIdcardFilled();

        registerUserInfoEntityToDatabase();

    }

    public void registerUserInfoEntityByEmail(){

        // 一方登录的注册方式  支持用户名 / 手机 / 邮箱 注册
        if(userInfoEntity.getUiEmail() == "" || userInfoEntity.getUiEmail() == null){
            resultObjectEnums = ResultObjectEnums.EMAIL_MUST_NOT_BLANK;
            return;
        }

        sureUserNameNotRegister();

        checkUserNameFilled();
        checkPhoneFilled();
        checkIdcardFilled();

        registerUserInfoEntityToDatabase();

    }


    // token 是否唯一 这个还有存在的必要嘛
    public void ensureTokenOnly(){}

    public void getUserRoles(){}

    public void getUserLimits(){}

    public void userLoginEvent(){}

    public void userRegisterEvent(){}


    public void getLoginAppInfo(){}

    public void getTriAuthorInfo(){}


    public void secondPartyLogin(){}

    public void triPartiteLogin(){}

    public void getSecondPartyInfo(){}

    public void getSecondPartyUserInfo(){}

    public void getTriPartiteInfo(){}

    public void getTriPartiteUserInfo(){}


    public void insertTriPartiteInfo(){}

    public void queryUserInfoEntity(){}

    public void cachedUserInfoEntity(){}

    // 该方法不在使用
    public void loginAddressCheck(){}

    // 仅测试用 后续需要删除
    private LoginRecordEntity loginRecordEntity;

    public void testInsertLoginRecord(){
        // 插入日志数据库没报错 但是还是存在一个问题：无法返回主key
        Integer integer = loginRecordEntityService.insertLoginRecord(loginRecordEntity);
        System.out.println("pri key " + loginRecordEntity.getUlId());
        System.out.println(integer);

    }


}
