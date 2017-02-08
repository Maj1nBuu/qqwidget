package com.itheima.parallaxlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView mParallaxListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParallaxListView = (ParallaxListView) findViewById(R.id.listView);
        mParallaxListView.setAdapter(new MainAdapter());
    }
}
