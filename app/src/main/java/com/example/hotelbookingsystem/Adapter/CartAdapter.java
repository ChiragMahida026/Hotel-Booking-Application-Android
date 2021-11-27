package com.example.hotelbookingsystem.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.example.hotelbookingsystem.Model.CartModel;
import com.example.hotelbookingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    FirebaseFirestore db;
    List<CartModel> cartModelList;

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

        //Connect database to remove to cart item

//        db=FirebaseFirestore.getInstance();
//
//        holder.removecart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(holder.imageOfCoffee.getContext());
//                builder.setTitle("Delete Panel");
//                builder.setMessage("Delete...?");
//
//                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        db.collection("Cart").document(cartModelList.get(i).getRoomid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful())
//                                {
//                                    AlertDialog.Builder builders=new AlertDialog.Builder(holder.imageOfCoffee.getContext());
//                                    builders.setTitle("Delete Panel");
//                                    builders.setMessage("Done");
//                                }
//                            }
//                        });
//
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//
//                    }
//                });
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void setCartModelList(List<CartModel> cartModelList) {
        this.cartModelList = cartModelList;
    }
    class CartHolder extends ViewHolder{

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