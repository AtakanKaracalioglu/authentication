package com.example.authentication;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface RetrofitInterface {
    @POST("/login")
    Call<loginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/sendgps")
    Call<SendGpsResult> executeSendGps(@Body HashMap<String, String> map);

    @POST("/sendmsg")
    Call<SendResult> executeSendMsg(@Body HashMap<String, String> map);


}