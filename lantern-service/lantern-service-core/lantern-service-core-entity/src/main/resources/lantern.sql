show databases ;

use lantern_v2;


drop table login_record;

show tables;

drop table user_info;

select * from user_info;

create table user_info (
                           ui_id bigint unsigned not null auto_increment comment '用户Id',
                           ui_name varchar(16) not null comment '用户名',
                           ui_nickname varchar(16) not null comment '用户昵称',
                           ui_idcard varchar(32) not null comment '用户唯一标识符',
                           ui_phone varchar(12) not null default '' comment '用户联系方式',
                           ui_email varchar(32) not null default '' comment '用户邮箱',
                           ui_head_portrait varchar(256) default '' comment '用户图像',
                           ui_lack_flag int unsigned not null default 0 comment '用户缺失字段标志位',
                           ui_sex enum('MALE','FEMALE', 'UNKNOW') comment '用户性别',
                           ui_age tinyint default -1 comment '用户年龄',
                           ui_birth datetime not null comment '用户生日',
                           ui_address varchar(32) not null default '' comment '用户家庭地址',
                           ui_password varchar(16) not null default '123456' comment '用户密码',
                           ui_salt varchar(64) not null default '' comment '用户盐',
                           ui_salt_password varchar(64) not null default '' comment '用户盐密',
                           ui_token varchar(512) not null default '' comment '用户令牌',
                           ui_login_address varchar(32) not null default '' comment '用户最近登录地址',
                           ui_login_time datetime  default current_timestamp comment '用户最近登录时间',
                           ui_exit_time datetime default  null comment '用户最近退出时间',
                           ui_first_login enum('True', 'False') default 'True' comment '用户是否为第一次登录',
                           tri_id bigint unsigned default null comment '用户第三方信息Id',
                           ui_status enum('ACTIVE','INACTIVE') default 'ACTIVE' comment '用户状态',
                           allow_login enum('ACTIVE','INACTIVE') default 'ACTIVE' comment '是否限制登录',
                           primary key (ui_id)
);

commit ;

show tables;

select * from login_record;

create table login_record (
                              ul_id bigint unsigned not null auto_increment comment '登录记录Id',
                              ui_id bigint unsigned not null comment '用户Id',
                              ul_login_time datetime default current_timestamp comment '用户登录时间',
                              ul_exit_time datetime  default current_timestamp comment '用户退出时间',
                              ul_login_address varchar(32)  default '' comment '用户登录地址',
                              ul_login_ip varchar(32)  default '' comment '用户登录Ip',
                              ul_login_device enum('APPLE','XIAOMI','VIVO','OPPO','HUAWEI','SAMSUNG','LENOVO','DELL','ALLENWARE','OTHER') default 'OTHER' comment '用户登录设备',
                              ul_login_device_model varchar(32)  default '' comment '用户登录设备型号',
                              ul_login_system enum('MACOS','WINDOWS','LINUX','IOS','ANDRIOD','HARMONY', 'OTHER') default 'OTHER' comment '用户登录操作系统',
                              ul_login_way enum('AccountLogin','VerifycodeLogin','ScanCodeLogin','AppLogin') default 'AccountLogin' comment '用户登录方式',
                              ul_login_terminal enum('MbBrowser','PcBrowser','TriApp','TriApplet') default 'MbBrowser' comment 'MbBrowser',
                              ul_quit_way enum('ActiveExit','SessionExpire','ForceExit') default 'SessionExpire' comment '用户退出方式',
                              tri_id bigint unsigned default null comment '第三方信息Id',
                              ul_login_status enum('SUCCESS','FAIL') default 'SUCCESS' comment '用户登录状态',
                              primary key (ul_id)
);

select * from user_info;


select * from login_record;


commit ;
