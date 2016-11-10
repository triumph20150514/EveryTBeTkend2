package com.trimph.everything.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.trimph.everything.R;
import com.trimph.everything.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
