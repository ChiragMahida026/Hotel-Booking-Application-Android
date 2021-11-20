package com.example.hotelbookingsystem.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelbookingsystem.CofeModel;

import java.util.List;

public class CoffeeViewModel extends ViewModel implements Repositoryu.CoffeeList {

    MutableLiveData<List<CofeModel>> mutableLiveData = new MutableLiveData<List<CofeModel>>();
    Repositoryu repositoryu = new Repositoryu(this);


    public CoffeeViewModel() {

        repositoryu.getCoffee();
    }



    public LiveData<List<CofeModel>> getCofeeList(){
        return mutableLiveData;
    }


    @Override
    public void coffeeLists(List<CofeModel> cofeModels) {
        mutableLiveData.setValue(cofeModels);
    }
}
