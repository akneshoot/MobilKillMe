package com.example.practica4.model;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {
    private final ImgService apiService;

    public DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://meow.senither.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ImgService.class);
    }

    public LiveData<DataItemModel> getRandomData() {
        MutableLiveData<DataItemModel> data = new MutableLiveData<>();

        apiService.getRandomData().enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {
                if (response.isSuccessful()) {
                    DataModel dataModel = response.body();
                    if (dataModel != null) {
                        DataItemModel dataItemModel = dataModel.getData();
                        data.setValue(dataItemModel);
                    } else {
                        Log.e("DataRepository", "Error");
                    }
                } else {
                    Log.e("DataRepository", "Unsuccessful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataModel> call, Throwable t) {
                Log.e("DataRepository", "Error: " + t.getMessage());
            }
        });

        return data;
    }
}

