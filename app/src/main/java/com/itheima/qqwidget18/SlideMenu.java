package com.itheima.qqwidget18;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者： itheima
 * 时间：2017-02-07 09:20
 * 网址：http://www.itheima.com
 */

public class SlideMenu extends FrameLayout {

    private static final String TAG = "SlideMenu";
    private static final float MULTIPLE = 0.65f;
    private ViewDragHelper mViewDragHelper;
    private View mMenuView;
    private View mMainView;
    private int mMainHeight;
    private int mMainWidth;
    private int mMaxRange;
    private int mMenuHeight;
    private int mMenuWidth;

    public SlideMenu(Context context) {
        this(context,null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }
     /**
     59.         * ViewDragHelper类的介绍:
     60.         *
     61.         * 谷歌在2013年I/O开发者大会上提出;
     62.         * 专门用于在ViewGroup中对子View进行拖拽处理;
     63.         * 在19(Android4.4)以及以上的v4包中;
     64.         * 本质是封装了对触摸事件的解析，包括触摸位置，触摸速度以及Scroller的封装，
     65.只需要我们在回调方法中指定是否移动，移动多少等等，但是需要注意的是：
     66.         * 它只是一个触摸事件的解析类(如GestureDecetor)，所以需要我们传递给它触摸事件，它才能工作;
     67.         */
    private void init() {
        /**
         * 参数1：因为ViewDragHelper是帮助ViewGroup拖拽子控件的，因此第一个参数就是这个ViewGroup
         */
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             *
             * @param child 点击到的View，询问是否要捕获这个View
             * @param pointerId 手指头的id 从0开始的
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            /**
             * 用于确定被捕获的View水平方向移动的距离
             * @param child
             * @param left 要水平移动往右移动的距离，往右>0,往左<0
             * @param dx 每次移动的偏移量
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //Log.d(TAG, "clampViewPositionHorizontal: left="+left+"/dx="+dx);
                //if (left<0){
                //    return 0;
                //}else if (left>200){
                //    return 200;
                //}
                //if (child==mMenuView){
                //    return 0;
                //}
                return left;
            }

            //@Override
            //public int clampViewPositionVertical(View child, int top, int dy) {
            //    return top;
            //}

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d(TAG, "onViewPositionChanged: changedView="+changedView+"/left="+left+"/dx="+dx);
                if (changedView==mMainView){
                    //限制mainView的范围[0,mMainWidth*0.85]
                    if (left<0){
                        mMainView.layout(0,0,mMainWidth,mMainHeight);
                    }else if (left>mMaxRange){
                        mMainView.layout(mMaxRange,0,mMaxRange+mMainWidth,mMainHeight);
                    }

                }else if (changedView==mMenuView){
                    mMenuView.layout(0,0,mMenuWidth,mMenuHeight);
                    //让MainView去移动
                    //获取到menuView的右侧的偏移量加给MainView作为MainVIew的偏移量
                    //获取MainView左侧到的偏移量
                    int newLeft = mMainView.getLeft()+dx;
                    if (newLeft<0){
                        newLeft = 0;
                    }else if (newLeft>mMaxRange){
                        newLeft = mMaxRange;
                    }
                    mMainView.layout(newLeft,0,newLeft+mMainWidth,mMainHeight);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }


        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //必须的：将ViewGroup的事件交给工具类处理
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO: 2017/02/07
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
        mMainWidth = mMainView.getMeasuredWidth();
        mMainHeight = mMainView.getMeasuredHeight();
        mMaxRange = (int) (mMainWidth * MULTIPLE);
        mMenuWidth = mMenuView.getMeasuredWidth();
        mMenuHeight = mMenuView.getMeasuredHeight();
    }

}
