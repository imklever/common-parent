create table sys_menu(
id varchar(64) not null primary key,
create_date datetime comment '创建时间',
remarks varchar(255) comment '描述',
display int comment '展现',
lab varchar(255) comment '菜单',
location varchar(255) comment '位置',
name varchar(255) comment '名称',
pid varchar(255) comment '父ID',
sort int comment '步骤',
status int comment '状态',
type varchar(255) comment '类型',
url varchar(255) comment '路径',
target varchar(255) comment '对象',
flag int comment '标示',
marker varchar(255) comment '标记',
create_user varchar(40) comment '创建人',
data_status varchar(40) comment '数据状态'
)