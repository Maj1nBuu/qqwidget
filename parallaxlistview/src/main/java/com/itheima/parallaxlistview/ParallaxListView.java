package com.itheima.parallaxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 作者： itheima
 * 时间：2017-02-08 09:10
 * 网址：http://www.itheima.com
 */

public class ParallaxListView extends ListView {
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
}
