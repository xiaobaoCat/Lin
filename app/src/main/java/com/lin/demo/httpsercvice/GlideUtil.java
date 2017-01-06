package com.lin.demo.httpsercvice;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lin.demo.AApplication;

import java.io.File;

public class GlideUtil {

    public static void displayImage(Context mContext,String path, ImageView imageView) {
        Glide.with(mContext)  //配置上下文
                .load(path)  //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(R.drawable.app)           //设置错误图片
//                .placeholder(R.drawable.app)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .into(imageView);
    }
}
