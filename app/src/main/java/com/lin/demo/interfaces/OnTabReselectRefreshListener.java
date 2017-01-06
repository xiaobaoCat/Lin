package com.lin.demo.interfaces;

/**
 * 用于监听Tab再一次被点击时刷新嵌套在Tab的tab的数据
 */
public interface OnTabReselectRefreshListener {
    void OnTabReselectRefresh(int position);
}
