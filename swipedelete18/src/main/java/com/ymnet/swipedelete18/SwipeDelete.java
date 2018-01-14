package com.ymnet.swipedelete18;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： example
 * 时间：2017-02-07 15:36
 * 网址：http://www.example.com
 */

public class SwipeDelete extends ViewGroup {

    private View mLeftView;
    private View mRightView;
    private int mRightHeight;
    private int mRightWidth;
    private int mLeftHeight;
    private int mLeftWidth;
    private ViewDragHelper mViewDragHelper;

    public SwipeDelete(Context context) {
        this(context,null);
    }

    public SwipeDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SwipeDelete(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }
    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //加上该限制目的是防止多个手指同时滑动
                if (pointerId==0){
                    return true;
                }else{
                    return false;
                }
            }
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child==mLeftView){
                    if (left>0){
                        return 0;
                    }else if (left<-mRightWidth){//往左滑动到正好能将右侧视图显示出来为止
                        return -mRightWidth;
                    }
                }else if (child==mRightView){
                    if (left<mLeftWidth - mRightWidth){
                        return mLeftWidth - mRightWidth;
                    }else if(left>mLeftWidth){
                        return mLeftWidth;
                    }
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView==mLeftView){
                    //当左侧的控件滑动的时候，需要让右侧的跟着滑动
                    //将左侧的dx累加到右侧的左边距，然后布局右侧控件
                    int newLeft = mRightView.getLeft()+dx;
                    mRightView.layout(newLeft,0,newLeft+mRightWidth,mRightHeight);
                }else if (changedView==mRightView){
                    int newLeft = mLeftView.getLeft() +dx;
                    mLeftView.layout(newLeft,0,newLeft+mLeftWidth,mLeftHeight);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (mLeftView.getLeft()<-mRightWidth/2){
                    open();
                }else{
                    close();
                }
            }
            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }
        });
    }

    private void close() {
        mViewDragHelper.smoothSlideViewTo(mLeftView,0,0);
        postInvalidateOnAnimation();
        //当关闭的时候，将当前对象从SwipeDeleteManger上移除
        SwipeDeleteManager.getInstance().setSwipeDelete(null);
    }

    private void open() {
        mViewDragHelper.smoothSlideViewTo(mLeftView,-mRightWidth,0);
        postInvalidateOnAnimation();
        //将当前对象传递给SwipeDeleteManager记录下
        SwipeDeleteManager.getInstance().setSwipeDelete(this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)){
                postInvalidateOnAnimation();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在该方法中判断当前有没有打开的条目，如果有先关闭掉
        final SwipeDelete swipeDelete = SwipeDeleteManager.getInstance().getSwipeDelete();
        if (swipeDelete!=null&&swipeDelete!=this){
                swipeDelete.close();
            return true;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
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
    public static class SwipeDeleteManager{
        private SwipeDeleteManager(){};
        private SwipeDelete mSwipeDelete;
        private static SwipeDeleteManager sSwipeDeleteManager;
        public static SwipeDeleteManager getInstance(){
            synchronized (SwipeDeleteManager.class){
                if (sSwipeDeleteManager==null){
                    sSwipeDeleteManager = new SwipeDeleteManager();
                }
            }
            return sSwipeDeleteManager;
        }

        public SwipeDelete getSwipeDelete() {
            return mSwipeDelete;
        }

        public void setSwipeDelete(SwipeDelete swipeDelete) {
            mSwipeDelete = swipeDelete;
        }
    }

}
