package com.kuraps.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterActTwo extends AppCompatActivity {
    Button btn1;
    ImageView avatar,btn_upload;
    EditText name,passion;
    LinearLayout l1;

    Uri ava_location;
    Integer ava_max = 1;
    DatabaseReference reference;
    StorageReference storage,storageReference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act_two);

        getUsernameLocal();

        avatar=findViewById(R.id.avatar);
        btn1=findViewById(R.id.btn1);
        l1=findViewById(R.id.btn2);
        name=findViewById(R.id.name);
        passion=findViewById(R.id.passion);
        btn_upload=findViewById(R.id.btn_upload);
        Bundle bundle1 = getIntent().getExtras();
        Bundle bundle2 = getIntent().getExtras();
        Bundle bundle3 = getIntent().getExtras();
        String value_username = bundle1.getString("value_username");
        String value_password = bundle2.getString("value_password");
        String value_email = bundle3.getString("value_email");

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO NOTHING
                findAva();
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.length() != 0 && passion.length() != 0) {
                    // ubah state menjadi loading
                    btn1.setEnabled(false);
                    btn1.setText(getResources().getString(R.string.loading_reg));

                    if (avatar.getDrawable() == null) {
                        btn1.setEnabled(true);
                        btn1.setText(getResources().getString(R.string.continue_reg));
                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.error_av), Toast.LENGTH_SHORT).show();

                    } else {
                            // menyimpan kepada firebase
                            reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(value_username);
                            storage = FirebaseStorage.getInstance().getReference().child("UsersAvatar").child(value_username);

                            // validasi untuk file (apakah ada?)
                            if (ava_location != null) {
                                final StorageReference storageReference1 =
                                        storage.child(System.currentTimeMillis() + "." +
                                                getFileExtension(ava_location));

                                storageReference1.putFile(ava_location)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String url = uri.toString();

                                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString(username_key, value_username);
                                                        editor.apply();

                                                        reference.getRef().child("username").setValue(value_username);
                                                        reference.getRef().child("password").setValue(value_password);
                                                        reference.getRef().child("email").setValue(value_email);
                                                        reference.getRef().child("balance").setValue(1000);
                                                        reference.getRef().child("url_ava").setValue(url);
                                                        reference.getRef().child("name").setValue(name.getText().toString());
                                                        reference.getRef().child("passion").setValue(passion.getText().toString());

                                                        Log.d("url", url);

                                                        // pindah activity
                                                        Intent a = new Intent(RegisterActTwo.this, RegisterActSuccess.class);
                                                        startActivity(a);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    }
                                });
                            }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getResources().getString(R.string.error_ed),
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findAva (){
        Intent ava = new Intent();
        ava.setType("image/*");
        ava.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(ava, ava_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ava_max && resultCode==RESULT_OK && data != null && data.getData() != null){
            ava_location = data.getData();
            Picasso.with(this).load(ava_location).centerCrop().fit().into(avatar);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}