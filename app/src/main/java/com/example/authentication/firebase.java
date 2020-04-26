package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebase extends AppCompatActivity {
    EditText name;
    Member member;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.firetext);
        Button fire_but = (Button) findViewById(R.id.firebutton);
        member = new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        fire_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(name.getText().toString().trim());
                reff.push().setValue(member);
                Toast.makeText(firebase.this, "Connection succeded", Toast.LENGTH_LONG).show();


            }
        });
        //Toast.makeText(firebase.this, "Connection succeded", Toast.LENGTH_LONG).show();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
