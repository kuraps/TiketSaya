package com.kuraps.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class HomeAct extends AppCompatActivity {

    ImageView avatar;
    ImageView ic_trip1,ic_trip2,ic_trip3,ic_trip4,ic_trip5,ic_trip6;
    TextView name,passion,balance;
    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUsernameLocal();

        ic_trip1=findViewById(R.id.dashboard_trip_ic1);
        ic_trip2=findViewById(R.id.dashboard_trip_ic2);
        ic_trip3=findViewById(R.id.dashboard_trip_ic3);
        ic_trip4=findViewById(R.id.dashboard_trip_ic4);
        ic_trip5=findViewById(R.id.dashboard_trip_ic5);
        ic_trip6=findViewById(R.id.dashboard_trip_ic6);
        avatar=findViewById(R.id.avatar);
        name=findViewById(R.id.name);
        passion=findViewById(R.id.passion);
        balance=findViewById(R.id.balance);

        blurView=(BlurView)findViewById(R.id.blurView);
        blurBackground();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        //setting timer untuk 2 detik
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Blur and Spinner
                blurView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Picasso.with(HomeAct.this)
                            .load(dataSnapshot.child("url_ava").getValue().toString())
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                            .into(avatar);
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    passion.setText(dataSnapshot.child("passion").getValue().toString());
                    balance.setText("US $" + dataSnapshot.child("balance").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ic_trip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Pisa");
                startActivity(a);
            }
        });
        ic_trip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Torri");
                startActivity(a);
            }
        });
        ic_trip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Pagoda");
                startActivity(a);
            }
        });
        ic_trip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Candi");
                startActivity(a);
            }
        });
        ic_trip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Sphinx");
                startActivity(a);
            }
        });
        ic_trip6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, TicketDetailAct.class);
                a.putExtra("wisata_name","Monas");
                startActivity(a);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (HomeAct.this, MyProfileAct.class);
                a.putExtra("wisata_name","Pagoda");
                startActivity(a);
            }
        });

    }

    boolean twice;
    @Override
    public void onBackPressed() {
        if(twice) {
            finish();
        }
        Toast.makeText(getApplicationContext(),
                getApplicationContext().getResources().getString(R.string.error_bck),
                Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                twice = false;
            }
        },3000);
        twice = true;
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

    public void blurBackground(){
        float radius = 22f;

        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }
}