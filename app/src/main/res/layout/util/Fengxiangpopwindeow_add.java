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
import com.hengshuo.chengszj.activity.yunyingzhanghao.MyQuanZi_Share;
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
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dom.data.baituoshuju.datafromas.Share;
import dom.data.baituoshuju.datafromas.WoDeQuanZiList;
import dom.data.baituoshuju.datafromas.WoDeQuanZiList.WoDeQuanZiData;
import httpservice.GsonUtils;
import httpservice.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Request;

import static com.baituo.me.R.id.lin_myquanzi;

public class Fengxiangpopwindeow_add <E extends Activity> implements OnClickListener, IWeiboHandler.Response{

	public static PopupWindow popupWindow;
	E mContext;
	//weixin
	public static IWXAPI api;
	private String flag_from="";
	//qq
		 public static Tencent mTencent;
		 private static final String mAppid = "1104949681";
		
		 private AuthInfo mAuthInfo;
			private SsoHandler mSsoHandler;
			private Oauth2AccessToken mAccessToken;
			 /** 微博微博分享接口实例 */
		    private IWeiboShareAPI  mWeiboShareAPI = null;
			private View view;
			private String sucaiId="";


	public Fengxiangpopwindeow_add(E mContext, View view, String sucaiId, String flag) {
		super();
		this.view = view;
		this.sucaiId = sucaiId;
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, true);
		this.flag_from = flag;
		// 将该app注册到微信
        api.registerApp(Constants.APP_ID);
        
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
		LinearLayout lin_myquanzi = (LinearLayout) inflatepop.findViewById(R.id.lin_myquanzi);
		lin_myquanzi.setOnClickListener(this);
		Log.e("sucaiIdsdgffd", sucaiId+"");

		if("2".equals(flag_from)){
			//在这里进行判断是否有圈子
			getData(lin_myquanzi) ;
		}else{
			lin_myquanzi.setVisibility(View.GONE);
		}
		lin_myquanzi.setOnClickListener(this);
		lin_weixin.setOnClickListener(this);
		lin_qq.setOnClickListener(this);
		lin_pengyouquan.setOnClickListener(this);
		lin_weibo.setOnClickListener(this);
		popupWindow = new PopupWindow(inflatepop , LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //要显示的布局
        popupWindow.setContentView(inflatepop);
        
        tv_quxiao.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		popupWindow.dismiss();
        		darkenBackgroud(1f);
        		mContext. getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        	}
        });
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        darkenBackgroud(0.4f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				  darkenBackgroud(1f);
				  mContext. getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			}
		});
		return inflatepop;  
		
	}
	private void darkenBackgroud(Float bgcolor) {
		  WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
		  lp.alpha = bgcolor;
		  mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		  mContext. getWindow().setAttributes(lp);
		 }
	
	//qq
		public void shareOnlyImageOnQQ(String title, String content, String url) {
	        final Bundle params = new Bundle();
	        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
	        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
	        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
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
		

	int click = -1; //区分分到哪里
	String SettingTitle="看广告，捡红包";
	String Setteingcontet= "你的任何一个商品都是广告--百托垂直广告分享平台";
String Settingurl="http://www.baituo.me/moblie/cn/recommend.html";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_weixin:
			click=1;
			if("0".equals(flag_from))
			{
				share2Wx(0,SettingTitle,Setteingcontet,Settingurl);
			}else if("1".equals(flag_from))
			{
				getqzInfo();
			}else
			{
				//分享id给后台
				shareidto();
			}
			popupWindow.dismiss();
			break;
		case R.id.lin_qq:
			click=2;
			if("0".equals(flag_from))
			{
					//分享的内容
				shareOnlyImageOnQQ(SettingTitle,Setteingcontet,Settingurl);
			}else if("1".equals(flag_from))
			{
				getqzInfo();
			}else
			{
				//分享id给后台
				shareidto();
			}

			popupWindow.dismiss();
			break;
		case R.id.lin_pengyouquan:
			click=3;
			if("0".equals(flag_from))
			{
				share2Wx(1,SettingTitle,Setteingcontet,Settingurl);
			}else if("1".equals(flag_from))
			{
				getqzInfo();
			}else
			{
				shareidto();
			}

			popupWindow.dismiss();
			break;
		case R.id.lin_weibo:
			click=4;
			initWEibo();
			if (!mAccessToken.isSessionValid()) {
				mSsoHandler.authorize(new AuthListener());
			}
			if("0".equals(flag_from))
			{
				shareWebPage(mContext,SettingTitle,Setteingcontet,Settingurl);//分享网址
			}else if("1".equals(flag_from))
			{
				getqzInfo();
			}else
			{
				shareidto();
			}


			popupWindow.dismiss();
			break;
		case lin_myquanzi:
			if("0".equals(flag_from))
			{

			}else if("1".equals(flag_from))
			{

			}else
			{

			}
			Intent intent = new Intent(mContext, MyQuanZi_Share.class);
			intent.putExtra("sucaiId", sucaiId);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}


	public void getqzInfo()
	{
		OKHttpUtil.getpartnerorderInfo(sucaiId, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int i) {
				Log.e("获取圈子分享信息",e.toString()+"");

			}

			@Override
			public void onResponse(String s, int i) {
				Log.e("获取圈子分享信息",s+"");


				Share share=	GsonUtils.parseJSON(s, Share.class);
				Share.DataBean data = share.getData();
				String shareurl = data.getUrl();
//				String title=	data.getPartnerName();
				String title=	SettingTitle;

				String content=data.getContent()+"";

				if(click==1)
				{
					share2Wx(0,title,content,shareurl);
				}else if(click==2)
				{
					//分享的内容
					shareOnlyImageOnQQ(title,content,shareurl);
				}else if(click==3)
				{
					share2Wx(1,title,content,shareurl);
				}else if(click==4)//分享微博
				{
					shareWebPage(mContext,title,content,shareurl);//分享网址
				}


			}
		});
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
	
	private WebpageObject getWebpageObj(String title, String content, String url) {
		
		WebpageObject webpageObject = new WebpageObject();
		webpageObject.actionUrl=url;
		
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		
		webpageObject.defaultText="a";
		webpageObject.description=content;
		webpageObject.identify="a";
		webpageObject.schema="a";
		webpageObject.thumbData=byteArray;
		webpageObject.title=title;
		return webpageObject;

		
	}

	private void shareWebPage(Context context, String title, String content, String url){
		Log.e("shareWebPage", "shareWebPage");
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj(title,content,url);
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

	private void share2Wx(int flag, String title, String content, String url) {
    	 WXWebpageObject webpage = new WXWebpageObject();  
    	    webpage.webpageUrl = url+"";
    	    WXMediaMessage msg = new WXMediaMessage(webpage);  
    	  if (flag==0) {
    		  msg.title = title+"";
		   }else if (flag==1) {
			   msg.title = title+"";
		   }
    	    msg.description =content+""; //网址说明
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
			Log.e("微博分享成功","微博分享成功");
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
	//分享id给
	private void shareidto()
	{
		OKHttpUtil.shareSucaiId(sucaiId+"", new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int i) {
				Log.e("把素材id传给服务器e", e.toString()+"");
			}
			@Override
			public void onResponse(String s, int i) {
				Log.e("把素材id传给服务器s", s+"");

				Share share=	GsonUtils.parseJSON(s, Share.class);
				Share.DataBean data = share.getData();
				String shareurl = data.getUrl();
//				String title=	data.getPartnerName();
				String title=	SettingTitle;

				String content=data.getContent()+"";

				if(click==1)
				{
					share2Wx(0,title,content,shareurl);
				}else if(click==2)
				{
					//分享的内容
					shareOnlyImageOnQQ(title,content,shareurl);
				}else if(click==3)
				{
					share2Wx(1,title,content,shareurl);
				}else if(click==4)//分享微博
				{
					shareWebPage(mContext,title,content,shareurl);//分享网址
				}

			}
		});
	}

	private List<WoDeQuanZiData> myCreateQuanZiList = new ArrayList<WoDeQuanZiData>();
	private List<WoDeQuanZiData> myJoinQuanZiList = new ArrayList<WoDeQuanZiData>();
	private void getData(final LinearLayout lin_myquanzi) {
		OKHttpUtil.myGroup(new StringCallback() {
			@Override
			public void onBefore(Request request, int id) {
				super.onBefore(request, id);
			}
			@Override
			public void onAfter(int id) {
				super.onAfter(id);
			}
			@Override
			public void onResponse(String arg0, int arg1) {
				Log.e("我的圈子", arg0);
				myCreateQuanZiList.clear();
				myJoinQuanZiList.clear();
				//"identity":{0-圈子的创建人 1-普通用户},"status":{0-已加入 1-已被删除});
				WoDeQuanZiList  woDeQuanZi=GsonUtils.parseJSONArray(arg0, WoDeQuanZiList.class);
				List<WoDeQuanZiData> data= woDeQuanZi.getData();
				for (int i = 0; i < data.size(); i++) {
					if ("0".equals(data.get(i).getIdentity())) {
						//1-圈子的创建人(我创建的圈子)
						myCreateQuanZiList.add(data.get(i));
					}else if ("1".equals(data.get(i).getIdentity())&&"0".equals(data.get(i).getStatus())) {
						//1-普通用户(我加入的圈子)   status=1已被删除
						myJoinQuanZiList.add(data.get(i));
					}
				}
				if(myCreateQuanZiList.size()==0){
					lin_myquanzi.setVisibility(View.GONE);
				}else{
					lin_myquanzi.setVisibility(View.VISIBLE);
				}
				Log.e("myJoinQuanZiList我的圈子", myJoinQuanZiList.toString());
				Log.e("我的圈子数目", myJoinQuanZiList.size()+"");
			}
			@Override
			public void onError(Call arg0, Exception arg1, int arg2) {
				Log.e("我的圈子", arg1.toString());
			}
		});
	}
	
}
