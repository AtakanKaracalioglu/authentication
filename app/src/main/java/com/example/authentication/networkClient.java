package com.example.authentication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class networkClient {
    public static final String BASE_URL = "http://192.168.43.82:3000";
    public static Retrofit retrofit;
    /*
    This public static method will return Retrofit client
    anywhere in the appplication
    */
    public static Retrofit getRetrofitClient() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}