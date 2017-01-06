package com.lin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.demo.AApplication;
import com.lin.demo.R;
import com.lin.demo.httpsercvice.GlideUtil;
import com.lin.demo.module.TodayList;
import com.lin.demo.module.XiaoHuaDaQuanList;
import com.lin.demo.ui.fragment.FragmentTab2Detail;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class ToDayListAdapter extends RecyclerView.Adapter<ToDayListAdapter.MyViewHolder>{

    private Context mContext;
    private List<TodayList.ResultBean> todayDatas;

    public ToDayListAdapter(Context mContext, List<TodayList.ResultBean> todayDatas){
        this.mContext=mContext;
        this.todayDatas=todayDatas;
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view =LayoutInflater.from( mContext).inflate(R.layout.item_todaylist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText(todayDatas.get(todayDatas.size()-1-position).getTitle());
        holder.tv_date.setText(todayDatas.get(todayDatas.size()-1-position).getDate());
    }
    @Override
    public int getItemCount() {
        return todayDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_date;
        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
