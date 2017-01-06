package util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class BitmapUtils {
	public static Drawable bitmap2Drawable(Bitmap bmp) {
		BitmapDrawable bd = new BitmapDrawable(bmp);
		return bd;
	}

	public static Bitmap drawable2Bitmap(Drawable d) {
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap bm = bd.getBitmap();
		return bm;
	}

	/**
	 * 获取bitmap
	 *
	 * @param filePath
	 * @return
	 */

//获取图片 宽高
	public static String getBitmapByPath_cl(String filePath) {

		BitmapFactory.Options options = new BitmapFactory.Options();

		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); // 此时返回的bitmap为null
		/**
		 *options.outHeight为原始图片的高
		 */
		int  height =options.outHeight;
		int width =	options.outWidth;
		Log.e("height+width", height+"cl  h " +width+"w");
		return "WH"+width+"*"+height;
	}

	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}
	public static Bitmap getBitmapByPath(String filePath,
										 BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	// ����Դ�л�ȡBitmap
	public static Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	// byte[] �� Bitmap
	public static Bitmap convertBytes2Bimap(byte[] b) {
		if (b.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	// Bitmap �� byte[]
	public static byte[] convertBitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap compressImageFromFile(String srcPath, int width, int height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		// 读取出图片实际的宽高
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int be = 1;
		if (w > h && w > width) {
			be = (int) (w / width);
		} else if (w < h && h > height) {
			be = (int) (h / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置压缩比例

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		// 读取出图片实际的宽高
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (w / ww);
		} else if (w < h && h > hh) {
			be = (int) (h / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置压缩比例

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 通过URI获取本地图片的path
	 * 兼容android 5.0
	 */
	public static String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";
	public static int Build_VERSION_KITKAT = 19;
	public static String getImageAbsolutePath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
		// DocumentProvider
		if (isKitKat && isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}
	private static final String PATH_DOCUMENT = "document";
	private static boolean isDocumentUri(Context context, Uri uri) {
		final List<String> paths = uri.getPathSegments();
		if (paths.size() < 2) {
			return false;
		}
		if (!PATH_DOCUMENT.equals(paths.get(0))) {
			return false;
		}

		return true;
	}
	private static String getDocumentId(Uri documentUri) {
		final List<String> paths = documentUri.getPathSegments();
		if (paths.size() < 2) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}
		if (!PATH_DOCUMENT.equals(paths.get(0))) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}
		return paths.get(1);
	}
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	public static int TYPE_GIF = 0;
	public static int TYPE_JPG = 1;
	public static int TYPE_PNG = 2;
	/**
	 * 获取图片类型
	 */
	public static  int getType(byte[] data) {
		// Png test:
		if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
			return TYPE_PNG;
		}
		// Gif test:
		if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
			return TYPE_GIF;
		}
		// JPG test:
		if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
				&& data[9] == 'F') {
			return TYPE_JPG;
		}
		return TYPE_JPG;
	}





	public static byte[] File2Bytes(File file) {
		int byte_size = 1024;
		byte[] b = new byte[byte_size];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
					byte_size);
			for (int length; (length = fileInputStream.read(b)) != -1;) {
				outputStream.write(b, 0, length);
			}
			fileInputStream.close();
			outputStream.close();
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
