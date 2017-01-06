package com.lin.demo.httpsercvice;
import com.lin.demo.module.TodayList;
import com.lin.demo.module.TouTiaoList;
import com.lin.demo.module.XiaoHuaDaQuanList;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 定义Retrofit访问的接口
 * @Query和@QueryMap  用于Get请求传递参数
 * @Field和@FieldMap  用于Post方式传递参数  请求接口方法上添加@FormUrlEncoded,即以表单的方式传递参数
 */
public interface RetrofitInterfaceService {

    //get笑话
    //http://japi.juhe.cn/joke/content/list.from?key=ec58a6b65513980369a97cbed0c8ed17&page=1&pagesize=10&sort=asc&time=1418745237
    @GET("joke/content/list.from")
    Observable<XiaoHuaDaQuanList> getXiaoHuaDaQuanList(
            @Query("key") String key,    //申请的key
            @Query("page") int page,  //当前页数,默认1
            @Query("pagesize") int pageSize,   //	每次返回条数,默认1,最大20
            @Query("time") String time,        //时间戳（10位），如：1418816972
            @Query("sort") String sort     //类型，desc:指定时间之前发布的，asc:指定时间之后发布的
    );
    //get趣图
    //http://japi.juhe.cn/joke/img/list.from?key=ec58a6b65513980369a97cbed0c8ed17&page=1&pagesize=20&time=1483061213&sort=desc
    @GET("joke/img/list.from")
    Observable<XiaoHuaDaQuanList> getQuTuDaQuanList(
            @Query("key") String key,   //申请的key
            @Query("page") int page,  //当前页数,默认1
            @Query("pagesize") int pageSize,   //	每次返回条数,默认1,最大20
            @Query("time") String time,      //时间戳（10位），如：1482991531
            @Query("sort") String sort    //类型，desc:指定时间之前发布的，asc:指定时间之后发布的

    );
    /***********************************************GET***********************************************/
    //Get方式获取头条信息(get/post)
    //http://v.juhe.cn/toutiao/index?type=&key=70ebae81f3cd752431cc91b67a93ad7c
    @GET("toutiao/index")
    Observable<TouTiaoList> getTouTiaoList(
            @Query("key") String key,    //申请的key
            @Query("type") String type     //类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
    );

    //Get方式获取头条信息(get/post)
    //http://v.juhe.cn/toutiao/index?type=&key=70ebae81f3cd752431cc91b67a93ad7c
    @GET("toutiao/index")
    Observable<TouTiaoList> getTouTiaoList(@QueryMap Map<String, String> map  );
    /***********************************************GET***********************************************/



    /***********************************************POST***********************************************/
     //获取头条信息(Field)
    //http://v.juhe.cn/toutiao/index?type=&key=70ebae81f3cd752431cc91b67a93ad7c
    @FormUrlEncoded
    @POST("toutiao/index")
    Observable<TouTiaoList> postTouTiaoList(
            @Field("key") String key,    //申请的key
            @Field("type") String type     //类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
    );

    //获取头条信息(FieldMap)
    //http://v.juhe.cn/toutiao/index?type=&key=70ebae81f3cd752431cc91b67a93ad7c
    @FormUrlEncoded
    @POST("toutiao/index")
    Observable<TouTiaoList> postTouTiaoList( @FieldMap Map<String, String> map);
    /***********************************************POST***********************************************/




    //历史上的今天
    //事件列表(v2.0推荐) http://v.juhe.cn/todayOnhistory/queryEvent.php?key=3b164a065588796ad43f267b4a80d3d4&date=1/1
    @GET("todayOnhistory/queryEvent.php")
    Observable<TodayList> getTodayOnhistoryList(
            @Query("key") String key,   //申请的key
            @Query("date") String date  //日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
    );

    //根据id查询详细信息(v2.0推)   http://v.juhe.cn/todayOnhistory/queryDetail.php?key=3b164a065588796ad43f267b4a80d3d4&e_id=1
    @GET("japi/toh")
    Observable<TodayList> getTodayOnhistoryDetail(@QueryMap Map<String, String> map );
}
