package com.kuraps.tiketsaya;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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


public class TicketDetailAct extends AppCompatActivity {
    Button btn1;
    LinearLayout l1;
    DatabaseReference reference;
    ImageView wisata_url;
    TextView wisata_name,wisata_location,wisata_photospots,wisata_wifi,wisata_festival,wisata_rules;

    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        wisata_url=findViewById(R.id.wisata_url);
        wisata_name=findViewById(R.id.wisata_name);
        wisata_location=findViewById(R.id.wisata_location);
        wisata_photospots=findViewById(R.id.wisata_photospots);
        wisata_wifi=findViewById(R.id.wisata_wifi);
        wisata_festival=findViewById(R.id.wisata_festival);
        wisata_rules=findViewById(R.id.wisata_rules);

        btn1=findViewById(R.id.btn1);
        l1=findViewById(R.id.btn2);

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


        Bundle bundle = getIntent().getExtras();
        String wisata_name_ticket = bundle.getString("wisata_name");

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Wisata").child(wisata_name_ticket);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    Picasso.with(TicketDetailAct.this)
                            .load(snapshot.child("wisata_url").getValue().toString())
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                            .into(wisata_url);
                    wisata_name.setText(snapshot.child("wisata_name").getValue().toString());
                    wisata_location.setText(snapshot.child("wisata_location").getValue().toString());
                    wisata_photospots.setText(snapshot.child("wisata_photospots").getValue().toString());
                    wisata_wifi.setText(snapshot.child("wisata_wifi").getValue().toString());
                    wisata_festival.setText(snapshot.child("wisata_festival").getValue().toString());
                    wisata_rules.setText(snapshot.child("wisata_rules").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(TicketDetailAct.this, TicketCheckoutAct.class);
                a.putExtra("wisata_name",wisata_name_ticket);
                startActivity(a);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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