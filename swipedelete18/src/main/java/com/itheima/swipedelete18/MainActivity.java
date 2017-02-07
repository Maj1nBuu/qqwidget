package com.itheima.swipedelete18;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnOptionsClickListener {

    private ListView mListView;
    private List<String> mNames;
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);

        //通过Arrays工具类转换过来的集合不支持删除，只能查询
        mNames = new ArrayList<>(Arrays.asList(Cheeses.NAMES));
        mMainAdapter = new MainAdapter(mNames);
        mListView.setAdapter(mMainAdapter);
        mMainAdapter.setOnOptionsClickListener(this);
    }

    @Override
    public void onTopClick(int position) {
        //从集合中删除哪个，则返回哪个条目
        final String remove = mNames.remove(position);
        mNames.add(0,remove);
        mMainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(int position) {
        mNames.remove(position);
        mMainAdapter.notifyDataSetChanged();
    }
}
