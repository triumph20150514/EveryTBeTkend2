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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.img);
        imageView.getHeight();
        imageView.getWidth();

        Date date = new Date();
        date.getTime();

        Log.e("Data", date.getTime() + "");
        Log.e("Data", System.currentTimeMillis() + "");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);


        Log.e("Data str", str + "");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
        System.out.println(df.format(new Date()));
        Toast.makeText(this, "Data:" + df.format(new Date()), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Data String:" + DataString.StringData(), Toast.LENGTH_SHORT).show();
        Log.e("Data str CHINA", df.format(new Date()) + "");
        Log.e("Data str DataString", DataString.StringData() + "");

//        Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料
//        t.setToNow(); // 取得系统时间。
//        int year = t.year;
//        int month = t.month;
//        int date = t.monthDay;
//        int hour = t.hour;    // 0-23


        ivBg = (ImageView) findViewById(R.id.imageView);

        //导入assets文件夹中的文件并将其设置到背景图片控件上
        try {
            InputStream open = getResources().getAssets().open("bg_1.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(open);
            ivBg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置图片的缩放模式和显示位置并设置
        mMatrix = new Matrix();
        mMatrix.postScale(1.5f, 1.5f, 0.5f, 0.5f);
        mMatrix.postTranslate(-20, 0);
        ivBg.setImageMatrix(mMatrix);

        ivBg.invalidate();

        //为图片控件设置动画
        ivBg.startAnimation(getAnimation());

        //获取系统传感器服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //设置监听器监听加速度传感器（重力感应器）的状态，精度为普通（SENSOR_DELAY_NORMAL）
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


    }

    private void start() {
        Log.e("Image", imageView.getWidth() + " heigth:" + imageView.getHeight() + " left:" + imageView.getLeft() + " right" + imageView.getTop());
        final Rotate3DAnimator rotate3DAnimator = new Rotate3DAnimator(this, 0, 5,
                imageView.getWidth() / 2, imageView.getHeight() / 2, -15, false);
        rotate3DAnimator.setDuration(3000);                         //设置动画时长
        rotate3DAnimator.setFillAfter(true);                        //保持旋转后效果
        rotate3DAnimator.setInterpolator(new AnticipateOvershootInterpolator()); //设置插值器
        imageView.startAnimation(rotate3DAnimator);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 计算中心点（这里是使用view的中心作为旋转的中心点）
                v.startAnimation(rotate3DAnimator);
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("imageView", imageView.getWidth() + " Attached::");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("imageView", imageView.getWidth() + " FocusChanged::");

        start();

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

//            Sensor.TYPE_GRAVITY
            //获取X坐标
            int x = (int) event.values[SensorManager.DATA_X];

            if (x == 0) orientation = RIGHT_1;//默认向左移动


            if (x < 2 && x > 0)
                start();
//                orientation = RIGHT_1;

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
        float[] rightValues = {1.5f, 0, -1080f, 0, 1.5f, -0.25f, 0, 0, 1.0f};
        float[] leftValues = {1.5f, 0, 0, 0, 1.5f, -0.25f, 0, 0, 1.0f};

        mMatrix.getValues(values);

        //若超出边界，为其设置自定义的位置
        if (values[2] < -1080) {
            mMatrix.setValues(rightValues);
        }
        if (values[2] > 0) {
            mMatrix.setValues(leftValues);
        }

        float leftX = values[2];
        float leftY = values[5];
        return new PointF(leftX, leftY);
    }
}
