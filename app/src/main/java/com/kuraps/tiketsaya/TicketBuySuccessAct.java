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

public class TicketBuySuccessAct extends AppCompatActivity {
    Button btn1;
    TextView tv1,txt1,txt2;
    Animation anim1,anim2,anim3;
    ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_buy_success);
        anim1 = AnimationUtils.loadAnimation(this,R.anim.app_spslash);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        anim3 = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);

        btn1 = findViewById(R.id.btn1);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        tv1 = findViewById(R.id.btn2);
        img1 = findViewById(R.id.imageView2);

        txt1.startAnimation(anim3);
        txt2.startAnimation(anim3);
        img1.startAnimation(anim1);
        btn1.startAnimation(anim2);
        tv1.startAnimation(anim2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(TicketBuySuccessAct.this, HomeAct.class);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(launchIntent);
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hei saya memulai wisata di Aplikasi TiketSaya, Yuk download di Playsotre");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent launchIntent = new Intent(TicketBuySuccessAct.this, HomeAct.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(launchIntent);
    }
}