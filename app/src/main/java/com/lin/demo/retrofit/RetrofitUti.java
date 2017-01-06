package com.lin.demo.retrofit;

import com.lin.demo.module.TouTiaoList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class RetrofitUti {

    public final static String BaseURL_TouTiao="http://v.juhe.cn/";//头条地址
    public final static String Key_TouTiao="70ebae81f3cd752431cc91b67a93ad7c";//头条地址

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("BaseURL_TouTiao")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    public void getTouTiaoList(String type){
        Call<TouTiaoList> call = retrofitInterface.getTouTiaoList(Key_TouTiao,type);

        //同步请求 这里需要注意的是网络请求一定要在子线程中完成，不能直接在UI线程执行，不然会crash
        try {
            TouTiaoList touTiaoList = call.execute().body();
        }catch (Exception e){
        }


        //异步请求
        call.enqueue(new Callback<TouTiaoList>() {
            @Override
            public void onResponse(Call<TouTiaoList> call, Response<TouTiaoList> response) {

            }
            @Override
            public void onFailure(Call<TouTiaoList> call, Throwable t) {

            }
        });
    }
}
