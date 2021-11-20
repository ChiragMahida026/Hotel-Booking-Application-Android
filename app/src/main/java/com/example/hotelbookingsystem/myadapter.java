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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<Noticedata,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<Noticedata> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Noticedata Noticedata) {
        holder.nametext.setText(Noticedata.getTitle());
        holder.coursetext.setText(Noticedata.getEditdescs());
        Glide.with(holder.img1.getContext()).load(Noticedata.getImage()).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new descfragment(Noticedata.getTitle(),Noticedata.getEditdescs(),Noticedata.getImage())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView nametext,coursetext,emailtext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            coursetext=itemView.findViewById(R.id.coursetext);
            emailtext=itemView.findViewById(R.id.emailtext);
        }
    }

}