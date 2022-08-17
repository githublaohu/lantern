drop table if exists  authority_role_relation;
create table  authority_role_relation (
id int default 0 not null comment ' id '        ,
authority_id int default 0 not null comment ' 权限id '  ,
operator_id int default 0 not null comment ' 操作id '   ,
role_id int default 0 not null comment ' 角色id '       ,
type varchar(127) default '' not null comment ' 类型 '  ,
type_id int default 0 not null comment ' 类型id '       ,
create_time timestamp default now() not null comment ' 创建时间 '       ,
terminal_time timestamp default now() not null comment ' 修改时间 '     ,
role_desc timestamp default now() not null comment ' 结束时间 ' ,
valid_time timestamp default now() not null comment ' 有效时间 '        ,
status varchar(127) default '' not null comment ' 状态 '
)
