drop table if exists  role;
create table  role (
id int default 0 not null comment ' id '        ,
role_name varchar(127) default '' not null comment ' 角色名 '   ,
create_time timestamp default now() not null comment ' 创建时间 '       ,
terminal_time timestamp default now() not null comment ' 结束时间 '     ,
role_desc varchar(127) default '' not null comment ' 角色描述 ' ,
valid_time timestamp default now() not null comment ' 有效时间 '        ,
status varchar(127) default '' not null comment ' 状态 '
)
