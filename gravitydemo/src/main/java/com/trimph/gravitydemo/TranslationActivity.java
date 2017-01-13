package com.trimph.gravitydemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * author: Trimph
 * data: 2016/12/5.
 * description:
 */

public class TranslationActivity extends Activity {

    protected static int GRAIEY_STATE = -1;
    public final static int VERTICALITY_STATE = 0;
    public final static int HORIZONTAL_STATE = 1;
    public final static int LEFT_STATE = 2;
    public final static int RIGHT_STATE = 3;
    public final static int TOP_STATE = 4;
    public final static int BOTOOM_STATE = 5;
    public int max = 100;
    public int x, y;    //重力感应下
    TextView textView;
    LinearLayout ll_content;

    SensorManager sensorManager;
    Button button;
    public SensorTransListener sensorTransListener;
    private Object typeValue;
    public int duration = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation_layout);

        textView = (TextView) findViewById(R.id.tv);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        button = (Button) findViewById(R.id.bt);
        linearInterpolator = new LinearInterpolator();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        sensorTransListener = new SensorTransListener();
        /**
         * 传感器获取
         */
        //获取系统传感器服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //设置监听器监听加速度传感器（重力感应器）的状态，精度为普通（SENSOR_DELAY_NORMAL） //TYPE_ACCELEROMETER
        //陀螺仪TYPE_GYROSCOPE  SENSOR_DELAY_NORMAL
        sensorManager.registerListener(sensorTransListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

//    public float getTypeValue() {
//        return (max-10)* getWindow()+10;
//    }

    public void setTypeValue(Object typeValue) {
        this.typeValue = typeValue;
    }


    /**
     * 监听器
     */
    public class SensorTransListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {



            //若传感器类型为加速度传感器（重力感应器）
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //获取X坐标  左边倾斜为正   值越大越倾斜  X Y最大值都为8
                x = (int) event.values[SensorManager.DATA_X];
                y = (int) event.values[SensorManager.DATA_Y];
                Log.e("Sensor ::x:", x + "");
                Log.e("Sensor ::y:", y + "");
                ///     8  0  -8
                //
//                ll_content.setTranslationX(-x * max);
//                ll_content.setTranslationY(y * max);
                if (x > 0 || x <= 6) {
                    startTrans();
                    leftTranslate();
                    GRAIEY_STATE = LEFT_STATE;
                } else if (x < 0) {
                    GRAIEY_STATE = RIGHT_STATE;
                } else if (x == 0) {
                    GRAIEY_STATE = HORIZONTAL_STATE;
                } else if (x > y & x != 0 & y != 0) {
                    if (x > 0) {
                        GRAIEY_STATE = LEFT_STATE;
                    } else if (x < 0) {
                        GRAIEY_STATE = RIGHT_STATE;
                    }
                } else if (y > x & x != 0 & y != 0) {
                    if (y > 0) {
                        GRAIEY_STATE = LEFT_STATE;
                    } else if (y < 0) {
                        GRAIEY_STATE = RIGHT_STATE;
                    }
                }

//                startTrans();
            /*
                Log.e("Sensor ::", x + "");
                if (x >= 1) {
                    if (GRAIEY_STATE != LEFT_STATE) {
                        //方向切換后馬上改變方向
                        if (valueAnimator.isRunning()) {
                            valueAnimator.cancel();
                        }
                        GRAIEY_STATE = LEFT_STATE; //左移
                        //判断动画结束的情况下还在倾斜时
                        startAnim(-50.0f, 20);
                    }

                } else if (x <= -1) {
                    GRAIEY_STATE = RIGHT_STATE; //右移
                    startAnim(50.0f, 20);
                } else if (x > -0.5 || x < 0.5) {
                    GRAIEY_STATE = STANDARE_STATE;//水平状态
                    startAnim(0, 20);
                }
*/
            }


        }


        /**
         * 左移动
         */
        private void leftTranslate() {
            MyType myType = new MyType();
//            myType.evaluate()

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public float fraction;
    ValueAnimator animator;
    public int curentX, curentY;
    public int maxX, maxYY;
    public LinearInterpolator linearInterpolator;

    /**
     *
     */
    public class MyType implements TypeEvaluator<Float> {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return (endValue - startValue) * fraction + startValue;
        }
    }

    /**
     * 启动动画
     */
    public void startTrans() {

        if (animator != null) {
//            animator.cancel();
        }

        switch (GRAIEY_STATE) {
            case LEFT_STATE:
                maxX = max * x;
                break;
            case RIGHT_STATE:
                maxYY = max * y;
                break;
            case TOP_STATE:

                break;
            case BOTOOM_STATE:

                break;
        }
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (float) animation.getAnimatedFraction();
                Log.e("Translation", fraction + "");
                Log.e("TranslationX", fraction * maxX + "");
                Log.e("TranslationY", fraction * maxYY + "");   //慢慢变大
                //根据当前状态判断    若反方向
                switch (GRAIEY_STATE) {
                    case LEFT_STATE:
                        ll_content.setTranslationX(-fraction * maxX);
//                        ll_content.setTranslationY(fraction * maxYY);
                        break;
                    case RIGHT_STATE:
                        ll_content.setTranslationX(fraction * maxX);
//                        ll_content.setTranslationY(fraction * maxYY);
                        break;
                    case TOP_STATE:
                        ll_content.setTranslationX(-fraction * maxX);
                        ll_content.setTranslationY(fraction * maxYY);
                        break;
                    case BOTOOM_STATE:
                        ll_content.setTranslationX(-fraction * maxX);
                        ll_content.setTranslationY(fraction * maxYY);
                        break;
                }


            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public float xFraction;
    public float oldEndX;
    public ValueAnimator valueAnimator = null;

    public boolean isStart = false;
    public float equsFloat = -1; //比较两次传入X值是否一致 ，若相同直接返回

    public void startAnim(float endX, int tvEndX) {
        if (isStart == true) {
            return;
        }
        if (equsFloat == endX) {
//            Log.e("Eques", "还是同一个方向倾斜");
            return;
        } else {
            equsFloat = endX;
        }
        Log.e("Sensor:", "startAnim:");
        isStart = true;

        if (endX != 0) {
            if (oldEndX != 0) {
                valueAnimator = ValueAnimator.ofFloat(oldEndX, endX); //移动}
                oldEndX = endX;
            } else {
                valueAnimator = ValueAnimator.ofFloat(0f, endX); //移动}
                oldEndX = endX;
            }
        } else if (endX == 0) {
            Log.e("Eques", "等于0的时候" + oldEndX);
            valueAnimator = ValueAnimator.ofFloat(oldEndX, endX);  //回到开始的位置
        }
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
               /* if (STANDARE_STATE == GRAIEY_STATE) {
                    oldEndX = 0;
                }*/
                isStart = false; // 可以继续下一个动画
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //可以实时弄这个值吧
                xFraction = (float) animation.getAnimatedValue();
                Log.e("Sensor:", "fraction:" + xFraction);
                //重绘
                ll_content.setTranslationX(xFraction);
                ll_content.postInvalidate();
            }
        });

        valueAnimator.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorTransListener != null && sensorManager != null) {
            sensorManager.unregisterListener(sensorTransListener);
        }
    }
}
