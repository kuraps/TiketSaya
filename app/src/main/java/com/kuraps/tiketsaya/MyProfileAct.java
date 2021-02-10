package com.kuraps.tiketsaya;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MyProfileAct extends AppCompatActivity {
    LinearLayout item_my_ticket;
    Button btn1;
    ImageView avatar;
    TextView name,passion,btn2;
    DatabaseReference reference,reference2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getUsernameLocal();

        name=findViewById(R.id.name);
        passion=findViewById(R.id.passion);
        btn2=findViewById(R.id.btn2);
        btn1=findViewById(R.id.btn1);
        avatar=findViewById(R.id.avatar);
        myticket_place = findViewById(R.id.myticket_place);
        item_my_ticket = findViewById(R.id.item_my_ticket);

        blurView=(BlurView)findViewById(R.id.blurView);
        blurBackground();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite wave = new WanderingCubes();
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


        myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();


        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                passion.setText(snapshot.child("passion").getValue().toString());
                Picasso.with(MyProfileAct.this)
                        .load(snapshot.child("url_ava").getValue().toString())
                        .centerCrop()
                        .fit()
                        .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                        .into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference("MyTickets").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(MyProfileAct.this, list);
                myticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MyProfileAct.this, EditProfileAct.class);
                startActivity(a);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        // menghapus isi / nilai / value username local
                        // menyimpan data kepada local storage
                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(username_key, null);
                        editor.apply();

                        // berpindah activity
                        Intent gotosignin = new Intent(MyProfileAct.this, SignInAct.class);
                        gotosignin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        gotosignin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(gotosignin);
            }
        });
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