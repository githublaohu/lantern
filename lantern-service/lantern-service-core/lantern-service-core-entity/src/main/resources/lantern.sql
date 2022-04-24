create DATABASE IF NOT EXISTS lantern_database;


create table user_info (
                           ui_id bigint unsigned not null auto_increment comment '用户Id',
                           ui_name varchar(16) not null comment '用户名',
                           ui_nickname varchar(16) not null comment '用户昵称',
                           ui_idcard varchar(20) not null comment '用户唯一标识符',
                           ui_phone varchar(12) not null default '' comment '用户联系方式',
                           ui_email varchar(32) not null default '' comment '用户邮箱',
                           ui_head_portrait varchar(32) default '' comment '用户图像',
                           ui_lack_flag int unsigned not null default 0 comment '用户缺失字段标志位',
                           ui_sex enum('MALE','FEMALE', 'UNKNOW') comment '用户性别',
                           ui_age tinyint default -1 comment '用户年龄',
                           ui_birth datetime not null comment '用户生日',
                           ui_address varchar(32) not null default '' comment '用户家庭地址',
                           ui_password varchar(32) not null default '123456' comment '用户密码',
                           ui_salt varchar(32) not null default '' comment '用户盐',
                           ui_salt_password varchar(32) not null default '' comment '用户盐密',
                           ui_token varchar(32) not null default '' comment '用户盐',
                           ui_login_address varchar(32) not null default '' comment '用户最近登录地址',
                           ui_login_time datetime not null default current_timestamp comment '用户最近登录时间',
                           ui_exit_time datetime default  null comment '用户最近退出时间',
                           ui_first_login enum('True', 'False') default 'True' comment '用户是否为第一次登录',
                           tri_id bigint unsigned default null comment '用户第三方信息Id',
                           ui_status enum('ACTIVE','INACTIVE') default 'ACTIVE' comment '用户状态',
                           primary key (ui_id)
);


create table login_record (
                              ul_id bigint unsigned not null auto_increment comment '登录记录Id',
                              ui_id bigint unsigned not null comment '用户Id',
                              ul_login_time datetime not null default current_timestamp comment '用户登录时间',
                              ul_exit_time datetime not null default current_timestamp comment '用户退出时间',
                              ul_login_address varchar(32) not null default '' comment '用户登录地址',
                              ul_login_ip varchar(32) not null default '' comment '用户登录Ip',
                              ul_login_device enum('苹果','小米','vivo','oppo','华为','三星','联想','戴尔','外星人','其他') default '其他' comment '用户登录设备',
                              ul_login_device_model varchar(32) not null default '' comment '用户登录设备型号',
                              ul_login_syxstem enum('macos','windows','linux','ios','andriod','harmony', 'other') default 'other' comment '用户登录操作系统',
                              ul_login_way enum('账号密码登录','手机验证码登录','第三方扫码登录','第三方小程序登录') default '账号密码登录' comment '用户登录方式',
                              ul_login_terminal enum('手机浏览器','电脑浏览器','第三方App','第三方小程序') not null comment '用户登录终端',
                              ul_quit_way enum('主动退出','会话结束','强制下线') default '会话结束' comment '用户退出方式',
                              tri_id bigint unsigned default null comment '第三方信息Id',
                              ul_login_status enum('SUCCESS','FAIL') default 'SUCCESS' comment '用户登录状态',
                              primary key (ul_id)

);


create table black_list(
        bl_id bigint unsigned not null auto_increment comment '黑名单Id',
        ui_id bigint,
        tri_id int,
        inactive_id Enum("登录错误次数超过"),
        inactive_time long comment "封禁时长",
        update_time,
        update_ui_id default null,





);
