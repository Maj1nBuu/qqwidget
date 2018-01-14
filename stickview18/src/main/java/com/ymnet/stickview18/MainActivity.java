package com.ymnet.stickview18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StickView stickView = (StickView) findViewById(R.id.stickView);
        stickView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int height = getSupportActionBar().getHeight();
                Log.d(TAG, "onCreate: "+height);
                stickView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }
}
