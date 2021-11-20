package com.example.hotelbookingsystem.MVVM;

import com.example.hotelbookingsystem.CofeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Repositoryu {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<CofeModel> cofeModelList = new ArrayList<>();


    CoffeeList interfaceocfeelist;


    public Repositoryu(CoffeeList interfaceocfeelist) {
        this.interfaceocfeelist = interfaceocfeelist;
    }

    public void getCoffee(){

        firebaseFirestore.collection("Coffies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {

                    cofeModelList.clear();

                    for (DocumentSnapshot ds: Objects.requireNonNull(task.getResult()).getDocuments()) {

                        CofeModel cofeModel  = ds.toObject(CofeModel.class);
                        cofeModelList.add(cofeModel);

                        interfaceocfeelist.coffeeLists(cofeModelList);


                    }


                }

            }
        });



    }

    public interface CoffeeList{
        void coffeeLists(List<CofeModel> cofeModels);
    }
}
