create table sys_user(
id varchar(64)  comment 'id',
username varchar(64) comment '用户名',
password varchar(64) comment '密码',
token varchar(64) comment '获取',
updatetime int comment '更新时间',
overduetime int comment '逾期',
create_user varchar(40) comment '创建人',
create_date varchar(40) comment '创建时间',
data_status varchar(40) comment '数据状态'
)