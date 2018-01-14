package com.ymnet.stickview18;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者： example
 * 时间：2017-02-08 16:42
 * 网址：http://www.example.com
 */

public class MyLayout extends FrameLayout {
    public MyLayout(Context context) {
        this(context,null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }
    private float x;
    private float y;
    public void setPosition(float x,float y){
        this.x = x;
        this.y = y;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final View childAt = getChildAt(0);//其实就是我们添加的ImageView
        final int measuredWidth = childAt.getMeasuredWidth();
        final int measuredHeight = childAt.getMeasuredHeight();
        childAt.layout((int)x-measuredWidth/2,(int)y-measuredHeight/2, (int) (x+measuredWidth/2), (int) (y+measuredHeight/2));
    }
}
