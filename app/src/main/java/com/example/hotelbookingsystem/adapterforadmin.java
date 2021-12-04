package com.example.hotelbookingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class adapterforadmin extends FirestoreRecyclerAdapter<Noticedata,adapterforadmin.adminforadapter> {


    public adapterforadmin(@NonNull FirestoreRecyclerOptions<Noticedata> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull adminforadapter holder, int position, @NonNull Noticedata model) {

        holder.t1ss.setText(model.getTitle());
        holder.t2ss.setText(model.getEditdescs());
        Glide.with(holder.img1ss.getContext()).load(model.getImage()).into(holder.img1ss);

//        holder.img1ss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppCompatActivity activity=(AppCompatActivity)view.getContext();
//
////                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new (model.getTitle(),model.getEditdescs(),model.getEditroomava(),model.getEditprice(),model.getImage())).addToBackStack(null).commit();
//            }
//        });
    }

    @NonNull
    @Override
    public adminforadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowadmin,parent,false);
        return new adminforadapter(view);
    }

    class adminforadapter extends RecyclerView.ViewHolder
    {
        TextView t1ss,t2ss;
        ImageView img1ss;

        public adminforadapter(@NonNull View itemView) {
            super(itemView);

            t1ss=itemView.findViewById(R.id.t1ss);
            t2ss=itemView.findViewById(R.id.t2ss);
            img1ss=itemView.findViewById(R.id.img1ss);
        }
    }
}
