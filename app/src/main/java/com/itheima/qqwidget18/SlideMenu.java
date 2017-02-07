package com.itheima.qqwidget18;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    private FloatEvaluator mFloatEvaluator;
    private ArgbEvaluator mArgbEvaluator;
    private boolean isOpen;

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
        mFloatEvaluator = new FloatEvaluator();
        mArgbEvaluator = new ArgbEvaluator();
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

                //添加伴随(MainView的移动)动画效果
                //if (changedView==mMainView){
                    //计算打开的比例[0,1]
                    float percent = (mMainView.getLeft()+0f) / mMaxRange;
                    executeAnimation(percent);
                //}
                if (mOnOpenStateChangedListener!=null){
                    mOnOpenStateChangedListener.onOpening(percent);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.d(TAG, "onViewReleased: xVel="+xvel);
                if (xvel>1000){
                    //打开
                    open();
                }else  if (xvel<-1000){
                    //关闭
                    close();
                }else{
                    //根据距离进行判断
                    if (mMainView.getLeft()>mMaxRange/2){
                        open();
                    }else{
                        close();
                    }
                }
            }

            /**
             *
             * @param child
             * @return 如果返回大于0的数字，代表当子控件消费触摸事件事件，水平方向拦截
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return mMaxRange;
            }
        });
    }
    //添加伴随(MainView的移动)动画效果
    private void executeAnimation(float percent) {
        /**
         * 1. mainView X ,Y 缩放 [1,0.8] scale
         * 2. menuView X,Y 缩放 [0.5,1]
         * 3. menuView透明度 [0.5,1]
         * 4. 背景颜色 从黑色到透明
         * 5. menuView 平移 [-menuWidth/2,0]
         */
        //根据percent 计算出MainView应该缩放的比例scale,然后把这个scale设置给MainView
        final Float scaleMain = mFloatEvaluator.evaluate(percent, 1, 0.8);
        mMainView.setScaleX(scaleMain);
        mMainView.setScaleY(scaleMain);
        final Float scaleMenu = mFloatEvaluator.evaluate(percent, 0.5f, 1);
        mMenuView.setScaleX(scaleMenu);
        mMenuView.setScaleY(scaleMenu);
        mMenuView.setAlpha(scaleMenu);
        final Float translateX = mFloatEvaluator.evaluate(percent, -mMenuWidth / 2, 0);
        mMenuView.setTranslationX(translateX);
        final Drawable background = getBackground();
        final int colorEvaluate = (int) mArgbEvaluator.evaluate(percent, Color.BLACK, Color.TRANSPARENT);
        background.setColorFilter(colorEvaluate, PorterDuff.Mode.SRC_OVER);

    }

    public void close() {
        mViewDragHelper.smoothSlideViewTo(mMainView,0,0);
        //必须重绘当前View，上面代码才有用
        //invalidate();//只能在主线程中被调用
        //postInvalidate();//可以在任何线程中调用
        postInvalidateOnAnimation();//针对动画效果作了优化
        //ViewCompat.postInvalidateOnAnimation(this);//向下兼容的重绘

        if (mOnOpenStateChangedListener!=null){
            mOnOpenStateChangedListener.onClose();
        }

        isOpen = false;

    }

    public void open() {
        mViewDragHelper.smoothSlideViewTo(mMainView,mMaxRange,0);//开始执行动画.内部只是修改了ChildView的布局位置，并未真正的绘制出来
        postInvalidateOnAnimation();
        if (mOnOpenStateChangedListener!=null){
            mOnOpenStateChangedListener.onOpen();
        }
        isOpen = true;
    }

    /**
     * 当View在重绘的时候，View会调用这个方法，用于判断动画是否执行完成
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //如果动画没有执行完，不断的重绘
        if (mViewDragHelper.continueSettling(true)){
            //如果动画时间还未执行完，继续重绘
            postInvalidateOnAnimation();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //必须的：将ViewGroup的事件交给工具类处理
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(isOpen){
            final float x = ev.getX();
            if (x>mMaxRange){
                return true;
            }
        }

        //让ViewDragHelper智能判断是否需要拦截事件
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
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

    public interface OnOpenStateChangedListener{
        void onOpen();
        void onClose();
        void onOpening(float percent);
    }
    private OnOpenStateChangedListener mOnOpenStateChangedListener;

    public void setOnOpenStateChangedListener(OnOpenStateChangedListener onOpenStateChangedListener) {
        mOnOpenStateChangedListener = onOpenStateChangedListener;
    }
}
