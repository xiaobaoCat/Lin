package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

public class AlterDialogTools{
	
	public static AlertDialog showQueRenDialog(Context context, String title, Drawable icon, String message,
											   DialogInterface.OnClickListener listener){
		//创建dialog对话框
		AlertDialog dialog=new AlertDialog.Builder(context)
		.setTitle(title)            //设置标题
		.setIcon(icon)  //设置图标
		.setMessage(message)     //设置内容
		//设置确定按钮
		.setPositiveButton("确定", listener)
		//设置取消按钮
		.setNegativeButton("取消",null)
		.create();
		//弹出对话框
		dialog.show();//弹出对话框
		return dialog;
	}
	
	
	public static AlertDialog showItemsDialog(Context context, String title, int resID, String[] items, DialogInterface.OnClickListener listener){
		//创建dialog对话框
		AlertDialog dialog=new AlertDialog.Builder(context)
		.setTitle(title)      //设置标题
		.setIcon(resID)       //设置图标
		.setItems(items, listener)//设置对话框内容为列表对话框
		.create();
		dialog.show();//弹出对话框
		return dialog;
	}
	
	
	public static AlertDialog showSingleItemsDialog(Context context, String title, int resId, String[] singleItems, int index,
													DialogInterface.OnClickListener listener){
		//创建dialog对话框
		AlertDialog dialog=new AlertDialog.Builder(context)
		.setTitle(title)            //设置标题
		.setIcon(resId)  //设置图标
//		.setPositiveButton("确定", null)//设置确定按钮(按需求设置)	
//		.setNegativeButton("取消",null)//设置取消按钮(按需求设置)
		.setSingleChoiceItems(singleItems, index,listener)//设置对话框内容为单选
		.create();
		//弹出对话框
		dialog.show();//弹出对话框
		return dialog;
	}
	
	
	public static AlertDialog showMultiItems(Context context, String title, int resID, String[] multiItems, boolean [] checkedItems,
											 DialogInterface.OnMultiChoiceClickListener listener,
											 DialogInterface.OnClickListener listener1){
		//创建dialog对话框
		AlertDialog dialog=new AlertDialog.Builder(context)
		.setTitle(title)            //设置标题
		.setIcon(resID)  //设置图标
		.setMultiChoiceItems(multiItems, checkedItems,listener)//设置对话框内容为多选列表
		.setPositiveButton("确定", listener1)//设置确定按钮
//		.setNegativeButton("取消",null)//设置取消按钮
		.create();
		dialog.show();//弹出对话框
		return dialog;
	}

	//多选
//	final boolean[] checkedItems =new boolean[]{true,true,false,true,true};
//	findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			AlertDialog dialog=showQueRenDialog(MainActivity.this, "", R.drawable.ic_launcher, new String[]{"1","2","3","4","5"},checkedItems,
//					new DialogInterface.OnMultiChoiceClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
//						
//							checkedItems[arg1]=arg2;
//						}
//					}
//					,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							String sp="";
//							for (int i = 0; i < checkedItems.length; i++) {
//								if (checkedItems[i]) {
//									sp=sp+i+",";
//								}
//							}
//							
//							
//							Toast.makeText(MainActivity.this, "第"+sp, Toast.LENGTH_SHORT).show();
//							arg0.dismiss();
//						}
//					});
//		}
//	});
	
	
	/**
	 * 显示自定义对话框：自定义一个View 让对话框显示
	 * @param context  上下文
	 * @param resource 自定义View的xml文件的id
	 */
	public static AlertDialog showMyDialog(Context context, int resource){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(resource,null);
		 AlertDialog dialog=new AlertDialog.Builder(context)
		.setView(view)
		.create();

		dialog.show();
		return dialog;
	}



}
