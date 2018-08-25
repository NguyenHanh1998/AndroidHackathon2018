package com.example.hanh.ava_hackathon18.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
        public static Retrofit retrofit = null;
        public static Retrofit getClient(String baseUrl) {
              return  retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
}
