package com.ymnet.qqwidget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者： example
 * 时间：2017-02-07 09:49
 * 网址：http://www.example.com
 */

public class MainAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return Cheeses.NAMES.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        }
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(Cheeses.NAMES[position]);
        switch (position%3) {
            case 0:
                ivAvatar.setImageResource(R.mipmap.head_1);
                break;
            case 1:
                ivAvatar.setImageResource(R.mipmap.head_2);
                break;
            case 2:
                ivAvatar.setImageResource(R.mipmap.head_3);
                break;
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
