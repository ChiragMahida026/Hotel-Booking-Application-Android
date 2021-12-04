package com.example.hotelbookingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PaymentlistView extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notsss = db.collection("Payments");

    private cust_paymentmodel custPaymentmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentlist_view);
        setUpRecyclerViews();
    }

    private void setUpRecyclerViews() {


        Query query = notsss.whereEqualTo("emails", "amit.mahida026@gmail.com");

//        Query query = notebookRef.orderBy("usertype", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<getsetmethodpayment> options = new FirestoreRecyclerOptions.Builder<getsetmethodpayment>()
                .setQuery(query, getsetmethodpayment.class)
                .build();
        custPaymentmodel = new cust_paymentmodel(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_payment_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(custPaymentmodel);


    }

    @Override
    protected void onStart() {
        super.onStart();
        custPaymentmodel.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        custPaymentmodel.stopListening();
    }
}