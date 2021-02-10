package com.kuraps.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MyTicketDetailAct extends AppCompatActivity {
    TextView btn2;
    Button btn1;
    DatabaseReference reference,reference3;
    TextView date_buy,time_buy,wisata_name, wisata_location, wisata_date, wisata_time, wisata_rules, ticket_total,expired_time,pesawat,total_harga;
    ImageView wisata_icon;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);
        btn2 = findViewById(R.id.btn2);
        btn1 = findViewById(R.id.btn1);

        getUsernameLocal();

        wisata_icon = findViewById(R.id.wisata_icon);
        wisata_name = findViewById(R.id.wisata_name);
        wisata_location = findViewById(R.id.wisata_location);
        date_buy = findViewById(R.id.date_buy);
        time_buy = findViewById(R.id.time_buy);
        wisata_date = findViewById(R.id.wisata_date);
        expired_time = findViewById(R.id.expired_time);
        wisata_rules = findViewById(R.id.wisata_rules);
        ticket_total = findViewById(R.id.ticket_total);
        pesawat = findViewById(R.id.pesawat);
        total_harga = findViewById(R.id.total_harga);

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

        // mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("wisata_name");

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(MyTicketDetailAct.this)
                        .load(dataSnapshot.child("wisata_icon").getValue().toString())
                        .centerCrop()
                        .fit()
                        .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                        .into(wisata_icon);
                wisata_rules.setText(dataSnapshot.child("wisata_rules").getValue().toString());
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                Bundle extrass = intent.getExtras();
                String xwisata_name = extras.getString("wisata_name");
                String xwisata_location = extras.getString("wisata_location");
                String tiket_tot = extras.getString("ticket_total");
                String id_ticket = extrass.getString("id_ticket");
                String xbuy_date = extrass.getString("date_buy");
                String xtime_buy = extrass.getString("time_buy");
                String xwisata_date = extrass.getString("date_wisata");
                String xexpired_time = extrass.getString("expired_time");
                String xpesawat = extrass.getString("pesawat");
                String xtotal_harga = extrass.getString("total_harga");
                if(tiket_tot.equals("1")){
                    // TIKET =  1
                    ticket_total.setText(tiket_tot+" "+getResources().getString(R.string.ticket));
                }else {
                    // TIKETS = tiket lebih dari 1
                    ticket_total.setText(tiket_tot+" "+getResources().getString(R.string.tickets));
                }

                wisata_name.setText(xwisata_name);
                wisata_location.setText(xwisata_location);
                date_buy.setText(xbuy_date);
                time_buy.setText(xtime_buy);
                wisata_date.setText(xwisata_date);
                expired_time.setText(xexpired_time);
                pesawat.setText(xpesawat);
                total_harga.setText("US $"+xtotal_harga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Runnable() {
                    int interfal = 500;
                    @Override
                    public void run() {
                        new CountDownTimer(1500, 1500) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                btn1.setText("PRINTING...");
                            }
                            @Override
                            public void onFinish() {
                                btn1.setText("PRINT TICKET");
                            }
                        }.start();
                    }
                }.run();
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Bitmap bitmap = takeScreenshot();
                        saveBitmap(bitmap);
                        Toast.makeText(MyTicketDetailAct.this, "Successfull Print Ticket on Storage\nPlease check your Gallery or Storage", Toast.LENGTH_SHORT).show();
                    }
                },1500);
            }
        });

    }
    public void getUsernameLocal() {
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
    private void AtakeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory()+"/tiketsaya_print.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

    }
}
