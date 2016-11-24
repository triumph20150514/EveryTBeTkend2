package com.trimph.card;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CardPostAdapter cardPostAdapter;
    public List<Bean> beens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardPostView cardPostView = (CardPostView) findViewById(R.id.card_post);

        Bean bean;
        for (int i = 0; i < 10; i++) {
            bean = new Bean();
            bean.setImgUrl("gjdoi");
            bean.setTitle("fefefe");
            beens.add(bean);
        }

        cardPostAdapter = new CardPostAdapter(this);
        cardPostAdapter.setList(beens);
        cardPostView.setAdapter(cardPostAdapter);

    }
}
