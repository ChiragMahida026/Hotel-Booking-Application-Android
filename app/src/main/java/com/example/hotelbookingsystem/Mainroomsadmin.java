package com.example.hotelbookingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Mainroomsadmin extends AppCompatActivity {

    FloatingActionButton float1;

    RecyclerView recviews;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private CollectionReference reference=firestore.collection("Notice");

//    private adapterforadmin adapterforadmin;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainroomsadmin);



        float1=findViewById(R.id.float1);
//
//        Query query=reference.orderBy("title",Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<Noticedata> options=new FirestoreRecyclerOptions.Builder<Noticedata>().setQuery(query,Noticedata.class).build();
//        adapterforadmin=new adapterforadmin(options);
//
//        recviews=(RecyclerView)findViewById(R.id.recviewss);
//        recviews.setHasFixedSize(true);
//        recviews.setLayoutManager(new LinearLayoutManager(this));
//        recviews.setAdapter(adapterforadmin);
//
        float1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Mainroomsadmin.this,AddRooms.class);
                startActivity(intent);
            }
        });
//
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapterforadmin.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapterforadmin.stopListening();
//    }
//
//    @Override
//    public void onBackPressed() {
//
//
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setMessage("Admin Dashboard")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i=new Intent(getApplicationContext(),ActivityAdminBoard.class);
//                        startActivity(i);
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog=builder.create();
//        alertDialog.show();
//
//    }
//
//
}
