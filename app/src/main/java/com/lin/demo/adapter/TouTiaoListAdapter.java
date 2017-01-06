package com.lin.demo.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.lin.demo.ui.activity.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class TouTiaoListAdapter extends RecyclerView.Adapter<TouTiaoListAdapter.MyViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;
    public interface OnItemClickLitener {
        abstract void onItemClick(View view, int position);
        abstract void onItemLongClick(View view , int position);
    }
    public void setRecyclerViewOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private Context mContext;
    private List<TouTiaoList.ResultBean.DataBean> touTiaoData;

    public TouTiaoListAdapter(Context mContext,List<TouTiaoList.ResultBean.DataBean> touTiaoData){
        this.mContext=mContext;
        this.touTiaoData=touTiaoData;
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( mContext).inflate(R.layout.item_newslist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener!= null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return false;
                }
            });
        }

        holder.tv_title.setText(touTiaoData.get(position).getTitle());
        holder.tv_author.setText(touTiaoData.get(position).getAuthor_name());
        holder.tv_data.setText(touTiaoData.get(position).getDate());

        final List<String>  images=new ArrayList<String>();
        if (touTiaoData.get(position).getThumbnail_pic_s()!=null){
            images.add(touTiaoData.get(position).getThumbnail_pic_s());
        }
        if (touTiaoData.get(position).getThumbnail_pic_s02()!=null){
            images.add(touTiaoData.get(position).getThumbnail_pic_s02());
        }
        if (touTiaoData.get(position).getThumbnail_pic_s03()!=null){
            images.add(touTiaoData.get(position).getThumbnail_pic_s03());
        }
        if (images.size()>0){
            holder.imageView.setVisibility(View.VISIBLE);
            GlideUtil.displayImage(AApplication.getInstance(),images.get(0),holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return touTiaoData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView imageView;
        TextView tv_author;
        TextView tv_data;
        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            imageView= (ImageView) view.findViewById(R.id.imageView);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
            tv_data = (TextView) view.findViewById(R.id.tv_data);
        }
    }
}
