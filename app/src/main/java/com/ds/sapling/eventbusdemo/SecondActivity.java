package com.ds.sapling.eventbusdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        EventBus.getInstance().register(this);
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
            }
        });

        findViewById(R.id.postGoods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsBean goodsBean = new GoodsBean("apple");
                EventBus.getInstance().post(goodsBean);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getInstance().unRegister(this);
    }

    public void postEvent(){
        UserBean userBean = new UserBean("hello cral",22);
        EventBus.getInstance().post(userBean);
    }
}
