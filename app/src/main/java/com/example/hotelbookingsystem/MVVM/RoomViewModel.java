package com.example.hotelbookingsystem.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelbookingsystem.Model.roommodel;

import java.util.List;

public class RoomViewModel extends ViewModel implements repositry.roomList {

    MutableLiveData<List<roommodel>> mutableLiveData = new MutableLiveData<List<roommodel>>();
    repositry repositoryus = new repositry(this);

    public RoomViewModel() {
        repositoryus.getroom();
    }

    public LiveData<List<roommodel>> getRoomList()
    {
        return mutableLiveData;
    }
    @Override
    public void roomLists(List<roommodel> roommodel) {
        mutableLiveData.setValue(roommodel);
    }



}
