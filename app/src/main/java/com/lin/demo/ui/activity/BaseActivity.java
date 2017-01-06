package com.lin.demo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lin.demo.R;

public class BaseActivity extends AppCompatActivity {
    private BaseActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


    }
}
