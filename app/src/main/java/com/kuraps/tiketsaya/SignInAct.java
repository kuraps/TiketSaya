package com.kuraps.tiketsaya;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInAct extends AppCompatActivity {
    EditText username_ed, password_ed;
    TextView tv1,forgot_acc;
    Button btn1;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    LinearLayout pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tv1=findViewById(R.id.txt1);
        forgot_acc=findViewById(R.id.forgot_acc);
        btn1=findViewById(R.id.btn1);
        username_ed=findViewById(R.id.username);
        password_ed=findViewById(R.id.password);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (SignInAct.this, RegisterActOne.class);
                startActivity(a);
            }
        });
        forgot_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(SignInAct.this, ForgotPassword.class);
                startActivity(a);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ubah state menjadi loading
                btn1.setEnabled(false);
                btn1.setText(getResources().getString(R.string.loading_reg));

                final String username = username_ed.getText().toString();
                final String password = password_ed.getText().toString();

                if (username.isEmpty()) {
                    btn1.setEnabled(true);
                    btn1.setText(getText(R.string.signin_btn));
                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.no_username),Toast.LENGTH_SHORT).show();

                } else {
                    if (password.isEmpty()) {
                        btn1.setEnabled(true);
                        btn1.setText(getText(R.string.signin_btn));
                        Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.no_password),Toast.LENGTH_SHORT).show();

                    } else {
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    // ambil data password dari  firebase
                                    String usernameFromFirebaseFromFirebase = dataSnapshot.child("password").getValue().toString();
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    // validasi password dengan pasword firebase
                                    if (username.equals(usernameFromFirebaseFromFirebase) && password.equals(passwordFromFirebase)) {

                                        // simpan username (key) pada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username_ed.getText().toString());
                                        editor.apply();

                                        // berpindah activity
                                        Intent launchIntent = new Intent(SignInAct.this, HomeAct.class);
                                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        finish();
                                        startActivity(launchIntent);

                                        Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.login_ok),Toast.LENGTH_SHORT).show();

                                    } else {
                                        btn1.setEnabled(true);
                                        btn1.setText(getText(R.string.signin_btn));
                                        Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.invalid_data),Toast.LENGTH_SHORT).show();
                                    } 

                                } else {
                                    btn1.setEnabled(true);
                                    btn1.setText(getText(R.string.signin_btn));
                                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.invalid_data),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.no_db),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }


            }
        });
    }
}