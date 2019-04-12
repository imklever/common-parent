package com.isoftstone.common.api.util;

import com.isoftstone.common.api.util.bean.FileSaveInfo;
import com.isoftstone.common.api.util.bean.UploadInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUtil {
	private final static List<UploadInfo> uploadInfoList = new ArrayList<>();

	public static String getResourcePath() {
		return null;
	}

	public static boolean saveFile(final String savePath, final String fileFullName, final MultipartFile file)
			throws Exception {
		byte[] data = readInputStream(file.getInputStream());
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File uploadFile = new File(savePath + fileFullName);
		// 判断文件夹是否存在，不存在就创建一个
		File fileDirectory = new File(savePath);
		if (!fileDirectory.exists()) {
			if (!fileDirectory.mkdirs()) {
				throw new Exception("文件夹创建失败！路径为：" + savePath);
			}
		}
		// 创建输出流
		try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {// 写入数据
			outStream.write(data);
			outStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return uploadFile.exists();
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	public static FileSaveInfo uploaded(final String md5, final String guid, final String chunk, final String chunks,
			final String uploadFolderPath, final String fileName, final String ext,final String  fileDir,final String  oldFilename) throws Exception {
		synchronized (uploadInfoList) {
			uploadInfoList.add(new UploadInfo(md5, chunks, chunk, uploadFolderPath, fileName, ext));
			//System.out.println(JSONObject.toJSON(uploadInfoList));
		}
		boolean allUploaded = isAllUploaded(md5, chunks);
		int chunksNumber = Integer.parseInt(chunks);

		if (allUploaded) {
			System.out.println("已经上传完成！");
			String lastSavePath = mergeFile(chunksNumber, ext, guid, uploadFolderPath);
			File file = new File(lastSavePath);
			FileSaveInfo info = new FileSaveInfo();
			info.setSaveName(guid + ext);
			info.setMd5(md5);
			info.setSize(file.length());
			info.setPath(lastSavePath);
			info.setFix(ext);
			info.setRelativePath(fileDir+"/" +  guid + ext);
			info.setName(oldFilename);
			return info;
		} else {
			return null;
		}
	}

    /**
     * @param chunksNumber
     * @param ext
     * @param guid
     * @param uploadFolderPath
     * @throws Exception
     */
    public static String mergeFile(final int chunksNumber,
                                  final String ext,
                                  final String guid,
                                  final String uploadFolderPath)
            throws Exception {
        /*合并输入流*/
        String mergePath = uploadFolderPath +"/" + guid + "/";
        SequenceInputStream s ;
        InputStream s1 = new FileInputStream(mergePath + 0 + ext);
        InputStream s2 = new FileInputStream(mergePath + 1 + ext);
        s = new SequenceInputStream(s1, s2);
        for (int i = 2; i < chunksNumber; i++) {
            InputStream s3 = new FileInputStream(mergePath + i + ext);
            s = new SequenceInputStream(s, s3);
        }
        String finalSavePath = uploadFolderPath + "/" +  guid + ext;
        //通过输出流向文件写入数据
        saveStreamToFile(s, finalSavePath);

        //删除保存分块文件的文件夹
        deleteFolder(mergePath);
        return finalSavePath;
    }
    /**
     * 从stream中保存文件
     *
     * @param inputStream inputStream
     * @param filePath    保存路径
     * @throws Exception 异常 抛异常代表失败了
     */
    public static void saveStreamToFile( final InputStream inputStream,
                                         final String filePath)
            throws Exception {
         /*创建输出流，写入数据，合并分块*/
        OutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }
    /**
     * 删除指定文件夹
     * @param folderPath 文件夹路径
     * @return 是否删除成功
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean deleteFolder( final String folderPath) {
        File dir = new File(folderPath);
        File[] files = dir.listFiles();
        if(files!=null){
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dir.delete();
    }


	public static boolean isAllUploaded(final String md5, final String chunks) {
		int size = uploadInfoList.stream().filter(item -> item.getMd5().equals(md5)).distinct()
				.collect(Collectors.toList()).size();
		boolean bool = (size == Integer.parseInt(chunks));
		if (bool) {
			synchronized (uploadInfoList) {
				uploadInfoList.removeIf(item -> Objects.equals(item.getMd5(), md5));
			}
		}
		return bool;
	}

}
