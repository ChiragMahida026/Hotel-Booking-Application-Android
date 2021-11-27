package com.example.hotelbookingsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hotelbookingsystem.Adapter.RoomAdapter;
import com.example.hotelbookingsystem.MVVM.RoomViewModel;
import com.example.hotelbookingsystem.Model.roommodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class recfragment extends Fragment implements RoomAdapter.GetOneRoom {

    FirebaseFirestore firebaseFirestore;
    RoomAdapter adapter;
    RecyclerView recview;
    RoomViewModel viewModel;
    FloatingActionButton fab;
    TextView quantityOnfAB;
    NavController navController;


//    myadapter adapter;

    public recfragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recfragment,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recview=(RecyclerView)view.findViewById(R.id.recview);

        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new RoomAdapter(this);
        navController= Navigation.findNavController(view);

        viewModel=new ViewModelProvider(getActivity()).get(RoomViewModel.class);
        viewModel.getRoomList().observe(getViewLifecycleOwner(), new Observer<List<roommodel>>() {
            @Override
            public void onChanged(List<roommodel> roommodels) {
                adapter.setRoommodelList(roommodels);
                recview.setAdapter(adapter);
            }
        });
    }

    @Override
    public void clickedroom(int position, List<roommodel> roommodels) {
        String roomname=roommodels.get(position).getTitle();
        String roomdescription=roommodels.get(position).getEditdescs();
        String roomprice=roommodels.get(position).getEditprice();
        String roomsavailable=roommodels.get(position).getEditroomava();
        String imageurls=roommodels.get(position).getImage();

        recfragmentDirections.ActionRecfragmentToDescfragment
                action=recfragmentDirections.actionRecfragmentToDescfragment();

        action.setRoomname(roomname);
        action.setRoomdescription(roomdescription);
        action.setImageurl(imageurls);
        action.setPrice(Integer.parseInt(roomprice));
        action.setRoomavailable(roomsavailable);


        navController.navigate(action);
    }
}


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view=inflater.inflate(R.layout.fragment_recfragment, container, false);
//
//        recview=(RecyclerView)view.findViewById(R.id.recview);
//        recview.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<Noticedata> options =
//                new FirebaseRecyclerOptions.Builder<Noticedata>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notice"), Noticedata.class)
//                        .build();
//
//        adapter=new myadapter(options);
//        recview.setAdapter(adapter);
//
//        return view;
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//}