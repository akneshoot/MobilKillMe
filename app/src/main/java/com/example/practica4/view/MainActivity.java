package com.example.practica4.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.example.practica4.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }

        app_specific_storage();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        common_storage();
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Log.d("MESSAGE", sharedText);
        }
    }


    public void app_specific_storage(){
        Context context = getApplicationContext();
        String fileName = "Text.txt";
        File directory = context.getFilesDir();
        System.out.println(getFilesDir().toString());;
        File file = new File(directory, fileName);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append("Вывод текста");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis = null;
        try {
            fis = context.openFileInput("Text.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader inputStreamReader =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
        } finally {
            String contents = stringBuilder.toString();
            Log.d("Saved", contents);
        }
    }
    public void common_storage(){
        File directory = Environment.getExternalStorageDirectory();
        File file1 = new File(directory, "Text1.txt");
        try {
            FileWriter writer = new FileWriter(file1);
            writer.write("Вывод другого текста");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        StringBuilder contentBuilder = new StringBuilder();
        directory.mkdirs();
        try {
            File file2 = new File(directory,"Text1.txt");
            FileInputStream inputStream = new FileInputStream(file2);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = contentBuilder.toString();
        Log.d("Saved", content);
    }
}




