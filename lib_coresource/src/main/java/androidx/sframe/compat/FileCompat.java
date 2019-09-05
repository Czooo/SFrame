package androidx.sframe.compat;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2016/3/14.
 * <p/>
 * File 缓存路径处理类
 */
public class FileCompat {

	static private final String F_IMAGE = "f_image";
	static private final String F_VIDEO = "f_video";
	static private final String F_AUDIO = "f_audio";
	static private final String F_FILE = "f_file";

	static private String _Path;
	static private File _CacheImage;
	static private File _CacheVideo;
	static private File _CacheAudio;
	static private File _CacheFile;

	static public final int TYPE_B = 1;// 获取文件大小单位为B的double值
	static public final int TYPE_KB = 2;// 获取文件大小单位为KB的double值
	static public final int TYPE_MB = 3;// 获取文件大小单位为MB的double值
	static public final int TYPE_GB = 4;// 获取文件大小单位为GB的double值

	static public void init(Context context) {
		_Path = getLocatDir(context) + File.separator;

		if (_CacheImage == null) {
			_CacheImage = new File(_Path + F_IMAGE);
			if (!_CacheImage.mkdir()) {
				_CacheImage.mkdirs();
			}
		}

		if (_CacheVideo == null) {
			_CacheVideo = new File(_Path + F_VIDEO);
			if (!_CacheVideo.mkdir()) {
				_CacheVideo.mkdirs();
			}
		}

		if (_CacheAudio == null) {
			_CacheAudio = new File(_Path + F_AUDIO);
			if (!_CacheAudio.mkdir()) {
				_CacheAudio.mkdirs();
			}
		}

		if (_CacheFile == null) {
			_CacheFile = new File(_Path + F_FILE);
			if (!_CacheFile.mkdir()) {
				_CacheFile.mkdirs();
			}
		}
	}

	static public boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	static public String getCachePath() {
		return _Path;
	}

	static public String getCacheImagePath() {
		return getPath(_CacheImage);
	}

	static public String getCacheVideoPath() {
		return getPath(_CacheVideo);
	}

	static public String getCacheAudioPath() {
		return getPath(_CacheAudio);
	}

	static public String getCacheFilePath() {
		return getPath(_CacheFile);
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
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formetFileSize(blockSize, sizeType);
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
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formetFileSize(blockSize);
	}

	// 文件目录下文件随APK卸掉而移除
	public static String getLocatDir(Context context) {
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
	static public long getFileSize(File file) throws Exception {
		long size = 0;
		if (file != null && file.exists()) {
			FileInputStream _InputStream = new FileInputStream(file);
			size = _InputStream.available();
			_InputStream.close();
			_InputStream = null;
		} else {
			file.createNewFile();
		}
		return size;
	}

	/**
	 * 获取指定文件夹大小
	 */
	static public long getFileSizes(File file) throws Exception {
		if (file == null || !file.isDirectory() || !file.exists()) {
			return 0l;
		}
		long size = 0;
		File flist[] = file.listFiles();
		if (flist == null) {
			return 0l;
		}
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 */
	static public String formetFileSize(long fileS) {
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
	static public double formetFileSize(long fileS, int sizeType) {
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

	static public Disposable getCacheSize(Consumer<String> consumer) {
		return Observable.just(getCachePath())
				.compose(new ObservableTransformer<String, String>() {
					@Override
					public ObservableSource<String> apply(Observable<String> upstream) {
						return upstream.map(new Function<String, String>() {
							@Override
							public String apply(String path) throws Exception {
								return getAutoFileOrFilesSize(path);
							}
						});
					}
				})
				.compose(RxCompat.<String>rxScheduler())
				.subscribe(consumer);
	}

	static public Disposable clearCache(final Consumer<Boolean> consumer) {
		return Observable.just(getCachePath())
				.compose(new ObservableTransformer<String, Boolean>() {
					@Override
					public ObservableSource<Boolean> apply(Observable<String> upstream) {
						return upstream.map(new Function<String, Boolean>() {
							@Override
							public Boolean apply(String path) throws Exception {
								Glide.get(CoreCompat.getContext())
										.clearDiskCache();
								return deleteFolderFile(path, false);
							}
						});
					}
				})
				.compose(RxCompat.<Boolean>rxScheduler())
				.subscribe(new Consumer<Boolean>() {

					@Override
					public void accept(Boolean result) throws Exception {
						Glide.get(CoreCompat.getContext())
								.clearMemory();
						if (consumer != null) {
							consumer.accept(result);
						}
					}
				});
	}

	// 按目录删除文件夹文件方法
	static private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
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

	static private String getPath(File file) {
		if (file != null) {
			if (!file.exists() && !file.mkdir()) {
				file.mkdirs();
			}
			return file.getAbsolutePath();
		}
		return "/Android/data/com.r747223875.nfu/cache/errCache/";
	}
}
