create table platform_user_info
(
    pui_id          bigint auto_increment
        primary key,
    pui_user_id     bigint                                               not null,
    pui_open_id     bigint                                               not null,
    pui_type        enum ('PLATFORM', 'SECOND', 'THIRD') default 'THIRD' not null,
    pui_authchannel enum ('Github', 'Alipay', 'Wechat', 'Taobao', 'QQ')  not null,
    pui_union_id    bigint                               default 0       not null
);

create table resource_role_relation
(
    rrr_id          bigint auto_increment comment ' id '
        primary key,
    rrr_resource_id bigint                                   not null comment ' 权限id ',
    rrr_operator_id bigint       default 0                   not null comment ' 操作id ',
    rrr_role_id     bigint                                   not null comment ' 角色id ',
    rrr_type        varchar(127) default ''                  not null comment ' 类型 ',
    rrr_type_id     bigint       default 0                   not null comment ' 类型id ',
    rrr_create_time datetime     default current_timestamp() not null comment ' 创建时间 ',
    rrr_update_time datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    rrr_end_time    datetime     default current_timestamp() not null comment ' 结束时间 ',
    rrr_valid_time  datetime     default current_timestamp() not null comment ' 有效时间 '
);

create index resource_role_relation_rrr_resource_id_rrr_role_id_index
    on resource_role_relation (rrr_resource_id, rrr_role_id);

create table resources
(
    resource_id                 bigint auto_increment comment ' id '
        primary key,
    resource_system_id          bigint       default 0                   not null comment ' 系统id ',
    resource_project_id         bigint       default 0                   not null comment ' 项目id ',
    resource_project_name       varchar(127) default ''                  not null comment ' 项目名称 ',
    resource_module_id          bigint       default 0                   not null comment ' 模块id ',
    resource_module_name        varchar(127) default ''                  not null comment ' 模块名称 ',
    resource_type               varchar(127) default ''                  not null comment ' 资源类型 ',
    resource_name               varchar(127) default ''                  not null comment ' 资源名 ',
    resource_create_time        datetime     default current_timestamp() not null comment ' 创建时间 ',
    resource_end_time           datetime     default current_timestamp() not null,
    resource_update_time        datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    resource_description        varchar(255) default ''                  not null comment '资源描述',
    resource_valid_time         datetime     default current_timestamp() not null comment ' 有效时间 ',
    resource_is_delete          int          default 0                   not null comment '0未删除，1已删除',
    resource_operator           varchar(127) default ''                  not null comment ' 操作 ',
    resource_conditions         varchar(127) default ''                  not null comment ' 条件 ',
    resource_parent_resource_id bigint       default 0                   not null comment ' 父权限id '
);

create table role
(
    role_id          bigint(11) auto_increment comment ' id '
        primary key,
    role_name        varchar(127) default ''                  not null comment ' 角色名 ',
    role_create_time datetime     default current_timestamp() not null comment ' 创建时间 ',
    role_update_time datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    role_end_time    datetime     default current_timestamp() not null comment ' 结束时间 ',
    role_valid_time  datetime     default current_timestamp() not null comment ' 有效时间 ',
    role_description varchar(127) default ''                  not null comment ' 角色描述 ',
    role_is_delete   int          default 0                   not null comment ' 状态 '
);

create table roletype
(
    roletype_id        bigint auto_increment
        primary key,
    roletype_is_delete int          default 0  not null,
    roletype_name      varchar(127) default '' not null
);

create table roletype_role_relation
(
    trr_id          bigint auto_increment
        primary key,
    trr_roletype_id bigint                               not null,
    trr_role_id     bigint                               not null,
    trr_create_time datetime default current_timestamp() not null,
    trr_end_time    datetime default current_timestamp() not null,
    trr_valid_time  datetime                             not null,
    trr_update_time datetime default current_timestamp() not null on update current_timestamp()
);

create table user_info
(
    ui_id            bigint unsigned auto_increment comment '用户Id'
        primary key,
    ui_name          varchar(16)                                                    not null comment '用户名',
    ui_nickname      varchar(16)                        default `ui_name`           not null comment '用户昵称',
    ui_idcard        varchar(20)                                                    not null comment '用户唯一标识符',
    ui_phone         varchar(12)                        default ''                  not null comment '用户联系方式',
    ui_email         varchar(32)                        default ''                  not null comment '用户邮箱',
    ui_head_portrait varchar(32)                        default ''                  null comment '用户图像',
    ui_lack_flag     int unsigned                       default 0                   not null comment '用户缺失字段标志位',
    ui_sex           enum ('MALE', 'FEMALE', 'UNKNOWN') default 'UNKNOWN'           null comment '用户性别',
    ui_birth         date                               default '1970-01-01'        not null comment '用户生日',
    ui_address       varchar(32)                        default ''                  not null comment '用户家庭地址',
    ui_salt          varchar(64)                        default ''                  not null comment '用户盐',
    ui_salt_password varchar(64)                        default ''                  not null comment '用户盐密',
    ui_login_address varchar(32)                        default ''                  not null comment '用户最近登录地址',
    ui_login_time    datetime                           default current_timestamp() not null comment '用户最近登录时间',
    ui_exit_time     datetime                                                       null comment '用户最近退出时间',
    ui_first_login   enum ('True', 'False')             default 'True'              null comment '用户是否为第一次登录',
    ui_status        enum ('ACTIVE', 'INACTIVE')        default 'ACTIVE'            null comment '用户状态',
    allow_login      enum ('ACTIVE', 'INACTIVE')        default 'ACTIVE'            null comment '是否限制登录',
    ui_is_delete     int                                default 0                   not null,
    token            varchar(128)                       default ''                  null
);

create table user_role_relation
(
    urr_id          bigint auto_increment comment ' id '
        primary key,
    urr_role_id     bigint                               not null comment ' 角色id ',
    urr_user_id     bigint                               not null,
    urr_create_time datetime default current_timestamp() not null comment ' 创建时间 ',
    urr_update_time datetime default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    urr_end_time    datetime default current_timestamp() not null comment ' 结束时间 ',
    urr_valid_time  datetime default current_timestamp() not null comment ' 有效时间 '
);

create table user_roletype_relation
(
    utr_id          int auto_increment
        primary key,
    utr_user_id     bigint                                 not null,
    utr_roletype_id int                                    not null,
    utr_create_time datetime default current_timestamp()   not null,
    utr_update_time datetime default '0000-00-00 00:00:00' not null on update current_timestamp(),
    utr_end_time    datetime default current_timestamp()   not null,
    utr_valid_time  datetime                               not null
);

create table userlogin_record
(
    ul_id                 bigint unsigned auto_increment comment '登录记录Id'
        primary key,
    ui_id                 bigint unsigned                                                                                                                   not null comment '用户Id',
    ul_login_time         datetime                                                                                              default current_timestamp() not null comment '用户登录时间',
    ul_exit_time          datetime                                                                                              default current_timestamp() not null comment '用户退出时间',
    ul_login_address      varchar(32)                                                                                           default ''                  not null comment '用户登录地址',
    ul_login_ip           varchar(32)                                                                                           default ''                  not null comment '用户登录Ip',
    ul_login_device       enum ('APPLE', 'XIAOMI', 'VIVO', 'OPPO', 'HUAWEI', 'SAMSUNG', 'LENOVO', 'DELL', 'ALIENWARE', 'OTHER') default 'OTHER'             not null comment '用户登录设备',
    ul_login_device_model varchar(32)                                                                                           default ''                  not null comment '用户登录设备型号',
    ul_login_system       enum ('MACOS', 'WINDOWS', 'LINUX', 'IOS', 'ANDROID', 'HARMONY', 'OTHER')                              default 'OTHER'             not null comment '用户登录操作系统',
    ul_session_id         varchar(32)                                                                                                                       not null,
    ul_login_way          varchar(32)                                                                                           default 'AccountLogin'      not null comment '用户登录方式',
    ul_login_terminal     enum ('MbBrowser', 'PcBrowser', 'TriApp', 'TriApplet')                                                default 'MbBrowser'         not null comment 'MbBrowser',
    ul_quit_way           enum ('ActiveExit', 'SessionExpire', 'ForceExit')                                                     default 'SessionExpire'     not null comment '用户退出方式',
    tri_id                bigint unsigned                                                                                       default 0                   not null comment '第三方信息Id',
    ul_login_status       enum ('SUCCESS', 'FAIL')                                                                              default 'SUCCESS'           not null comment '用户登录状态'
);

