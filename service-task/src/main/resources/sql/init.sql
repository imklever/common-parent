
--任务日志表
create table IOC_TASK_LOG
(
  id         VARCHAR2(255) not null  primary key,
  task_id    VARCHAR2(255),
  start_date DATE,
  end_date   DATE,
  end_status VARCHAR2(255),
  status     VARCHAR2(255),
  run_log    CLOB
)

----任务表
create table IOC_TASK
(
  id               VARCHAR2(255) not null primary key,
  task_type        VARCHAR2(255),
  task_name        VARCHAR2(255),
  task_status      VARCHAR2(255),
  task_params      CLOB,
  task_create_time DATE,
  schedule_type    VARCHAR2(255),
  schedule_role    VARCHAR2(255)
)