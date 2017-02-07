package com.itheima.swipedelete18;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 作者： itheima
 * 时间：2017-02-07 15:34
 * 网址：http://www.itheima.com
 */

public class MainAdapter extends BaseAdapter {

    private List<String> data;

    public MainAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvTop = (TextView) convertView.findViewById(R.id.tv_top);
        TextView tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
        tvName.setText(data.get(position));
        tvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOptionsClickListener!=null){
                    mOnOptionsClickListener.onTopClick(position);
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOptionsClickListener!=null){
                    mOnOptionsClickListener.onDeleteClick(position);
                }
            }
        });
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

    public interface OnOptionsClickListener{
        void onTopClick(int position);
        void onDeleteClick(int position);
    }
    private OnOptionsClickListener mOnOptionsClickListener;

    public void setOnOptionsClickListener(OnOptionsClickListener onOptionsClickListener) {
        mOnOptionsClickListener = onOptionsClickListener;
    }
}
