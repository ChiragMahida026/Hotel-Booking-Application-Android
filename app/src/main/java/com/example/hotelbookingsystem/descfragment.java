package com.example.hotelbookingsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbookingsystem.Model.roommodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class descfragment extends Fragment {


    NavController navController;
    String quantity = "0";
    FirebaseFirestore firestore;
    Button add, sub, order;
    TextView roomname, roomdescription, roomprices, roomavability, orderINFO;
    String roomnames, roomdescriptions, imageURL;
    ImageView imagegholder;
    int price = 0;
    int quan = 0;

    int totalPrice = 0;

    public descfragment() {

    }

    public descfragment(String title, String editdescs, String editroomava, String editprice, String image) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_descfragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagegholder = view.findViewById(R.id.imagegholder);
        roomname = view.findViewById(R.id.courseholder);
        roomdescription = view.findViewById(R.id.emailholder);
        roomprices = view.findViewById(R.id.roomprice);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        roomavability = view.findViewById(R.id.quantityDETAILnUMBER);
        firestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.orderdetail);
        orderINFO = view.findViewById(R.id.orderINFO);


        roomnames = descfragmentArgs.fromBundle(getArguments()).getRoomname();
        imageURL = descfragmentArgs.fromBundle(getArguments()).getImageurl();
        roomdescriptions = descfragmentArgs.fromBundle(getArguments()).getRoomdescription();
        price = descfragmentArgs.fromBundle(getArguments()).getPrice();


        Glide.with(view.getContext()).load(imageURL).into(imagegholder);
        roomname.setText(roomnames + " $" + String.valueOf(price));
        roomdescription.setText(roomdescriptions);
    }


//        firestore.collection("Notice").document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
//                roommodel roommodel = value.toObject(roommodel.class);
//                quantity = roommodel.getEditroomava();
//                roomavability.setText(String.valueOf(quantity));
//
//                //convert String into integer
//                quan = Integer.parseInt(quantity);
//               //                String.valueOf(Integer.parseInt(t1) * Integer.parseInt(m1))
//
//                totalPrice = quan*price;
//                orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
//
//
//                if (quan == 0) {
//
//
//                    firestore.collection("Cart").document(roomnames).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(Task<Void> task) {
//
//                        }
//                    });
//
//                }
//
//            }
//        });
//
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (quan == 10) {
//
//                    Toast.makeText(getContext(), "Cant Order More than 10", Toast.LENGTH_SHORT).show();
//                    roomavability.setText(String.valueOf(quantity));
//
//                } else {
//
//
//                    quan++; // quantity = quantity+1; similar
//                    roomavability.setText(String.valueOf(quantity));
//
//                    // showing the price
//                    totalPrice = quan * price;
//                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
//
//                    //updating quantities
//                    firestore.collection("Notice").document().update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(Task<Void> task) {
//
//                        }
//                    });
//
//                }
//
//            }
//        });
//
//        sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (quan == 0) {
//
//                    Toast.makeText(getContext(), "Nothing in Cart", Toast.LENGTH_SHORT).show();
//                    roomavability.setText(String.valueOf(quantity));
//                    quan = 0;
//                    totalPrice = 0;
//
//                } else {
//
//
//
//                    quan--;
//                    roomavability.setText(String.valueOf(quantity));
//
//                    // showing the price
//                    totalPrice = quan * price;
//                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
//
//
//                    //updating quantity
//                    firestore.collection("Notice").document().update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(Task<Void> task) {
//
//                        }
//                    });
//                }
//            }
//        });
//
//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (quan == 0) {
//
//                    navController.navigate(R.id.action_descfragment_to_recfragment);
//                    Toast.makeText(getContext(), "You did not order " + roomnames, Toast.LENGTH_SHORT).show();
//
//
//
//                } else {
//                    AddToCart();
//
//                    descfragmentDirections.actionDescfragmentToRecfragment
//                            action = descfragmentDirections.actionDescfragmentToRecfragment();
//
//                    action.setQuantity(quantity);
//                    navController.navigate(action);
//                    Toast.makeText(getContext(), "You've ordered " + name, Toast.LENGTH_SHORT).show();
//
//
//
//                }
//
//            }
//        });
//
//
//    }

//        public void onBackPressed ()
//        {
//            AppCompatActivity activity = (AppCompatActivity) getContext();
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).addToBackStack(null).commit();
//
//        }
//    private void AddToCart() {
//
//
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("roomname", roomnames);
//        hashMap.put("quantity", quan);
//        hashMap.put("totalprice", totalPrice);
//        hashMap.put("imageURL", imageURL);
//
//
//
//
//
////           creating new collection for cart
//        firestore.collection("Cart").document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(Task<Void> task) {
//
//
//
//            }
//        });
//
//
//
//
//    }
    }
