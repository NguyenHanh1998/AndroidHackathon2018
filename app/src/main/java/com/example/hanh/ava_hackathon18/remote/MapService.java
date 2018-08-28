package com.example.hanh.ava_hackathon18.remote;

import com.example.hanh.ava_hackathon18.model.AnswerInsrtuct;
import com.example.hanh.ava_hackathon18.model.Map;
import com.example.hanh.ava_hackathon18.model.AnswerInsrtuct;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MapService {

    @POST("api/local")
    Call<Map> createMap(@Header("Content-Type") String contentType,
                        @Body java.util.Map local);

    @GET("/api/local")
    Call<AnswerInsrtuct> getWaypoint(@Query("start_lat") Double startLocationlat,
                                     @Query("start_lng") Double startLocationlng,
                                     @Query("end_lat") Double endLocationlat,
                                     @Query("end_lng") Double endLocationlng);


}
