package com.isoftstone.common.api.util;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.api.service.oss.OssStorageFactory;
import com.isoftstone.common.util.IdService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.MyProps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service
public class ExcelUtils {
	    private final static String excel2003L =".xls";    //2003- 版本的excel
	    private final static String excel2007U =".xlsx";   //2007+ 版本的excel
	 
		@Autowired
		JsonService jsonService;
		@Autowired
		IdService idService;
		@Autowired
	    MyProps myProps;
		@Autowired
		OssStorageFactory ossStorageFactory;
		
	    /**
	     * 拼装单个obj  通用
	     *
	     * @param obj
	     * @param row
	     * @return
	     * @throws Exception
	     */
/*	    private Map<String, Object> dataObj(String objJson, Row row) throws Exception {
	    	 
	    	List<Map<String, Object>> objList =(List<Map<String, Object>>) jsonService.parseObject(objJson);
	      	
	    	if(objList==null){
	    		return null;
	    	}
	    	 
	        //容器
	        Map<String, Object> map = new HashMap<String, Object>();
	        int i=0;
	        for (Map<String, Object> m : objList) {
	        	map.put(m.get("name").toString(), getVal(row.getCell(i)));
				i++;
			}
	         
	        return map;
	    }*/
	    
	    private  Map<String, Object> dataObj(Object obj, Row row) throws Exception {
	    	String errorMessage = "";
	        //容器
	        Map<String, Object> map = new HashMap<String, Object>();
	    	if(obj instanceof String){
	    		List<Map<String, Object>> objList =(List<Map<String, Object>>) jsonService.parseObject(obj.toString());
		      	
		    	if(objList==null){
		    		return null;
		    	}

		    	 
		        int i=0;
		        for (Map<String, Object> m : objList) {
		        	Cell  cell= row.getCell(i);
		        	if (cell==null){
		        		 continue;
		            }
		        	if(!StringUtils.isEmpty(getVal(cell).trim())){
//		        		规则校验
		        		JSONObject verifyTypeMap =  (JSONObject) m.get("verifyType");
		        		String displayName = m.get("value").toString();
		        		String messageStr = "";
		        		if(verifyTypeMap!=null) {
							for (Entry<String, Object> entry : verifyTypeMap.entrySet()) {
								String key = entry.getKey();
								Object value = entry.getValue();
								messageStr += ArgsValidateUtils.validateResult(key, value, getVal(cell));
							}
					   }
		        	    if (!StringUtils.isEmpty(messageStr)) {
							messageStr = messageStr.substring(0,
									messageStr.length() - 1);
							errorMessage += displayName + messageStr;
						}
		        	    if (StringUtils.isEmpty(errorMessage)) {
		        	    	map.put(m.get("name").toString(), getVal(cell));	
		        	    }else {
		        	    	map.put("hasErr", errorMessage);
		        	    }
		        	}
					i++;
				}
	    	}
	    	else{	    	
		        Class<?> rowClazz= obj.getClass();
		        Field[] fields = FieldUtils.getAllFields(rowClazz);
		        if (fields == null || fields.length < 1) {
		            return null;
		        }

		        //注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
		        for (int j = 0; j < fields.length; j++) {
		        	Cell  cell= row.getCell(j);
		        	if (cell==null){
		        		 continue;
		            }
		        	if(!StringUtils.isEmpty(getVal(cell).trim())){
		              map.put(fields[j].getName(), getVal(cell));
		        	}
		        }
	    	}
	    	
	    	if(map!=null && map.size()>0){
		    	//id 为自动生成的值
		    	if(!map.containsKey("id")){
		    		map.put("id", idService.newOne());
		    	}
		    	else{
		    		map.replace("id", idService.newOne());
		    	}
	    	}
	        return map;
	    }
	    
	    
	    /**
	     * 处理val
	     *
	     * @param cell
	     * @return
	     */
	    @SuppressWarnings("deprecation")
		public String getVal(Cell cell) {
	        Object value = null;
//	        DecimalFormat df = new DecimalFormat("0");  //格式化字符类型的数字
	        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
	        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字	        
		        switch (cell.getCellType()) {		       
		            case Cell.CELL_TYPE_STRING:
		                value = cell.getRichStringCellValue().getString();
		                break;
		            case  Cell.CELL_TYPE_NUMERIC:
		                if("General".equals(cell.getCellStyle().getDataFormatString())){
		                	double numericCellValue = cell.getNumericCellValue();
//		                    value = df.format(numericCellValue);
		                	value = numericCellValue;
		                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
		                    value = sdf.format(cell.getDateCellValue());
		                }else{
		                	double numericCellValue = cell.getNumericCellValue();
		                    value = df2.format(numericCellValue);
		                }
		                break;
		            case Cell.CELL_TYPE_BOOLEAN:
		                value = cell.getBooleanCellValue();
		                break;
		            case Cell.CELL_TYPE_BLANK:
		                value = "";
		                break;
		            default:
		                break;
		        }
	        
	        return value.toString();
	    }
	    /**
	     * * 读取出filePath中的所有数据信息
	     *
	     * @param file excel文件的绝对路径
	     * @param obj
	     * @return
	     * @throws Exception 
	     */
	    public Map<String, Object> getDataFromExcel(String ossType, List<Map<String, Object>> ossMapList, MultipartFile file, Object obj) throws Exception{
	 
	        if (null == obj) {
	            return null;
	        }
	        Map<String, Object> ret = new HashMap<String, Object>();
	        FileInputStream fis =null;
	        int lineNum = 0;
	        Workbook workbook = null;
	        Sheet sheet = null;
	        try{
	            //获取一个绝对地址的流
	            fis = (FileInputStream) file.getInputStream();
	            workbook =getWorkbook(fis, file.getOriginalFilename());
	            //得到一个工作表
	            sheet = workbook.getSheetAt(0);
	            //获得表头
	            Row rowHead = sheet.getRow(0);
	            //列数
	            int rows = rowHead.getPhysicalNumberOfCells();
	            //行数
	            lineNum = sheet.getLastRowNum();
	            if(0 == lineNum){
	               // log.info("ImportExcelFileUtil中的getDataFromExcel方法导入的Excel内没有数据！");
	            }
	            
	            //读取图片
	            Map<String, Object> picMap =new HashMap<String, Object>();
	            if (workbook instanceof HSSFWorkbook) {
            		 Map<String, Object> process = process((HSSFSheet)sheet, (HSSFWorkbook)workbook,picMap,ossMapList,ossType);
            		 if(!process.isEmpty()) {
            			 ret.put("hasErr", process);
            			 return ret;
            		 }
            	}else{
            		process((XSSFSheet)sheet,picMap,ossMapList,ossType);
            	}           
	            	            
	            ret = getData(sheet, lineNum, rows, obj,picMap);
	            
	            workbook.close();
	        } catch (Exception e){
	           throw e;
	        }
	        return ret;
	    }
	   
	  
	     /** @param obj
	     * @return
	     */
	    public Map<String, Object>  getData(Sheet sheet, int lineNum, int rowNum, Object obj,Map<String, Object> picMap){
	    	Map<String, Object> retMap=new HashMap<String, Object>();
	    	
	    	List<Map<String, Object>> dataList = new LinkedList<Map<String,Object>>();
	    	List<Map<String, Object>> picList =new LinkedList<Map<String,Object>>();
	        try {
	            //容器
	        	dataList = new LinkedList<Map<String,Object>>();
	            //获得所有数据
	            for(int i = 1; i <= lineNum; i++){
	                //获得第i行对象
	                Row row = sheet.getRow(i);
	                if(row!=null){	
	                	//添加表格数据
	                	Map<String, Object> map =dataObj(obj,row);
	                	if(map !=null&&map.size()>0){
	                	System.out.println(map);

	                		dataList.add(map);
	                	
		                	//添加表格图片
		                	for(String key: picMap.keySet()) {
		                		Map<String, Object> fileMap = new HashMap<String, Object>();
		                		if(key.contains(i+"_")){
		                			fileMap.put("id", idService.newOne());
		                			fileMap.put("fid",map.get("id"));
		                			if(key.contains("$$")){
		                				fileMap.put("type", "0");
		                			}
		                			else{
		                				fileMap.put("type", "1");
		                			}
		                			fileMap.put("filepath",picMap.get(key));
		                			picList.add(fileMap);
		                		}	                		
		                	} 
	                	}
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        retMap.put("dataList", dataList);
	        retMap.put("picList", picList);
	        return retMap;
	    }
	 
	    /**
	     * 描述：根据文件后缀，自适应上传文件的版本[.xls,.xlsx]
	     *
	     * @param inStr,fileName
	     * @return
	     * @throws Exception
	     */
	    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception{
	        Workbook wb = null;
	        String fileType = fileName.substring(fileName.lastIndexOf("."));
	        if(excel2003L.equals(fileType)){  //2003-
	        	wb = new HSSFWorkbook(inStr);
	        }else if(excel2007U.equals(fileType)){ //2007+
	        	wb = new XSSFWorkbook(inStr);
	        }else{
	            throw new Exception("解析的文件格式有误！");
	        }
	        return wb;
	    }
	    
	    public  Map<String, Object> process(HSSFSheet sheet, HSSFWorkbook workbook,Map<String, Object> map,List<Map<String, Object>> ossMapList,String ossType)
				throws Exception {
	    	Map<String, Object> ret = new HashMap<String, Object>();
	    	int handle=0;
			List<HSSFPictureData> pictures = workbook.getAllPictures();
			if (!CollectionUtils.isEmpty(pictures)) {
				
				
				for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
					HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
					if (shape instanceof HSSFPicture) {
						HSSFPicture pic = (HSSFPicture) shape;
						int pictureIndex = pic.getPictureIndex() - 1;
						handle = handle(anchor.getRow1(), anchor.getCol1(),
								pictures.get(pictureIndex),map,ossMapList,ossType);
						if(handle<0) {
							String result=anchor.getRow1()+"行"+ anchor.getCol1()+"列";
							ret.put(result, "照片文件不能超过1M，请重新上传。");
						}
					}
				}
				if(handle==0&&ossType!=null) {
					String rootPath = myProps.getUpFilePath();
					String separator =System.getProperty("file.separator");
					for (String key : map.keySet()) {
						String xiangdio=(String) map.get(key);	
						String savepath=rootPath+separator+xiangdio;
						String  storageFilePath= ossStorageFactory.getInstance(ossType).transferFile(ossMapList,savepath, xiangdio);
				        new File(savepath).delete();
				        map.put(key, storageFilePath);
					}
				}
			}
			return ret;
		}
	    
	    public  Map<String, Object> process(XSSFSheet sheet,Map<String, Object> map,List<Map<String, Object>> ossMapList,String ossType) throws Exception {
	    	Map<String, Object> ret = new HashMap<String, Object>();
	    	int handle=0;
	    	for (POIXMLDocumentPart dr : sheet.getRelations()) {
				if (dr instanceof XSSFDrawing) {
					XSSFDrawing drawing = (XSSFDrawing) dr;
					List<XSSFShape> shapes = drawing.getShapes();
					for (XSSFShape shape : shapes) {
						XSSFPicture pic = (XSSFPicture) shape;
						XSSFClientAnchor anchor = pic.getPreferredSize();
						CTMarker ctMarker = anchor.getFrom();
						 handle = handle(ctMarker.getRow(), ctMarker.getCol(),
								pic.getPictureData(),map,ossMapList,ossType);
					   if(handle<0) {
								String result=anchor.getRow1()+"行"+ anchor.getCol1()+"列";
						    	ret.put(result, "照片文件不能超过1M，请重新上传。");
							}
					}
				}
			}
			if(handle==0&&ossType!=null) {
				String rootPath = myProps.getUpFilePath();
				String separator =System.getProperty("file.separator");
				for (String key : map.keySet()) {
					String xiangdio=(String) map.get(key);	
					String savepath=rootPath+separator+xiangdio;
					String  storageFilePath= ossStorageFactory.getInstance(ossType).transferFile(ossMapList,savepath, xiangdio);
			        new File(savepath).delete();
			        map.put(key, storageFilePath);
				}
			}
			return ret;
		}

		public  int handle(int rowIndex, int colIndex, PictureData picData,Map<String, Object> map,List<Map<String, Object>> ossMapList,String ossType)
				throws Exception {
			int result = 1;
			String fileName = idService.newOne();
			
			String rootPath = myProps.getUpFilePath();
			String separator =System.getProperty("file.separator");
	    	String fileNamePrefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
	    	
	    	String fileDirPath =rootPath+separator+fileNamePrefix;
	    	
	    	File currFile=new File(fileDirPath);
	    	
	    	try {
	    		if(!currFile.exists()){
	    			FileUtils.forceMkdir(currFile);
	    		}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String fileExtension = picData.suggestFileExtension();			
			String filePath = fileDirPath+separator + rowIndex
					+ "_" + colIndex+"_" +fileName+ "." + fileExtension;

			byte[] data = picData.getData();
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(data);
			out.close();
			
			File file1=new File(filePath);
			System.out.println(filePath);
			long l= file1.length();
			if(l>1024*1024) {
				System.out.println(l);
				return result=-1;
			}
			
			String relativeFilePath =fileNamePrefix +"/" + rowIndex+ "_" + colIndex+"_"+fileName+"."+fileExtension;
			
			String storageFilePath = relativeFilePath;
//	        if(!StringUtils.isEmpty(ossType)){
//	           storageFilePath= ossStorageFactory.getInstance(ossType).transferFile(ossMapList,filePath, relativeFilePath);
//	           new File(filePath).delete();
//	        }
			
			boolean isExistKey=false;
			for(String key: map.keySet()){
				if(key.contains(rowIndex+ "_")){
					isExistKey=true;
				}
			}
			 if(!StringUtils.isEmpty(ossType)){
			   if(isExistKey){
			      map.put(rowIndex+ "_" + colIndex, storageFilePath);	
			    }else{
				  map.put(rowIndex+ "_" + colIndex+"$$", storageFilePath);	
			    }
			} else {
				if(isExistKey){
			      map.put(rowIndex+ "_" + colIndex, rootPath+separator+storageFilePath);	
			    }else{
			     map.put(rowIndex+ "_" + colIndex+"$$", rootPath+separator+storageFilePath);	
			    }
				}
			return result;
			
		}
}
