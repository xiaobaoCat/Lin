package com.lin.demo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lin.demo.R;
import com.lin.demo.interfaces.OnTabReselectListener;
import com.lin.demo.ui.fragment.BaseFragment;
import com.lin.demo.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTab4 extends BaseFragment implements OnTabReselectListener {


    public FragmentTab4() {
        // Required empty public constructor
    }

    @Override
    public void onTabReselect() {
        ToastUtil.showToast(mContext,"我");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        if (view == null) {
            view=inflater.inflate(R.layout.fragment4, container, false);
        }

        // 缓存view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

}
