package com.itheima.stickview18;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者： itheima
 * 时间：2017-02-08 11:09
 * 网址：http://www.itheima.com
 */

public class StickView extends View {

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
        mP2 = new PointF(600,400);
        mRadius1 = 100f;
        mRadius2 = 100f;

        mP1A = new PointF(200,300);
        mP1B = new PointF(200,500);
        mP2A = new PointF(600,300);
        mP2B = new PointF(600,500);
        mPc = new PointF(400,400);

        //画笔的轨迹（路径）
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);//空心的
        canvas.drawCircle(mP1.x,mP1.y,mRadius1,mPaint);
        canvas.drawCircle(mP2.x,mP2.y,mRadius2,mPaint);
        //轨迹移动的p1A位置
        mPath.moveTo(mP1A.x,mP1A.y);
        //绘制二阶贝塞尔曲线
        //第一个点是控制点，第二个是结束的
        mPath.quadTo(mPc.x,mPc.y,mP2A.x,mP2A.y);
        mPath.lineTo(mP2B.x,mP2B.y);
        mPath.quadTo(mPc.x,mPc.y,mP1B.x,mP1B.y);
        mPath.close();//让线条闭合
        //将path展示到canvas上
        canvas.drawPath(mPath,mPaint);




    }
}
