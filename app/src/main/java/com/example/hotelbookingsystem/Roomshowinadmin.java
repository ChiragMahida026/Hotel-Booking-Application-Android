package com.example.hotelbookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Roomshowinadmin extends AppCompatActivity {

    ImageView imgss;
    TextView tv1ss,tv2ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomshowinadmin);

        imgss=(ImageView)findViewById(R.id.desc_img);
        tv1ss=(TextView)findViewById(R.id.desc_header);
        tv2ss=(TextView)findViewById(R.id.desc_desc);

        imgss.setImageResource(getIntent().getIntExtra("imagename",0));
        tv1ss.setText(getIntent().getStringExtra("header"));
        tv2ss.setText(getIntent().getStringExtra("desc"));
    }
}