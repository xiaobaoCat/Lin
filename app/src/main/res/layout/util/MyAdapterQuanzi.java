package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.hengshuo.chengszj.activity.main.GeRenZiLiao;

import java.util.List;

import dom.data.baituoshuju.datafromas.HuaTiMangeList;
import httpservice.UILUtils;
import widget.FlowLayout;

/**
 * Created by jianghejie on 15/11/26.
 */
public class MyAdapterQuanzi extends RecyclerView.Adapter<util.MyAdapterQuanzi.ViewHolder> implements View.OnClickListener{
    public List<HuaTiMangeList.HuaTiMangeData> datas = null;
    private Context context;

    public MyAdapterQuanzi(List<HuaTiMangeList.HuaTiMangeData> datas , Context context) {
        this.datas = datas;
        this.context = context;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quanzi_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final HuaTiMangeList.HuaTiMangeData item =datas.get(position);
        viewHolder.tv_title.setText(item.getTitle()+"");
        viewHolder.tv_sucai_num.setText("浏览"+item.getClickSum()+"次");
        viewHolder.tv_sucai_lynum.setText(item.getMsgSum()+"");
        viewHolder.tv_sucai_dainzan.setText(item.getLikeSum()+"");
        UILUtils.displayImageNoAnim(item.getUserId().getPhoto(), viewHolder.img_touxiang);
        viewHolder.tv_name.setText( item.getUserId().getNiceName()+"");

        viewHolder.tv_time.setText( util.TimeUtil.gettimecl(item.getTime()));

        if("0".equals(item.getUserId().getSex()))
        {
            viewHolder.img_sex.setImageResource(R.drawable.nan);
        }else
        {
            viewHolder.img_sex.setImageResource(R.drawable.nv);

        }
        //TODO 点击头像进入个人资料
        viewHolder.img_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getUserId().getId()!=null)
                {
                    Intent intent = new Intent(context, GeRenZiLiao.class);
                    intent.putExtra("which", "grsq");
                    intent.putExtra("userId", item.getUserId().getId());
                    context.startActivity(intent);
                }
            }
        });

        FlowLayout flowLayout=viewHolder.flowLayout;
        if(!"".equals(item.getImg())){

            flowLayout.setVisibility(View.VISIBLE);
            //split:将一个字符串以某个字符分割为子字符串，然后将结果作为字符串数组返回
            final String[] strArray =item.getImg().split(","); //拆分字符为"," ,然后把结果交给数组strArray
            flowLayout.removeAllViews();
            if (strArray.length > 0) {
                //获取屏幕信息
                DisplayMetrics dm = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenHeigh = dm.heightPixels;
                int width = 0;
                if(strArray.length<=3)
                {
                    width=(screenWidth-flowLayout.getPaddingLeft()-flowLayout.getPaddingRight())/strArray.length;
                }else
                {
                    width=(screenWidth-flowLayout.getPaddingLeft()-flowLayout.getPaddingRight())/3;
                }
                int width2=0;
                if(strArray.length==1){
                    width2=(screenWidth-flowLayout.getPaddingLeft()-flowLayout.getPaddingRight())/2;
                }else{
                    width2=(screenWidth-flowLayout.getPaddingLeft()-flowLayout.getPaddingRight())/3;
                }
                for (int i = 0; i <strArray.length; i++)  {
                    if (i<3) {//只显示四张
                        final int index=i;
                        ImageView imageView = new ImageView(context);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, (int) (width2)));
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//将图片等比例缩放，让图像的短边与ImageView的边长度相同，即不能留有空白，缩放后截取中间部分进行显示。
                        imageView.setPadding(5,5,5,5);
                        flowLayout.addView(imageView);
                        Glide.with(AApplication.getInstance()).load(strArray[i]).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                    }
                }
            }
        }else{
            flowLayout.setVisibility(View.GONE);//防止复用导致的错乱
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(datas.get(position));
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_touxiang ;

        public TextView tv_sucai_lynum;
        public TextView tv_title;
        public FlowLayout flowLayout;

        public TextView tv_sucai_num;
        public ImageView img_sex;

        public TextView tv_sucai_dainzan;
        public TextView tv_name;
        public TextView tv_time;

        public ViewHolder(View view){
            super(view);
            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);

            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_sucai_num = (TextView) view.findViewById(R.id.tv_sucai_num);

            tv_sucai_lynum = (TextView) view.findViewById(R.id.tv_sucai_lynum);
            tv_sucai_dainzan = (TextView) view.findViewById(R.id.tv_sucai_dainzan);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

            tv_time = (TextView) view.findViewById(R.id.tv_time);

            img_sex = (ImageView) view.findViewById(R.id.img_sex);

            flowLayout= (FlowLayout)view.findViewById(R.id.flowLayout);
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
            mOnItemClickListener.onItemClick(view,(HuaTiMangeList.HuaTiMangeData)view.getTag());
        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, HuaTiMangeList.HuaTiMangeData data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
