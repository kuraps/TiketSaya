package com.kuraps.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPassword extends AppCompatActivity {
    EditText username_ed, email_ed,pass;
    Button btn1;
    DatabaseReference reference;
    LinearLayout pw,l1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        pw=findViewById(R.id.pw);
        pass=findViewById(R.id.pass);

        username_ed=findViewById(R.id.username);
        email_ed=findViewById(R.id.email);
        btn1=findViewById(R.id.btn1);
        l1=findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn1.setEnabled(false);
                btn1.setText(getResources().getString(R.string.loading_reg));

                final String username = username_ed.getText().toString();
                final String email = email_ed.getText().toString();


                if (username.length() != 0 && email.length() != 0) {
                    // menyimpan kepada firebase

                    if(isValidEmailId(email_ed.getText().toString().trim())) {

                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    // ambil data password dari  firebase
                                    String emailFromFirebase = dataSnapshot.child("email").getValue().toString();
                                    String usernameFromFirebase = dataSnapshot.child("username").getValue().toString();

                                    // validasi password dengan pasword firebase
                                    if (email.equals(emailFromFirebase) && username.equals(usernameFromFirebase)) {
                                        String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                        pw.setVisibility(View.VISIBLE);
                                        pass.setText(passwordFromFirebase);

                                        btn1.setEnabled(false);
                                        btn1.setText(getResources().getString(R.string.complete_btn));

                                        // simpan username (key) pada local
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.cekpw), Toast.LENGTH_SHORT).show();

                                    } else {
                                        btn1.setEnabled(true);
                                        btn1.setText(getText(R.string.continue_btn));
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    btn1.setEnabled(true);
                                    btn1.setText(getText(R.string.continue_btn));
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_db), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),
                                getApplicationContext().getResources().getString(R.string.error_em),
                                Toast.LENGTH_SHORT).show();
                        btn1.setEnabled(true);
                        btn1.setText(getResources().getString(R.string.continue_reg));
                    }
                }else {
                    btn1.setEnabled(true);
                    btn1.setText(getResources().getString(R.string.continue_reg));
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getResources().getString(R.string.error_ed),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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