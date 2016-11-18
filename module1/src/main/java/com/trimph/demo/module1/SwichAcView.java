package com.trimph.demo.module1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

/**
 * author: Trimph
 * data: 2016/11/17.
 * description:
 */

public class SwichAcView extends FrameLayout implements View.OnClickListener {

    CardView cardView1, cardView2, cardView3;
    public int time1;    //时间计算
    public int dantance = 100; //间隔
    public int allTime = 4000;
    public int speed = allTime / dantance;

    public int screenWidth;
    public int screenHeight;
    public Context context;

    public SwichAcView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public SwichAcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwichAcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        screenWidth = ScreenUtil.getScreenWidth(context);
        screenHeight = ScreenUtil.getScreenHeight(context);
        View view = LayoutInflater.from(context).inflate(R.layout.swich_activity, null, false);
        cardView1 = (CardView) view.findViewById(R.id.first_cartView);
        cardView2 = (CardView) view.findViewById(R.id.two_cartView);
        cardView3 = (CardView) view.findViewById(R.id.three_cartView);
        this.addView(view);
        initListener();
    }

    private void initListener() {
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("CardView toWindow", cardView1.getHeight() + "");
        cardView1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardView1.getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("CardView:: tree", cardView1.getHeight() + "***");
                Log.e("CardView:: top2222", cardView1.getTop() + "***");
                chioseStartItem(1);   //初始化数据
                return true;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    float wScale, hScale;
    public int cardTop1, cardTop2, cardTop3;

    public void startAnim(final int position) {
        Log.e("first_cartView::", "startAnim--------------------------");
        cardView1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardView1.getViewTreeObserver().removeOnPreDrawListener(this);
                chioseStartItem(position);
                return true;
            }
        });
    }

    private void chioseStartItem(int position) {
        Log.e("first_cartView::", "startAnim1--------------------------");
        Log.e("CardView:: startAnim H", cardView1.getHeight() + "***");
        Log.e("CardView:: startAnim w", cardView1.getWidth() + "***");
        cardHeight = cardView1.getHeight();
        Log.e("CardView:: top", cardView1.getTop() + "***");
        Log.e("CardView:: screenWidth", screenWidth + "***");
        wScale = screenWidth % cardView1.getWidth() > 0 ? screenWidth / cardView1.getWidth() + 0.5f : screenWidth / cardView1.getWidth();

        card1 = new int[2];
        cardView1.getLocationOnScreen(card1);

        card2 = new int[2];
        cardView2.getLocationOnScreen(card2);

        card3 = new int[2];
        cardView3.getLocationOnScreen(card3);


        cardTop1 = (card1[1] + cardHeight) / 2000;
        cardTop1 = (screenHeight - card3[1]) / 2000;

//        startScale(cardView2, wScale, hScale);
//        startTranslation1(cardView1, -(card1[1] + cardHeight), 4700);
    }

    private void computeScale(int position) {
        switch (position) {
            case 1:
                //初始化缩放大小
                if ((card1[1] + cardHeight / 2) - (screenHeight / 2) < 0) {
                    if ((screenHeight / 2) % cardHeight > 0) {//计算扩大倍数
                        hScale = screenHeight / cardHeight + 2.2f;
                    } else {
                        hScale = screenHeight / cardHeight + 1;
                    }
                } else {
                    hScale = screenHeight / cardHeight + 0.5f;
                }
                break;
            case 2:
                //初始化缩放大小
                if ((card2[1] + cardHeight / 2) - (screenHeight / 2) < 0) {
                    if ((screenHeight / 2) % cardHeight > 0) {//计算扩大倍数
                        hScale = screenHeight / cardHeight + 0.5f;
                    } else {
                        hScale = screenHeight / cardHeight;
                    }
                } else {
                    hScale = screenHeight / cardHeight + 0.5f;
                }
                break;
            case 3:
                //初始化缩放大小
                if ((card1[1] + cardHeight / 2) - (screenHeight / 2) < 0) {
                    if ((screenHeight / 2) % cardHeight > 0) {//计算扩大倍数
                        hScale = screenHeight / cardHeight + 2.2f;
                    } else {
                        hScale = screenHeight / cardHeight + 1;
                    }
                } else {
                    hScale = screenHeight / cardHeight + 0.5f;
                }
                break;
        }
    }

    private void switchAnim(int position) {
        switch (position) {
            case 1:
                //ji suan shi jian
                computeScale(position);
                startScale(cardView1, wScale, hScale);
                startTranslation1(cardView2, (screenHeight - card2[1]), 4700);
                Log.e("Time", "sudu::" + ((screenHeight - card2[1]) / 47) + "");
                Log.e("Time", "sudu2::" + (screenHeight - (card3[1])) / ((screenHeight - card2[1]) / 47) + "");
                startTranslation2(cardView3, screenHeight - (card3[1]), (screenHeight - (card3[1])) / ((screenHeight - card2[1]) / 47) * 100);
                break;
            case 2:
                //ji suan shi jian
                Log.e("Time", "sudu:: 2222::::" + ((screenHeight - card2[1]) / 47) + "");
                computeScale(position);
                startScale(cardView2, wScale, hScale);
                //通过缩放的时间计算移动的时间
                startTranslation1(cardView1, -(card1[1] + cardHeight), 4900);
                startTranslation2(cardView3, screenHeight - (card3[1]), (screenHeight - (card3[1])) / ((cardHeight + card1[1]) / 49) * 100);
                break;
            case 3:
                //ji suan shi jians
                computeScale(position);
                startScale(cardView3, wScale, hScale);
                startTranslation1(cardView2, -(card2[1] + cardHeight), 5500);
                startTranslation2(cardView1, -(cardHeight + (card2[1])),
                        (cardHeight + (card2[1])) / ((cardHeight + card2[1]) / 55) * 100);
                break;
        }
    }

    /**
     * 扩散动画
     *
     * @param view
     * @param wScale
     * @param hScale
     */
    public void startScale(CardView view, float wScale, float hScale) {
        Log.e("CardView::startScale", " wScale::" + wScale + " hScale::" + hScale + "***");
        view.animate().scaleX(wScale).scaleY(hScale).setDuration(5000).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                Intent intent = new Intent(context, NextActivity.class);
//                context.startActivity(intent);
//                ((MainActivity) context).overridePendingTransition(0, 0);

                if (jumpNextActivity != null) {
                    jumpNextActivity.jump();
                }
            }
        }).start();
    }

    public JumpNextActivity jumpNextActivity;

    public void setJumpNextActivity(JumpNextActivity jumpNextActivity) {
        this.jumpNextActivity = jumpNextActivity;
    }

    public interface JumpNextActivity {
        public void jump();
    }

    private void back(CardView view, float wScale, float hScale, int time1) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleY", hScale, 1f);
        objectAnimator.setDuration(time1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleX", wScale, 1f);
        objectAnimator2.setDuration(time1);
        objectAnimator2.setInterpolator(new LinearInterpolator());
        objectAnimator2.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimator, objectAnimator2);
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(time1);
        set.start();
    }

    public int creentPosition = -1;
    public Handler handler = new Handler();

    public void backBefore() {
        Log.e("back Card2:", card2[1] + "");
        Log.e("back Card3:", card3[1] + "");
        Log.e("back Card3:*********", (card2[1] + cardHeight) + "");
        switch (creentPosition) {
            case 1:
                back(cardView1, wScale, hScale, 5000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackTranslation1(cardView2, 4500);
                    }
                }, 500);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackTranslation1(cardView3, 4000);
                    }
                }, 500);
                break;
            case 2:
                back(cardView2, wScale, hScale, 5000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackTranslation1(cardView1, 4500);
                        BackTranslation1(cardView3, 5000);
                    }
                }, 400);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackTranslation1(cardView3, 5000);
                    }
                }, 800);
                break;
            case 3:
//                back(cardView3, wScale, hScale, 5000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        BackTranslation1(cardView2, 5000);
                        back(cardView3, wScale, hScale, 4700);
                    }
                }, 300);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        BackTranslation1(cardView2, 5000);
                        BackTranslation1(cardView1, 4500);
                    }
                }, 200);


                BackTranslation1(cardView2, 5000);
                BackTranslation1(cardView1, 4500);
                break;
        }
    }

    private void BackTranslation1(CardView cardView2, int time) {
        cardView2.animate().translationY(0).setInterpolator(new LinearInterpolator()).setDuration(time).start();
    }

    public int[] card1, card2, card3;
    public int cardHeight;

    /**
     * 位移动画
     *
     * @param view
     * @param yDentance
     */
    public void startTranslation1(View view, float yDentance, int time) {
        view.animate().translationY(yDentance).setInterpolator(new LinearInterpolator()).setDuration(time).start();
    }

    public void startTranslation2(View view, float yDentance, int time) {
        view.animate().translationY(yDentance).setInterpolator(new LinearInterpolator()).setDuration(time).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_cartView:
                Log.e("first_cartView::", "--------------------------");
                creentPosition = 1;
                switchAnim(creentPosition);
                break;
            case R.id.two_cartView:
                Log.e("two_cartView::", "--------------------------");
                creentPosition = 2;
                switchAnim(creentPosition);
                break;
            case R.id.three_cartView:
                creentPosition = 3;
                switchAnim(creentPosition);

                break;
        }
    }
}
