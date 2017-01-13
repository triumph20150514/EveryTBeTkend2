package com.trimph.guagua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: Trimph
 * data: 2016/12/20.
 * description:
 */

public class MyScratchView extends View {

    public Paint paint;
    public Path path;

    public MyScratchView(Context context) {
        super(context);
    }

    public MyScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        paint=new Paint();
        paint.setColor(Color.parseColor("#847CC2"));






    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);





    }
}
