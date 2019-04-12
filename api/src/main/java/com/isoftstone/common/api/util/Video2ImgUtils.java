package com.isoftstone.common.api.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.common.constant.CommonConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
public class Video2ImgUtils {
    public static void main(String[] args) throws Exception, InterruptedException, ExecutionException {
        String file = "E:\\tmp\\test.mp4";
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.start();
        int lenght = grabber.getLengthInFrames();
        long times = grabber.getLengthInTime() / (1000 * 1000);
        System.out.println(times);
        String imgPath = "E:\\tmp\\test.jpg";
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = grabber.grabFrame();
            if ((i > 10) && (f.image != null)) {
                break;
            }
            i++;
        }
        doExecuteFrame(f, imgPath);
        grabber.stop();
        grabber.close();
    }

    /**
     * 是否图片文件
     *
     * @param fileName
     * @return
     */
    public boolean isPicFile(String fileName) {
        boolean flag = false;
        if (!StringUtils.isEmpty(fileName)) {
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            //avi、wmv、mpeg、mp4、mov、mkv、flv、f4v、m4v、rmvb、rm、3gp、dat、ts、mts、vob
            switch (fileExt) {
                case "jpg":
                case "png":
                case "bmp":
                case "gif":
                    flag = true;
                default:
                    break;
            }
        }
        return flag;
    }

    /**
     * 是否音频文件
     *
     * @param fileName
     * @return
     */
    public boolean isAudioFile(String fileName) {
        boolean flag = false;
        if (!StringUtils.isEmpty(fileName)) {
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            fileExt = fileExt.toLowerCase();
            //avi、wmv、mpeg、mp4、mov、mkv、flv、f4v、m4v、rmvb、rm、3gp、dat、ts、mts、vob
            switch (fileExt) {
                case "mp3":
                case "wav":
                case "wma":
                    flag = true;
                default:
                    break;
            }
        }
        return flag;
    }

    /***
     * 是否视频文件
     *
     * @param fileName
     * @return
     */
    public boolean isVideoFile(String fileName) {
        boolean flag = false;
        if (!StringUtils.isEmpty(fileName)) {
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            fileExt = fileExt.toLowerCase();
            //avi、wmv、mpeg、mp4、mov、mkv、flv、f4v、m4v、rmvb、rm、3gp、dat、ts、mts、vob
            switch (fileExt) {
                case "avi":
                case "wmv":
                case "mp4":
                case "rmvb":
                case "mpeg":
                case "mov":
                case "mkv":
                case "flv":
                case "f4v":
                case "rm":
                case "3gp":
                case "dat":
                case "ts":
                case "mts":
                case "vob":
                case "m4v":
                    flag = true;
                default:
                    break;
            }
        }
        return flag;
    }

    /**
     * 获得截屏
     *
     * @param videofile
     * @return
     * @throws Exception
     */
    public String fetchFrame(String videofile)
            throws Exception {

        String imgPath = videofile.substring(0, videofile.lastIndexOf(".") + 1) + "jpg";

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videofile);
        grabber.start();
        doExecuteFrame(grabber.grabFrame(), imgPath);
        grabber.stop();
        grabber.close();

        return imgPath;
    }

    /**
     * @param videofile
     * @return
     * @throws Exception
     */
    public String fetchFrameJust(String videofile)
            throws Exception {
        String imgPath = videofile.substring(0, videofile.lastIndexOf(".") + 1) + "jpg";
        return imgPath;
    }

    /**
     * 获得视频截图截音频
     *
     * @param videofile
     * @return
     * @throws Exception
     */
    public Map<String, Object> fetchAll(String videofile) throws Exception {
        String imgPath = videofile.substring(0, videofile.lastIndexOf(".") + 1) + "jpg";
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videofile);
        grabber.start();
        long lenght = grabber.getLengthInTime();
        long times = lenght / (1000 * 1000);//秒
        Frame f = null;
        int i = 0;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = grabber.grabFrame();
            if ((i > 10) && (f.image != null)) {
                break;
            }
            i++;
        }
        doExecuteFrame(f, imgPath);
        grabber.stop();
        grabber.close();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CommonConstants.VOIDE_LONG, times);
        map.put(CommonConstants.VOIDE_IMG, imgPath);
        return map;
    }

    /**
     * 获得时长
     *
     * @param videofile
     * @return
     * @throws Exception
     */
    public long fetchLength(String videofile)
            throws Exception {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videofile);
        grabber.start();
        long times = grabber.getLengthInTime() / (1000 * 1000);//秒
        grabber.stop();
        grabber.close();
        return times;
    }


    public static void doExecuteFrame(Frame frame, String imgPath) {
        if (frame == null || frame.image == null) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
      
        /*
            int owidth = frame.imageWidth;
	        int oheight = frame.imageHeight;
	        // 对截取的帧进行等比例缩放
	        int width = 800;
	        int height = (int) (((double) width / owidth) * oheight);
        */

        int width = 132;
        int height = 70;
        BufferedImage bi = converter.getBufferedImage(frame);
        BufferedImage newBi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        newBi.getGraphics().drawImage(bi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        File output = new File(imgPath);
        try {
            ImageIO.write(newBi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
