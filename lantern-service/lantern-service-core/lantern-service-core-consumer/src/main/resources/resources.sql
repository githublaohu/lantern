drop table if exists  resources;
create table  resources (
id int default 0 not null comment ' id '        ,
system_id int default 0 not null comment ' 系统id '     ,
project_id int default 0 not null comment ' 项目id '    ,
project_name varchar(127) default '' not null comment ' 项目名称 '      ,
module_id int default 0 not null comment ' 模块id '     ,
module_name varchar(127) default '' not null comment ' 模块名称 '       ,
resources_type varchar(127) default '' not null comment ' 资源类型 '    ,
resources_name varchar(127) default '' not null comment ' 资源名 '      ,
create_time timestamp default now() not null comment ' 创建时间 '       ,
terminal_time timestamp default now() not null comment ' 修改时间 '     ,
role_desc timestamp default now() not null comment ' 结束时间 ' ,
valid_time timestamp default now() not null comment ' 有效时间 '        ,
status varchar(127) default '' not null comment ' 状态 '        ,
operator varchar(127) default '' not null comment ' 操作 '      ,
conditions varchar(127) default '' not null comment ' 条件 '
)
