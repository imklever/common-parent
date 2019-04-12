create table sys_role(
id varchar(64) comment 'ID',
name varchar(50) comment '名称',
enabled int comment '是否可用',
p_id varchar(64) comment '父ID',
create_user varchar(40) comment '创建人',
create_date varchar(40) comment '创建时间',
data_status varchar(40) comment '数据状态'
)