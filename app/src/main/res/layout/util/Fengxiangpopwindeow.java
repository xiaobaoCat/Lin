package util;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baituo.me.R;
import com.hengshuo.chengszj.utilcll.AccessTokenKeeper;
import com.hengshuo.chengszj.utilcll.Constants;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import httpservice.OKHttpUtil;

public class Fengxiangpopwindeow <E extends Activity> implements OnClickListener, IWeiboHandler.Response{

	private static PopupWindow popupWindow;
	E mContext;
	//weixin
		public static IWXAPI api;
		//qq
		 public static Tencent mTencent;
		 private static final String mAppid = "1104949681";
		
		 private AuthInfo mAuthInfo;
			private SsoHandler mSsoHandler;
			private Oauth2AccessToken mAccessToken;
			 /** 微博微博分享接口实例 */
		    private IWeiboShareAPI  mWeiboShareAPI = null;
			private View view;
			
			
	public Fengxiangpopwindeow(E mContext,View view) {
		super();
		this.view = view;
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mContext, "wx1c164f6e8b1186bb", true);
        // 将该app注册到微信
        api.registerApp("wx1c164f6e8b1186bb");
        
//      qq
      if (mTencent == null) {
          mTencent = Tencent.createInstance(mAppid, mContext);
      }
        
        this.mContext = mContext;
        
        //微博、
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.APP_KEY);
        
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
		mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(mContext, mAuthInfo);
        
        
	}
	
	

	public View showSharePopWindows()
	{
		View inflatepop = mContext.getLayoutInflater().inflate(R.layout.fenxiang_popwindow, null);
		TextView tv_quxiao = (TextView) inflatepop.findViewById(R.id.tv_quxiao);
		LinearLayout lin_weixin = (LinearLayout) inflatepop.findViewById(R.id.lin_weixin);
		LinearLayout lin_qq = (LinearLayout) inflatepop.findViewById(R.id.lin_qq);
		LinearLayout lin_pengyouquan = (LinearLayout) inflatepop.findViewById(R.id.lin_pengyouquan);
		LinearLayout lin_weibo = (LinearLayout) inflatepop.findViewById(R.id.lin_weibo);
		
		lin_weixin.setOnClickListener(this);
		lin_qq.setOnClickListener(this);
		lin_pengyouquan.setOnClickListener(this);
		lin_weibo.setOnClickListener(this);
//		
		
		tv_quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		
		popupWindow = new PopupWindow(inflatepop , LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //要显示的布局
        popupWindow.setContentView(inflatepop);

        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.7f;
        mContext.getWindow().setAttributes(lp);
        
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1f;
                mContext.getWindow().setAttributes(lp);
            }
        });
       //要显示在的大布局
//        View rootview = LayoutInflater.from(mContext).inflate(R.layout.activity_yun_ying_detail, null);  
        
//        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		return inflatepop;  
		
	}
	
	//qq
		public void shareOnlyImageOnQQ() {
	        final Bundle params = new Bundle();
	        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	        params.putString(QQShare.SHARE_TO_QQ_TITLE, "【百托】");
	        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "一款同城互帮的实用工具。");
	        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  Constants.shareContent);
	        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,OKHttpUtil.URL+"upload/img/photo.png");
	        
	        doShareToQQ(params);
	    }
		 private void doShareToQQ(final Bundle params) {
		        // QQ分享要在主线程做
		        ThreadManager.getMainHandler().post(new Runnable() {
		            @Override
		            public void run() {
		                if (null != mTencent) {
		                    mTencent.shareToQQ(mContext, params, qqShareListener);
		                }
		            }
		        });
		    }
		 IUiListener qqShareListener = new IUiListener() {
		        @Override
		        public void onCancel() {
		        }
		        @Override
		        public void onComplete(Object response) {
		        	Toast.makeText(mContext, response.toString()+"", Toast.LENGTH_SHORT).show();
		        	Log.e("onComplete", response.toString()+"");
		        }
		        @Override
		        public void onError(UiError e) {
		        	Toast.makeText(mContext, e.errorMessage+"", Toast.LENGTH_SHORT).show();
		        	Log.e("onComplete",e.errorMessage+"");
		        }
		    };
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
		    }
		

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_weixin:
			share2Wx(0);
			popupWindow.dismiss();
			break;
		case R.id.lin_qq:
			shareOnlyImageOnQQ();
			popupWindow.dismiss();
			break;
		case R.id.lin_pengyouquan:
			share2Wx(1);
			popupWindow.dismiss();
			break;
		case R.id.lin_weibo:
			initWEibo();
			share();
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}
	private void share() {
		if (!mAccessToken.isSessionValid()) {
			mSsoHandler.authorize(new AuthListener());
        }
//		sendMultiMessage(true);//分享文字
		shareWebPage(mContext);//分享网址
	}

	private void sendMultiMessage(boolean hasText) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
		if (hasText) {
		weiboMessage. textObject = getTextObj();
		}
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(mContext, request); //发送请求消息到微博，唤起微博分享界面
		}
	
	private TextObject getTextObj() {
		
		
		TextObject textObject = new TextObject();
		textObject.text = Constants.shareContent;
		return textObject;
		}
	
	private WebpageObject getWebpageObj() {
		
		WebpageObject webpageObject = new WebpageObject();
		webpageObject.actionUrl=Constants.shareContent;
		
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		
		webpageObject.defaultText="a";
		webpageObject.description="一款同城互帮的实用工具软件。";
		webpageObject.identify="a";
		webpageObject.schema="a";
		webpageObject.thumbData=byteArray;
		webpageObject.title="【百托】";
		return webpageObject;

		
	}

	private void shareWebPage( Context context){
		Log.e("shareWebPage", "shareWebPage");
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj();
		//初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = "一款同城互帮的实用工具软件。";
		request.message = weiboMessage;
		//发送请求信息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(mContext,request);
	}
	
	
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		mWeiboShareAPI.handleWeiboResponse(intent, mContext); //当前应用唤起微博分享后，返回当前应用
//		}
	private void initWEibo() {

        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
	}
	
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(mAccessToken.getExpiresTime()));
		String format = "Token：%1$s \n有效期：%2$s";
		String message = String.format(format, mAccessToken.getToken(), date);
		if (hasExisted) {
			message = "Token 仍在有效期内，无需再次登录" + "\n" + message;
		}
	}


	/**
     * 分享到朋友，false分享到朋友圈
     */private void share2Wx(int flag) {
    	 WXWebpageObject webpage = new WXWebpageObject();  
    	    webpage.webpageUrl = Constants.shareContent;  
    	    WXMediaMessage msg = new WXMediaMessage(webpage);  
    	  if (flag==0) {
    		  msg.title = "【百托】"; 
		   }else if (flag==1) {
			   msg.title = "【百托】一款同城互帮的实用工具软件。"; 
		   }
    	    msg.description ="一款同城互帮的实用工具软件。"; //网址说明 
    	    Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app);
    	    msg.setThumbImage(thumb);  
    	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
    	    req.transaction = String.valueOf(System.currentTimeMillis());
    	    req.message = msg;  
    	    req.scene = flag;  
    	    boolean sendReq = api.sendReq(req);  
    	    
    }

     
     class AuthListener implements WeiboAuthListener {

 		@Override
 		public void onComplete(Bundle values) {
 			// �� Bundle �н��� Token
 			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
 			String phoneNum = mAccessToken.getPhoneNum();
 			if (mAccessToken.isSessionValid()) {
 				updateTokenView(false);

 				AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
 				Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
 			} else {
 				String code = values.getString("code");
 				String message = "授权失败";
 				if (!TextUtils.isEmpty(code)) {
 					message = message + "\nObtained the code: " + code;
 				}
 				Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
 			}
 		}

 		@Override
 		public void onCancel() {	
 		}

 		@Override
 		public void onWeiboException(WeiboException e) {
 		}
 	}
     
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK: 
			Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
		break;
		case WBConstants.ErrorCode.ERR_CANCEL: 
			
		break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Log.e("baseResp.errCode", baseResp.errCode+"");
			Log.e("baseResp", baseResp.toString());
		break;
	}
	}
}
