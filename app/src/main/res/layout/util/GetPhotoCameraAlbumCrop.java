package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetPhotoCameraAlbumCrop <E extends Activity>{

	E mContext;
	//请求相机拍照
	public static final int Camera = 101;
	//打开相册
	public static final int Crop = 102;
	CharSequence[] items = {"本地相册", "拍照"};
	//缓存图片保存路径
	private final static String FILE = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/baituo/Portrait/";
	public String photo;//缓存图片的绝对路径
	
	
	
	public GetPhotoCameraAlbumCrop(E mContext){
		this.mContext=(E)mContext;
	}
	/**
	 * 操作选择
	 */
	public void ShowPhotoChoose() {
		AlertDialog imageDialog = new AlertDialog.Builder(mContext)
				.setTitle("上传图片")
				.setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 判断是否挂载了SD卡
						String storageState = Environment.getExternalStorageState();
						if (storageState.equals(Environment.MEDIA_MOUNTED)) {
							File savedir = new File(FILE);
							if (!savedir.exists()) {
								savedir.mkdirs();
							}
						} else {
							Toast.makeText(mContext, "无法保存上传的图片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
							return;
						}
						// 裁剪后图片的绝对路径
						photo = FILE+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"yz.jpg";
						if (item == 0) {
							startActionPickCrop();// 开启相册选择图片并裁剪
						} else if (item == 1) {
							startActionCamera();//选择相机拍照
						}
					}
				}).create();
		imageDialog.show();
	}

	/**
	 * 打开相机
	 */
	public void startActionCamera() {
		
		// 判断是否挂载了SD卡
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			Toast.makeText(mContext, "无法保存上传的图片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
			return;
		}
		// 裁剪后图片的绝对路径
		photo = FILE+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"yz.jpg";
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//设置拍照后图片保存Uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photo)));
		mContext.startActivityForResult(intent, 101);
	}
	
	/**
	 * 打开相册
	 */
	public  void startActionPickCrop() {
		// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					File savedir = new File(FILE);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}
				} else {
					Toast.makeText(mContext, "无法保存上传的图片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
					return;
				}
				// 裁剪后图片的绝对路径
				photo = FILE+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"yz.jpg";
				
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");//设置选取的文件类型为图片
		mContext.startActivityForResult(intent, 102);
	}
	
	/**
	 * 裁剪图片
	 */
	public void startActionCrop(Uri uri ) {
		Intent intent = new Intent("com.android.camera.action.CROP");//开启剪接界面
		intent.putExtra("crop", "true");//设置是否剪接
		intent.setDataAndType(uri, "image/*");//设置裁剪图片的Uri和类型
		intent.putExtra("aspectX", 108);// 裁剪框比例
		intent.putExtra("aspectY", 72);
		intent.putExtra("outputX", 1080);// 输出图片大小
		intent.putExtra("outputY", 720);
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photo)));//设置剪接后输出图片保存的Uri
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//设置剪接后图片输出格式
		mContext.startActivityForResult(intent, 103);
	}

	public String onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("resultCodee", resultCode+"");



		String myPhoto=null;//取得的图片的绝对路径
		if (resultCode == Activity.RESULT_OK&&requestCode==101) {//相机
			Log.e("裁剪照片xiangji", "裁剪照片");
			startActionCrop(Uri.fromFile(new File(photo)));
			}
		if (requestCode==102&&data!=null) {//相册
			Log.e("裁剪照片xiangce", "裁剪照片");
			ContentResolver resolver = mContext.getContentResolver();
            //照片的原始资源地址  
            Uri originalUri = data.getData();
            startActionCrop(originalUri);
		}
		if (requestCode==103) {//剪切
			myPhoto=photo;
		}
		return myPhoto;
	}
}
