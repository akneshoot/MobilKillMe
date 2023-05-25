package com.example.practica4.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.practica4.R;
import com.example.practica4.viewmodel.MyWorker;
import com.example.practica4.viewmodel.Mymodel;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.practica4.viewmodel.NetworkService;
import com.example.practica4.viewmodel.PlaceholderPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NewFragment extends Fragment {

    private Mymodel viewModel;

    private ImageView image1;
    private ImageView image2;

    private ImageView image3;



    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(Mymodel.class);

    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        image1 = view.findViewById(R.id.image1);
        loadDogAkitaImage();
        image2 = view.findViewById(R.id.image2);
        loadDogRedBoneImage();
        image3 = view.findViewById(R.id.image3);
        View button = view.findViewById(R.id.button1);

        viewModel = new ViewModelProvider(this).get(Mymodel.class);

        Mymodel finalViewModel = viewModel;
        button.setOnClickListener(v -> {
            finalViewModel.loadRandomImage();
        });

        viewModel.getImageData().observe(getViewLifecycleOwner(), dataItemModel -> {
            if (dataItemModel != null) {
                Glide.with(requireContext())
                        .load(dataItemModel.getUrl())
                        .into(image3);
            }
        });





        Button post_request = view.findViewById(R.id.button);
        post_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceholderPost placeholderPost = new PlaceholderPost();
                placeholderPost.setUserId(200);
                placeholderPost.setId(300);
                placeholderPost.setBody("A");
                placeholderPost.setTitle("AHAHA");

                NetworkService.getInstance()
                        .getJSONApi()
                        .postData(placeholderPost)
                        .enqueue(new Callback<PlaceholderPost>() {
                            @Override
                            public void onResponse(Call<PlaceholderPost> call, retrofit2.Response<PlaceholderPost> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    System.out.println("POST REQUEST " + response.body().toString());
                                } else {
                                    System.out.println("POST REQUEST Failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceholderPost> call, Throwable t) {
                                System.out.println("Error");
                                t.printStackTrace();
                            }
                        });
            }
        });

        return view;
    }



    private void loadDogAkitaImage() {
        RequestQueue volleyQueue = Volley.newRequestQueue(requireContext());
        String url = "https://dog.ceo/api/breed/akita/images/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String dogImageUrl = response.getString("message");
                            Glide.with(getActivity().getApplicationContext()).load(dogImageUrl).into(image1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext().getApplicationContext(),
                                "Ошибка!", Toast.LENGTH_LONG).show();
                        Log.e("Error", "Ошибка: " + error.getLocalizedMessage());
                    }
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }
    private void loadDogRedBoneImage() {
        RequestQueue volleyQueue = Volley.newRequestQueue(requireContext());
        String url = "https://dog.ceo/api/breed/redbone/images/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String dogImageUrl = response.getString("message");
                            Glide.with(getActivity().getApplicationContext()).load(dogImageUrl).into(image2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext().getApplicationContext(),
                                "Ошибка!", Toast.LENGTH_LONG).show();
                        Log.e("Error", "Ошибка: " + error.getLocalizedMessage());
                    }
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }

}
