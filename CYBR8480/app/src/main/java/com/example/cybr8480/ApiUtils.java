package com.example.cybr8480;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class ApiUtils {

    private ApiUtils() {

    }

    public static String url = "http:://localhost:9200/phone1/doc/";

    public static ElasticService getService() {
        return RetrofitClient.getClient(url).create(ElasticService.class);
    }


}
