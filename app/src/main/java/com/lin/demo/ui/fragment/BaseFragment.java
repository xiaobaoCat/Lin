package com.lin.demo.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lin.demo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    public Context mContext;
    public View view;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //避免FragmentTabHost切换fragment时会调用onCreateView()重绘UI。
//        if (view == null) {
//
//
//
//        }else{
//            // 缓存view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//        }
        return view;
    }
}
