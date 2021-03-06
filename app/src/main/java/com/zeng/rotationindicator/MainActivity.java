package com.zeng.rotationindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zeng.rotationindicator.RotationIndicator.Style;

public class MainActivity extends AppCompatActivity {
    private RotationIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = (RotationIndicator) findViewById(R.id.indicator);
        Button btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.rotate(15);
            }
        });

        Button btn_minus = (Button) findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.rotate(-15);
            }
        });

        Button btn_rect = (Button) findViewById(R.id.btn_rect);
        btn_rect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.setStyle(Style.RECT);
            }
        });

        Button btn_vertical = (Button) findViewById(R.id.btn_circle);
        btn_vertical.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.setStyle(Style.CIRCLE);
            }
        });

        Button btn_anim = (Button) findViewById(R.id.btn_anim);
        btn_anim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.setAnimationEnabled(true);
            }
        });

        Button btn_no_anim = (Button) findViewById(R.id.btn_no_anim);
        btn_no_anim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indicator.setAnimationEnabled(false);
            }
        });
    }
}
