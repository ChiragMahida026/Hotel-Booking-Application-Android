package com.example.hotelbookingsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingsystem.Adapter.CoffeeAdapter;
import com.example.hotelbookingsystem.MVVM.CoffeeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllCoffeeListFragment extends Fragment implements CoffeeAdapter.GetOneCoffee {


    FirebaseFirestore firebaseFirestore;
    CoffeeAdapter mAdapter;
    RecyclerView recyclerView;
    CoffeeViewModel viewModel;
    NavController navController;
    int quantity = 0;
    FloatingActionButton fab;
    TextView quantityOnfAB;
    List<Integer> savequantity = new ArrayList<>();
    int quantitysum = 0;



    public AllCoffeeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_coffee_list, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quantityOnfAB = view.findViewById(R.id.quantityOnfAB);
        fab = view.findViewById(R.id.fab);

        firebaseFirestore  = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter  = new CoffeeAdapter(this);
        navController = Navigation.findNavController(view);


        viewModel = new ViewModelProvider(getActivity()).get(CoffeeViewModel.class);
        viewModel.getCofeeList().observe(getViewLifecycleOwner(), new Observer<List<CofeModel>>() {
            @Override
            public void onChanged(List<CofeModel> cofeModels) {

                mAdapter.setCofeModelList(cofeModels);
                recyclerView.setAdapter(mAdapter);

            }
        });



        quantity = AllCoffeeListFragmentArgs.fromBundle(getArguments()).getQuantity();


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
                navController.navigate(R.id.action_allCoffeeListFragment_to_cartFragment);

            }
        });





    }


    @Override
    public void clickedCoffee(int position, List<CofeModel> cofeModels) {

        String coffeeid = cofeModels.get(position).getCoffeeid();
        String description = cofeModels.get(position).getDescription();
        String coffeename = cofeModels.get(position).getCoffeename();
        int price = cofeModels.get(position).getPrice();
//        String imageURL = cofeModels.get(position).getImageURL();


        AllCoffeeListFragmentDirections.ActionAllCoffeeListFragmentToCoffeeDetailFragment
                action = AllCoffeeListFragmentDirections.actionAllCoffeeListFragmentToCoffeeDetailFragment();


        action.setCoffeename(coffeename);
        action.setDescription(description);
//        action.setImageurl(imageURL);
        action.setPrice(price);
        action.setId(coffeeid);

        navController.navigate(action);

    }

}