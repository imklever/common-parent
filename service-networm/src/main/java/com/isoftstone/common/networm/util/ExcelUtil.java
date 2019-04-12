//package com.isoftstone.common.networm.util;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//public class ExcelUtil {
//	public static void ExportExcelData(String sheetName,String fileName, int columnNumber, int[] columnWidth,
//			String[] columnName, List<List<String>> dataList) throws Exception {
//		if (columnNumber == columnWidth.length&& columnWidth.length == columnName.length) {
//			// 第一步，创建一个webbook，对应一个Excel文件
//			HSSFWorkbook wb = new HSSFWorkbook();
//			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
//			HSSFSheet sheet = wb.createSheet(sheetName);
//
//			for (int i = 0; i < columnNumber; i++)
//			{
//				for (int j = 0; j <= i; j++)
//				{
//					if (i == j)
//					{
//						sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
//					}
//				}
//			}
//
//			// 创建第1行 也就是表头
//			HSSFRow row = sheet.createRow(0);
//
//			// 第四.一步，创建表头的列
//			for (int i = 0; i < columnNumber; i++)
//			{
//				HSSFCell cell = row.createCell(i);
//				cell.setCellValue(columnName[i]);
//			}
//
//			// 第五步，创建单元格，并设置值
//			for (int i = 0; i < dataList.size(); i++)
//			{
//				row = sheet.createRow((int) i + 1);
//				HSSFCell datacell = null;
//				for (int j = 0; j < columnNumber; j++)
//				{
//					datacell = row.createCell(j);
//					datacell.setCellValue(dataList.get(i).get(j));
//				}
//			}
//
//			// 第六步，将文件存到指定位置
//			try {
//				File  file= new File("E:/"+fileName+".xls");
//				if(!file.exists()){
//				    FileUtils.touch(file);
//				}
//				FileOutputStream fout = new FileOutputStream(file);
//				wb.write(fout);
//				String str = "导出" + fileName + "成功！";
//				System.out.println(str);
//				fout.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				String str1 = "导出" + fileName + "失败！";
//				System.out.println(str1);
//			}
//		} else {
//			System.out.println("列数目长度名称三个数组长度要一致");
//		}
//
//	}
//}
