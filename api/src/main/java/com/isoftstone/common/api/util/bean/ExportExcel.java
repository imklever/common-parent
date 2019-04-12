package com.isoftstone.common.api.util.bean;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/1.
 */
public class ExportExcel<T> {
    public static final String FILE_SEPARATOR = System.getProperties()
            .getProperty("file.separator");

    public void exportExcel(Collection<T> dataset, OutputStream out) {
        exportExcel("测试POI导出EXCEL文档", null,null, dataset, out, "yyyy-MM-dd");
    }

    public void exportExcel(String[] headers, Collection<T> dataset,
                            OutputStream out) {
        exportExcel("测试POI导出EXCEL文档", headers,null, dataset, out, "yyyy-MM-dd");
    }

    public void exportExcel(String[] headers, Collection<T> dataset,
                            OutputStream out, String pattern) {
        exportExcel("测试POI导出EXCEL文档", headers,null, dataset, out, pattern);
    }
    public void exportExcel(String[] headers, String[] fields, Collection<T> dataset,
                            OutputStream out, String pattern) {
        exportExcel("测试POI导出EXCEL文档", headers,fields, dataset, out, pattern);
    }
    public void exportExcel(String title,String[] headers, String[] fields, Collection<T> dataset,
                            OutputStream out) {
        exportExcel(title, headers,fields, dataset, out, null);
    }
    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title
     *            表格标题名
     * @param headers
     *            表格属性列名数组
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("unchecked")
    public void exportExcel(String title, String[] headers, String[] fields,
                            Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
       /* style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);*/
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        /*font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);*/
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
     /*   style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);*/
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        /*font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);*/
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 声明一个画图的顶级管理器
       // HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
       // HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
      //          0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        //comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
       // comment.setAuthor("leno");
        // 产生表格标题行
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        HSSFSheet sheet = null;
        HSSFRow row=null;
        int sheetCount=1;
        while (it.hasNext()) {
            if(index%65500==0) {
            	sheet= createSheet(workbook,title+"("+sheetCount+")");
                createSheetHeaders(sheet,headers,style);
                index = 0;
                sheetCount++;
            }
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            if(t instanceof Map) {
            	createByMap(t,row,fields,style2,workbook);
            }else {
            	createByTClass(t,row,fields,style2,workbook);
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


	private void createSheetHeaders(HSSFSheet sheet, String[] headers, HSSFCellStyle style) {
		HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
	}

	private HSSFSheet createSheet(HSSFWorkbook workbook, String title) {
		// 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
		return sheet;
	}

	private void createByMap(Object  t, HSSFRow row, String[] fields, HSSFCellStyle style2, HSSFWorkbook workbook) {
		if(fields==null||fields.length==0) {
			throw new RuntimeException("fields 不能为null");
		}
		int dd=fields.length;
		HSSFCell cell=null;
		for (short i = 0; i < dd; i++) {
			cell = row.createCell(i);
            cell.setCellStyle(style2);
            String fieldName=fields[i];
            Object value=((Map)t).get(fieldName);
            // 判断值的类型后进行强制类型转换
            String textValue = null;
                // 其它数据类型都当作字符串简单处理
             if (value==null){
                    textValue ="";
             }else{
                    textValue = value.toString();
             }
            // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
            if (textValue != null) {
               /* Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                Matcher matcher = p.matcher(textValue);
                if (matcher.matches()) {
                    // 是数字当作double处理
                    cell.setCellValue(Double.parseDouble(textValue));
                } else {*/
                    HSSFRichTextString richString = new HSSFRichTextString(
                            textValue);
                    richString.applyFont(style2.getFontIndex());
                    cell.setCellValue(richString);
               // }
            }
		}
	}

	private void createByTClass(Object t,HSSFRow row,String[] fields,HSSFCellStyle style2,HSSFWorkbook workbook) {
		 Field[] allfields= t.getClass().getDeclaredFields();
         int dd=0;
         if(fields==null){
             dd=allfields.length;
         }else {
             dd=fields.length;
         }
         for (short i = 0; i < dd; i++) {
             HSSFCell cell = row.createCell(i);
             cell.setCellStyle(style2);
             String fieldName;
             if(fields==null){
                 Field field = allfields[i];
                 fieldName  = field.getName();
             }else {
                 fieldName= fields[i];
             }
             String getMethodName  = "get"
                     + fieldName.substring(0, 1).toUpperCase()
                     + fieldName.substring(1);
             try {
                 Class tCls = t.getClass();
                 Method getMethod = tCls.getMethod(getMethodName,
                         new Class[] {});
                 Object value = getMethod.invoke(t, new Object[] {});
                 // 判断值的类型后进行强制类型转换
                 String textValue = null;
                     // 其它数据类型都当作字符串简单处理
                  if (value==null){
                         textValue ="";
                  }else{
                         textValue = value.toString();
                  }
                 // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                 if (textValue != null) {
                     Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                     Matcher matcher = p.matcher(textValue);
                     if (matcher.matches()) {
                         // 是数字当作double处理
                         cell.setCellValue(Double.parseDouble(textValue));
                     } else {
                         HSSFRichTextString richString = new HSSFRichTextString(
                                 textValue);
                         HSSFFont font3 = workbook.createFont();
                         font3.setColor(HSSFColor.BLUE.index);
                         richString.applyFont(font3);
                         cell.setCellValue(richString);
                     }
                 }
             } catch (SecurityException e) {
                 e.printStackTrace();
             } catch (NoSuchMethodException e) {
                 e.printStackTrace();
             } catch (IllegalArgumentException e) {
                 e.printStackTrace();
             } catch (IllegalAccessException e) {
                 e.printStackTrace();
             } catch (InvocationTargetException e) {
                 e.printStackTrace();
             } finally {
                 // 清理资源
             }
          }
		
	}
}
