create table VISUA_SQL_EXAMPLE  
(
  id            VARCHAR(255) not null primary key,
  title         VARCHAR(4000)   comment '接口描述--标题',
  business_code VARCHAR(255)    comment '业务代码',
  create_user   VARCHAR(255)    comment '创建者',
  sql_status    INT  DEFAULT 1  comment '状态 1,使用 0,启用',
  business_type INT             comment '类型:0.系统级,1.业务级',
  sql_type      INT             comment 'sql类型:0:默认接口,1:表格类型,2:表格类型', 
  create_date   DATETIME        comment '创建日期',
  update_date   DATETIME        comment '更新日期',
  output_data   TEXT            comment '输出输出数据',
  input_data    TEXT            comment '输入数据json数据',
  sql_templates TEXT            comment 'sql模板列表'
)

