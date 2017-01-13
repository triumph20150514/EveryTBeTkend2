package com.trimph.everytbetkend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trimph.everytbetkend.MainActivity;
import com.trimph.everytbetkend.R;
import com.trimph.guagua.ScatchView;
import com.trimph.guagua.WaveView;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * author: Trimph
 * data: 2016/12/20.
 * description:
 */

public class ScratchActivity extends Activity {
    private ScatchView mScratchView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scratch_layout);
        initViews();

     /*   final WaveView waveView = (WaveView) findViewById(R.id.waveView);
        WaveSwipeRefreshLayout swipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.waveSwipeRefresh);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveView.animationDropCircle();

//                waveView.startDropAnimation();
            }
        });
        swipeRefreshLayout.setShadowRadius(20);
        swipeRefreshLayout.setMaxDropHeight(400);
//        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

*/
    }

    private void initViews() {
        mScratchView = (ScatchView) findViewById(R.id.scrachView);

        mScratchView.startAnim();

        mTextView = (TextView) findViewById(R.id.tv);
        mScratchView.setOnScratchListener(new ScatchView.OnScratchListener() {
            @Override
            public void onScratch() {
                //do your things here

                //如果希望onScratch只被调用一次
                //可在此处mScratchView.setOnScratchListener(null);
            }
        });
        mScratchView.setOnCompleteListener(new ScatchView.OnCompleteListener() {
            @Override
            public void complete() {
                //do your things here
                //设置刮刮卡不可见 否则下面的view无法获得touch事件
                mScratchView.setVisibility(View.GONE);
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ScratchActivity.this, mTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
