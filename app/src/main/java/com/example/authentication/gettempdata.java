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

                Call<TemperatureResult[]> call = retrofitInterface.executetempget();

                call.enqueue(new Callback<TemperatureResult[]>() {
                    @Override
                    public void onResponse(Call<TemperatureResult[]> call, Response<TemperatureResult[]> response) {
                        if(response.code()==200)
                        {
                            Toast.makeText(gettempdata.this,
                                    response.toString(), Toast.LENGTH_LONG).show();
                            TemperatureResult[] resultx = response.body();
                            lst = new ArrayList<>();
                            String a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, index;

                            Integer indexx;
                            TemperatureResult result;

                            for(int z = 0; z < 5; z++)
                            {
                                result = resultx[z];
                                lst.add(result.getA());
                                lst.add(result.getB());
                                lst.add(result.getC());
                                lst.add(result.getD());
                                lst.add(result.getE());
                                lst.add(result.getF());
                                lst.add(result.getG());
                                lst.add(result.getH());
                                lst.add(result.getI());
                                lst.add(result.getJ());
                                lst.add(result.getK());
                                lst.add(result.getL());
                                lst.add(result.getM());
                                lst.add(result.getN());
                                lst.add(result.getO());
                                lst.add(result.getP());
                                lst.add(result.getQ());
                                lst.add(result.getR());
                                lst.add(result.getS());
                                lst.add(result.getT());
                            }


                            responseText.setTextSize(25);
                            responseText.setText(String.valueOf(lst));




                        }
                        else if(response.code()==400){
                            Toast.makeText(gettempdata.this,
                                    "Sent Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TemperatureResult[]> call, Throwable t) {
                        Toast.makeText(gettempdata.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    }