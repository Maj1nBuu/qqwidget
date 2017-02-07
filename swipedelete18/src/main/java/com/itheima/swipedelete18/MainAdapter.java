package com.itheima.swipedelete18;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 作者： itheima
 * 时间：2017-02-07 15:34
 * 网址：http://www.itheima.com
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
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvTop = (TextView) convertView.findViewById(R.id.tv_top);
        TextView tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
        tvName.setText(Cheeses.NAMES[position]);
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
