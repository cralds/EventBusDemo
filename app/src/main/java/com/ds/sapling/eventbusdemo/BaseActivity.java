package com.ds.sapling.eventbusdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Subcribe(value = ThreadMode.io)
    public void getGoods(GoodsBean goodsBean){
        Log.d("BaseActivity","goodsname===>"+goodsBean.goodsName+"====thread id ===>"+Thread.currentThread().getId());
    }
}
