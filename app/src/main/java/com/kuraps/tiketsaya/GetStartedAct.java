package com.kuraps.tiketsaya;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {
Animation anim1,anim2;
Button btn1,btn2;
ImageView img1;
TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        img1=findViewById(R.id.img1);
        tv1=findViewById(R.id.txt1);

        img1.startAnimation(anim1);
        tv1.startAnimation(anim1);

        btn1.startAnimation(anim2);
        btn2.startAnimation(anim2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (GetStartedAct.this, SignInAct.class);
                startActivity(a);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent (GetStartedAct.this, RegisterActOne.class);
                startActivity(b);
            }
        });
    }
}