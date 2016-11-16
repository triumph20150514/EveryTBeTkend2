package com.trimph.gravitydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView ivBg;

    private Matrix mMatrix;

    private SensorManager sensorManager;

    //以速度1向左移动
    private static final int LEFT_1 = 1;

    //以速度2向左移动
    private static final int LEFT_2 = 2;

    //以速度1向右移动
    private static final int RIGHT_1 = 3;


    //以速度2向右移动
    private static final int RIGHT_2 = 4;

    private int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivBg = (ImageView) findViewById(R.id.imageView);

        //导入assets文件夹中的文件并将其设置到背景图片控件上
        try {

            InputStream open = getResources().getAssets().open("a.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(open);
            ivBg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置图片的缩放模式和显示位置并设置
        mMatrix = new Matrix();
        mMatrix.postScale(1f, 1f, 1f, 1f);
        mMatrix.postTranslate(150, 0);
        ivBg.setImageMatrix(mMatrix);
        ivBg.invalidate();

        //为图片控件设置动画
        ivBg.startAnimation(getAnimation());

        //获取系统传感器服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //设置监听器监听加速度传感器（重力感应器）的状态，精度为普通（SENSOR_DELAY_NORMAL）
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

    }

    /**
     * 设置动画属性
     * 将时间设置稍长以避免动画执行完成后重复执行的时候的停顿现象
     *
     * @return
     */
    private Animation getAnimation() {
        MAnimation animation = new MAnimation();
        animation.setDuration(10000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        return animation;
    }

    /**
     * 自定义动画
     */
    public class MAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //运行方法，获取左上点的坐标，用于设置边界
            getLeftPointF();

            if (orientation == LEFT_1) {
                mMatrix.postTranslate(interpolatedTime * 2, 0);
            }
            if (orientation == RIGHT_1) {
                mMatrix.postTranslate(-interpolatedTime * 2, 0);
            }
            if (orientation == LEFT_2) {
                mMatrix.postTranslate(interpolatedTime * 5, 0);
            }
            if (orientation == RIGHT_2) {
                mMatrix.postTranslate(-interpolatedTime * 5, 0);
            }

            ivBg.setImageMatrix(mMatrix);
            ivBg.invalidate();
        }
    }

    /**
     * 传感器监听
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        //若传感器类型为加速度传感器（重力感应器）
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //获取X坐标
            int x = (int) event.values[SensorManager.DATA_X];

            Log.e("Position", x + "");

//            if (x == 0) orientation = RIGHT_1;//默认向左移动

            if (x < 2 && x > 0) orientation = RIGHT_1;

            if (x > 2) orientation = RIGHT_2;

            if (x < 0 && x > -2) orientation = LEFT_1;

            if (x < -2) orientation = LEFT_2;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 获取左上坐标，用于边界条件的判断
     *
     * @return
     */
    private PointF getLeftPointF() {

        float[] values = new float[9];
        float[] rightValues = {1.5f, 0, 960f, 0, 1.5f, -0.25f, 0, 0, 1.0f};
//        float[] rightValues = {1.5f, 0, -1080f, 0, 1.5f, -0.25f, 0, 0, 1.0f};
        float[] leftValues = {1.5f, 0, 0, 0, 1.5f, -0.25f, 0, 0, 1.0f};

        mMatrix.getValues(values);

        //若超出边界，为其设置自定义的位置
        if (values[2] < -960) {
            mMatrix.setValues(rightValues);
        }
        if (values[2] > 0) {
            mMatrix.setValues(leftValues);
        }

        float leftX = values[2];
        float leftY = values[5];
        Log.e("Position", "leftX" + leftX + ":leftY" + leftY);

        return new PointF(leftX, leftY);
    }
}
