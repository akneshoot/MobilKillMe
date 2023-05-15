package com.example.practica4.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.practica4.model.Arraydata;
import com.example.practica4.model.Repository;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Mymodel extends ViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private int NUMBER = 0;

    private Context context;
    private Repository<String> repository;



    public void onButtonClick(int item_id) {
        Log.d(TAG, String.valueOf(item_id));
    }
    private final MutableLiveData<Textchange> text_changer =
            new MutableLiveData(new Textchange(null));

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    private MutableLiveData<Integer> value;
    public LiveData<Integer> getValue(){
        if (value == null){
            value = new MutableLiveData<Integer>(0);
        }
        return value;
    };
    MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();

    public LiveData<Bitmap> getBitmap(){
        return bitmap;
    }

    public void execute() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                        TimeUnit.MILLISECONDS.sleep(100);

                    URL url = new URL("https://i.pinimg.com/564x/4d/c3/99/4dc39900d51fcc28d7344cd3e68ad6c4.jpg");
                    bitmap.postValue(BitmapFactory.decodeStream((InputStream) url.getContent()));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void executeRevert() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);


                    URL originalUrl = new URL("https://i.pinimg.com/564x/35/d0/b6/35d0b69708775ec7c61843e35de96fc8.jpg");
                    bitmap.postValue(BitmapFactory.decodeStream((InputStream) originalUrl.getContent()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void closeExecutor() {

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        System.out.println("Executor is closed");
    }



    public LiveData<Textchange> getData(){
        return text_changer;
    }
    public void createMV(Context context){
        this.context = context;
        repository = new Repository<>(new Arraydata<>(), context);
    }
    public void Change_text(String data) {

        text_changer.setValue(new Textchange(data));
        repository.add(data);
        repository.insert(data);
        NUMBER += 1;
        repository.getHistory();
        System.out.println(repository.getAll() + "DB");
    }
}