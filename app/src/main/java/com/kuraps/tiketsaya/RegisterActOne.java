package com.kuraps.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActOne extends AppCompatActivity {

    LinearLayout l1;
    Button btn1;
    EditText username,password,email;
    DatabaseReference reference,reference_username;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act_one);
        l1=findViewById(R.id.btn2);
        btn1=findViewById(R.id.btn1);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.length() != 0 && password.length() != 0 && email.length() != 0) {
                    // ubah state menjadi loading
                    btn1.setEnabled(false);
                    btn1.setText(getResources().getString(R.string.loading_reg));

                    // mengambil username dari Firebase database
                    reference_username = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(username.getText().toString());

                    reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // jika username tersedia
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.username_already), Toast.LENGTH_SHORT).show();
                                btn1.setEnabled(true);
                                btn1.setText(getResources().getString(R.string.continue_reg));
                            }else{
                            // menyimpan data kepada local storage
                                if(isValidEmailId(email.getText().toString().trim())){
                                    String value_username=username.getText().toString();
                                    String value_password=password.getText().toString();
                                    String value_email=email.getText().toString();
                                    Intent a = new Intent(RegisterActOne.this, RegisterActTwo.class);
                                    a.putExtra("value_username",value_username);
                                    a.putExtra("value_password",value_password);
                                    a.putExtra("value_email",value_email);
                                    startActivity(a);
                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            getApplicationContext().getResources().getString(R.string.error_em),
                                            Toast.LENGTH_SHORT).show();
                                    btn1.setEnabled(true);
                                    btn1.setText(getResources().getString(R.string.continue_reg));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getResources().getString(R.string.error_ed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}