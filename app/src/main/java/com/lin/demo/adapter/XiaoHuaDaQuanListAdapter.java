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
import com.lin.demo.module.TouTiaoList;
import com.lin.demo.module.XiaoHuaDaQuanList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class XiaoHuaDaQuanListAdapter extends RecyclerView.Adapter<XiaoHuaDaQuanListAdapter.MyViewHolder>{

    private Context mContext;
    private List<XiaoHuaDaQuanList.ResultBean.DataBean> xiaoHuaDaQuanDatas;

    public XiaoHuaDaQuanListAdapter(Context mContext,List<XiaoHuaDaQuanList.ResultBean.DataBean> xiaoHuaDaQuanDatas){
        this.mContext=mContext;
        this.xiaoHuaDaQuanDatas=xiaoHuaDaQuanDatas;
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( mContext).inflate(R.layout.item_xiaohualist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText(xiaoHuaDaQuanDatas.get(position).getContent());
        holder.tv_date.setText(xiaoHuaDaQuanDatas.get(position).getUpdatetime());
        if (xiaoHuaDaQuanDatas.get(position).getUrl()==null){
            holder.iv_image.setVisibility(View.GONE);
        }else{
            holder.iv_image.setVisibility(View.VISIBLE);
            GlideUtil.displayImage(AApplication.getInstance(),xiaoHuaDaQuanDatas.get(position).getUrl(),holder.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        return xiaoHuaDaQuanDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_image;
        TextView tv_date;
        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_image= (ImageView) view.findViewById(R.id.iv_image);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
