package com.itheima.qqwidget18;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者： itheima
 * 时间：2017-02-07 09:20
 * 网址：http://www.itheima.com
 */

public class SlideMenu extends FrameLayout {

    private ViewDragHelper mViewDragHelper;

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
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
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
}
