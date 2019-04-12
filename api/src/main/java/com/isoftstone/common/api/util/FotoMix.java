package com.isoftstone.common.api.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;


/*import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;*/
public class FotoMix {

	 public static void main(String[] args) throws Exception {  
		 	
	        InputStream imagein = new FileInputStream("D://1.jpg");  
	        InputStream imagein2 = new FileInputStream("D://2.jpg");  
	        BufferedImage image = ImageIO.read(imagein);  
	        BufferedImage image2 = ImageIO.read(imagein2);  
	        Graphics g = image.getGraphics();  
	        /*drawImage方法参数说明
	         * img - 要绘制的指定图像。如果 img 为 null，则此方法不执行任何动作。
	         * x - x 坐标。
	         * y - y 坐标。
	         * width - 矩形的宽度。
	         * height - 矩形的高度。*/
	        int x=(image.getWidth() - image2.getWidth())/2;
		 	int y=(image.getHeight() - image2.getHeight())/2;
		 	int width=image2.getWidth() + 5;
		 	int height=image2.getHeight() + 5;

	        g.drawImage(image2, x, y, width,height, null);  

	        OutputStream outImage = new FileOutputStream("D://3.jpg");  
//	        String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);   
//	        ImageIO.write(image, /*"GIF"*/ formatName /* format desired */ , new File("custom" + j + "-" + i + ".jpg") /* target */ );    

	      /*  JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);  
	        enc.encode(image);  */
	        imagein.close();  
	        imagein2.close();  
	        outImage.close();  


	    } 
}


