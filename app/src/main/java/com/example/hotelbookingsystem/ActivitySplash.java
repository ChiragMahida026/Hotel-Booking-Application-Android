package com.example.hotelbookingsystem;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivitySplash extends AppCompatActivity {

    ImageView img12;
    ProgressBar simpleProgressBar;
    FirebaseFirestore firestore;
    FirebaseAuth fauth;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img12=findViewById(R.id.img12);
        simpleProgressBar=findViewById(R.id.simpleProgressBar);
        fauth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=fauth.getCurrentUser();


        if(firebaseUser !=null && firebaseUser.isEmailVerified())
        {
            simpleProgressBar.setVisibility(View.VISIBLE);
            String Uid=fauth.getCurrentUser().getUid();
            DocumentReference reference = firestore.collection("users").document(Uid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.getString("usertype").equals("admin") ){
                        Toast.makeText(getApplicationContext(), "Hello Admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ActivityAdminBoard.class));
                        finish();
                    }
                    if (documentSnapshot.getString("usertype").equals("receptionist") ){
                        Toast.makeText(getApplicationContext(), "Hello Receptionist", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ActivityreceptionistBoard.class));
                        finish();
                    }
                    if (documentSnapshot.getString("usertype").equals("cust")){
                        Toast.makeText(getApplicationContext(), "Hello Customer", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }
            });
        }

        img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySplash.this,LoginActivity.class));
            }
        });
    }
}