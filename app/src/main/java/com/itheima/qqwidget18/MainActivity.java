package com.itheima.qqwidget18;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ListView mLvMenu;
    private ListView mLvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvMenu = (ListView) findViewById(R.id.lv_menu);
        mLvMain = (ListView) findViewById(R.id.lv_main);
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
    }
}
