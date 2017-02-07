package com.itheima.qqwidget18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mLvMenu;
    private ListView mLvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvMenu = (ListView) findViewById(R.id.lv_menu);
        mLvMain = (ListView) findViewById(R.id.lv_main);
        mLvMenu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Cheeses.sCheeseStrings));
        mLvMain.setAdapter(new MainAdapter());
    }
}
