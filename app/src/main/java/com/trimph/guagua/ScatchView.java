package com.trimph.guagua;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.trimph.everytbetkend.R;

/**
 * author:
 * data: 2016/12/20.
 * description:
 */

public class ScatchView extends View {

    private static Bitmap mOutterBitmap;
    private static Bitmap mBitmap;
    public final int DEFAULT_SWIPE_PAINT_WIDTH = 40;
    public final int DEFAULT_SWIPE_COMPLETE_PERCENTAGE = 70;
    private final int mOutterBgId;
    private Paint mOutterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private int mLastX;
    private int mLastY;
    private Paint mOutBmpPaint;
    private int mSwipePaintWidth;
    private int mSwipeCompletePercentage;
    private OnCompleteListener mOnCompleteListener;
    private OnScratchListener mOnScratchListener;
    private int mWidth;
    private int mHeight;
    //判断遮盖层区域是否达到消除的比例
    private volatile boolean mCompleted = false;
    /**
     * 起一个线程来计算已经扫的面积及占总区域的比例
     * 根据区域来判断是否完成
     */
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;

            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w * h];
            //获取bitmap的所有像素信息
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            for (int i = 0; i < w; i++)
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (mPixels[index] == 0) {
                        wipeArea++;
                    }
                }
            //计算已扫区域所占的比例
            if (wipeArea > 0 && totalArea > 0) {
                int percent = (int) (wipeArea * 100 / totalArea);
                if (percent > mSwipeCompletePercentage) {
                    //清除图层区域
                    mCompleted = true;
                    postInvalidate();
                }
            }
        }
    };
    private Bitmap bitmap;

    public ScatchView(Context context) {
        this(context, null);
    }

    public ScatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.scratchView);
        mOutterBgId = typedArray.getResourceId(R.styleable.scratchView_outter_bg, 0);
        init();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = (int) Math.ceil(height / reqHeight);
            } else {
                inSampleSize = (int) Math.ceil(width / reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 设置刮卡完成比例
     *
     * @param per
     */
    public void setCompletePercentage(int per) {
        this.mSwipeCompletePercentage = per;
    }

    /**
     * 设置刮卡完成监听
     *
     * @param mOnCompleteListener
     */
    public void setOnCompleteListener(OnCompleteListener mOnCompleteListener) {
        this.mOnCompleteListener = mOnCompleteListener;
    }

    /**
     * 设置刮卡开始监听
     *
     * @param mOnScratchListener
     */
    public void setOnScratchListener(OnScratchListener mOnScratchListener) {
        this.mOnScratchListener = mOnScratchListener;
    }

    /**
     * 进行初始化操作
     */
    private void init() {
        mOutterPaint = new Paint();
        mOutBmpPaint = new Paint();
        mPath = new Path();
        mSwipePaintWidth = DEFAULT_SWIPE_PAINT_WIDTH;
        mSwipeCompletePercentage = DEFAULT_SWIPE_COMPLETE_PERCENTAGE;

        float wid = mOutterPaint.getStrokeWidth();
        startY = wid / 2;
    }

    public float fraction;
    int startX;
    float startY;

    ValueAnimator valueAnimator;

    public boolean isFirst = false;

    /**
     * 自动绘制路径
     */
    public void startAnim() {

        float wid = mOutterPaint.getStrokeWidth();

        if (!isFirst) {
            startY = 20;
            isFirst = true;
        }

        mPath.moveTo(0, startY);


        Log.e("StartAnim", mWidth + " mHeight:" + mHeight);
        Log.e("StartAnim", " startY:" + startY);
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();
                float result = fraction * mWidth;
                Log.e("resutl", result + "");
                if (!mCompleted) {
                    mPath.lineTo(result, startY);
                    invalidate();
                    post(mTask);
                } else {
                    valueAnimator.cancel();
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCompleted) {
                    startY = startY + 40;
                    startAnim();
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        drawView();
    }

    private void drawView() {
        if (mOutterBitmap == null)
            mOutterBitmap = decodeSampledBitmapFromResource(getResources(), mOutterBgId, mWidth, mHeight);
        //初始化bitmap
        if (mBitmap == null)
            mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        //初始化canvas
        if (mCanvas == null)
            mCanvas = new Canvas(mBitmap);
        //设置画笔的一些属性
        setOutterPaint();
        setOutBmpPaint();
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        mCanvas.drawBitmap(mOutterBitmap, null, rect, mOutBmpPaint);
    }

    /**
     * 设置刮扫画笔的属性
     */
    private void setOutterPaint() {
        mOutterPaint.setColor(Color.parseColor("#c3c3c3"));
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);//设置圆角
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeWidth(mSwipePaintWidth);
    }

    /**
     * 设置绘制刮刮卡圆角背景的画笔属性
     */
    private void setOutBmpPaint() {
        mOutBmpPaint.setColor(Color.parseColor("#c3c3c3"));
        mOutBmpPaint.setAntiAlias(true);
        mOutBmpPaint.setDither(true);
        mOutBmpPaint.setStrokeJoin(Paint.Join.ROUND);//设置圆角
        mOutBmpPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutBmpPaint.setStyle(Paint.Style.FILL);
        mOutBmpPaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //刮扫完成回调
        if (mCompleted) {
            if (null != mOnCompleteListener) {
                mOnCompleteListener.complete();
            }
        }
        //判断是否完成，如果完成了就不绘制遮盖层
        if (!mCompleted) {
            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 设置Xfermode模式为DST_OUT，并绘制扫的路径
     */
    private void drawPath() {
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);
                if (dx > 3 || dy > 3) {
                    mPath.lineTo(x, y);
                    if (null != mOnScratchListener)
                        mOnScratchListener.onScratch();
                }
                mLastX = x;
                mLastY = y;
                post(mTask);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    public void recycle() {
        if (mBitmap != null)
            mBitmap.recycle();
        if (mOutterBitmap != null)
            mOutterBitmap.recycle();
    }

    /**
     * 刮刮卡开始刮时回调
     */
    public interface OnScratchListener {
        void onScratch();
    }

    /**
     * 刮刮卡刮完之后的回调接口
     */
    public interface OnCompleteListener {
        void complete();
    }
}
