function dianji() {
    var obj = new WxLogin({
        self_redirect:true,
        id:"login_container", //要与你在那里生成二维码的div id 一致
        appid: "appid", //你申请的appid
        scope:"snsapi_login,snsapi_userinfo",//你的权限
        redirect_uri:encodeURIComponent("你自己配置的回调地址"),//url要进行encodeURIComponent编码
        state: "STATE",//微信官方接口为了防止跨域攻击要加的参数 可以自己定义
        style: "black",//样式
        href: ""//可以引入你自己的样式 注意这个只能是网络中的
    });
}

