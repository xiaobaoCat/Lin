package util;

import android.media.MediaRecorder;

import java.io.File;
import java.util.UUID;

/**
 * 录音类的实现
 * prepareAudio()准备录音
 * release()录音完成释放资源
 * cancel()录音取消释放资源和删除录音文件
 * getVoiceLevel()获取音量等级
 */
public class MediaRecorderUtil {

	private MediaRecorder mMediaRecorder;
	private String mDir;
	private String mCurrentFilePath;
	private boolean isPrepared = false;
	/**
	 * 回调准备完毕
	 */
	public interface AudioStateListener{
		void wellPrepared();
	}
	public AudioStateListener mListener;
	public void setOnAudioStateListener(AudioStateListener mListener) {this.mListener = mListener;}
	
	//AudioManager单实例的实现
	private MediaRecorderUtil(String mDir) {this.mDir = mDir;}
	private static util.MediaRecorderUtil mInstance;
	public static util.MediaRecorderUtil getInstance(String mDir){
		if(mInstance == null){
			synchronized (util.MediaRecorderUtil.class){
				mInstance = new util.MediaRecorderUtil(mDir);
			}
		}
		return mInstance;
	}
	
    //准备录音
	public void prepareAudio(){
		try {
			isPrepared = false;
			//创建文件夹
			File dir = new File(mDir);
			if(!dir.exists()){
				dir.mkdirs();//不存在则创建
			}
			//随机生成文件名
			String fileName = UUID.randomUUID().toString()+".amr";
			//创建录音输出文件
			File file = new File(dir, fileName);
			//录音输出文件的绝对路径
			mCurrentFilePath = file.getAbsolutePath();
			//获取MediaRecorder实例
			mMediaRecorder = new MediaRecorder();
			//设置MediaRecorder输出文件(文件绝对路径)
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			//设置MediaRecorder音频源为麦克风
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置音频格式
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			//设置音频编码
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			mMediaRecorder.setAudioSamplingRate(8000);
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			isPrepared = true;
			//准备结束
			if(mListener != null){
				mListener.wellPrepared();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//录音完成释放资源
	public void release(){
		if(mMediaRecorder != null){
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}
	
	//录音取消释放资源和删除录音文件
	public void cancel(){
		release();
		//删除文件
		if(mCurrentFilePath != null){
			File file = new File(mCurrentFilePath);
			if(file.exists()){
				file.delete();
				mCurrentFilePath = null;
			}
		}
	}
	

	//获取音量等级
	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			try {
				return ((mMediaRecorder.getMaxAmplitude()*maxLevel)/32768)+1;
			} catch (Exception e) {
			}
		}
		return 1;
	}

	public String getmCurrentFilePath() {
		return mCurrentFilePath;
	}
}
