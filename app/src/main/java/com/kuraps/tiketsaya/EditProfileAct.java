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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {


    ImageView add_ava,avatar;
    EditText xnama_lengkap, xbio, xusername, xpassword, xemail_address;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference,reference2;
    StorageReference storage,storage2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Button save;
    LinearLayout back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        save=findViewById(R.id.btn1);
        back=findViewById(R.id.btn2);

        add_ava = findViewById(R.id.img1);
        avatar = findViewById(R.id.avatar);
        xnama_lengkap = findViewById(R.id.name);
        xbio = findViewById(R.id.passion);
        xusername = findViewById(R.id.username);
        xpassword = findViewById(R.id.password);
        xemail_address = findViewById(R.id.email);

        getUsernameLocal();

        storage = FirebaseStorage.getInstance().getReference().child("UsersAvatar").child(username_key_new);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xnama_lengkap.setHint(snapshot.child("name").getValue().toString());
                xbio.setHint(snapshot.child("passion").getValue().toString());
                xemail_address.setHint(snapshot.child("email").getValue().toString());
                xusername.setHint(snapshot.child("username").getValue().toString());
                xpassword.setHint(snapshot.child("password").getValue().toString());
                xnama_lengkap.setText(snapshot.child("name").getValue().toString());
                xbio.setText(snapshot.child("passion").getValue().toString());
                xemail_address.setText(snapshot.child("email").getValue().toString());
                xusername.setText(snapshot.child("username").getValue().toString());
                xpassword.setText(snapshot.child("password").getValue().toString());
                Picasso.with(EditProfileAct.this)
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xnama_lengkap.length() != 0 && xbio.length() != 0 && xusername.length() != 0 && xpassword.length() != 0 && xemail_address.length() != 0) {
                    save.setEnabled(false);
                    save.setText(getResources().getString(R.string.update_reg));
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                            dataSnapshot.getRef().child("name").setValue(xnama_lengkap.getText().toString());
                            dataSnapshot.getRef().child("passion").setValue(xbio.getText().toString());
                            dataSnapshot.getRef().child("email").setValue(xemail_address.getText().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // validasi untuk file (apakah ada?)
                    if (photo_location != null) {

                        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    String url_old_ava = snapshot.child("url_ava").getValue().toString();
                                    StorageReference oldAva = FirebaseStorage.getInstance().getReferenceFromUrl(url_old_ava);

                                    oldAva.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        final StorageReference storageReference1 =
                                storage.child(System.currentTimeMillis() + "." +
                                        getFileExtension(photo_location));

                        storageReference1.putFile(photo_location)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String url = uri.toString();
                                                reference.getRef().child("url_ava").setValue(url);
                                                Log.d("url", url);

                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                            }
                                        });
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            }
                        });
                    }

                    // pindah activity
                    Intent launchIntent = new Intent(EditProfileAct.this, MyProfileAct.class);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(launchIntent);
                }else {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getResources().getString(R.string.error_ed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

    }



    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    // setelah fungsi findPhoto selesai, kita akan meletakkannya kepada onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this)
                    .load(photo_location)
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                    .into(avatar);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}