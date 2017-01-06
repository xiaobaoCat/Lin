package com.lin.demo.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.demo.AApplication;
import com.lin.demo.R;
import com.lin.demo.adapter.TouTiaoListAdapter;
import com.lin.demo.httpsercvice.GlideUtil;
import com.lin.demo.httpsercvice.HttpService;
import com.lin.demo.interfaces.OnTabReselectListener;
import com.lin.demo.interfaces.OnTabReselectRefreshListener;
import com.lin.demo.module.TouTiaoList;
import com.lin.demo.ui.activity.MainActivity;
import com.lin.demo.ui.activity.NewsDetailActivity;
import com.lin.demo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTab1Detail extends BaseFragment {

    private int position;
    private String[] types = new String[]{"top", "shehui", "guonei","guoji", "yule", "tiyu","junshi", "keji", "caijing","shishang"};
    private RecyclerView mRecyclerView;
    private TouTiaoListAdapter adapter;
    private List<TouTiaoList.ResultBean.DataBean> touTiaoData;

    private int refreshType=0;//0下拉刷新
    private PtrClassicFrameLayout ptrClassicFrameLayout;

    public FragmentTab1Detail() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        FragmentTab1.getOnTabReselectRefreshListener(new OnTabReselectRefreshListener() {
            @Override
            public void OnTabReselectRefresh(int position1) {
                ToastUtil.showToast(mContext, "新闻" + types[position]);
                ptrClassicFrameLayout.autoRefresh();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args!=null) {
            position =args.getInt("position");
        }
        mContext=getActivity();
        if (view == null) {
            view=inflater.inflate(R.layout.fragment_tab1_detail, container, false);
            initView();
            initRecyclerView();
            initData();
            initPtrFrameLayout();

        }else{
            // 缓存view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ptrClassicFrameLayout= (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout);
    }


    private void initRecyclerView(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置adapter
        touTiaoData=new ArrayList<TouTiaoList.ResultBean.DataBean>();
        adapter=new TouTiaoListAdapter(mContext,touTiaoData);
        mRecyclerView.setAdapter(adapter);
        adapter.setRecyclerViewOnItemClickLitener(new TouTiaoListAdapter.OnItemClickLitener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("url",touTiaoData.get(position).getUrl());
                mContext.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initPtrFrameLayout(){
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉开始刷新
                refreshType=0;
                initData();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        ptrClassicFrameLayout.setResistance(1.7f);
        ptrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrClassicFrameLayout.setDurationToClose(200);
        ptrClassicFrameLayout.setDurationToCloseHeader(1000);
        // default is false
        ptrClassicFrameLayout.setPullToRefresh(false);
        // default is true
        ptrClassicFrameLayout.setKeepHeaderWhenRefresh(true);
        ptrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh();
            }
        },500);
    }

    private void initData(){
        HttpService.getTouTiaoList(types[position], new Observer<TouTiaoList>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(TouTiaoList touTiaoList) {
                if (refreshType==0){//下拉刷新
                    touTiaoData.clear();
                }
                touTiaoData.addAll(touTiaoList.getResult().getData());
                ptrClassicFrameLayout.refreshComplete();
                adapter.notifyDataSetChanged();
                Log.e("touTiaoData",touTiaoData.toString());
            }
            @Override
            public void onError(Throwable e) {
                Log.e("onError",e.toString());
                ToastUtil.showToast(mContext,"网络错误");
                ptrClassicFrameLayout.refreshComplete();
            }
            @Override
            public void onComplete() {
                Log.e("onComplete","onComplete");
                ptrClassicFrameLayout.refreshComplete();
            }
        });
    }
}
