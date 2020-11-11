package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class NotesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_notes );

        mAuth = FirebaseAuth.getInstance ();


    }

    public void logout(View view) {
        mAuth.getInstance().signOut();
        startActivity ( new Intent ( NotesActivity.this , MainActivity.class ) );
        finish ();
    }
}