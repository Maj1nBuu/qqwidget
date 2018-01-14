package com.ymnet.stickview18;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

/**
 * 作者： example
 * 时间：2017-02-08 11:09
 * 网址：http://www.example.com
 */

public class StickView extends View {

    private static final String TAG = "StickView";
    private Paint mPaint;
    private PointF mP1;
    private PointF mP2;
    private float mRadius2;
    private float mRadius1;
    private PointF mP1A;
    private PointF mP1B;
    private PointF mP2A;
    private PointF mP2B;
    private PointF mPc;
    private Path mPath;
    private final float maxDistance = 600;
    private final float maxRadius = 100;
    private final float minRadius = 40;
    private WindowManager mWindowManager;

    public StickView(Context context) {
        this(context,null);
    }

    public StickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public StickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        mP1 = new PointF(200,400);
        mP2 = new PointF(200,400);
        mRadius1 = 100f;
        mRadius2 = 100f;

        mP1A = new PointF(200,300);
        mP1B = new PointF(200,500);
        mP2A = new PointF(200,300);
        mP2B = new PointF(200,500);
        mPc = new PointF(200,400);

        //画笔的轨迹（路径）
        mPath = new Path();
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);//空心的
        canvas.drawCircle(mP1.x,mP1.y,maxDistance,mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        //根据两个点之间的距离[0,maxDistance]，修改mRadius1[100,20]
        final float distance = getDistance(mP1, mP2);
        if (distance>maxDistance){
            //只绘制第二个圆
            canvas.drawCircle(mP2.x,mP2.y,mRadius2,mPaint);
            return;
        }
        canvas.drawCircle(mP1.x,mP1.y,mRadius1,mPaint);
        canvas.drawCircle(mP2.x,mP2.y,mRadius2,mPaint);
        mRadius1 = maxRadius + (minRadius-maxRadius)*(distance/maxDistance);
        //重新计算未知点的坐标
        mPc = getMiddlePoint(mP1,mP2);
        computeTangencyPoints();
        //path是成员变量，path上面的轨迹线清空了
        mPath.reset();
        //轨迹移动的p1A位置
        mPath.moveTo(mP1A.x,mP1A.y);
        //绘制二阶贝塞尔曲线
        //第一个点是控制点，第二个是结束点
        mPath.quadTo(mPc.x,mPc.y,mP2A.x,mP2A.y);
        mPath.lineTo(mP2B.x,mP2B.y);
        mPath.quadTo(mPc.x,mPc.y,mP1B.x,mP1B.y);
        mPath.close();//让线条闭合
        //将path展示到canvas上
        canvas.drawPath(mPath,mPaint);
    }

    private void computeTangencyPoints() {
        //弧度制 计算两个圆的斜率
        double A = Math.atan((mP2.y-mP1.y)/(mP2.x-mP1.x));
        mP1A.x = (float) (mP1.x+mRadius1*Math.sin(A));
        mP1A.y = (float) (mP1.y-mRadius1*Math.cos(A));
        mP1B.x = (float) (mP1.x-mRadius1*Math.sin(A));
        mP1B.y = (float) (mP1.y+mRadius1*Math.cos(A));

        mP2A.x = (float) (mP2.x+mRadius2*Math.sin(A));
        mP2A.y = (float) (mP2.y-mRadius2*Math.cos(A));
        mP2B.x = (float) (mP2.x-mRadius2*Math.sin(A));
        mP2B.y = (float) (mP2.y+mRadius2*Math.cos(A));
    }

    /**
     *
     * @param p1
     * @param p2
     * @return 返回p1和p2的中心点
     */
    private PointF getMiddlePoint(PointF p1, PointF p2) {
        PointF pointF = new PointF((p1.x+p2.x)/2,(p1.y+p2.y)/2);
        return pointF;
    }
    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //计算原始圆和点击事件点的距离是否大于mRadius1
                float distance = getDistance(mP1,new PointF(event.getX(),event.getY()));
                if (distance>mRadius1){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //获取最新的坐标作为p2的坐标，然后重新绘制，重新计算未知点
                mP2.x = event.getX();
                mP2.y =event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                final float distance1 = getDistance(mP1, mP2);
                if (distance1>maxDistance){
                    //播放爆炸效果动画
                    playBoomAnimation();
                }else{
                    //回弹过去
                    final PointF startPoint = new PointF(event.getX(),event.getY());
                    final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration((long) distance1);
                    valueAnimator.setInterpolator(new OvershootInterpolator(3));
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float percent = (float) animation.getAnimatedValue();
                            //动画执行的百分比
                            //final float percent2 = animation.getAnimatedFraction();
                            //Log.d(TAG, "onAnimationUpdate: "+percent);
                            //根据动画执行的百分比计算mP2点的坐标
                            //0.5
                            //mP2.x = 起始x坐标 + （终止坐标-起始坐标）*percent;
                            //Log.d(TAG, "onAnimationUpdate:event.getRawY() "+event.getRawY());
                            //Log.d(TAG, "onAnimationUpdate:event.getY() "+event.getY());
                            mP2.x = startPoint.x + (mP1.x-startPoint.x)*percent;
                            mP2.y = startPoint.y +(mP1.y-startPoint.y)*percent;
                            //由于p2的位置不断的被改变，因此需要不断的重绘才能看到动画效果
                            invalidate();
                        }
                    });
                    valueAnimator.start();
                }

                break;
        }
        return true;
    }

    //播放爆炸效果
    private void playBoomAnimation() {

        final MyLayout myLayout = new MyLayout(getContext());
        //myLayout.setBackgroundColor(Color.GREEN);
        //将爆炸点的X和Y坐标传递给myLayout
        myLayout.setPosition(mP2.x,mP2.y);
        //使用WindowManager在窗口上动态添加一个布局，在布局中播放帧动画
        //当帧动画播放完之后，移除这个布局
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.boom_anim);
        ViewGroup.LayoutParams myLayoutlayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myLayout.addView(imageView,myLayoutlayoutParams);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //设置背景为透明
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        mWindowManager.addView(myLayout,layoutParams);
        //执行ImageView上的帧动画
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }
        animationDrawable.start();
        //animationDrawable.getDuration()
        int duration = 0;
        //获取帧动画总共的帧数
        final int numberOfFrames = animationDrawable.getNumberOfFrames();
        for (int i = 0; i < numberOfFrames; i++) {
            //获取 每个帧的时长，累加起来
            duration+=animationDrawable.getDuration(i);
        }
        //等duration毫秒之后移除imageView
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(myLayout);
                //将圆恢复到原来的位置
                mP2.x = mP1.x;
                mP2.y = mP1.y;
                invalidate();
            }
        },duration);


    }

    /**
     * 计算两个点之间的距离
     * @param p1
     * @param p2
     * @return
     */
    private float getDistance(PointF p1, PointF p2) {
       return (float) Math.sqrt(Math.pow(p2.x - p1.x,2)+Math.pow(p2.y-p1.y,2));
    }
}
