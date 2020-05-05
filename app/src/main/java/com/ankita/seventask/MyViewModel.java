package com.ankita.seventask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ankita.seventask.repository.CreateResponse;
import com.ankita.seventask.repository.MyRepository;

import java.util.List;

public class MyViewModel extends ViewModel {


    private MutableLiveData<List<CreateResponse.User>> mutableLiveData;
    private MyRepository myRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        myRepository = MyRepository.getInstance();
        mutableLiveData = myRepository.getUser(10, 10);

    }

    public MutableLiveData<List<CreateResponse.User>> getNewsRepository() {
        return mutableLiveData;
    }

}
