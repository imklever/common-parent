package com.isoftstone.common.api.util;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("deprecation")
public class WordUtils {
	  
    public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, Map map,String templateRootPath, String title,String ftlFile) throws IOException {  
    	Configuration configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");          
        try {  
              configuration.setDirectoryForTemplateLoading(new File(templateRootPath+"/template"));  
            } catch (IOException e) {  
              e.printStackTrace();  
        }  
    	Template freemarkerTemplate = configuration.getTemplate(ftlFile);  
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
        	  // 设置浏览器以下载的方式处理该文件名  
            String fileName = title +"_"+ new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()) + ".doc";  
            // 调用工具类的createDoc方法生成Word文档  
            file = createDoc(map,freemarkerTemplate,templateRootPath,fileName);  
            fin = new FileInputStream(file);  
  
            response.setCharacterEncoding("utf-8");  
            response.setContentType("application/msword");  
          
            response.setHeader("Content-Disposition", "attachment;filename="  
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));  
  
            out = response.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } finally {  
            if(fin != null) fin.close();  
            if(out != null) out.close();  
            //if(file != null) file.delete(); // 删除临时文件  
        }  
    }  
  
    private static File createDoc(Map<?, ?> dataMap, Template template,String templateRootPath,String fileName) {  
        String tmpFilePath =  templateRootPath+"/tmp/"+fileName;  
        File f = new File(tmpFilePath);  
        if(!f.exists()){
          try {
			//FileUtils.forceMkdir(f);
			FileUtils.touch(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        Template t = template;  
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");  
            t.process(dataMap, w);  
            w.close();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            throw new RuntimeException(ex);  
        }  
        return f;  
    }  
	
	
    /*
     * 添加图片到word中（获得图片的base64码）  后期按照要求再添加
     */
    @SuppressWarnings("deprecation")
    public String getImageBase(String src) {
        if(src==null||src==""){
            return "";
        }
        File file = new File("");
        if(!file.exists()) {
            return "";
        }
        InputStream in = null;
        byte[] data = null;  
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
          e.printStackTrace();  
        } 
        Base64Encoder encoder = new Base64Encoder();
        return encoder.encode(data);
    }
}
