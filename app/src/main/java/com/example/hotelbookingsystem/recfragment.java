package com.example.hotelbookingsystem;

import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbookingsystem.Adapter.RoomAdapter;
import com.example.hotelbookingsystem.MVVM.RoomViewModel;
import com.example.hotelbookingsystem.Model.CartModel;
import com.example.hotelbookingsystem.Model.roommodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class recfragment extends Fragment implements RoomAdapter.GetOneRoom {

    FirebaseFirestore firebaseFirestore;
    RoomAdapter adapter;
    RecyclerView recview;
    RoomViewModel viewModel;
    FloatingActionButton fab;
    TextView quantityOnfAB;
    NavController navController;
    int quantity = 0;
    List<Integer> savequantity = new ArrayList<>();
    int quantitysum = 0;


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

        firebaseFirestore  = FirebaseFirestore.getInstance();
        recview=(RecyclerView)view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter=new RoomAdapter(this);
        navController= Navigation.findNavController(view);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);

        quantityOnfAB = view.findViewById(R.id.quantityOnfAB);
        viewModel=new ViewModelProvider(getActivity()).get(RoomViewModel.class);
        viewModel.getRoomList().observe(getViewLifecycleOwner(), new Observer<List<roommodel>>() {
            @Override
            public void onChanged(List<roommodel> roommodels) {
                adapter.setRoommodelList(roommodels);
                recview.setAdapter(adapter);
            }
        });
        quantity = recfragmentArgs.fromBundle(getArguments()).getRoomquantity();

        firebaseFirestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds: task.getResult().getDocuments()) {

                        CartModel cartModel = ds.toObject(CartModel.class);
                        int initialquantity = cartModel.getQuantity();
                        savequantity.add(initialquantity);
                    }
                    for (int i =0; i < savequantity.size(); i++) {
                        quantitysum+= Integer.parseInt(String.valueOf(savequantity.get(i)));
                    }
                    quantityOnfAB.setText(String.valueOf(quantitysum));
                    quantitysum = 0;
                    savequantity.clear(); // unless we add something new to our list// previous records are cleared.

                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_recfragment_to_roomcartFragment);

            }
        });
    }

    @Override
    public void clickedroom(int position, List<roommodel> roommodels) {
        String roomid=roommodels.get(position).getRoomid();
        String roomname=roommodels.get(position).getTitle();
        String roomdescription=roommodels.get(position).getEditdescs();
        int roomprice=roommodels.get(position).getEditprice();
        int roomsavailable=roommodels.get(position).getEditroomava();
        String imageurls=roommodels.get(position).getImage();

        recfragmentDirections.ActionRecfragmentToDescfragment
                action=recfragmentDirections.actionRecfragmentToDescfragment();

        action.setRoomid(roomid);
        action.setRoomname(roomname);
        action.setRoomdescription(roomdescription);
        action.setImageurl(imageurls);
        action.setPrice(roomprice);
        action.setRoomavailable(roomsavailable);

        navController.navigate(action);
    }


}