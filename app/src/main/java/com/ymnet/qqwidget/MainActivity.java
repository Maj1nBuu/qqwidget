package com.ymnet.qqwidget;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ListView mLvMenu;
    private ListView mLvMain;
    private SlideMenu mSlideMenu;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvMenu = (ListView) findViewById(R.id.lv_menu);
        mLvMain = (ListView) findViewById(R.id.lv_main);
        mImageView = (ImageView) findViewById(R.id.iv_avatar);
        mLvMenu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Cheeses.sCheeseStrings){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                if (view instanceof TextView){
                    TextView textView = (TextView) view;
                    textView.setTextColor(Color.WHITE);
                }
                return view;
            }
        });
        mLvMain.setAdapter(new MainAdapter());
        mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
        mSlideMenu.setOnOpenStateChangedListener(new SlideMenu.OnOpenStateChangedListener() {
            @Override
            public void onOpen() {
                ToastUtils.showToast(MainActivity.this,"打开了");
            }

            @Override
            public void onClose() {
                //ToastUtils.showToast(MainActivity.this,"关闭了");
                //让头像左右摆动(插值器)几下
                final ObjectAnimator animator = ObjectAnimator.ofFloat(mImageView, "translationX", 0, 50);
                animator.setDuration(1000);
                animator.setInterpolator(new CycleInterpolator(2));
                animator.start();

            }

            //[0,1]
            @Override
            public void onOpening(float percent) {
                //ToastUtils.showToast(MainActivity.this,"正在打开="+percent);
                //修改头像的透明度[1,0]
                mImageView.setAlpha(1-percent);//0 是完全透明 1 是完全不透明

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开菜单
                mSlideMenu.open();
            }
        });

    }
}
