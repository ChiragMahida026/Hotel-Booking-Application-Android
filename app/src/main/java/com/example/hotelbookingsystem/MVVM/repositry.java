package com.example.hotelbookingsystem.MVVM;

import com.example.hotelbookingsystem.Model.roommodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class repositry {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<roommodel> roommodelList = new ArrayList<>();

    roomList interfaceocfeelist;

    public repositry(roomList interfaceocfeelist) {
        this.interfaceocfeelist = interfaceocfeelist;
    }

    public void getroom() {

        firebaseFirestore.collection("Notice").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {

                    roommodelList.clear();

                    for (DocumentSnapshot ds : Objects.requireNonNull(task.getResult()).getDocuments()) {

                        roommodel roommodel = ds.toObject(roommodel.class);
                        roommodelList.add(roommodel);

                        interfaceocfeelist.roomLists(roommodelList);


                    }


                }

            }
        });

    }
    public interface roomList
    {
        void roomLists(List<roommodel> roommodel);
    }
}
