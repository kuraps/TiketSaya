package com.kuraps.tiketsaya;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class TicketCheckoutAct extends AppCompatActivity {
    View view1;
    int Click=0;
    Button btn1,btn2,btn3,btn4,btn6;
    LinearLayout l1;
    ImageView dropdown,wisata_icon,wisata_url;

    TextView total_ticket,wisata_desc,balance,wisata_price,wisata_name,wisata_location;
    DatabaseReference reference, reference2, reference3, reference4,reference_maskapai;
    Integer ticket = 1;
    Integer allValue = 0;
    Integer ticket_value = 0; //12
    Integer my_money = 0;
    Integer new_balance = 0;

    Integer id_trans = new Random().nextInt();
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String date_wisata = "";
    String time_wisata = "";

    BlurView blurView;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout_new);

        getUsernameLocal();

        Bundle bundle = getIntent().getExtras();
       String wisata_name_ticket = bundle.getString("wisata_name");

        btn1=findViewById(R.id.btn1);
        l1=findViewById(R.id.btn2);
        wisata_icon=findViewById(R.id.wisata_icon);
        wisata_url=findViewById(R.id.wisata_url);

        dropdown = findViewById(R.id.ticket_checkoutdrop);
        view1 = findViewById(R.id.ticket_checkoutview);
        wisata_desc = findViewById(R.id.wisata_desc);
        wisata_name = findViewById(R.id.wisata_name);
        wisata_location = findViewById(R.id.wisata_location);
        wisata_price = findViewById(R.id.wisata_price);
        total_ticket = findViewById(R.id.total_ticket);
        balance = findViewById(R.id.balance);
        btn2 = findViewById(R.id.ticket_checkout_btn_min);
        btn3 = findViewById(R.id.ticket_checkout_btn_pls);
        btn4 = findViewById(R.id.ticket_checkout_btn_alert);
        btn6 = findViewById(R.id.ticket_checkout_btn_pls10);
        blurView=(BlurView)findViewById(R.id.blurView);
        myDialog = new Dialog(this, R.style.DialogTrans);

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

        //DB
        // mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_money = Integer.valueOf(dataSnapshot.child("balance").getValue().toString());
                balance.setText("US$ " + my_money);
                btn4.setAlpha(0);
                btn2.setEnabled(false);
                btn4.setAlpha(0);
                total_ticket.setTextColor(getResources().getColor(R.color.blackPrimary));
                total_ticket.setBackgroundResource(R.drawable.bg_ed_pressed);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Wisata").child(wisata_name_ticket);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    Picasso.with(TicketCheckoutAct.this)
                            .load(snapshot.child("wisata_url").getValue().toString())
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                            .into(wisata_url);
                    Picasso.with(TicketCheckoutAct.this)
                            .load(snapshot.child("wisata_icon").getValue().toString())
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                            .into(wisata_icon);
                    wisata_name.setText(snapshot.child("wisata_name").getValue().toString());
                    wisata_location.setText(snapshot.child("wisata_location").getValue().toString());
                    wisata_desc.setText(snapshot.child("wisata_desc").getValue().toString());
                    wisata_price.setText("US$ "+ snapshot.child("wisata_price").getValue().toString());
                    ticket_value = Integer.valueOf(snapshot.child("wisata_price").getValue().toString());
                    date_wisata = snapshot.child("wisata_date").getValue().toString();
                    time_wisata = snapshot.child("wisata_time").getValue().toString();
                    total_ticket.setText(ticket.toString());

                    allValue = ticket_value * ticket;
                    wisata_price.setText("US$ "+ allValue+"");

                    if(allValue > my_money) {
                        btn1.animate().translationY(250).alpha(0).setDuration(350).start();
                        btn1.setEnabled(false);
                        balance.setTextColor(Color.parseColor("#D1206B"));
                        btn4.animate().translationY(0).alpha(1).setDuration(300).start();
                        total_ticket.setTextColor(getResources().getColor(R.color.redPrimary));
                        total_ticket.setBackgroundResource(R.drawable.sign_in_edittext_bg_pressed2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Animation in = new AlphaAnimation(0.0f,1.0f);
        in.setDuration(300);
        final Animation out = new AlphaAnimation(1.0f,0.0f);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                wisata_price.startAnimation(in);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

        });
        //

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dropdown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Click==0) {
                    dropdown.setImageResource(R.drawable.ic_droptop);
                    wisata_desc.startAnimation(in);
                    dropdown.startAnimation(in);
                    view1.setVisibility(View.VISIBLE);
                    wisata_desc.setVisibility(View.VISIBLE);
                    Click++;
                }
                else if(Click==1) {
                    Click=0;
                    dropdown.setImageResource(R.drawable.ic_dropdown);
                    view1.setVisibility(View.GONE);
                    wisata_desc.setVisibility(View.GONE);
                }
            }
        });

        // gas

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ticket-=1;
                total_ticket.setText(ticket.toString());
                if(ticket < 2) {
                    btn2.animate().alpha(0).setDuration(300).start();
                    btn2.setEnabled(false);
                }
                allValue = ticket_value * ticket;
                wisata_price.startAnimation(out);
                wisata_price.setText("US$ "+ allValue+"");
                if(allValue < my_money) {
                    btn1.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn1.setEnabled(true);
                    balance.setTextColor(Color.parseColor("#203dd1"));
                    btn4.animate().alpha(0).setDuration(300).start();
                    total_ticket.setTextColor(getResources().getColor(R.color.blackPrimary));
                    total_ticket.setBackgroundResource(R.drawable.bg_ed_pressed);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ticket+=1;
                total_ticket.setText(ticket.toString());
                if(ticket > 1) {
                    btn2.animate().alpha(1).setDuration(300).start();
                    btn2.setEnabled(true);
                    allValue = ticket_value * ticket;
                    wisata_price.startAnimation(out);
                    wisata_price.setText("US$ "+ allValue+"");
                }
                if(allValue > my_money) {
                    btn1.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn1.setEnabled(false);
                    balance.setTextColor(Color.parseColor("#D1206B"));
                    btn4.animate().translationY(0).alpha(1).setDuration(300).start();
                    total_ticket.setTextColor(getResources().getColor(R.color.redPrimary));
                    total_ticket.setBackgroundResource(R.drawable.sign_in_edittext_bg_pressed2);
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(TicketCheckoutAct.this, "Your balance is not enough!", Toast.LENGTH_SHORT).show();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ticket+=10;
                total_ticket.setText(ticket.toString());
                if(ticket > 1) {
                    btn2.animate().alpha(1).setDuration(300).start();
                    btn2.setEnabled(true);
                    allValue = ticket_value * ticket;
                    wisata_price.startAnimation(out);
                    wisata_price.setText("US$ "+ allValue+"");
                }
               if(allValue > my_money) {
                    btn1.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn1.setEnabled(false);
                    balance.setTextColor(Color.parseColor("#D1206B"));
                    btn4.animate().translationY(0).alpha(1).setDuration(300).start();
                    total_ticket.setTextColor(getResources().getColor(R.color.redPrimary));
                    total_ticket.setBackgroundResource(R.drawable.sign_in_edittext_bg_pressed2);
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogSelanjutnya();

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

    // DIALOG

    public void DialogSelanjutnya() {
        TextView stv1,exp_tv,go_tv,go_time_tv,ticket_tim_buy,pesawat;
        final RadioButton srb1;
        RadioButton srb2, srb3, srb4, srb5;
        Button sbtn1;
        final RadioGroup rg1;
        final RadioGroup rg2;
        myDialog.setContentView(R.layout.popup_ticket);
        stv1 =(TextView) myDialog.findViewById(R.id.popup_ticket_tv1);
        exp_tv =(TextView) myDialog.findViewById(R.id.exp_tv);
        go_tv =(TextView) myDialog.findViewById(R.id.ticket_go_on);
        go_time_tv =(TextView) myDialog.findViewById(R.id.ticket_time_go_on);
        ticket_tim_buy =(TextView) myDialog.findViewById(R.id.ticket_time_buy);
        pesawat =(TextView) myDialog.findViewById(R.id.pesawat);


        srb1 = (RadioButton)myDialog.findViewById(R.id.maskapainbtn0);
        srb2 = (RadioButton)myDialog.findViewById(R.id.maskapainbtn1);
        srb3 = (RadioButton)myDialog.findViewById(R.id.maskapainbtn2);
        srb4 = (RadioButton)myDialog.findViewById(R.id.tglbtn0);
        srb5 = (RadioButton)myDialog.findViewById(R.id.tglbtn1);

        rg1 = (RadioGroup)myDialog.findViewById(R.id.radiogroup0);
        rg2 = (RadioGroup)myDialog.findViewById(R.id.radiogroup1);
        int SelectedId1 = rg1.getCheckedRadioButtonId();
        int SelectedId2 = rg2.getCheckedRadioButtonId();
        sbtn1 = (Button) myDialog.findViewById(R.id.buy);
        //stv1.setTypeface(tf2);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat tgl = new SimpleDateFormat("EEEE, dd/MM/yyyy");
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date(0);

        String dayOfTheWeek = tgl.format(cal.getTime());
        String time = jam.format(cal.getTime());

        Calendar cal_week = Calendar.getInstance();
        Calendar cal_exp = Calendar.getInstance();
        Calendar cal_exp_tmrw = Calendar.getInstance();
        SimpleDateFormat mingdep = new SimpleDateFormat("dd/MM/yyyy");
        cal_week.add(Calendar.DATE, 7);
        cal_exp.add(Calendar.DATE, 8);
        cal_exp_tmrw.add(Calendar.DATE, 1);
        String expired = mingdep.format(cal_exp.getTime());
        String expired_tmrw = mingdep.format(cal_exp_tmrw.getTime());
        String depature_week = mingdep.format(cal_week.getTime());
        srb4.setText(getResources().getString(R.string.next_day) + " 08:00");
        srb5.setText(depature_week);
        exp_tv.setText(expired_tmrw);
        go_tv.setText(srb4.getText());
        srb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exp_tv.setText(expired_tmrw);
                go_tv.setText(srb4.getText() + " AM");
            }
        });
        srb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exp_tv.setText(expired);
                go_tv.setText("08:00 AM "+srb5.getText());
            }
        });
        stv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        reference_maskapai = FirebaseDatabase.getInstance().getReference().child("Maskapai");
        reference_maskapai.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                srb1.setText(snapshot.child("Garuda").child("pesawat").getValue().toString());
                srb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pesawat.setText(snapshot.child("Garuda").child("pesawat").getValue().toString());

                    }
                });
                srb2.setText(snapshot.child("Citilink").child("pesawat").getValue().toString());
                srb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pesawat.setText(snapshot.child("Citilink").child("pesawat").getValue().toString());

                    }
                });
                srb3.setText(snapshot.child("Lion").child("pesawat").getValue().toString());
                srb3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pesawat.setText(snapshot.child("Lion").child("pesawat").getValue().toString());

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menyimpan data user kepada firebase dan membuat tabel baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTickets")
                        .child(username_key_new)
                        .child(wisata_name.getText().toString() + id_trans);

                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        go_time_tv.setText(time);
                        ticket_tim_buy.setText(dayOfTheWeek);
                        reference3.getRef().child("id_ticket").setValue(wisata_name.getText().toString() + id_trans);
                        reference3.getRef().child("wisata_name").setValue(wisata_name.getText().toString());
                        reference3.getRef().child("wisata_location").setValue(wisata_location.getText().toString());
                        reference3.getRef().child("ticket_total").setValue(ticket.toString());
                        reference3.getRef().child("total_harga").setValue(allValue.toString());
                        reference3.getRef().child("date_wisata").setValue(go_tv.getText().toString());
                        reference3.getRef().child("time_buy").setValue(go_time_tv.getText().toString());
                        reference3.getRef().child("date_buy").setValue(ticket_tim_buy.getText().toString());
                        reference3.getRef().child("expired_time").setValue(exp_tv.getText().toString());
                        reference3.getRef().child("pesawat").setValue(pesawat.getText().toString());
                        myDialog.dismiss();
                        Intent a = new Intent(TicketCheckoutAct.this, TicketBuySuccessAct.class);
                        startActivity(a);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // update data balance kepada users (yang saat ini login)
                // mengambil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        new_balance = my_money - allValue;
                        reference4.getRef().child("balance").setValue(new_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        srb4.setChecked(true);
        srb4.performClick();
        rg1.check(srb1.getId());
        rg2.check(srb4.getId());
        srb1.performClick();
        srb1.setChecked(true);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}