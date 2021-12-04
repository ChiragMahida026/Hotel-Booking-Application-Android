package com.example.hotelbookingsystem;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbookingsystem.Model.roommodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class descfragment extends Fragment {


    NavController navController;
    int quantity = 0;
    FirebaseFirestore firestore;
    Button add, sub, order,datepick;
    TextView roomname, roomdescription, roomprices, roomavability, orderINFO,nameholder,demo1,new2,text68;
    String roomnames, roomdescriptions, imageURL,roomid;
    ImageView imagegholder,tvteam3,new1;
    int price = 0;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ProgressDialog pd;
    CheckBox checkbox1;


    int totalPrice = 0;

    public descfragment() {

    }

    public descfragment(String title, String editdescs, int editroomava, int editprice, String image) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_descfragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        demo1=view.findViewById(R.id.demo1);
        new2=view.findViewById(R.id.new2);
        tvteam3=view.findViewById(R.id.tvteam3);
        new1=view.findViewById(R.id.new1);
        text68=view.findViewById(R.id.text68);
        imagegholder = view.findViewById(R.id.imagegholder);
        nameholder=view.findViewById(R.id.nameholder);
        roomname = view.findViewById(R.id.courseholder);
        roomdescription = view.findViewById(R.id.emailholder);
        roomprices = view.findViewById(R.id.roomprice);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        roomavability = view.findViewById(R.id.quantityDETAILnUMBER);
        firestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.orderdetail);
        datepick=view.findViewById(R.id.datepick);
        orderINFO = view.findViewById(R.id.orderINFO);
        checkbox1 = view.findViewById(R.id.checkbox1);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();



        pd=new ProgressDialog(getContext());

        roomid = descfragmentArgs.fromBundle(getArguments()).getRoomid();
        roomnames = descfragmentArgs.fromBundle(getArguments()).getRoomname();
        imageURL = descfragmentArgs.fromBundle(getArguments()).getImageurl();
        roomdescriptions = descfragmentArgs.fromBundle(getArguments()).getRoomdescription();
        price = descfragmentArgs.fromBundle(getArguments()).getPrice();


        Glide.with(view.getContext()).load(imageURL).into(imagegholder);
        nameholder.setText(roomnames.toString());
        roomname.setText(roomnames + "₹" + String.valueOf(price));
        roomprices.setText("₹"+String.valueOf(price));
        roomdescription.setText(roomdescriptions);

        if(price >= 12400 && price < 15000) {
            tvteam3.setVisibility(View.GONE);
            demo1.setVisibility(View.GONE);

            new1.setVisibility(View.VISIBLE);
            new2.setVisibility(View.VISIBLE);
        }
        if(price >= 2000 && price < 5000) {
            tvteam3.setVisibility(View.VISIBLE);
            demo1.setVisibility(View.VISIBLE);

            new1.setVisibility(View.GONE);
            new2.setVisibility(View.GONE);

        }
        if(price >= 300 && price < 400) {
            tvteam3.setVisibility(View.VISIBLE);
            demo1.setVisibility(View.VISIBLE);

            new1.setVisibility(View.GONE);
            new2.setVisibility(View.GONE);
        }

        CalendarConstraints.Builder constrainbuilder=new CalendarConstraints.Builder();
        constrainbuilder.setValidator(DateValidatorPointForward.now());

        //datetimepicker

        MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.dateRangePicker();
        builder.setCalendarConstraints(constrainbuilder.build());
        final MaterialDatePicker datePicker=builder.build();

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getChildFragmentManager(),"DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        text68.setVisibility(View.VISIBLE);
                        text68.setText("Selected Date is : "+datePicker.getHeaderText());
                    }
                });
            }
        });

        //fetching recent quantity and display
        firestore.collection("Notice").document(roomid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value,FirebaseFirestoreException error) {
                roommodel roommodel=value.toObject(roommodel.class);
                quantity=roommodel.getQuantity();
                roomavability.setText(String.valueOf(quantity));

                totalPrice=price*quantity;
                orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
            }
        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox1.isChecked())
                {
                    Toast.makeText(getContext(), "You want extra bed sheet", Toast.LENGTH_SHORT).show();
                    totalPrice=(price*quantity)+20;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

                }
                else
                {
                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 3) {

                    Toast.makeText(getContext(), "Cant Book More than 3", Toast.LENGTH_SHORT).show();
                    roomavability.setText(String.valueOf(quantity));

                } else {
                    quantity++;
                    roomavability.setText(String.valueOf(quantity));

                    // showing the price
                    totalPrice = quantity * price;

                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));
                    //updating quantities
                    firestore.collection("Notice").document(roomid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

            }

        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 0) {

                    Toast.makeText(getContext(), "Nothing to select", Toast.LENGTH_SHORT).show();
                    roomavability.setText(String.valueOf(quantity));
                    quantity = 0;
                    totalPrice = 0;

                }
                else {
                    quantity--;
                    roomavability.setText(String.valueOf(quantity));

                    // showing the price
                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));


                    //updating quantity
                    firestore.collection("Notice").document(roomid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    navController.navigate(R.id.action_descfragment_to_recfragment);
                    Toast.makeText(getContext(), "You did not Booking " + roomnames, Toast.LENGTH_SHORT).show();
                } else {
                    AddToCart();
                    descfragmentDirections.ActionDescfragmentToRecfragment
                            action = descfragmentDirections.actionDescfragmentToRecfragment();
                    action.setRoomquantity(quantity);
                    navController.navigate(action);
                    Toast.makeText(getContext(), "You've Booking " + roomnames, Toast.LENGTH_SHORT).show();
                }
            }
            private void AddToCart() {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("roomname", roomnames);
                hashMap.put("quantity", quantity);
                hashMap.put("totalprice", totalPrice);
                hashMap.put("roomid", roomid);
                hashMap.put("imageURL", imageURL);

//           creating new collection for cart
                firestore.collection("Cart").document(roomnames).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        pd.setMessage("Adding");
                    }

                });
            }
        });
    }
}