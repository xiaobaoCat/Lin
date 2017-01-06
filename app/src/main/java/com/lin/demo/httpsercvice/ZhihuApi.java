package com.lin.demo.httpsercvice;


import com.lin.demo.module.XiaoHuaDaQuanList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhihuApi {

    @GET("/api/4/news/latest")
    Observable<XiaoHuaDaQuanList> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<XiaoHuaDaQuanList> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<XiaoHuaDaQuanList> getZhihuStory(@Path("id") String id);

    @GET("http://lab.zuimeia.com/wallpaper/category/1/?page_size=1")
    Observable<XiaoHuaDaQuanList> getImage();

}
