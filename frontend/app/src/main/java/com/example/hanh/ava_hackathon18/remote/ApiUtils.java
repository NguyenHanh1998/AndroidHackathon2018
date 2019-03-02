package com.example.hanh.ava_hackathon18.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://13.250.21.177/";

    public static MapService getMapService() {
        return RetrofitClient.getClient(BASE_URL).create(MapService.class);

    }
}
