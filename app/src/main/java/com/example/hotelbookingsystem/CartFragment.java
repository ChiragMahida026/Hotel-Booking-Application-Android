package com.example.hotelbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingsystem.Adapter.CartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {


    CartAdapter mAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Button orderbutton;
    TextView orderSummary;
    NavController navController;
    private static String values;

    public static String getValues() {
        return values;
    }

    List<CartModel> cartModelList = new ArrayList<>();
    int totalOrderCost = 0;
    List<Integer> saveTotalCost = new ArrayList<>();


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firestore = FirebaseFirestore.getInstance();
        mAdapter = new CartAdapter();
        orderbutton = view.findViewById(R.id.orderNow);
        recyclerView = view.findViewById(R.id.cartRecView);

        orderSummary = view.findViewById(R.id.orderSummary);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {


                cartModelList.clear();
                if (task.isSuccessful()) {



                    for (DocumentSnapshot ds: task.getResult().getDocuments()) {


                        CartModel cartModel = ds.toObject(CartModel.class);
                        cartModelList.add(cartModel);

                        mAdapter.setCartModelList(cartModelList);
                        recyclerView.setAdapter(mAdapter);






                    }


                }




            }
        });

//        adding the totalof all orders


        firestore.collection("Cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value,  FirebaseFirestoreException error) {


                if (value!=null) {

                    for (DocumentSnapshot ds: value.getDocuments()) {

                        CartModel cartModel = ds.toObject(CartModel.class);
                        // we are adding all prices into a list of an integers
                        int valueofallprices = cartModel.getTotalprice();
                        saveTotalCost.add(valueofallprices);


                    }


                    for (int i=0; i <saveTotalCost.size(); i++) {

                        totalOrderCost += Integer.parseInt(String.valueOf(saveTotalCost.get(i)));
                    }


                    orderSummary.setText("Total is "  +String.valueOf(totalOrderCost));
                    values=String.valueOf(totalOrderCost);
                    totalOrderCost = 0;
                    saveTotalCost.clear();



                }




            }
        });





        // cart should be empty

        orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




//                                reseting the quantities of coffies once order is placed.

                firestore.collection("Coffies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {



                        if (task.isSuccessful()) {


                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()) {


                                ds.getReference()
                                        .update("quantity", 0);


                            }

                        }


                    }
                });




                // clearing the cart

                firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {


                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()) {


                                ds.getReference()
                                        .delete();




                            }
                        }
                    }
                });




                navController.navigate(R.id.paymentActivity);

//                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }

        });




    }



}