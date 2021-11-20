package com.example.hotelbookingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityAdminBoard extends AppCompatActivity {

    CardView cardhome;
    CardView cardlogout;
    CardView carduserprofile;
    CardView cardaddroom;


    private  long backpresstime;
    private  static final int Time_interval=2000;
    SharedPreferences sharedPreferences;
    private Toast backToast;
    boolean singleBack = false;

    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_EMAIL="name";
    private static final String KEY_PASSWORD="password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_board);

        cardlogout=findViewById(R.id.cardlogout);
        cardaddroom=findViewById(R.id.cardaddroom);
        cardhome=findViewById(R.id.cardhome);
        carduserprofile=findViewById(R.id.carduserprofile);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String emailshareds=sharedPreferences.getString(KEY_EMAIL,null);
        String passwordshareds=sharedPreferences.getString(KEY_PASSWORD,null);

        if(emailshareds!=null || passwordshareds!=null)
        {
//            Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
        }
        cardhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
            }
        });

        carduserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Userprofile.class));
            }
        });

        cardaddroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddRooms.class));
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



//        if(backpresstime+Time_interval>System.currentTimeMillis())
//        {
//            backToast.cancel();
//            super.onBackPressed();
//            return;
//
//        }
//        else
//        {
//            backToast=Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
//            backToast.show();
//        }
//        backpresstime=System.currentTimeMillis();
    }
}