package com.example.practica4.viewmodel;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {

    static final String TAG = "WorkerClass";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");
        String our_url = getInputData().getString("key");
        Bitmap bitmap = null;
        try {
            TimeUnit.SECONDS.sleep(10);
            URL url = new URL(our_url);
            bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "doWork: end");
        assert bitmap != null;
        Data outputData = new Data.Builder().
                putString("key", bitmap.toString())
                .build();
        return Result.success(outputData);
    }
}

