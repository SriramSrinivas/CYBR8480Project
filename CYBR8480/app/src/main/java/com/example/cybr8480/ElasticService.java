package com.example.cybr8480;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ElasticService {
    @Headers("Content-Type: application/json")
    @POST("phone1/_doc/")

   Call<POST>  saveData(@Body SensorDataModel sensormodel

    );

//    @GET("phone1/doc/6/")
//    Call<GET> readdata();

}
