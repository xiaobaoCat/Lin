package com.lin.demo.ui.fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.demo.R;
import com.lin.demo.adapter.ToDayListAdapter;
import com.lin.demo.httpsercvice.HttpService;
import com.lin.demo.interfaces.OnTabReselectRefreshListener;
import com.lin.demo.module.TodayList;
import com.lin.demo.ui.activity.MainActivity;
import com.lin.demo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FragmentTab2Detail extends BaseFragment{

    private int position;
    private String  date="";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private List<TodayList.ResultBean> todayDatas;

    private int page=1;//请求的页码（默认为第一页）
    private int pageSize=20;//每页请求的条数（默认为1，最大20）

    private int refreshType=0;//0下拉刷新
    private PtrClassicFrameLayout ptrClassicFrameLayout;

    public FragmentTab2Detail() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        FragmentTab2.getOnTabReselectRefreshListener(new OnTabReselectRefreshListener() {
            @Override
            public void OnTabReselectRefresh(int position1) {
                ToastUtil.showToast(mContext, "过往--" + date);
                ptrClassicFrameLayout.autoRefresh();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args!=null) {
            position =args.getInt("position");
            date=args.getString("date");
        }
        if (view == null) {
            mContext=getActivity();
            view=inflater.inflate(R.layout.fragment_tab2_detail, container, false);
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
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置adapter
        todayDatas=new ArrayList<>();
        adapter=new ToDayListAdapter(mContext,todayDatas);
        mRecyclerView.setAdapter(adapter);
    }

    private void initData(){
        HttpService.getTodayOnhistoryList(date, new Observer<TodayList>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("Disposable",d.toString());
            }
            @Override
            public void onNext(TodayList todayList) {
                if (todayList!=null){
                    if (todayList.getError_code()==0){
                        if (todayList.getResult()!=null){
                            if (refreshType==0){//下拉刷新
                                todayDatas.clear();
                            }
                            todayDatas.addAll(todayList.getResult());
                            ptrClassicFrameLayout.refreshComplete();
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        ToastUtil.showToast(mContext,todayList.getReason());
                    }
                }

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
    }
}
