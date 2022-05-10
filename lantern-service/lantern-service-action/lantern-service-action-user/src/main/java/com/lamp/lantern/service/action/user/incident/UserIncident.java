package com.lamp.lantern.service.action.user.incident;


import com.lamp.lantern.service.action.user.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import com.lamp.lantern.service.action.login.security.HttpSessionService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Builder
public class UserIncident {

    private UserInfoEntityService userInfoEntityService;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private UserInfoEntity userInfoEntity;

    private ResultObjectEnums resultObjectEnums;

    private LoginPatternEnum loginPatternEnum;

    private HttpSessionService httpSessionService;

    public ResultObjectEnums getResultObjectEnums(){
        return resultObjectEnums;
    }


    public void changeUserPassword() {
        // userInfoEntity 现在是已经登录的账户 所以可以通过 ui_id 定位到
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserId(this.userInfoEntity);
        if(Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.UNKONWN_ERROR;
            return;
        }

        if(Objects.equals(userInfoEntity.getUiPassword(), queryUserInfoEntity.getUiPassword())){
            resultObjectEnums = ResultObjectEnums.CHANGE_PASSWORD_SAME_AS_OLD;
            return;
        }

        queryUserInfoEntity.setUiPassword(userInfoEntity.getUiPassword());
        String uiSalt = queryUserInfoEntity.getUiSalt();
        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
        queryUserInfoEntity.setUiSaltPassword(uiSaltPassword);
        userInfoEntityService.updateUserAllField(queryUserInfoEntity);
    }

    public void changeUserHeadPortrait(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserId(this.userInfoEntity);
        if(Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.UNKONWN_ERROR;
            return;
        }

        queryUserInfoEntity.setUiHeadPortrait(userInfoEntity.getUiHeadPortrait());

        userInfoEntityService.updateUserAllField(queryUserInfoEntity);

    }

    public void changeUserNickname(){
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserByUserId(this.userInfoEntity);

        if(Objects.equals(queryUserInfoEntity, null)){
            resultObjectEnums = ResultObjectEnums.UNKONWN_ERROR;
            return;
        }

        if(Objects.equals(userInfoEntity.getUiNickname(), queryUserInfoEntity.getUiNickname())){
            resultObjectEnums = ResultObjectEnums.CHANGE_NICKNAME_SAME_AS_OLD;
            return;
        }

        queryUserInfoEntity.setUiNickname(userInfoEntity.getUiNickname());
        userInfoEntityService.updateUserAllField(userInfoEntity);

    }

    public void logout(){
        httpSessionService.logoutAndDeleteSession(loginPatternEnum, userInfoEntity.getUiId());
    }
}
