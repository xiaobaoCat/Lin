package util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baituo.me.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hengshuo.chengszj.activity.main.AApplication;

import java.util.List;

import dom.data.baituoshuju.datafromas.Getfujinsucaidata;
import httpservice.UILUtils;
import widget.FlowLayout;


/**
 * Created by jianghejie on 15/11/26.
 */
public class MyAdapter extends RecyclerView.Adapter<util.MyAdapter.ViewHolder> implements View.OnClickListener{
    public List<Getfujinsucaidata.Datadata> datas = null;
    private Context context;

    public MyAdapter(List<Getfujinsucaidata.Datadata> datas , Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.e( "position: ",position+"" );
        if(datas.size()>0&&position>=0&&position<datas.size())
        {
            if("0".equals(datas.get(position).getType()))
            {
                return 0;
            }else if("1".equals(datas.get(position).getType()))
            {
                return 1;
            }else
            {
                return super.getItemViewType(position);
            }
        }else
        {
            return super.getItemViewType(position);
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view=null;
        ViewHolder vh;
        if(viewType==0)
        {
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shequ,viewGroup,false);
             vh = new ViewHolder(view,viewType);
        }else
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shequ_new,viewGroup,false);
            vh = new ViewHolder(view,viewType);
        }

        //将创建的View注册点击事件
        view.setOnClickListener(this);
//        Log.e("viewType文件类型",viewType+"" );
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Getfujinsucaidata.Datadata item =datas.get(position);
        viewHolder.tv_title.setText(item.getTitle()+"");

        if( item.getPartnerName()!=null)
        {
            viewHolder.tv_yyzh.setText( item.getPartnerName()+"");
        }else
        {
            viewHolder.tv_yyzh.setText( "未知来源");
        }

        UILUtils.displayImageNoAnim(item.getPhoto(), viewHolder.img_tou);
        if("0".equals(item.getType())) {
            viewHolder.tv_liuyanNumb.setText(item.getMsgSum()+"");
        }else{
            viewHolder.tv_liuyanNumb.setText(item.getMsgSum()+"");
        }
        if("0".equals(item.getType()))
        {
            viewHolder.img_tuiguang.setVisibility(View.VISIBLE);
            if("1".equals(item.getExtend())){
                viewHolder.img_tuiguang.setVisibility(View.VISIBLE);
            }else{
                viewHolder.img_tuiguang.setVisibility(View.GONE);
            }
            viewHolder.tv_dianzanNum.setText( item.getLikeSum()+"");
//            viewHolder.tv_liuyanNumb.setText(item.getMsgSum()+"");

            if(!"".equals(item.getImg())){
                viewHolder.flowLayout.setVisibility(View.VISIBLE);
                //split:将一个字符串以某个字符分割为子字符串，然后将结果作为字符串数组返回
                final String[] strArray =item.getImg().split(","); //拆分字符为"," ,然后把结果交给数组strArray
                viewHolder.flowLayout.removeAllViews();
                if (strArray.length > 0) {
                    //获取屏幕信息
                    DisplayMetrics dm = new DisplayMetrics();
                    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth = dm.widthPixels;
                    int screenHeigh = dm.heightPixels;
                    int width=0;
                    if(strArray.length<=3){
                        width=(screenWidth-viewHolder.flowLayout.getPaddingLeft()-viewHolder.flowLayout.getPaddingRight())/strArray.length;
                    }else{
                        width=(screenWidth-viewHolder.flowLayout.getPaddingLeft()-viewHolder.flowLayout.getPaddingRight())/3;
                    }
                    int width2=0;
                    if(strArray.length==1){
                        width2=(screenWidth-viewHolder.flowLayout.getPaddingLeft()-viewHolder.flowLayout.getPaddingRight())/2;
                    }else{
                        width2=(screenWidth-viewHolder.flowLayout.getPaddingLeft()-viewHolder.flowLayout.getPaddingRight())/3;
                    }
                    for (int i = 0; i <strArray.length; i++)  {
                        if (i<3) {//只显示四张
                            final int index=i;
                            ImageView imageView = new ImageView(context);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams((int) (width), (int) (width2)));
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//将图片等比例缩放，让图像的短边与ImageView的边长度相同，即不能留有空白，缩放后截取中间部分进行显示。
                            imageView.setPadding(5,5,5,5);
                            viewHolder.flowLayout.addView(imageView);
                            Glide.with(AApplication.getInstance()).load(strArray[i]).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);

                        }
                    }
                }
            }else{
                viewHolder.flowLayout.setVisibility(View.GONE);//防止复用导致的错乱
            }

        }else
        {
            viewHolder.img_news.setVisibility(View.GONE);
            if(!"".equals(item.getImg())){
                viewHolder.img_news.setVisibility(View.VISIBLE);
                Glide.with(AApplication.getInstance()).load(item.getImg()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.img_news);
            }else
            {
                viewHolder.img_news.setVisibility(View.GONE);
            }

            if(item.getClickSum()!=null)
            {
                viewHolder.tv_dianzanNum.setText("浏览"+ item.getClickSum()+"次");
            }else
            {
                viewHolder.tv_dianzanNum.setText( "浏览"+ 0+"次");
            }

        }


        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(datas.get(position));
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_dianzanNum;
        public TextView tv_title;
        public FlowLayout flowLayout;
        public ImageView img_tou ;
        public TextView tv_yyzh;
        public ImageView img_tuiguang;

        public TextView tv_liuyanNumb;

        public ImageView img_news;

        public ViewHolder(View view, int viewType){
            super(view);

            if(viewType==0)
            {
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_yyzh = (TextView) view.findViewById(R.id.tv_yyzh);
                tv_dianzanNum = (TextView) view.findViewById(R.id.tv_dianzanNum);
                tv_liuyanNumb = (TextView) view.findViewById(R.id.tv_liuyanNumb);
                img_tou = (ImageView) view.findViewById(R.id.img_tou);
                img_tuiguang = (ImageView) view.findViewById(R.id.img_tuiguang);
                flowLayout= (FlowLayout)view.findViewById(R.id.flowLayout);
            }else if(viewType==1)
            {
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_yyzh = (TextView) view.findViewById(R.id.tv_yyzh);
                img_tou = (ImageView) view.findViewById(R.id.img_tou);
                tv_liuyanNumb = (TextView) view.findViewById(R.id.tv_liuyanNumb);
                img_news = (ImageView) view.findViewById(R.id.img_news);
                tv_dianzanNum = (TextView) view.findViewById(R.id.tv_dianzanNum);
            }

        }



    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,(Getfujinsucaidata.Datadata)view.getTag());
        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Getfujinsucaidata.Datadata data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
