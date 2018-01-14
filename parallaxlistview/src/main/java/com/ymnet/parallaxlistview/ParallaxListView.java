package com.ymnet.parallaxlistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 作者： example
 * 时间：2017-02-08 09:10
 * 网址：http://www.example.com
 */

public class ParallaxListView extends ListView {

    private static final String TAG = "ParallaxListView";
    private View mHeaderView;
    private ImageView mImageView;
    private int mOriginalHeight;

    public ParallaxListView(Context context) {
        this(context,null);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void addHeaderView(int resId) {
        mHeaderView = LayoutInflater.from(getContext()).inflate(resId, this, false);
        mImageView = (ImageView) mHeaderView.findViewById(R.id.imageView);
        mOriginalHeight = mImageView.getLayoutParams().height;

        //由于调用该方法的时候还没有对ImageView进行测量，因此在这里调用该方法返回的是0
        //final int measuredHeight = mHeaderView.getMeasuredHeight();
        //给ImageView添加布局监听器，当视图树布局好后被回调
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //第一次测量出来的高度，就是原始高度
                final int measuredHeight1 = mImageView.getMeasuredHeight();
                Log.d(TAG, "onGlobalLayout: "+measuredHeight1);
                //移除监听器
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //获取到的是ImageView上的图片转换为的Bitmap的高度
        final int intrinsicHeight = mImageView.getDrawable().getIntrinsicHeight();
        Log.d(TAG, "intrinsicHeight: "+intrinsicHeight);  //583(xxhdpi) 1167(hdpi) 420
        Log.d(TAG, "addHeaderView: "+mOriginalHeight);
        //将头布局添加给ListView
        addHeaderView(mHeaderView);
    }

    /**
     *  在Android中可以滚动的View，如果滚动到头了，还继续让他滚动，则该方法会被回调
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //Log.d(TAG, "overScrollBy: deltaY="+deltaY+"\nscrollY="+scrollY+"\nscrollRangeY="+scrollRangeY+"\nmaxOverScrollY="+maxOverScrollY+"\nisTouchEvent="+isTouchEvent);
        if (isTouchEvent&&deltaY<0){//如果是手动往下拉
            //将deltaY累加到ImageView的布局高度上，让后将新的布局参数重新设置给ImageView
            final ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
            Log.d(TAG, "overScrollBy: "+layoutParams.height);
            layoutParams.height = layoutParams.height - deltaY;
            //限制最大的高度等于原始高度的倍数（2倍）
            if (layoutParams.height>2*mOriginalHeight){
                layoutParams.height = 2*mOriginalHeight;
            }
            mImageView.setLayoutParams(layoutParams);
        }
        //自己处理过度滚动事件
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_UP){
            //让ImageView的高度从当前的高度，复原到初始的高度
            int startHeight = mImageView.getLayoutParams().height;
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(startHeight, mOriginalHeight).setDuration(1000);
            valueAnimator.setInterpolator(new OvershootInterpolator(3));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float newHeight = (float) animation.getAnimatedValue();
                    final ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
                    layoutParams.height = (int) newHeight;
                    mImageView.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.start();
        }
        return super.onTouchEvent(ev);
    }
}
