package com.example.hotelbookingsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;


public class CoffeeDetailFragment extends Fragment {


    NavController navController;
    int quantity = 0;
    FirebaseFirestore firebaseFirestore;
    Button add, sub, order;
    TextView coffeename, description, quantityview, orderINFO;
    ImageView imageView;
    String coffeeid, name, coffeedescription, imageURL;
    int price = 0;

    int totalPrice = 0;


    public CoffeeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.CoffeeDetailImage);
        coffeename = view.findViewById(R.id.coffeenamedetail);
        description = view.findViewById(R.id.coffeedetaildetail);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        quantityview = view.findViewById(R.id.quantityDETAILnUMBER);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.orderdetail);
        orderINFO = view.findViewById(R.id.orderINFO);


        name = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getCoffeename();
        coffeeid = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getId();
//        imageURL = R.drawable.add;
        coffeedescription = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        price = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getPrice();

//        Glide.with(view.getContext()).load(imageURL).into(imageView);
        coffeename.setText(name + " ₹" + String.valueOf(price));
        description.setText(coffeedescription);

//        fetching the recent quantity in firestore and displaying it
        firebaseFirestore.collection("Coffies").document(coffeeid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value,  FirebaseFirestoreException error) {

                CofeModel cofeModel = value.toObject(CofeModel.class);
                quantity = cofeModel.getQuantity();
                quantityview.setText(String.valueOf(quantity));
                // showing the price
                totalPrice = quantity * price;
                orderINFO.setText(String.valueOf("Total Price is " + totalPrice));


                if (quantity == 0) {



                    firebaseFirestore.collection("Cart").document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {

                        }
                    });

                }

            }
        });





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 3) {

//                    Toast.makeText(getContext(), "Cant Order More than 10", Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));

                } else {


                    quantity++; // quantity = quantity+1; similar
                    quantityview.setText(String.valueOf(quantity));

                    // showing the price
                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

                    //updating quantities
                    firebaseFirestore.collection("Coffies").document(coffeeid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });






                }

            }
        });


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 0) {

//                    Toast.makeText(getContext(), "Nothing in Cart", Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));
                    quantity = 0;
                    totalPrice = 0;












                } else {



                    quantity--;
                    quantityview.setText(String.valueOf(quantity));

                    // showing the price
                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));


                    //updating quantity
                    firebaseFirestore.collection("Coffies").document(coffeeid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });



                }



            }
        });



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (quantity == 0) {

                    navController.navigate(R.id.action_coffeeDetailFragment_to_allCoffeeListFragment);
                    Toast.makeText(getContext(), "You did not order " + name, Toast.LENGTH_SHORT).show();



                } else {


                    AddToCart();

                    CoffeeDetailFragmentDirections.ActionCoffeeDetailFragmentToAllCoffeeListFragment
                            action = CoffeeDetailFragmentDirections.actionCoffeeDetailFragmentToAllCoffeeListFragment();

                    action.setQuantity(quantity);
                    navController.navigate(action);
                    Toast.makeText(getContext(), "You've ordered " + name, Toast.LENGTH_SHORT).show();



                }

            }
        });





    }

    private void AddToCart() {



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("coffeename", name);
        hashMap.put("quantity", quantity);
        hashMap.put("totalprice", totalPrice);
        hashMap.put("coffeeid", coffeeid);
//        hashMap.put("imageURL", imageURL);





//           creating new collection for cart
        firebaseFirestore.collection("Cart").document(name).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {



            }
        });




    }
}