package com.example.authentication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
private Retrofit retrofit;
private RetrofitInterface retrofitInterface;
private String BASE_URL = "http://139.179.32.243:3000";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginDialog();
            }
        });
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignupDialog();
            }
        });
            }

    private void handleLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();
        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);
        loginBtn.setOnClickListener(new View.OnClickListener()
                                    {
                                        public void onClick(View view)
                                        {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("email",emailEdit.getText().toString());
                                            map.put("password",passwordEdit.getText().toString());
                                            Call<loginResult> call = retrofitInterface.executeLogin(map);
                                            call.enqueue(new Callback<loginResult>() {
                                                @Override
                                                public void onResponse(Call<loginResult> call, Response<loginResult> response) {
                                                      if(response.code() == 200)
                                                      {
                                                          loginResult result = response.body();
                                                          AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                                          builder1.setTitle(result.getName());
                                                          builder1.setMessage(result.getEmail());

                                                          builder1.show();
                                                          Intent intent = new Intent(MainActivity.this , Main2Activity.class);
                                                          startActivity(intent);


                                                      }else if(response.code() == 404)
                                                      {
                                                          Toast.makeText(MainActivity.this,"Wrong bro", Toast.LENGTH_LONG).show();
                                                      }
                                                }

                                                @Override
                                                public void onFailure(Call<loginResult> call, Throwable t) {
                                                    Toast.makeText(MainActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
        );

    }
    private void handleSignupDialog() {
    View view = getLayoutInflater().inflate(R.layout.signup_dialog,null);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(view).show();

    Button signupBtn = view.findViewById(R.id.signup);
    final EditText nameEdit = view.findViewById(R.id.nameEdit);
    final EditText emailEdit = view.findViewById(R.id.emailEdit);
    final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

    signupBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HashMap<String,String> map = new HashMap<>();
            map.put("name",nameEdit.getText().toString());
            map.put("email",emailEdit.getText().toString());
            map.put("password",passwordEdit.getText().toString());

            Call<Void> call = retrofitInterface.executeSignup(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                   if(response.code() == 200)
                   {
                       Toast.makeText(MainActivity.this, "Signed Up succesfully",Toast.LENGTH_LONG).show();
                   }else if (response.code() == 400)
                   {
                       Toast.makeText(MainActivity.this, "Already Registered",Toast.LENGTH_LONG).show();

                   }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}