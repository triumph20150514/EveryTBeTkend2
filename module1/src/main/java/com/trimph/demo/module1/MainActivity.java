package com.trimph.demo.module1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3;

    SwichAcView swichAcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.tv);
        TextView textView2 = (TextView) findViewById(R.id.tv2);
        TextView textView3 = (TextView) findViewById(R.id.tv3);

//        FontUtils.setFont(this, textView);
        FontUtils.setFont(this, textView3);

      /*  swichAcView = (SwichAcView) findViewById(R.id.Swich_view);

        swichAcView.setJumpNextActivity(new SwichAcView.JumpNextActivity() {
            @Override
            public void jump() {
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
