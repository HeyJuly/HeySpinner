package com.hey.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hey.lib.HeySpinner;
import com.hey.lib.HeySpinnerBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HeySpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        data.add("4");
        spinner = findViewById(R.id.spinner);
//        spinner.attachData(data);
        spinner.setAdapter(new HeySpinnerBaseAdapter<String>(data) {
            @Override
            protected int getViewId() {
                return R.id.textView;
            }

            @Override
            protected View bindView(int position, View convertView, ViewGroup parent) {
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.item_spinner, null);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, data.getStringExtra("result"), Toast.LENGTH_LONG).show();
        }
    }


}
