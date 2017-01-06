package com.lin.demo.httpsercvice;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lin.demo.module.TodayList;
import com.lin.demo.module.TouTiaoList;
import com.lin.demo.module.XiaoHuaDaQuanList;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class HttpService {
    public final static String BaseURL_TouTiao="http://v.juhe.cn/";//头条地址
    public final static String BaseURL_XiaoHuaDaQuan="http://japi.juhe.cn/";//笑话大全地址
    public final static String BaseURL_Today="http://v.juhe.cn/";//历史上的今天

    public static Retrofit getTRetrofit(String BasURL,boolean isCache){
        OkHttpClient okHttpClient;
        if (isCache){
            okHttpClient=OkHttpUtil.getOkHttpClientCache();//有缓存
        }else{
            okHttpClient=OkHttpUtil.getOkHttpClientNoCache();//没缓存
        }
        return new Retrofit.Builder()
                .client(OkHttpUtil.getOkHttpClientCache())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Retrofit2适配RxJava2
                .baseUrl(BasURL)
                .build();
    }

    /**
     * GET方式获取头条
     * @param type 类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
     * @param observer
     */
    public static void getTouTiaoList(String type,Observer<TouTiaoList> observer){
        RetrofitInterfaceService retrofitInterfaceService = getTRetrofit(BaseURL_TouTiao,false).create(RetrofitInterfaceService.class);
        retrofitInterfaceService.getTouTiaoList("70ebae81f3cd752431cc91b67a93ad7c",type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * Post方式获取头条
     * @param type 类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
     * @param observer
     */
    public static void postTouTiaoList(String type,Observer<TouTiaoList> observer){
        Map<String, String> map=new HashMap<String, String>();
        map.put("key","70ebae81f3cd752431cc91b67a93ad7c");
        map.put("type","type");
        RetrofitInterfaceService retrofitInterfaceService = getTRetrofit(BaseURL_TouTiao,false).create(RetrofitInterfaceService.class);
        retrofitInterfaceService.postTouTiaoList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



    /**
     * 笑话大全
     * @param page  当前页数,默认1
     * @param pageSize  每次返回条数,默认1,最大20
     * @param type  0 获取当前时间之前的笑话  1 获取最新笑话  2 获取当前时间之前的趣图  3 获取最新趣图
     * @param observer
     */
    public static void getXiaoHuaDaQuanList(int page,int pageSize,int type,Observer<XiaoHuaDaQuanList> observer){
        RetrofitInterfaceService retrofitInterfaceService = getTRetrofit(BaseURL_XiaoHuaDaQuan,false).create(RetrofitInterfaceService.class);
        if (type==0){//获取当前时间之前的笑话
            retrofitInterfaceService.getXiaoHuaDaQuanList("ec58a6b65513980369a97cbed0c8ed17",page,pageSize,(System.currentTimeMillis()+"").substring(0,10),"desc")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        if (type==1){//获取当前时间之前的趣图
            retrofitInterfaceService.getQuTuDaQuanList("ec58a6b65513980369a97cbed0c8ed17",page,pageSize,(System.currentTimeMillis()+"").substring(0,10),"desc")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }


    /**
     * 获取历史上的今天事件列表
     * @param date  日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
     */
    public static void getTodayOnhistoryList(String date ,Observer<TodayList> observer){
        RetrofitInterfaceService retrofitInterfaceService = getTRetrofit(BaseURL_Today,false).create(RetrofitInterfaceService.class);
        retrofitInterfaceService.getTodayOnhistoryList("3b164a065588796ad43f267b4a80d3d4",date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    /**
     * 获取历史上的今天事件列表的事件详情
     * @param e_id  事件id
     */
    public static void getTodayOnhistoryDetail(String e_id ,Observer<TodayList> observer){
        Map<String, String> map=new HashMap<String, String>();
        map.put("key","3b164a065588796ad43f267b4a80d3d4");//申请的key
        map.put("e_id",e_id); //日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
        RetrofitInterfaceService retrofitInterfaceService = getTRetrofit(BaseURL_Today,false).create(RetrofitInterfaceService.class);
        retrofitInterfaceService.getTodayOnhistoryDetail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
