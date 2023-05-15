package com.example.practica4.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.practica4.R;
import com.example.practica4.viewmodel.Imagechange;
import com.example.practica4.viewmodel.MyWorker;
import com.example.practica4.viewmodel.Mymodel;



public class FirstFragment extends Fragment {

    private static final String TAG = "MyApp";

    private ImageView imageView;
    private Imagechange imageChange;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final int SEARCH_COUNT = 0;

    public Context context;
    private int count = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel Description");
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        Button button1 = getView().findViewById(R.id.Button1);
        Button button2 = getView().findViewById(R.id.Button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myArg = "Hello from Second Fragment";
                Bundle bundle = new Bundle();
                bundle.putString("myArg", myArg);
                Navigation.findNavController(view).navigate(R.id.action_firstFragment_to_secondFragment, bundle);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myArg = "Hello from Third Fragment";
                Bundle bundle = new Bundle();
                bundle.putString("myArg", myArg);
                Navigation.findNavController(view).navigate(R.id.action_firstFragment_to_thirdFragment, bundle);
            }
        });

        EditText editText = getView().findViewById(R.id.editTextTextPersonName);

        Mymodel viewModel = new ViewModelProvider(this).get(Mymodel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), text_changer -> {
            String changed_text = text_changer.getEdited_text();
            System.out.println(changed_text);
        });
        viewModel.createMV(getActivity().getApplicationContext());
        Button button3 = getView().findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Log.i(TAG, "Запоминание имени");

                viewModel.Change_text(editText.getText().toString());
                saveData();
            }
        });
        loadData();

        Button button4 = getView().findViewById(R.id.subscribe);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                String query = editText.getText().toString();
                String url = "https://t.me/" + query;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        imageView = getView().findViewById(R.id.imageView9);
        imageChange = new Imagechange(R.drawable.vocabulary);

        Button buttonChangeImage = getView().findViewById(R.id.buttonChangeImage);
        Mymodel viewModel2 = new ViewModelProvider(this).get(Mymodel.class);


        viewModel2.getBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                Log.d("Done", "Picture done!");
            }
        });

        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel2.execute();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewModel2.executeRevert();
                    }
                }, 5000);
            }
        });

        Button send_info = view.findViewById(R.id.button);
        WorkManager workManager = WorkManager.getInstance();
        Data.Builder builder = new Data.Builder();
        builder.putString("key", "https://i.pinimg.com/564x/4d/c3/99/4dc39900d51fcc28d7344cd3e68ad6c4.jpg");
        OneTimeWorkRequest newRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(builder.build())
                .build();

        send_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workManager.enqueue(newRequest);

            }
        });

    }



    public void saveData() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(String.valueOf(SEARCH_COUNT), count+1);
        Log.d("Storages", Integer.toString(count+1));
        count += 1;
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        count = sharedPreferences.getInt(String.valueOf(SEARCH_COUNT), 1);
    }


}

