package com.ymnet.parallaxlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView mParallaxListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParallaxListView = (ParallaxListView) findViewById(R.id.listView);
        mParallaxListView.setAdapter(new MainAdapter());
        mParallaxListView.addHeaderView(R.layout.header_layout);
        /**
         * 设置过度拉时不要阴影效果
         */
        mParallaxListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        //mParallaxListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //    @Override
        //    public void onGlobalLayout() {
        //        //final int measuredHeight1 = mImageView.getMeasuredHeight();
        //
        //    }
        //});

    }
}
