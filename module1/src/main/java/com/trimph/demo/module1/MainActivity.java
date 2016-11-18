package com.trimph.demo.module1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3;

    SwichAcView swichAcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swichAcView = (SwichAcView) findViewById(R.id.Swich_view);

        swichAcView.setJumpNextActivity(new SwichAcView.JumpNextActivity() {
            @Override
            public void jump() {
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            swichAcView.backBefore();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
