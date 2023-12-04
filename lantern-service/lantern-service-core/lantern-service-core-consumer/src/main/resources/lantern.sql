create or replace table platform_user_info
(
    pui_id             bigint auto_increment
    primary key,
    system_id          bigint                                              not null comment '系统id',
    user_id            bigint   default 0                                  not null comment '用户id',
    corporation_id     varchar(31)                                         not null comment '公司id, union',
    app_id             varchar(31)                                         not null comment '小程序，公众号id, openid',
    pui_open_id        varchar(255)                                        not null comment '厂商 open id',
    pui_union_id       varchar(255)                                        not null comment '厂商 union id',
    pui_type           enum ('PLATFORM', 'SECOND', 'THIRD')                not null comment '第三方类型：平台，第三方',
    pui_auth_channel   enum ('Github', 'Alipay', 'Wechat', 'Taobao', 'QQ') not null comment '第三方厂商',
    pui_status         int      default 0                                  not null comment '可以禁止使用该信息登录    ',
    pui_create_time    datetime default current_timestamp()                not null comment ' 创建时间 ',
    pui_create_user_id bigint                                              not null comment '创建人id',
    pui_update_time    datetime default current_timestamp()                not null on update current_timestamp() comment ' 修改时间 ',
    pui_update_user_id bigint                                              not null comment '修改人id',
    is_delete          int      default 0                                  not null comment '0未删除，1已删除'
)
comment '第三方登录信息表。第一次登录，默认创建一个用户，如果进行用户核心信息绑定，怎么处理';

create or replace table resource_role_relation
(
    rrr_id             bigint auto_increment
    primary key,
    rrr_resource_id    bigint                                   not null comment ' 权限id ',
    rrr_role_id        bigint                                   not null comment ' 角色id ',
    rrr_type           varchar(127) default ''                  not null comment ' 类型 ',
    rrr_type_id        bigint       default 0                   null comment ' 类型id ',
    rrr_end_time       datetime     default current_timestamp() null comment ' 结束时间 ',
    rrr_valid_time     datetime     default current_timestamp() null comment ' 有效时间 ',
    rrr_create_time    datetime     default current_timestamp() not null comment ' 创建时间 ',
    rrr_update_time    datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    rrr_start_time     datetime     default current_timestamp() not null comment ' 开始时间 ',
    rrr_create_user_id bigint                                   not null comment '创建人id',
    rrr_update_user_id bigint                                   not null comment '修改人id',
    is_delete          int          default 0                   not null comment '0未删除，1已删除'
)
comment '资源与角色关系表';

create or replace table resources
(
    resource_id                 bigint auto_increment comment ' id '
    primary key,
    system_id                   bigint       default 0                   not null comment ' 系统id ',
    product_id                  bigint       default 0                   not null comment '产品id',
    project_id                  bigint       default 0                   not null comment ' 项目id ',
    project_name                varchar(127) default ''                  not null comment ' 项目名称 ',
    module_id                   bigint       default 0                   not null comment ' 模块id ',
    module_name                 varchar(127) default ''                  not null comment ' 模块名称 ',
    resource_type               varchar(127) default ''                  not null comment ' 资源类型 ',
    resource_version            varchar(31)  default ''                  not null comment '资源版本号',
    resource_name               varchar(127) default ''                  not null comment ' 资源名 ',
    resource_create_time        datetime     default current_timestamp() not null comment ' 创建时间 ',
    resource_start_time         datetime     default current_timestamp() not null comment '权限开始时间',
    resource_valid_time         datetime     default current_timestamp() not null comment ' 有效时间 ',
    resource_end_time           datetime     default current_timestamp() not null comment '权限结束时间，自动与手动触发',
    resource_update_time        datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    resource_create_user_id     bigint                                   not null comment '创建人id',
    resource_update_user_id     bigint                                   not null comment '修改人id',
    resource_description        varchar(255) default ''                  not null comment '资源描述',
    resource_tag                varchar(255) default ''                  not null,
    resource_operator           varchar(127) default ''                  not null comment ' 操作 ',
    resource_conditions         varchar(127) default ''                  not null comment ' 条件，视图触发条件为主 ',
    resource_parent_resource_id bigint       default 0                   not null comment ' 父权限id ',
    is_delete                   int          default 0                   not null comment '0未删除，1已删除'
);

create or replace table role
(
    role_id             bigint(11) auto_increment comment ' id '
    primary key,
    role_type_id        bigint                                   not null comment ' 角色类型id ',
    system_id           bigint       default 0                   not null comment ' 系统id ',
    product_id          bigint       default 0                   not null comment '产品id',
    project_id          bigint       default 0                   not null comment ' 项目id ',
    project_name        varchar(127) default ''                  not null comment ' 项目名称 ',
    role_type           varchar(15)  default ''                  not null comment '角色类型',
    role_name           varchar(127) default ''                  not null comment ' 角色名 ',
    role_create_time    datetime     default current_timestamp() not null comment ' 创建时间 ',
    role_start_time     datetime     default current_timestamp() not null comment ' 开始时间 ',
    role_update_time    datetime     default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    role_end_time       datetime     default current_timestamp() not null comment ' 结束时间 ',
    role_valid_time     datetime     default current_timestamp() not null comment ' 有效时间 ',
    role_create_user_id bigint                                   not null comment '创建人id',
    role_update_user_id bigint                                   not null comment '修改人id',
    role_description    varchar(127) default ''                  not null comment ' 角色描述 ',
    role_tag            varchar(127) default ''                  not null,
    is_delete           int          default 0                   not null comment '0未删除，1已删除'
);

create or replace table role_type
(
    role_type_id          bigint auto_increment
    primary key,
    role_type_description varchar(127) default '' not null,
    role_type_is_delete   int          default 0  not null,
    role_type_name        varchar(127) default '' not null
);

create or replace table user_info
(
    ui_id             bigint unsigned auto_increment
    primary key,
    system_id         bigint                                                         not null comment '系统id',
    ui_name           varchar(36)                                                    not null comment '用户名，使用前缀索引',
    ui_nickname       varchar(16)                        default `ui_name`           not null comment '用户昵称',
    ui_id_card        varchar(36)                                                    not null comment '用户唯一标识符，使用前缀索引',
    ui_phone          varchar(36)                        default ''                  not null comment '用户联系方式，使用前缀索引',
    ui_email          varchar(36)                        default ''                  not null comment '用户邮箱，使用前缀索引',
    ui_head_portrait  varchar(32)                        default ''                  not null comment '用户图像,默认为用户id',
    ui_sex            enum ('MALE', 'FEMALE', 'UNKNOWN') default 'UNKNOWN'           not null comment '用户性别，可以在详细信息表',
    ui_birth          date                               default '1970-01-01'        not null comment '用户生日，可以在详细信息表',
    ui_address        varchar(32)                        default ''                  not null comment '用户家庭地址，可以在详细信息表',
    ui_salt           varchar(64)                        default ''                  not null comment '用户盐',
    ui_salt_password  varchar(64)                        default ''                  not null comment '用户盐密',
    ui_login_address  varchar(32)                        default ''                  not null comment '用户最近登录地址，应该在登录记录表里面',
    ui_login_time     datetime                           default current_timestamp() not null comment '用户最近登录时间，，应该在登录记录表里面',
    ui_exit_time      datetime                           default current_timestamp() not null comment '用户最近退出时间，，应该在登录记录表里面',
    ui_status         bigint                             default 0                   not null comment '用户状态。1 第一次登录。2：认证中',
    ui_token          varchar(128)                       default ''                  not null comment '用于简单系统，简单系统建立所有',
    ui_create_time    datetime                           default current_timestamp() not null comment ' 创建时间 ',
    ui_create_user_id bigint                                                         not null comment '创建人id',
    ui_update_time    datetime                           default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    ui_update_user_id bigint                                                         not null comment '修改人id',
    is_delete         int                                default 0                   not null comment '0未删除，1已删除'
);

create or replace table user_login_record
(
    ul_id                   bigint unsigned auto_increment
    primary key,
    system_id               bigint                                      not null comment '系统id',
    ui_id                   bigint unsigned                             not null comment '用户Id',
    tri_id                  bigint unsigned default 0                   null comment '第三方信息Id,如果是第一方登录是不存在id的',
    product_id              varchar(31)     default ''                  not null comment '登录那个产品',
    ul_login_time           datetime        default current_timestamp() not null comment '用户登录时间',
    ul_login_address        varchar(31)     default ''                  not null comment '用户登录地址',
    ul_login_ip             varchar(40)     default ''                  not null comment '用户登录Ip',
    ul_login_device_type    varchar(15)     default ''                  not null comment '用户登录设配类型。台式机，笔记本，手机，平板',
    ul_login_device         varchar(15)     default ''                  not null comment '登录设备属于什么品牌',
    ul_login_device_model   varchar(32)     default ''                  not null comment '用户登录设备品牌型号',
    ul_login_system         varchar(15)     default ''                  not null comment '用户登录操作系统',
    ul_login_system_model   varchar(15)     default ''                  not null comment '用户登录系统版本',
    ul_session_id           varchar(36)     default ''                  not null comment '用户登录时token',
    ul_login_way            varchar(15)     default ''                  not null comment '用户登录方式。一方，三方，平台，二维码',
    ul_login_way_platform   varchar(15)     default ''                  not null comment '一方：手机，邮箱，用户名，手机验证，邮箱验证，三方：QQ，淘宝，微信.... 平台: ....',
    ul_login_terminal       varchar(15)     default ''                  not null comment 'MbBrowser',
    ul_login_terminal_model varchar(15)     default ''                  not null comment '用户登录的浏览器版本，可探测到的',
    ul_quit_way             varchar(15)     default ''                  not null comment '用户退出方式，用户主动，登录时间超时，系统主动，',
    ul_quit_ip              varchar(40)     default ''                  not null comment '用户登录Ip',
    ul_quit_time            datetime        default current_timestamp() not null comment '用户退出时间',
    ul_quit_address         varchar(31)     default ''                  not null comment '用户登录地址',
    ul_login_status         varchar(7)      default 'SUCCESS'           not null comment '用户登录状态:SUCCESS,FAIL',
    ul_login_fail_reason    varchar(127)    default ''                  not null comment '登录失败原因'
)
comment '用户登录记录表，有一个问题：IP发生改变，是否新增一条。';

create or replace table user_role_relation
(
    urr_id          bigint auto_increment
    primary key,
    urr_type        varchar(31)                          not null comment '与角色关联的对象的类型',
    role_id         bigint                               not null comment ' 角色id ',
    user_id         bigint                               not null,
    urr_create_time datetime default current_timestamp() not null comment ' 创建时间 ',
    urr_update_time datetime default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
    urr_end_time    datetime default current_timestamp() not null comment ' 结束时间 ',
    urr_valid_time  datetime default current_timestamp() not null comment ' 有效时间 ',
    urr_start_time  datetime default current_timestamp() not null comment ' 开始时间 ',
    urr_create_user_id bigint                               not null comment '创建人id',
    urr_update_user_id bigint                               not null comment '修改人id',
    is_delete       int      default 0                    not null comment '0未删除，1已删除'
);

