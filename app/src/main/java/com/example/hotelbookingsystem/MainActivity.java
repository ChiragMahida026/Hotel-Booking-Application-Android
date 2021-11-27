package com.example.hotelbookingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.hotelbookingsystem.news.MainActivityss;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CardView cardhome;
    CardView cardlogout;
    CardView carduserprofile;
    CardView cardchangepassword;
    CardView cardroombook;


    private  long backpresstime;
    private  static final int Time_interval=2000;
    SharedPreferences sharedPreferences;
    private Toast backToast;
    boolean singleBack = false;
    FirebaseAuth fauth;
    FirebaseUser user;

    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_EMAIL="name";
    private static final String KEY_PASSWORD="password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardlogout=findViewById(R.id.cardlogout);

        cardhome=findViewById(R.id.cardhome);
        carduserprofile=findViewById(R.id.carduserprofile);
        cardchangepassword=findViewById(R.id.resetpassword);
        cardroombook=findViewById(R.id.cardroombook);
        fauth=FirebaseAuth.getInstance();
        FirebaseFirestore fstore=FirebaseFirestore.getInstance();

        String uids=fauth.getCurrentUser().getUid();
        user=fauth.getCurrentUser();

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String emailshareds=sharedPreferences.getString(KEY_EMAIL,null);
        String passwordshareds=sharedPreferences.getString(KEY_PASSWORD,null);

        if(emailshareds!=null || passwordshareds!=null)
        {
//            Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
        }

        cardhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent=new Intent(MainActivity.this,MainActivity21.class);
//                startActivity(intent);

//                Intent intent=new Intent(MainActivity.this,ShowingRooms.class);
                Intent intent=new Intent(MainActivity.this, MainActivityss.class);
                startActivity(intent);
            }
        });

//        cardroombook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,Activitysinglebed.class);
//                startActivity(intent);
//            }
//        });

        cardchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view= LayoutInflater.from(v.getContext()).inflate(R.layout.activity_activityresetpassword,null);

                final EditText passwordEt=view.findViewById(R.id.passwordEt);
                final EditText passwordchangeEts=view.findViewById(R.id.passwordchangeEt);
                Button set_new_password_btn=view.findViewById(R.id.set_new_password_btn);

                final AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setView(view);

                final AlertDialog dialog=builder.create();
                dialog.show();

                set_new_password_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String old = passwordEt.getText().toString();
                        String news = passwordchangeEts.getText().toString();
                        if (TextUtils.isEmpty(old)) {
                            Toast.makeText(getApplicationContext(), "Enter Your Current Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (news.length()<6)
                        {
                            Toast.makeText(getApplicationContext(), "Password length must atleast 6 chara", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                        updatePassword(old,news);
                    }
                });


            }
        });


        carduserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Userprofile.class));
            }
        });
        cardlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });


    }

    private void updatePassword(String old, String news) {

        AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),old);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Password Updated....", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}