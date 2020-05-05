package com.ankita.seventask.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepository {
    private static MyRepository newsRepository;
    APIInterface apiInterface;

    public static MyRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new MyRepository();
        }
        return newsRepository;
    }
    public MyRepository() {
        apiInterface= APIClient.getClient().create(APIInterface.class);

    }

    public MutableLiveData<List<CreateResponse.User>> getUser(int lim,int pos){
        final MutableLiveData<List<CreateResponse.User>> data=new MutableLiveData<>();
        apiInterface.doGetListResour(10,10).enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                if(response.isSuccessful()){
                    CreateResponse res=response.body();
                    data.setValue(res.data.userArrayList);
                }
            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {

            }
        });
        return data;
    }
}
