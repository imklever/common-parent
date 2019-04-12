package org.common.constant;

public interface CommonConstants {
	/**模板类型*/
	String VISUA_TYPE_SEL="sel";
	String VISUA_TYPE_ADD="add";
	String VISUA_TYPE_ADD_GET_ID="add_get_id";
	String VISUA_TYPE_DEL="del";
	String VISUA_TYPE_EDIT="edit";
	String VISUA_TYPE_ECHARTS="templet";
	String VISUA_TYPE_MONGO_SEL="mongoSel";
	String VISUA_TYPE_MONGO_DEL="mongoDel";
	String VISUA_TYPE_MONGO_ADD="mongoAdd";
	String VISUA_TYPE_MONGO_EDIT="mongoEdit";
	String VISUA_TYPE_ES_SEL="esSel";
	String VISUA_TYPE_CHECK="check";
	String VISUA_TYPE_REFLEX="reflex";
	
	
	String VISUA_OUTPUT="visuaOutput";
	String VISUA_INPUT="visuaInput";
	String SUPPORT_TO_DO ="supportToDo";
	String UDF_TO_DO ="udfToDo";
	
	String CONF_VISUA_IS_SKIP="isSkip";
	
	String TEST_MODE_TYPE_SQL="sql";
	String TEST_MODE_TYPE_EXECSQL="execSql";
	
	
	/** 类型: pc端 , app端   */
	String PC_TYPE="pc";
	String APP_TYPE="app";
	/** Video2Img */
	String VOIDE_LONG="voide_long";//视频音频时长
	String VOIDE_IMG="voide_img";//视频截图
	/**
	 * 文件上传的属性
	 */
	String FILE_UP_ID="id";//id
	String FILE_UP_AUDIO_TIME="audio_time";
	String FILE_UP_FILE_TYPENUM="file_typenum";//类型数字  1:图片，2，音频，3.视频d
	String FILE_UP_SAVE_PATH="save_path";
	String FILE_UP_RELATIVE_PATH="relative_path";//相对路径
	/**
	 *  默认返回数据列表名称
	 */
	String RETURN_DATA="datalist";
	/**
	 * 缓存key值,
	 */
	String CACHE_TYPE_KEY_WXAPP="wxapp";//微信小程序配置
	String CACHE_TYPE_KEY_WXAPP_ROUTER = "wxapp_router";//小程序转发
	String CACHE_TYPE_KEY_USER="user";//微信配置
	String CACHE_TYPE_KEY_PERMISSION="permission";
	String CACHE_TYPE_KEY_ROLE="role";
	String CACHE_TYPE_KEY_MENU="menu";
	
	
	/*验证类型*/
	String CHECK_TYPE_NOTNULL ="notNull";
	String CHECK_TYPE_PHONE ="phone";
	String CHECK_TYPE_ID_CARD ="idCard";
	String CHECK_TYPE_EMAIL ="Email";
	String CHECK_TYPE_NULL = "Null";
	String CHECK_TYPE_MAX ="max";
	String CHECK_TYPE_REGULAR ="regular";
	String CHECK_TYPE_MAX_LENGTH ="maxLength";
	String CHECK_TYPE_MIN_LENGTH ="minLength";
	String CHECK_TYPE_MIN ="min";
	String CHECK_TYPE_CORN ="corn";
	
	/**定时任务*/
	String TASK_DATA_PARM_JOB_DATA ="job-data";//静态参数
	String TASK_DATA_PARM_DEF_DATA ="default-data";//静态参数
	String TASK_DATA_PARM_DYNA_DATA ="dynamic-data";//动态参数
	
	String TASK_ACTION_START="start";
	String TASK_ACTION_STOP="stop";
	String TASK_ACTION_RESTART="restart";
	String TASK_ACTION_RESCHEDULE="reschedule";
	String TASK_ACTION_DEL="del";
	
	String TASK_RUN_STATUS_READY="ready";
	String TASK_RUN_STATUS_RUNNING="running";
	String TASK_RUN_STATUS_STOP="stop";
	String TASK_RUN_STATUS_END="end";
	
	   String TASK_RESULT_STATUS_SUCCESS="Success";
	   String TASK_RESULT_STATUS_FAIL="fail";
	
	/**系统类型的数据返回默认的值*/
	String BUSINESS_DEF_KEY_DATA_LIST="dataList";
	
	
	String MAP_SQL="IOC_SQL";
	String MAP_SQL_ADD_ID="id";
	
	String CONTEXT_NOSE="nose";//前端参数
	String CONTEXT_RETRUN="return";//后台返回对象
	String CONTEXT_SESSION="session";//session
	String FLOW_NEXT="next";//
	String FLOW_PRE="pre";//
	String FLOW_START="start";//
}
