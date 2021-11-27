package com.example.hotelbookingsystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Userprofile extends AppCompatActivity {

    TextView username, usermail, userphone, usercity, useraddress;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    Button editbtn;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        username = findViewById(R.id.username);
        usermail = findViewById(R.id.usermail);
        userphone = findViewById(R.id.userphone);
        usercity = findViewById(R.id.city);
        useraddress = findViewById(R.id.useraddress);
        profileImage = findViewById(R.id.newimages);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        editbtn = findViewById(R.id.editbtn);

        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                username.setText(value.getString("name"));
                usermail.setText(value.getString("email"));
                userphone.setText(value.getString("phone"));
                usercity.setText(value.getString("dropdowns"));
                useraddress.setText(value.getString("address"));

            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click Update Profile", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), EditUserProfile.class);
                i.putExtra("name", username.getText().toString());
                i.putExtra("email", usermail.getText().toString());
                i.putExtra("phone", userphone.getText().toString());
                i.putExtra("dropdowns", usercity.getText().toString());
                i.putExtra("address", useraddress.getText().toString());
                startActivity(i);
            }
        });
    }
}