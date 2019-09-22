package androidx.sframe.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public class FileCompat {

	public static final int TYPE_B = 1;// 获取文件大小单位为B的double值
	public static final int TYPE_KB = 2;// 获取文件大小单位为KB的double值
	public static final int TYPE_MB = 3;// 获取文件大小单位为MB的double值
	public static final int TYPE_GB = 4;// 获取文件大小单位为GB的double值

	/**
	 * 是否存在SD卡
	 */
	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取文件指定文件的指定单位的大小
	 *
	 * @param filePath 文件路径
	 * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
	 */
	public static double getFileOrFilesSize(String filePath, int sizeType) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getDirectorySize(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatFileSize(blockSize, sizeType);
	}

	/**
	 * 自动计算指定文件或指定文件夹的大小
	 *
	 * @param filePath 文件或指定文件夹路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String getAutoFileOrFilesSize(String filePath) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getDirectorySize(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatFileSize(blockSize);
	}

	// 文件目录下文件随APK卸掉而移除
	public static String getLocatDir(@NonNull Context context) {
		File mCacheFile = null;
		try {
			if (isSdcardExist()) {
				mCacheFile = context.getExternalCacheDir();
			}
			if (mCacheFile == null) {
				mCacheFile = context.getCacheDir();
			}
		} catch (Exception e) {
			mCacheFile = context.getFilesDir();
		}
		if (!mCacheFile.exists()) {
			mCacheFile.mkdirs();
		}
		return mCacheFile.getAbsolutePath();
	}

	/**
	 * 获取指定文件大小
	 */
	public static long getFileSize(@Nullable File file) throws Exception {
		if (file == null || !file.exists()) {
			return 0L;
		}
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return inputStream.available();
		} catch (Exception e) {
			return 0L;
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
	}

	/**
	 * 获取指定文件夹大小
	 */
	public static long getDirectorySize(@Nullable File file) throws Exception {
		if (file == null || !file.isDirectory() || !file.exists()) {
			return 0L;
		}
		long size = 0;
		File[] mFiles = file.listFiles();
		if (mFiles == null) {
			return 0L;
		}
		for (File tempFile : mFiles) {
			if (tempFile.isDirectory()) {
				size = size + getDirectorySize(tempFile);
			} else {
				size = size + getFileSize(tempFile);
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 */
	public static double formatFileSize(long fileS, int sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
			case TYPE_B:
				fileSizeLong = Double.valueOf(df.format((double) fileS));
				break;
			case TYPE_KB:
				fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
				break;
			case TYPE_MB:
				fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
				break;
			case TYPE_GB:
				fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
				break;
		}
		return fileSizeLong;
	}

	// 按目录删除文件夹文件方法
	public static boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
		try {
			File file = new File(filePath);
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File file1 : files) {
					deleteFolderFile(file1.getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {
					file.delete();
				} else {
					if (file.listFiles().length == 0) {
						file.delete();
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getAbsolutePath(@NonNull File file) {
		if (!file.exists() && !file.mkdir()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}
}
