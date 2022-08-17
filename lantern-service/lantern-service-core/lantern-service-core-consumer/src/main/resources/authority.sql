drop table if exists  authority;
create table  authority (
id int default 0 not null comment ' id '        ,
authority_type int default 0 not null comment ' 权限类型 '      ,
parent_authority_id int default 0 not null comment ' 父权限id ' ,
parent_authority_name varchar(127) default '' not null comment ' 父权限名称 '   ,
authority_name varchar(127) default '' not null comment ' 权限名称 '    ,
authority_desc varchar(127) default '' not null comment ' 权限描述 '    ,
create_time timestamp default now() not null comment ' 创建时间 '       ,
terminal_time timestamp default now() not null comment ' 修改时间 '     ,
role_desc varchar(127) default '' not null comment ' 结束时间 ' ,
valid_time timestamp default now() not null comment ' 有效时间 '        ,
status varchar(127) default '' not null comment ' 状态 '
)

