package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.jd.FaceIdCollectionDto;
import com.isoftstone.common.jd.FaceLogCollectionDto;
import com.isoftstone.common.jd.FaceVisitorLogDto;
import com.isoftstone.common.jd.HourStatisticsDto;
import com.isoftstone.common.jd.StatisticsDto;
import com.isoftstone.common.jd.SyncScheduleDto;
import com.isoftstone.common.mongobackup.domain.FaceIdCollection;
import com.isoftstone.common.mongobackup.domain.LogCollection;
import com.isoftstone.common.mongobackup.domain.mysql.DeviceDto;
import com.isoftstone.common.mongobackup.domain.mysql.FaceIdTypeCountDto;
import com.isoftstone.common.mongobackup.domain.mysql.TempAppearedLogDto;
import com.isoftstone.common.mongobackup.domain.mysql.TempHourDto;
import com.isoftstone.common.mongobackup.domain.mysql.TempResidenceLogDto;
import com.isoftstone.common.mongobackup.service.common.jd.DeviceDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdTypeCountDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceLogCollectionDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceVisitorLogDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.HourStatisticsDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.LogCollectionService;
import com.isoftstone.common.mongobackup.service.common.jd.StatisticsDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.SyncScheduleDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.SyncTaskService;
import com.isoftstone.common.mongobackup.service.common.jd.TempAppearedLogDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.TempHourDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.TempResidenceLogDtoService;
import com.isoftstone.common.util.IdService;
import com.isoftstone.common.util.JsonService;

@Service
public class SyncTaskServiceImpl implements SyncTaskService {

	@Autowired
	SyncScheduleDtoService syncScheduleDtoService; 
	@Autowired
	FaceIdCollectionService faceIdCollectionService;
	@Autowired
	LogCollectionService logCollectionService;
	@Autowired
	IdService idService;
	@Autowired
	JsonService jsonService;
	@Autowired
	FaceIdCollectionDtoService faceIdCollectionDtoService;
	@Autowired
	FaceLogCollectionDtoService faceLogCollectionDtoService;
	@Autowired
	TempResidenceLogDtoService tempResidenceLogDtoService;
	@Autowired
	TempAppearedLogDtoService tempAppearedLogDtoService;
	@Autowired
	DeviceDtoService deviceDtoService;
	@Autowired
	FaceIdTypeCountDtoService faceIdTypeCountDtoService;
	@Autowired
	StatisticsDtoService statisticsDtoService;
	@Autowired
	HourStatisticsDtoService hourStatisticsDtoService;
	@Autowired
	FaceVisitorLogDtoService faceVisitorLogDtoService;
	@Autowired
	TempHourDtoService TempHourDtoService;
	
	@Override
	public void sync() {
		
		long startTimeMillis = 0L;		
		SyncScheduleDto syncScheduleDto = syncScheduleDtoService.selectOne();
		if(syncScheduleDto!=null){
			if(!StringUtils.isEmpty(syncScheduleDto.getSyncTime())){
				startTimeMillis=Long.parseLong(syncScheduleDto.getSyncTime());
			}
		}
		else 
		{
			syncScheduleDto=new SyncScheduleDto();
		}
		
		Long timeMillis=System.currentTimeMillis()-5*60*1000;
		Date curDate=new Date(timeMillis);
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(curDate);
		int curMinute = calendar.get(Calendar.MINUTE);
		//如果是整点 ，需要设置为 59分 。如: 10:00 要改为 9:59.59 的格式 方便统计时的数据准确性
		if(curMinute==0){
			calendar.add(Calendar.SECOND, -1);
		}
		
		long currentTimeMillis = calendar.getTimeInMillis();
		
		SimpleDateFormat ymdhmFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
/*		String beforeOneDayTime = ymdhmFormat.format(new Date(currentTimeMillis- 24*60*60*1000L));
		String beforeSevenDayTime =  ymdhmFormat.format(new Date(currentTimeMillis- 7*24*60*60*1000L));
		
		String currentDateTime = ymdhmFormat.format(calendar.getTime());*/
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		//获取对应的摄像头类型【进，出】
		List<DeviceDto> deviceDtos = deviceDtoService.selectAll();
		if (deviceDtos.size()==0) {
			return ;
		}
		//设备类型为进、出
		Map<String, Object> deviceMap =new HashMap<String, Object>();
		//设备门店
		Map<String, Object> unitMap = new HashMap<String, Object>();
		List<String> unitList=new ArrayList<String>();
		for (DeviceDto obj: deviceDtos) {
			String key=obj.getMac();
			int value =obj.getTypeState();
			String unitId=obj.getStoreId();
			if(!StringUtils.isEmpty(key)){
				
				deviceMap.put(key, value);				
				unitMap.put(key, unitId);
				if(!unitList.contains(unitId))
				{
					unitList.add(unitId);	
				}
			}
		}
		 /*-------------------------------同步开始------------------------------------- */
		//同步mongodb数据
		List<FaceIdCollection> list =faceIdCollectionService.getByTime(startTimeMillis, currentTimeMillis);
		List<LogCollection> logList=logCollectionService.getByTime(startTimeMillis, currentTimeMillis);
		
		List<FaceIdCollectionDto> faceIdCollectionDtos = new ArrayList<FaceIdCollectionDto>();
		for (FaceIdCollection faceIdCollection : list) {
			if(!deviceMap.containsKey(faceIdCollection.getDevice())){
				continue;
			}
			FaceIdCollectionDto faceIdCollectionDto =new FaceIdCollectionDto();
			faceIdCollectionDto.setId(faceIdCollection.getId());
			faceIdCollectionDto.setAge(faceIdCollection.getAge());
			faceIdCollectionDto.setAppearedNum(faceIdCollection.getAppeared_num());
			faceIdCollectionDto.setCaptureTime(faceIdCollection.getCapture_time());
			faceIdCollectionDto.setDevice(faceIdCollection.getDevice());
			faceIdCollectionDto.setFaceId(faceIdCollection.getFace_id());
			faceIdCollectionDto.setFeature("");
			faceIdCollectionDto.setGender(faceIdCollection.getGender());
			faceIdCollectionDto.setImage(faceIdCollectionDto.getImage());
			faceIdCollectionDto.setDeviceType(Integer.parseInt(deviceMap.get(faceIdCollection.getDevice()).toString()));
			faceIdCollectionDto.setUnitId(unitMap.get(faceIdCollection.getDevice()).toString());
			faceIdCollectionDto.setCaptureTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(faceIdCollection.getCapture_time())));
			faceIdCollectionDtos.add(faceIdCollectionDto);
		}
		
		List<FaceLogCollectionDto> faceLogCollectionDtos = new ArrayList<FaceLogCollectionDto>();
		List<FaceVisitorLogDto> faceVisitorLogDtos =new ArrayList<FaceVisitorLogDto>();
		for (LogCollection logCollection : logList) {
			if(!deviceMap.containsKey(logCollection.getDevice())){
				continue;
			}
			
			String dateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String date_TimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date(logCollection.getAppeared_time()));
			String hourStr = new SimpleDateFormat("HH").format(new Date(logCollection.getAppeared_time()));
			String minuteStr = new SimpleDateFormat("mm").format(new Date(logCollection.getAppeared_time()));
			
			//人脸历史记录表，只存储当天的数据，用于当天统计
			FaceLogCollectionDto faceLogCollectionDto =new FaceLogCollectionDto();
			faceLogCollectionDto.setId(logCollection.getId());
			faceLogCollectionDto.setFaceId(logCollection.getFace_id());
			faceLogCollectionDto.setDevice(logCollection.getDevice());
			faceLogCollectionDto.setAppearedTime(logCollection.getAppeared_time());
			faceLogCollectionDto.setImage(logCollection.getImage());
			faceLogCollectionDto.setDeviceType(Integer.parseInt(deviceMap.get(logCollection.getDevice()).toString()));
			faceLogCollectionDto.setUnitId(unitMap.get(logCollection.getDevice()).toString());
			faceLogCollectionDto.setAppearedFormatTime(date_TimeStr);
			faceLogCollectionDto.setAppearedDate(dateStr);
			faceLogCollectionDto.setAppearedHour(hourStr);
			faceLogCollectionDto.setAppearedMinute(minuteStr);
			faceLogCollectionDtos.add(faceLogCollectionDto);
			
			//访客历史记录表，用于存储所有的访客数据。		
			FaceVisitorLogDto faceVisitorLogDto=new FaceVisitorLogDto();
			faceVisitorLogDto.setId(idService.newOne());
			faceVisitorLogDto.setFaceId(logCollection.getFace_id());
			faceVisitorLogDto.setUnitId(unitMap.get(logCollection.getDevice()).toString());
			faceVisitorLogDto.setDeviceType(Integer.parseInt(deviceMap.get(logCollection.getDevice()).toString()));
			faceVisitorLogDto.setDateTime(dateTimeStr);
			faceVisitorLogDto.setFaceDate(dateStr);
			faceVisitorLogDto.setImage(logCollection.getImage());
			faceVisitorLogDto.setDevice(logCollection.getDevice());
			faceVisitorLogDtos.add(faceVisitorLogDto);
		}
		
		if(faceVisitorLogDtos.size()>0){
			faceVisitorLogDtoService.insertBatch(faceVisitorLogDtos);
		}
		
		
		String oldDate ="";
		FaceLogCollectionDto faceLogCollectionDto = faceLogCollectionDtoService.selectOne();
		if(faceLogCollectionDto!=null){
			oldDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(faceLogCollectionDto.getAppearedTime()));
		}
		try {
			//如果是换天操作 ,清空当日数据。
			if(!StringUtils.isEmpty(oldDate)){
				if(!oldDate.equals(currentDate)){
					//faceIdCollectionDtoService.deleteAll();
					//faceLogCollectionDtoService.deleteAll();
					faceLogCollectionDtoService.deleteByDate(oldDate);
				}
			}
			
			if(faceIdCollectionDtos.size()>0){
				faceIdCollectionDtoService.insertBatch(faceIdCollectionDtos);
			}
			if(faceLogCollectionDtos.size()>0){
				System.out.println("face_log_collection count :"+faceLogCollectionDtos.size());
				faceLogCollectionDtoService.insertBatch(faceLogCollectionDtos);
			}
		} catch (Exception e) {
			System.out.println(curDate.toString()+": 同步face表异常");
			e.printStackTrace();
		}
		 
		//新增同步时间
		syncScheduleDtoService.deleteAll();		
		//String syncTime = String.valueOf(timeMillis);	
		String syncTime =String.valueOf(currentTimeMillis);  //防止拉取的数据产生1秒的误差
		syncScheduleDto.setId(idService.newOne());
		syncScheduleDto.setSyncTime(syncTime);
		syncScheduleDtoService.insert(syncScheduleDto);		
		
		 /*-------------------------------同步完成------------------------------------- */
		 
		/*--------------------------------开始处理统计业务逻辑------------------------------------------*/
	    
			
		 String id = idService.newOne();
		
		 if(faceLogCollectionDtos.size()==0)
			 return;
		  
		 //循环门店信息
		 for(String unitId:unitList){
			List<FaceLogCollectionDto> faceLogDatesFromUnitList = faceLogCollectionDtoService.selectLogDates(unitId);
			
			if(faceLogDatesFromUnitList.size()==0){
				continue;
			}
			
			for (FaceLogCollectionDto flDates : faceLogDatesFromUnitList) {
				String appearedDate = flDates.getAppearedDate();
				String appearedHour = flDates.getAppearedHour();
				
				//停留时间统计数据，临时数据
				saveTempData(unitId,appearedDate,appearedHour);		
				
				id = idService.newOne();
				//保存统计数据
				saveStatistic(unitId,id,appearedDate,appearedHour);				
			}
						
			//时报表数据
			saveHourStatistic(unitId);			
		}
	
	}
	
	
	private void saveTempData(String unitId,String appearedDate,String appearedHour){
		 //清空临时进出店时间  , 临时停留时间数据 
		tempAppearedLogDtoService.deleteAll();
		tempResidenceLogDtoService.deleteAll();
		
		List<TempAppearedLogDto> tempAppearedLogDtos = tempAppearedLogDtoService.selectByDate(unitId, appearedDate, appearedHour);
		if(tempAppearedLogDtos.size()>0){
			tempAppearedLogDtoService.insertBatch(tempAppearedLogDtos);
		}
		List<TempResidenceLogDto> tempResidenceLogDtos = tempResidenceLogDtoService.selectByTempAppeared();
		if(tempResidenceLogDtos.size()>0){
			tempResidenceLogDtoService.insertBatch(tempResidenceLogDtos);
		}
	}
	
	private void saveStatistic(String unitId,String id,String date,String hour) {
		
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date curDate = null;
		try {
			curDate = sDateFormat.parse(date.trim()+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String beforeoneDayDate =getDate(curDate,1).trim();
		String beforeSevenDayDate=getDate(curDate,7).trim();
		
		String beforeOneDayTime=beforeoneDayDate+" "+hour+":00";
		String beforeSevenDayTime=beforeSevenDayDate+" "+hour+":00";
		String currentDateTime=date.trim()+" "+hour+":00";
		
		//获取faceId库中统计的数据格式:		
		List<FaceIdTypeCountDto> faceIdTypeCountDtos = faceIdTypeCountDtoService.selectTypeCount(unitId,date,hour);
		Map<String, Object> faceIdTypeCountMap=new HashMap<String, Object>();
		for (FaceIdTypeCountDto faceIdTypeCountDto : faceIdTypeCountDtos) {
			faceIdTypeCountMap.put(faceIdTypeCountDto.getFaceType(), faceIdTypeCountDto.getFaceCount());
		}
		 
		//统计表
		List<StatisticsDto> statisticsDtos=new LinkedList<StatisticsDto>();
		StatisticsDto beforeOneDayStatisticsDto= statisticsDtoService.selectByDateTime(unitId,beforeOneDayTime,beforeoneDayDate);
		StatisticsDto beforeSevenDayStatisticsDto= statisticsDtoService.selectByDateTime(unitId,beforeSevenDayTime,beforeSevenDayDate);
		
		StatisticsDto statisticsDto =new StatisticsDto();
		statisticsDto.setId(id);
		statisticsDto.setUnitId(unitId);
		statisticsDto.setDateTime(currentDateTime);
		//statisticsDto.setDateTimeMillis(currentTimeMillis);
		//统计年岁人数
		
		statisticsDto.setOnetonighteenCount(Long.parseLong(faceIdTypeCountMap.get("1_19").toString()));
		statisticsDto.setTwentyOtonineCount(Long.parseLong(faceIdTypeCountMap.get("20_29").toString()));
		statisticsDto.setThirtyOtonineCount(Long.parseLong(faceIdTypeCountMap.get("30_39").toString()));
		statisticsDto.setFortyOtonineCount(Long.parseLong(faceIdTypeCountMap.get("40_49").toString()));
		statisticsDto.setFiftyOtonineCount(Long.parseLong(faceIdTypeCountMap.get("50_").toString()));
		
		statisticsDto.setMaleCount(0L);
		statisticsDto.setFemaleCount(0L);
		//男女人数
		if(faceIdTypeCountMap.containsKey("女")){
			statisticsDto.setMaleCount(Long.parseLong(faceIdTypeCountMap.get("女").toString()));
		}
		if(faceIdTypeCountMap.containsKey("男")){
			statisticsDto.setFemaleCount(Long.parseLong(faceIdTypeCountMap.get("男").toString()));
		}
		
		Long currentTotalCount =Long.parseLong(faceIdTypeCountMap.get("all").toString());
		
		//当前总人数
		statisticsDto.setCurrentTotalCount(currentTotalCount);
		//statisticsDto.setCurrentTotalInCount(Long.parseLong(faceIdTypeCountMap.get("in").toString()));
		long beforeOneDayTotalCount =0L;
		long beforeSevenDayTotalCount = 0L;
		long beforeOneDayAvgResidenceTime =0L; 
		long beforeSevenDayAvgResidenceTime =0L; 
		
		if(beforeOneDayStatisticsDto!=null){
		    beforeOneDayTotalCount= beforeOneDayStatisticsDto.getCurrentTotalCount();
			beforeOneDayAvgResidenceTime = beforeOneDayStatisticsDto.getAvgResidenceTime();
		}
		if(beforeSevenDayStatisticsDto!=null){
			beforeSevenDayTotalCount = beforeSevenDayStatisticsDto.getCurrentTotalCount();
			beforeSevenDayAvgResidenceTime = beforeSevenDayStatisticsDto.getAvgResidenceTime();
		}
		 DecimalFormat df = new DecimalFormat("#.0");
		 DecimalFormat dFormat = new DecimalFormat("0.000");//格式化小数  

		 float countRateForYestoday=0;
		 float countRateForSevenDay=0;
		
		if(beforeOneDayTotalCount==0)
		{  
			if(currentTotalCount==0){
				statisticsDto.setCountRateOverYesterday(0);
			}else {
				statisticsDto.setCountRateOverYesterday(999);
			}
		}
		else 
		{   
		  String num = dFormat.format((float)currentTotalCount/beforeOneDayTotalCount);//返回的是String类型 
		  countRateForYestoday=(Float.parseFloat(num)-1)*100;
		  statisticsDto.setCountRateOverYesterday(Float.parseFloat(df.format(countRateForYestoday)));
		}
		if(beforeSevenDayTotalCount==0)
		{	
			if(currentTotalCount==0){
				statisticsDto.setCountRateOverLastweek(0);
			}else {
				statisticsDto.setCountRateOverLastweek(999);
			}
		}
		else{
		   String num = dFormat.format((float)currentTotalCount/beforeSevenDayTotalCount);//返回的是String类型 
		   countRateForSevenDay=(Float.parseFloat(num)-1)*100;
		   statisticsDto.setCountRateOverLastweek(Float.parseFloat(df.format(countRateForSevenDay)));
		}
		
		Long arg_residence_time =Long.parseLong(faceIdTypeCountMap.get("avg_residence_time").toString());
		//平均停留时长
		statisticsDto.setAvgResidenceTime(arg_residence_time);
		
		float avgRateForYestoday=0;
		float avgRateForSevenDay=0;
		
		if(beforeOneDayAvgResidenceTime==0)
		{
			if(arg_residence_time==0){
				statisticsDto.setAvgRateOverYesterday(0);
			}else{
				statisticsDto.setAvgRateOverYesterday(999);
			}
		}
		else{
		   String num = dFormat.format((float)arg_residence_time/beforeOneDayAvgResidenceTime);//返回的是String类型 
		   avgRateForYestoday=(Float.parseFloat(num)-1)*100;
		   statisticsDto.setAvgRateOverYesterday(Float.parseFloat(df.format(avgRateForYestoday)));
		}
		
		if(beforeSevenDayAvgResidenceTime==0)
		{
			if(arg_residence_time==0){
				statisticsDto.setAvgRateOverLastweek(0);
			}else{
				statisticsDto.setAvgRateOverLastweek(999);
			}
		}
		else{
		   String num = dFormat.format((float)arg_residence_time/beforeSevenDayAvgResidenceTime);//返回的是String类型 
		   avgRateForSevenDay=(Float.parseFloat(num)-1)*100;
		   statisticsDto.setAvgRateOverLastweek(Float.parseFloat(df.format(avgRateForSevenDay)));
		}
	  
		//停留时长
		statisticsDto.setOtooneResidenceCount(Long.parseLong(faceIdTypeCountMap.get("0_1").toString()));
		statisticsDto.setOnetothreeResidenceCount(Long.parseLong(faceIdTypeCountMap.get("1_3").toString()));
		statisticsDto.setThreetofiveResidenceCount(Long.parseLong(faceIdTypeCountMap.get("3_5").toString()));
		statisticsDto.setFivetotenResidenceCount(Long.parseLong(faceIdTypeCountMap.get("5_10").toString()));
		statisticsDto.setTentothirtyResidenceCount(Long.parseLong(faceIdTypeCountMap.get("10_30").toString()));
		statisticsDto.setOverthirtyResidenceCount(Long.parseLong(faceIdTypeCountMap.get("30_").toString()));
		
		statisticsDtos.add(statisticsDto);
		
		if(statisticsDtos.size()>0){
		    statisticsDtoService.deleteByDateTime(unitId, currentDateTime);
			statisticsDtoService.insertBatch(statisticsDtos);
		}
	}
	
	/**
	 * 根据门店，保存客流时统计结果
	 * @param unitId  门店ID
 	 */
	private void saveHourStatistic(String unitId){
		List<TempHourDto> tempHourDtos=TempHourDtoService.selectAll(unitId);
		//List<TempHourDto> subTempHourDtos =new ArrayList<TempHourDto>();
		
		Set<String> tempDateSet =new HashSet();
		for (TempHourDto tempHourDto : tempHourDtos) {	
			if(!tempDateSet.contains(tempHourDto.getAppearedDate())){
				tempDateSet.add(tempHourDto.getAppearedDate());
			}
		}
		
		Map<String, List<TempHourDto>> hourMaps=new HashMap<String, List<TempHourDto>>();
		
		for (String tempDate : tempDateSet) {
			List<TempHourDto> tempHourList=new ArrayList<TempHourDto>();
			for (TempHourDto tempHourDto : tempHourDtos) {
				if(tempHourDto.getAppearedDate().equals(tempDate)){
					tempHourList.add(tempHourDto);
				}
			}
			
			hourMaps.put(tempDate, tempHourList);
		}
		 
		for (String tempDate:tempDateSet) {
			//时报表数据
			List<HourStatisticsDto> hourStatisticsDtos=hourStatisticsDtoService.selectByDateTime(tempDate,unitId);
			if(hourStatisticsDtos==null)
			{
				hourStatisticsDtos=new LinkedList<HourStatisticsDto>();
			}	
			List<TempHourDto> tmpHourDtoList = new ArrayList<TempHourDto>(); 
			tmpHourDtoList=hourMaps.get(tempDate);
			if(hourStatisticsDtos.size()>0){
				for (HourStatisticsDto hs : hourStatisticsDtos) {
					hs.setDateTime(tempDate); 
					if("男".equals(hs.getGender())){
						setHourPropery(tempDate,hs,tmpHourDtoList,"男");
					}
					else{
						setHourPropery(tempDate,hs,tmpHourDtoList,"女"); 
					}
				}
			}
			else{
				 
				HourStatisticsDto hsMale=new HourStatisticsDto();
				hsMale.setDateTime(tempDate);
				hsMale.setId(idService.newOne());
				hsMale.setUnitId(unitId);
				hsMale.setGender("男");
				setHourPropery(tempDate,hsMale,tmpHourDtoList,"男"); 
				
				HourStatisticsDto hsFemale=new HourStatisticsDto();
				hsFemale.setDateTime(tempDate);
				hsFemale.setId(idService.newOne());
				hsFemale.setUnitId(unitId);
				hsFemale.setGender("女");
				setHourPropery(tempDate,hsFemale,tmpHourDtoList,"女"); 
									
				hourStatisticsDtos.add(hsFemale);
				hourStatisticsDtos.add(hsMale); 
			}
			
			hourStatisticsDtoService.deleteByDateTime(tempDate,unitId);
			hourStatisticsDtoService.insertBatch(hourStatisticsDtos);
		}
	}
	
	private void setHourPropery(String dateTime,HourStatisticsDto hourStatisticsDto,List<TempHourDto> tempHourList,String gender){
		long zero = hourStatisticsDto.getZeroCount()==null?0L:hourStatisticsDto.getZeroCount();
		long one =hourStatisticsDto.getOneCount()==null?0L:hourStatisticsDto.getOneCount();
		long two =hourStatisticsDto.getTwoCount()==null?0L:hourStatisticsDto.getTwoCount(); 
		long three = hourStatisticsDto.getThreeCount()==null?0L:hourStatisticsDto.getThreeCount();
		long four =hourStatisticsDto.getFourCount()==null?0L:hourStatisticsDto.getFourCount();
		long five = hourStatisticsDto.getFiveCount()==null?0L:hourStatisticsDto.getFiveCount();
		long six = hourStatisticsDto.getSixCount()==null?0L:hourStatisticsDto.getSixCount();
		long seven=hourStatisticsDto.getSevenCount()==null?0L:hourStatisticsDto.getSevenCount();
		long eight=hourStatisticsDto.getEightCount()==null?0L:hourStatisticsDto.getEightCount();
		long nine =hourStatisticsDto.getNineCount()==null?0L:hourStatisticsDto.getNineCount();
		long ten =hourStatisticsDto.getTenCount()==null?0L:hourStatisticsDto.getTenCount();
		long eleven =hourStatisticsDto.getElevenCount()==null?0L:hourStatisticsDto.getElevenCount();
		long twelve =hourStatisticsDto.getTwelveCount()==null?0L:hourStatisticsDto.getTwelveCount();
		long thirteen=hourStatisticsDto.getThirteenCount()==null?0L:hourStatisticsDto.getThirteenCount();
		long forteen =hourStatisticsDto.getForteenCount()==null?0L:hourStatisticsDto.getForteenCount();
		long fifteen =hourStatisticsDto.getFifteenCount()==null?0L:hourStatisticsDto.getFifteenCount();
		long sixteen =hourStatisticsDto.getSixteenCount()==null?0L:hourStatisticsDto.getSixteenCount();
		long seventeen =hourStatisticsDto.getSeventeenCount()==null?0L:hourStatisticsDto.getSeventeenCount();
		long eighteen =hourStatisticsDto.getEighteenCount()==null?0L:hourStatisticsDto.getEighteenCount();
		long nintheen =hourStatisticsDto.getNinteenCount()==null?0L:hourStatisticsDto.getNinteenCount();
		long twenty=hourStatisticsDto.getTwentyCount()==null?0L:hourStatisticsDto.getTwentyCount();
		long twentyone=hourStatisticsDto.getTwentyoneCount()==null?0L:hourStatisticsDto.getTwentyoneCount();
		long twentytwo=hourStatisticsDto.getTwentytwoCount()==null?0L:hourStatisticsDto.getTwentytwoCount();
		long twentythree=hourStatisticsDto.getTwentythreeCount()==null?0L:hourStatisticsDto.getTwentythreeCount();
	 	
		for (TempHourDto tempHourDto : tempHourList) {
			if(gender.equals(tempHourDto.getGender())){
				int hour =Integer.parseInt(tempHourDto.getAppearedHour());
				switch (hour) {
				case 0:
					zero = tempHourDto.getFaceCount();
					break;
				case 1:
				    one= tempHourDto.getFaceCount();
					break;
				case 2:
					two= tempHourDto.getFaceCount();
					break;
				case 3:
					three= tempHourDto.getFaceCount();
					break;
				case 4:
					four= tempHourDto.getFaceCount();
					break;
				case 5:
					five= tempHourDto.getFaceCount();
					break;
				case 6:
					six= tempHourDto.getFaceCount();
					break;
				case 7:
					seven= tempHourDto.getFaceCount();
					break;
				case 8:
					eight= tempHourDto.getFaceCount();
					break;
				case 9:
					nine= tempHourDto.getFaceCount();
					break;
				case 10:
					ten= tempHourDto.getFaceCount();
					break;
				case 11:
					eleven= tempHourDto.getFaceCount();
					break;
				case 12:
					twelve= tempHourDto.getFaceCount();
					break;
				case 13:
					thirteen= tempHourDto.getFaceCount();
					break;
				case 14:
					forteen= tempHourDto.getFaceCount();
					break;
				case 15:
					fifteen= tempHourDto.getFaceCount();
					break;
				case 16:
					sixteen= tempHourDto.getFaceCount();
					break;
				case 17:
					seventeen= tempHourDto.getFaceCount();
					break;
				case 18:
					eighteen= tempHourDto.getFaceCount();
					break;
				case 19:
					nintheen= tempHourDto.getFaceCount();
					break;
				case 20:
					twenty= tempHourDto.getFaceCount();
				    break;
				case 21:
					twentyone= tempHourDto.getFaceCount();
					break;
				case 22:
					twentytwo= tempHourDto.getFaceCount();
					break;
				case 23:
					twentythree= tempHourDto.getFaceCount();
					break;
				default:
					break;
				}
			}
			
			hourStatisticsDto.setZeroCount(zero);
			hourStatisticsDto.setOneCount(one);
			hourStatisticsDto.setTwoCount(two);
			hourStatisticsDto.setThreeCount(three);
			hourStatisticsDto.setFourCount(four);
			hourStatisticsDto.setFiveCount(five);
			hourStatisticsDto.setSixCount(six);
			hourStatisticsDto.setSevenCount(seven);
			hourStatisticsDto.setEightCount(eight);
			hourStatisticsDto.setNineCount(nine);
			hourStatisticsDto.setTenCount(ten);
			hourStatisticsDto.setElevenCount(eleven);
			hourStatisticsDto.setTwelveCount(twelve);
			hourStatisticsDto.setThirteenCount(thirteen);
			hourStatisticsDto.setForteenCount(forteen);
			hourStatisticsDto.setFifteenCount(fifteen);		
			hourStatisticsDto.setSixteenCount(sixteen);
			hourStatisticsDto.setSeventeenCount(seventeen);
			hourStatisticsDto.setEighteenCount(eighteen);
			hourStatisticsDto.setNinteenCount(nintheen);
			hourStatisticsDto.setTwentyCount(twenty);
			hourStatisticsDto.setTwentyoneCount(twentyone);
			hourStatisticsDto.setTwentytwoCount(twentytwo);
			hourStatisticsDto.setTwentythreeCount(twentythree);
		} 
	}
	
	private String getDate(Date curDate,int day) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-day);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}


	/*
	 * 按时间段进行同步，并进行统计 
	 */
	@Override
	public void sync(Date startDate, Date endDate) {
				
		Calendar startCalendar =Calendar.getInstance();
		startCalendar.setTime(startDate); 
		long startMillis = startCalendar.getTimeInMillis();
		
		Calendar endCalendar =Calendar.getInstance();
		endCalendar.setTime(endDate); 
		long endMillis = endCalendar.getTimeInMillis();
		 
		SimpleDateFormat ymdhmFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(startCalendar.getTime());
		//获取对应的摄像头类型【进，出】
		List<DeviceDto> deviceDtos = deviceDtoService.selectAll();
		if (deviceDtos.size()==0) {
			return ;
		}
		//设备类型为进、出
		Map<String, Object> deviceMap =new HashMap<String, Object>();
		//设备门店
		Map<String, Object> unitMap = new HashMap<String, Object>();
		List<String> unitList=new ArrayList<String>();
		for (DeviceDto obj: deviceDtos) {
			String key=obj.getMac();
			int value =obj.getTypeState();
			String unitId=obj.getStoreId();
			if(!StringUtils.isEmpty(key)){
				
				deviceMap.put(key, value);				
				unitMap.put(key, unitId);
				if(!unitList.contains(unitId))
				{
					unitList.add(unitId);	
				}
			}
		}
		 /*-------------------------------同步开始------------------------------------- */
		//同步mongodb数据
		List<FaceIdCollection> list =faceIdCollectionService.getByTime(startMillis, endMillis);
		List<LogCollection> logList=logCollectionService.getByTime(startMillis, endMillis);
		
		//根据日期区间获取Face ID 表中的数据
		List<FaceIdCollectionDto> targetFaceIdList =faceIdCollectionDtoService.selectByTime(startMillis, endMillis);
		 
		List<FaceIdCollectionDto> faceIdCollectionDtos = new ArrayList<FaceIdCollectionDto>();
		
		//筛选集合中的数据，如果已存在，清空数据。
		for (Iterator iterator = list.iterator(); iterator
				.hasNext();) {
			FaceIdCollection fic = (FaceIdCollection) iterator
					.next();
			for (Iterator targetIterator = targetFaceIdList.iterator(); targetIterator
					.hasNext();) {
				FaceIdCollectionDto dto = (FaceIdCollectionDto) targetIterator
						.next();
				if(dto.getFaceId().equals(fic.getFace_id())
						&&dto.getCaptureTime()==fic.getCapture_time()
						&&dto.getDevice().equals(fic.getDevice())
					  ){
					iterator.remove();
				}
			}
		}
		
		for (FaceIdCollection faceIdCollection : list) {
			if(!deviceMap.containsKey(faceIdCollection.getDevice())){
				continue;
			}
						
			FaceIdCollectionDto faceIdCollectionDto =new FaceIdCollectionDto();
			faceIdCollectionDto.setId(faceIdCollection.getId());
			faceIdCollectionDto.setAge(faceIdCollection.getAge());
			faceIdCollectionDto.setAppearedNum(faceIdCollection.getAppeared_num());
			faceIdCollectionDto.setCaptureTime(faceIdCollection.getCapture_time());
			faceIdCollectionDto.setDevice(faceIdCollection.getDevice());
			faceIdCollectionDto.setFaceId(faceIdCollection.getFace_id());
			faceIdCollectionDto.setFeature("");
			faceIdCollectionDto.setGender(faceIdCollection.getGender());
			faceIdCollectionDto.setImage(faceIdCollectionDto.getImage());
			faceIdCollectionDto.setDeviceType(Integer.parseInt(deviceMap.get(faceIdCollection.getDevice()).toString()));
			faceIdCollectionDto.setUnitId(unitMap.get(faceIdCollection.getDevice()).toString());
			faceIdCollectionDto.setCaptureTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(faceIdCollection.getCapture_time())));
			faceIdCollectionDtos.add(faceIdCollectionDto);			
		}
		
		//List<FaceLogCollectionDto> targetFaceLogList = faceLogCollectionDtoService.selectByTime(startMillis, endMillis);
		String startDateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(startMillis));
		String endDateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(endMillis));
		//根据日期区间获取访问记录jd_face_visitor_log 表中的数据
		List<FaceVisitorLogDto> targetVisitorLogList = faceVisitorLogDtoService.selectByTime(startDateTimeStr, endDateTimeStr);
		
		List<FaceLogCollectionDto> faceLogCollectionDtos = new ArrayList<FaceLogCollectionDto>();
		List<FaceVisitorLogDto> faceVisitorLogDtos =new ArrayList<FaceVisitorLogDto>();
		 
		for (LogCollection logCollection : logList) {
			if(!deviceMap.containsKey(logCollection.getDevice())){
				continue;
			}
			
			String dateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String date_TimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date(logCollection.getAppeared_time()));
			String hourStr = new SimpleDateFormat("HH").format(new Date(logCollection.getAppeared_time()));
			String minuteStr = new SimpleDateFormat("mm").format(new Date(logCollection.getAppeared_time()));
			
			//人脸历史记录表，只存储当天的数据，用于当天统计
			FaceLogCollectionDto faceLogCollectionDto =new FaceLogCollectionDto();
			faceLogCollectionDto.setId(logCollection.getId());
			faceLogCollectionDto.setFaceId(logCollection.getFace_id());
			faceLogCollectionDto.setDevice(logCollection.getDevice());
			faceLogCollectionDto.setAppearedTime(logCollection.getAppeared_time());
			faceLogCollectionDto.setImage(logCollection.getImage());
			faceLogCollectionDto.setDeviceType(Integer.parseInt(deviceMap.get(logCollection.getDevice()).toString()));
			faceLogCollectionDto.setUnitId(unitMap.get(logCollection.getDevice()).toString());
			faceLogCollectionDto.setAppearedFormatTime(date_TimeStr);
			faceLogCollectionDto.setAppearedDate(dateStr);
			faceLogCollectionDto.setAppearedHour(hourStr);
			faceLogCollectionDto.setAppearedMinute(minuteStr);
			faceLogCollectionDtos.add(faceLogCollectionDto); 
		}
		//筛选集合中的数据，如果已存在，清空数据。
		for (Iterator iterator = logList.iterator(); iterator
				.hasNext();) {
			LogCollection fic = (LogCollection) iterator
					.next();
			for (Iterator targetIterator = targetVisitorLogList.iterator(); targetIterator
					.hasNext();) {
				FaceVisitorLogDto dto = (FaceVisitorLogDto) targetIterator
						.next();
				String dateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(fic.getAppeared_time()));
				if(dto.getFaceId().equals(fic.getFace_id())
						&&dto.getDateTime().equals(dateTimeStr)
						&&dto.getDevice().equals(fic.getDevice())
					  ){
					iterator.remove();
				}
			}
		}
		
		
		for (LogCollection logCollection : logList) {
			if(!deviceMap.containsKey(logCollection.getDevice())){
				continue;
			}
			
			String dateTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String date_TimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(logCollection.getAppeared_time()));
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date(logCollection.getAppeared_time()));
			String hourStr = new SimpleDateFormat("HH").format(new Date(logCollection.getAppeared_time()));
			String minuteStr = new SimpleDateFormat("mm").format(new Date(logCollection.getAppeared_time()));
			
			//访客历史记录表，用于存储所有的访客数据。		
			FaceVisitorLogDto faceVisitorLogDto=new FaceVisitorLogDto();
			faceVisitorLogDto.setId(idService.newOne());
			faceVisitorLogDto.setFaceId(logCollection.getFace_id());
			faceVisitorLogDto.setUnitId(unitMap.get(logCollection.getDevice()).toString());
			faceVisitorLogDto.setDeviceType(Integer.parseInt(deviceMap.get(logCollection.getDevice()).toString()));
			faceVisitorLogDto.setDateTime(dateTimeStr);
			faceVisitorLogDto.setFaceDate(dateStr);
			faceVisitorLogDto.setImage(logCollection.getImage());
			faceVisitorLogDto.setDevice(logCollection.getDevice());
			faceVisitorLogDtos.add(faceVisitorLogDto);
		}
		
		try {
			
			if(faceVisitorLogDtos.size()>0){
				faceVisitorLogDtoService.insertBatch(faceVisitorLogDtos);
			}
			if(faceIdCollectionDtos.size()>0){
				faceIdCollectionDtoService.insertBatch(faceIdCollectionDtos);
			}
			if(faceLogCollectionDtos.size()>0){
				faceLogCollectionDtoService.deleteFromStartToEnd(startMillis, endMillis);
				System.out.println("face_log_collection count :"+faceLogCollectionDtos.size());
				faceLogCollectionDtoService.insertBatch(faceLogCollectionDtos);
			}
		} catch (Exception e) {
			System.out.println(new Date().toString()+": 同步face表异常");
			e.printStackTrace();
		}
		  
		 /*-------------------------------同步完成------------------------------------- */
		 
		/*--------------------------------开始处理统计业务逻辑------------------------------------------*/
	    			
		 String id = idService.newOne();
		 if(faceLogCollectionDtos.size()==0)
			 return;
		  
		 //循环门店信息
		 for(String unitId:unitList){
			List<FaceLogCollectionDto> faceLogDatesFromUnitList = faceLogCollectionDtoService.selectLogDates(unitId);
			
			if(faceLogDatesFromUnitList.size()==0){
				continue;
			}
			
			for (FaceLogCollectionDto flDates : faceLogDatesFromUnitList) {
				String appearedDate = flDates.getAppearedDate();
				String appearedHour = flDates.getAppearedHour();
				
				//停留时间统计数据，临时数据
				saveTempData(unitId,appearedDate,appearedHour);		
				
				id = idService.newOne();
				//保存统计数据
				saveStatistic(unitId,id,appearedDate,appearedHour);				
			}
						
			//时报表数据
			saveHourStatistic(unitId);			
		}
		
	}
	
}
