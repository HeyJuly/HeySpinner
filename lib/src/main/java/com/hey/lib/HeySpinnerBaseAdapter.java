package com.hey.lib;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public abstract class HeySpinnerBaseAdapter<T> extends BaseAdapter {



    private  int background;
    private int[] padding;

    private int textGravity;


    private int textColor;

    protected List<T> data;



    public HeySpinnerBaseAdapter(List<T> data, int textColor, int textGravity, int[] padding,int background) {
        this.textGravity = textGravity;
        this.textColor = textColor;
        this.padding = padding;
        this.data = data;
        this.background = background;
    }

    public HeySpinnerBaseAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = bindView(position, convertView, parent);
            textView = convertView.findViewById(getViewId());
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }
        convertView.setBackgroundResource(background);
        if (padding != null) {
            textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
        }
        if (textColor != 0) {
            textView.setTextColor(textColor);
        }
        if (textGravity != 0) {
            textView.setGravity(textGravity);
        }
        textView.setText(getItem(position).toString());
        return convertView;
    }

    protected abstract int getViewId();

    protected abstract View bindView(int position, View convertView, ViewGroup parent);

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder {

        TextView textView;

        ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setBackground(int background) {
        this.background = background;
        notifyDataSetChanged();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyDataSetChanged();
    }
}


