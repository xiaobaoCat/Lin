package com.lin.demo.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lin.demo.R;
import com.lin.demo.interfaces.OnTabReselectListener;
import com.lin.demo.interfaces.OnTabReselectRefreshListener;
import com.lin.demo.ui.fragment.BaseFragment;
import com.lin.demo.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTab2 extends BaseFragment implements OnTabReselectListener{

    private String[] tabs = new String[]{"12/25", "12/26", "12/27","12/28", "12/29", "12/30"};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;

    public FragmentTab2() {
    }

    @Override
    public void onTabReselect() {
        ToastUtil.showToast(mContext,"--"+onTabReselectRefreshListener);
        if (onTabReselectRefreshListener!=null){
            onTabReselectRefreshListener.OnTabReselectRefresh(tabLayout.getSelectedTabPosition());
        }
    }
    public static OnTabReselectRefreshListener onTabReselectRefreshListener;
    public static void getOnTabReselectRefreshListener(OnTabReselectRefreshListener listener){
        onTabReselectRefreshListener=listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        if (view == null) {
            view=inflater.inflate(R.layout.fragment2, container, false);
            initTabLayout();
        }

        // 缓存view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initTabLayout(){
        //Fragment+ViewPager+FragmentViewPager组合的使用
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                //设置Tab标题
                return tabs[position];
            }
            @Override
            public Fragment getItem(int position) {
                //设置Tab内容
                FragmentTab2Detail fragmentTab2Detail=	new FragmentTab2Detail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("date", tabs[position]);
                fragmentTab2Detail.setArguments(bundle);
                return fragmentTab2Detail;
            }
            @Override
            public int getCount() {
                //设置Tab个数
                return tabs.length;
            }
        };
        viewPager.setAdapter(adapter);
        //TabLayout
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

