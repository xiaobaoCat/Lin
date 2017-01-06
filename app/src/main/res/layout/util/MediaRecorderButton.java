package util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.baituo.me.R;

public class MediaRecorderButton extends Button implements MediaRecorderUtil.AudioStateListener{

	private static final int DISTANCE_Y_CANCEL = 50;
	private static final int STATE_NORMAL = 1;//正常状态
	private static final int STATE_RECORDING = 2;//录音状态
	private static final int STATE_WANT_TO_CANCEL = 3;//取消录音状态
	//是否已經開始錄音
	private boolean isRecording = false;
	//是否触发longclick
	private boolean mReady;

	private int mCurState = STATE_NORMAL;
	private util.DialogManager mDialogManager;
	public MediaRecorderUtil mAudioManager;

	private float mTime;
	private Context context;


	public MediaRecorderButton(Context context) {
		this(context, null);//默认调用两个参数的构造方法
		this.context = context;
	}
	public MediaRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		mDialogManager = new util.DialogManager(getContext());
		String dir = Environment.getExternalStorageDirectory()+"/baituo/mp3";
		mAudioManager = MediaRecorderUtil.getInstance(dir);
		mAudioManager.setOnAudioStateListener(this);
	}


	/**
	 * 录音完成后回调
	 */
	public interface AudioFinishRecorderListener{
		void onFinish(float seconds, String filepath);
	}
	private AudioFinishRecorderListener mListener;
	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
		this.mListener = listener;

	}


	/**
	 * 获取音量大小
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable() {
		@Override
		public void run(){
			while(isRecording){
				try {
					Thread.sleep(10);
					mTime+=0.01f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private static final int MSG_VOICE_CHANGE = 0X111;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
				case MSG_VOICE_CHANGE:
					//TODO
					mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(300));
					break;
			}
		};
	};

	//录音准备完成回调
	@Override
	public void wellPrepared() {
		isRecording = true;
		new Thread(mGetVoiceLevelRunnable).start();
	}


	//重写onTouchEvent，监听Button的触摸状态
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		Log.e("action",action+"");
		switch (action){
			case MotionEvent.ACTION_DOWN://
					mDialogManager.showRecordingDialog();
					mReady = true;
					mAudioManager.prepareAudio();
					changeState(STATE_RECORDING);


				break;
			case MotionEvent.ACTION_UP:

				mDialogManager.dismissDialog();
				if(!mReady){
					reset();
					return super.onTouchEvent(event);
				}
				if(!isRecording || mTime < 2f){//录制时间过短
					Toast.makeText(getContext(),"录制时间过短", Toast.LENGTH_SHORT).show();
					mDialogManager.tooShort();
					mAudioManager.cancel();
				}
				else if(mCurState == STATE_RECORDING){//正常结束录制
					mAudioManager.release();
					if(mListener != null){
						mListener.onFinish(mTime, mAudioManager.getmCurrentFilePath());
					}
				}
				reset();
				break;
			case MotionEvent.ACTION_CANCEL:
				mDialogManager.dismissDialog();
				reset();
				break;

		}
		return super.onTouchEvent(event);
	}

	//重置标志位
	private void reset() {
		isRecording = false;
		mReady = false;
		mTime = 0;
		changeState(STATE_NORMAL);
	}
	//改变Button文本状态
	private void changeState(int stateRecording) {
		if(mCurState != stateRecording){
			mCurState = stateRecording;
			switch (stateRecording) {
				case STATE_NORMAL://Button正常状态
					setBackgroundResource(R.drawable.shape_yuanjiao_bai);
					setText("按住录音开始");
					break;
				case STATE_RECORDING://Button录音状态
					setBackgroundResource(R.drawable.shape_yuanjiao_new);
					setText("松开录音结束");
					if(isRecording){
						mDialogManager.recording();
					}
					break;
			}
		}
	}
}
