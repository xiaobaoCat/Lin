package util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.baituo.me.R;

public class DialogManager {

	private Dialog mDialog;
	private ImageView mVoice1;
	private ImageView mVoice2;
	private ImageView mVoice3;

	private Context mContext;

	public DialogManager(Context context)
	{
		this.mContext = context;
	}

	public void showRecordingDialog()
	{
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog, null);
		mDialog.setContentView(view);
		mVoice1 = (ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice1);
		mVoice2 = (ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice2);
		mVoice3 = (ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice3);
		mDialog.show();
	}

	public void recording(){
		if(mDialog != null && mDialog.isShowing()){
			mVoice1.setVisibility(View.VISIBLE);;
		}
	}

	public void tooShort(){
		if(mDialog != null && mDialog.isShowing()){
			mVoice1.setVisibility(View.GONE);
			mVoice2.setVisibility(View.GONE);
			mVoice3.setVisibility(View.GONE);
		}
	}

	public void dismissDialog()
	{
		if(mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void updateVoiceLevel(int level){
		if(mDialog != null && mDialog.isShowing()){
			Log.e("当前音量等级为:",""+level);
			//通过level找到资源id
			if(level<=10){
				mVoice1.setVisibility(View.VISIBLE);
				mVoice2.setVisibility(View.GONE);
				mVoice3.setVisibility(View.GONE);
			}else if (10<level&&level<=50){
				mVoice1.setVisibility(View.VISIBLE);
				mVoice2.setVisibility(View.VISIBLE);
				mVoice3.setVisibility(View.GONE);
			}else if (50<level&&level<=300){
				mVoice1.setVisibility(View.VISIBLE);
				mVoice2.setVisibility(View.VISIBLE);
				mVoice3.setVisibility(View.VISIBLE);
			}
		}
	}
}
