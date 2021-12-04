package com.example.hotelbookingsystem.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.hotelbookingsystem.Model.roommodel;
import com.example.hotelbookingsystem.R;

import org.w3c.dom.Text;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomListHolder> {

    List<roommodel> roommodelList;

    GetOneRoom interfacegetrooms;

    public RoomAdapter(GetOneRoom interfacegetrooms) {
        this.interfacegetrooms = interfacegetrooms;
    }

    @NonNull
    @Override
    public RoomListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new RoomListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListHolder holder, int position) {

        //showing customer card of all data here

        holder.nametext.setText(roommodelList.get(position).getTitle());
        holder.coursetext.setText(String.valueOf(roommodelList.get(position).getEditdescs()));
       holder.emailtext.setText( String.valueOf(roommodelList.get(position).getEditprice()+"₹"));

        Glide.with(holder.itemView.getContext()).load(roommodelList.get(position).getImage()).into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return roommodelList.size();
    }

    public void setRoommodelList(List<roommodel> roommodelList)
    {
        this.roommodelList=roommodelList;
    }

    class RoomListHolder extends ViewHolder implements View.OnClickListener
    {
        TextView nametext,coursetext,emailtext;
        ImageView img1;



        public RoomListHolder(@NonNull View itemView) {
            super(itemView);

            nametext=itemView.findViewById(R.id.nametext);
            coursetext=itemView.findViewById(R.id.coursetext);
            emailtext=itemView.findViewById(R.id.emailtext);
            img1=itemView.findViewById(R.id.img1);

            nametext.setOnClickListener(this);
            coursetext.setOnClickListener(this);
            img1.setOnClickListener(this);
            emailtext.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            interfacegetrooms.clickedroom(getAdapterPosition(),roommodelList);
        }
    }

    public interface GetOneRoom
    {
        void clickedroom(int position,List<roommodel> roommodels);
    }


}