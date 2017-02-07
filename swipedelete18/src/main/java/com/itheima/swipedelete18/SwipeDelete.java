package com.itheima.swipedelete18;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： itheima
 * 时间：2017-02-07 15:36
 * 网址：http://www.itheima.com
 */

public class SwipeDelete extends ViewGroup {

    private View mLeftView;
    private View mRightView;
    private int mRightHeight;
    private int mRightWidth;
    private int mLeftHeight;
    private int mLeftWidth;

    public SwipeDelete(Context context) {
        this(context,null);
    }

    public SwipeDelete(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SwipeDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SwipeDelete(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取两个子控件，然后测量
        mLeftView = getChildAt(0);
        mRightView = getChildAt(1);
        //由于继承自ViewGroup，没有对子控件进行遍历测量，因此我们必须测量，否则所有的子控件的尺寸都为0
        measureChild(mLeftView,widthMeasureSpec,heightMeasureSpec);
        measureChild(mRightView,widthMeasureSpec,heightMeasureSpec);
        mLeftWidth = mLeftView.getMeasuredWidth();
        mLeftHeight = mLeftView.getMeasuredHeight();
        mRightWidth = mRightView.getMeasuredWidth();
        mRightHeight = mRightView.getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftView.layout(0,0,mLeftWidth,mLeftHeight);
        mRightView.layout(mLeftWidth,0,mLeftWidth+mRightWidth,mRightHeight);
    }
}
