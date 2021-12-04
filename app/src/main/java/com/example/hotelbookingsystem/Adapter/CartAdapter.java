package com.example.hotelbookingsystem.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.example.hotelbookingsystem.MainActivity;
import com.example.hotelbookingsystem.Model.CartModel;
import com.example.hotelbookingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    FirebaseFirestore db;
    List<CartModel> cartModelList;
    String name;

    NavController navController;

    public CartAdapter() {
    }



    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roomcartlistlayout, parent, false);
        return new CartHolder(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CartAdapter.CartHolder holder, int position) {



        Glide.with(holder.itemView.getContext()).load(cartModelList.get(position).getImageURL()).into(holder.imageOfCoffee);

        holder.price.setText(" Booked " + String.valueOf(cartModelList.get(position).getQuantity())
                + " for ₹" + String.valueOf(cartModelList.get(position).getTotalprice()));

        holder.name.setText(cartModelList.get(position).getRoomname());

         name=cartModelList.get(position).getRoomname();

        db=FirebaseFirestore.getInstance();



        holder.removecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map<String,Object> nots=new HashMap<>();
//                nots.put("name",);
                    Deletedata();
//                navController = Navigation.findNavController(v);

                Toast.makeText(v.getContext(), "Remove Successfully", Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();
//                navController.navigate(R.id.mainActivity);

            }

        });

    }

    private void Deletedata() {

        db.collection("Cart").whereEqualTo("roomname",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty())
                {
                    DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                    String documnetid=documentSnapshot.getId();
                    db.collection("Cart").document(documnetid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           e.printStackTrace();
                        }
                    });
                    notifyDataSetChanged();
                }
                else
                {


                }


            }

        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void setCartModelList(List<CartModel> cartModelList) {
        this.cartModelList = cartModelList;
    }
    class CartHolder extends ViewHolder {

        TextView name, price;
        ImageView imageOfCoffee;
        Button removecart;


        public CartHolder( View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cartcoffeename);
            price = itemView.findViewById(R.id.orderdetail);
            imageOfCoffee = itemView.findViewById(R.id.cartImage);
            removecart=itemView.findViewById(R.id.removecart);


        }


    }
}