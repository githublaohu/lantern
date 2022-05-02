package com.lamp.lantern.service.action.login.security;

import org.apache.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.Arrays;



@Service
public class WxSignatureCheckServiceImpl implements WxSignatureCheckService {

    //token
    private final String token = "jaycase";

    public String wxSignatureCheck(String signature, String timestamp, String nonce, String echostr) {
        System.out.println(signature + " " + timestamp + " " +nonce + " " + echostr);
        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);

        //排序
        String sortString = sort(token, timestamp, nonce);
        //加密
        String mytoken = Decript.SHA1(sortString);
        //校验签名
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("签名校验通过。");
            System.out.println(echostr);
            System.out.println(signature);
            return echostr; //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
        } else {
            System.out.println("签名校验失败。");
            return null;
        }
    }

    /**
     * 排序方法
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = { token, timestamp, nonce };
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
    }

}
