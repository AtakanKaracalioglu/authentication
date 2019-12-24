package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class gettempdata extends AppCompatActivity {

    private Button button;
    public static  ArrayList<String> lst;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gettempdata);
        button = findViewById(R.id.buttonnn);
        final TextView responseText = findViewById(R.id.texttt);
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Call<TemperatureResult> call = retrofitInterface.executetempget();

                call.enqueue(new Callback<TemperatureResult>() {
                    @Override
                    public void onResponse(Call<TemperatureResult> call, Response<TemperatureResult> response) {
                        if(response.code()==200)
                        {
                            Toast.makeText(gettempdata.this,
                                    response.toString(), Toast.LENGTH_LONG).show();
                            TemperatureResult result = response.body();
                            lst = new ArrayList<>();
                            String a = result.getA();
                            String b = result.getB();
                            String c = result.getC();
                            String d = result.getD();
                            String e = result.getE();
                            String f = result.getF();
                            String g = result.getG();
                            String h = result.getH();
                            String i = result.getI();
                            String j = result.getJ();
                            String k = result.getK();
                            String l = result.getL();
                            String m = result.getM();
                            String n = result.getN();
                            String o = result.getO();
                            String p = result.getP();
                            String r = result.getR();
                            String s = result.getS();
                            String t = result.getT();
                            lst.add(a);
                            lst.add(b);
                            lst.add(c);
                            lst.add(d);
                            lst.add(e);
                            lst.add(f);
                            lst.add(g);
                            lst.add(h);
                            lst.add(i);
                            lst.add(j);
                            lst.add(k);
                            lst.add(l);
                            lst.add(m);
                            lst.add(n);
                            lst.add(o);
                            lst.add(p);
                            lst.add(r);
                            lst.add(s);
                            lst.add(t);
                            responseText.setTextSize(25);
                            responseText.setText(String.valueOf(lst));




                        }
                        else if(response.code()==400){
                            Toast.makeText(gettempdata.this,
                                    "Sent Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TemperatureResult> call, Throwable t) {
                        Toast.makeText(gettempdata.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    }