package com.kuraps.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {
Animation anim1,anim2;
ImageView img1;
TextView tv1;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anim1 = AnimationUtils.loadAnimation(this,R.anim.app_spslash);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.app_splash_title);

        img1 = findViewById(R.id.img1);
        tv1 = findViewById(R.id.txt1);

        img1.startAnimation(anim1);
        tv1.startAnimation(anim2);


        getUsernameLocal();
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if (username_key_new.isEmpty()) {
            //setting timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // pindah activty lain
                    Intent gogetStarted = new Intent(SplashAct.this, GetStartedAct.class);
                    startActivity(gogetStarted);
                    finish();
                }
            }, 2000);

        } else {

            //setting timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // pindah activty lain
                    Intent gogethome = new Intent(SplashAct.this, HomeAct.class);
                    startActivity(gogethome);
                    finish();
                }
            }, 2000);
        }
    }
}