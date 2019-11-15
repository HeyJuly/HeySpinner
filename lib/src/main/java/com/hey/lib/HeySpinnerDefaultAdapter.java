package com.hey.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HeySpinnerDefaultAdapter extends HeySpinnerBaseAdapter {

    private Context mContext;

    private List<String> data;


    public HeySpinnerDefaultAdapter(Context context, List<String> data, int textColor, int textGravity, int[] padding, int background) {
        super(data, textColor, textGravity, padding, background);
        this.mContext = context;
        this.data = data;
    }


    @Override
    protected int getViewId() {
        return R.id.textView;
    }

    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_spinner, null);
    }


}
