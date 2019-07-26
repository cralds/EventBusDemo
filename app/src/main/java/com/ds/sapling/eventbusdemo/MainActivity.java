package com.ds.sapling.eventbusdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    TextView tvHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getInstance().register(this);
        tvHello = findViewById(R.id.hello);
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unRegister(this);
    }

    @Subcribe(value = ThreadMode.main)
    public void getUser(UserBean userBean){
        Toast.makeText(this,"name==="+userBean.toString(),Toast.LENGTH_LONG).show();
        tvHello.setText(userBean.name + userBean.age+Thread.currentThread().getId());
    }
}
