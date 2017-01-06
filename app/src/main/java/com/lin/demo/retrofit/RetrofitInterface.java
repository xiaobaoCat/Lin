package com.lin.demo.retrofit;

import com.lin.demo.module.TouTiaoList;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public interface RetrofitInterface {

    //get请求
    @GET("toutiao/index")  //@GET注解标识为get请求，@GET中填写接口名称
    Call<TouTiaoList> getTouTiaoList(
            @Query("key") String key,    //接口参数key
            @Query("type") String type  //接口参数type
    );
    @GET("toutiao/index")  //@GET注解标识为get请求，@GET中填写接口名称
    Call<TouTiaoList> getTouTiaoList(
            @QueryMap Map<String, String> map  //接口参数包含在map中
    );


    //post请求
    @FormUrlEncoded
    @GET("toutiao/index")  //@GET注解标识为get请求，@GET中填写接口名称
    Call<TouTiaoList> postTouTiaoList(
            @Field("key") String key,    //接口参数key  @Field注解将每一个请求参数都存放至请求体中，还可以添加encoded参数，该参数为boolean型，
                                          // 具体的用法为@Field(value = "book", encoded = true) String book encoded参数为true的话，key-value-pair将会被编码，即将中文和特殊字符进行编码转换
            @Field("type") String type  //接口参数type
    );
    @GET("toutiao/index")  //@GET注解标识为get请求，@GET中填写接口名称
    @FormUrlEncoded
    Call<TouTiaoList> postTouTiaoList(
            @FieldMap Map<String, String> map  //接口参数包含在map中
    );


   //@Path 如果请求的相对地址也是需要调用方传递，那么可以使用@Path注解，示例代码如下：
    @GET("book/{id}")
    Call<TouTiaoList> getBook(@Path("id") String id);


    //@Body  如果Post请求参数有多个，那么统一封装到类中应该会更好，这样维护起来会非常方便
    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviews(@Body Reviews reviews);
    public class Reviews {
        public String book;
        public String title;
        public String content;
        public String rating;
    }



    // 上传单个文件
    @Multipart
    @POST("upload")
    Call<String> uploadFile(
            @Part("data") String String,
            @Part MultipartBody.Part file
    );

    // 上传多个文件
    @Multipart
    @POST("upload")
    Call<String> uploadMultipleFiles(
            @Part("data") String String,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2
    );

}
