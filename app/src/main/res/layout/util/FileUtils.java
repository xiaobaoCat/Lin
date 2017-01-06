package util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public final class FileUtils {
	public static long fileLen = 0;


	public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值



	public static String getJsonStringFromAssets(String fileName, Context context) throws IOException {
		AssetManager manager = context.getAssets();
		InputStream file = manager.open(fileName);
		byte[] data = new byte[file.available()];
		file.read(data);
		file.close();
		return new String(data);
	}


	public static void delFilesFromPath(File filePath) {
		if (filePath == null){
			return;
		}
		if (!filePath.exists()){
			return;
		}
		File[] files = filePath.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				files[i].delete();
			} else {
				delFilesFromPath(files[i]);
				files[i].delete();// 刪除文件夾
			}
		}
	}
	
	public static String size(File filePath) {
		if (filePath == null){
			return "0字节";
		}
		if (!filePath.exists()){
			return "0字节";
		}
		long fileLen2 = getFileLen(filePath);
		String size = size(fileLen2);
		return size;
	}

	public static long getFileLen(File filePath) {
		fileLen = 0;
		return getFileLenFromPath(filePath);
	}

	private static long getFileLenFromPath(File filePath) {
		File[] files = filePath.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileLen += files[i].length();
			} else {
				getFileLenFromPath(files[i]);
			}
		}
		return fileLen;
	}

	private FileUtils() {
	}

	public static String size(long size) {

		if (size / (1024 * 1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "GB";
		} else if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "KB";
		} else
			return "" + size + "B";
	}
	public static double getFileOrFilesSize(String filePath, int sizeType){
		File file=new File(filePath);
		long blockSize=0;
		try {
			if(file.isDirectory()){
				blockSize = getFileSizes(file);
			}else{
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小","获取失败!");
		}
		return FormetFileSize(blockSize, sizeType);
	}

	private static long getFileSizes(File f) throws Exception
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++){
			if (flist[i].isDirectory()){
				size = size + getFileSizes(flist[i]);
			}
			else{
				size =size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	private static long getFileSize(File file) throws Exception
	{
		long size = 0;
		if (file.exists()){
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		}
		else{
			file.createNewFile();
			Log.e("获取文件大小","文件不存在!");
		}
		return size;
	}


	/**
	 * 转换文件大小,指定转换的类型
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	private static double FormetFileSize(long fileS,int sizeType)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
			case SIZETYPE_B:
				fileSizeLong= Double.valueOf(df.format((double) fileS));
				break;
			case SIZETYPE_KB:
				fileSizeLong= Double.valueOf(df.format((double) fileS / 1024));
				break;
			case SIZETYPE_MB:
				fileSizeLong= Double.valueOf(df.format((double) fileS / 1048576));
				break;
			case SIZETYPE_GB:
				fileSizeLong= Double.valueOf(df.format((double) fileS / 1073741824));
				break;
			default:
				break;
		}
		return fileSizeLong;
	}

}
