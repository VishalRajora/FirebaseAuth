package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class signUpActivity extends AppCompatActivity {



    //for store the id or use in user ID var
    String user_ID;
    //views
    TextInputLayout username, age, phone, email, password;
    Button signUpButton;
    TextView logInTextView;
    //alert Dialog for display processing
    AlertDialog alertDialog;
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    //Declare an instance of FirebaseAuth
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_up );


        //initialize the FireStore for Saving phone , email , username
        db = FirebaseFirestore.getInstance ();

        //initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance ();

        //for getting current user
        FirebaseUser currentUser = mAuth.getCurrentUser ();

        //check if user exist or not if exist then get the ID of the user
        currentUser = mAuth.getCurrentUser ();

        if (currentUser != null) {
            startActivity ( new Intent ( signUpActivity.this , NotesActivity.class ) );
            finish ();
        }

        //view casting
        //TextInputLayout
        username = findViewById ( R.id.signup_user );
        email = findViewById ( R.id.signup_email );
        age = findViewById ( R.id.signup_age );
        phone = findViewById ( R.id.signup_contact );
        password = findViewById ( R.id.signup_password );

        //buttons
        signUpButton = findViewById ( R.id.signup_button );
        logInTextView = findViewById ( R.id.signup_loginbutton );

        signUpButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                createAccount ();
            }
        } );

        logInTextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent ( getApplicationContext () , MainActivity.class );
                startActivity ( intent );

            }
        } );
        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( this );
        LayoutInflater layoutInflater = LayoutInflater.from ( this );
        View view = layoutInflater.inflate ( R.layout.progress_dialog , null );
        myDialog.setView ( view );
        alertDialog = myDialog.create ();
        alertDialog.setCancelable ( false );
        // alertDialog.getWindow ().setLayout ( 850,1200 );

    }

    //create an Account
    void createAccount() {
        final String user_name = username.getEditText ().getText ().toString ().trim ();
        final String user_age = age.getEditText ().getText ().toString ().trim ();
        final String user_phone = phone.getEditText ().getText ().toString ().trim ();
        final String user_email = email.getEditText ().getText ().toString ().trim ();
        String user_password = password.getEditText ().getText ().toString ().trim ();

        if (user_name.isEmpty ()) {
            username.setError ( "Field Can't be empty" );
            username.requestFocus ();
            return;
        } else {
            username.setError ( null );
        }

        if (user_age.isEmpty ()) {
            age.setError ( "Field Can't be empty" );
            age.requestFocus ();
            return;
        } else if (user_age.length () >= 3) {
            age.setError ( "Enter Valid age" );
            age.requestFocus ();
            return;
        } else {
            age.setError ( null );
        }

        if (user_phone.isEmpty ()) {
            phone.setError ( "Field Can't be empty" );
            phone.requestFocus ();
            return;
        } else {
            phone.setError ( null );
        }
        if (user_phone.length () < 10) {
            phone.setError ( "Enter Valid Mobile Number" );
            phone.requestFocus ();
            return;
        }
        if(!user_phone.matches ("[1-9][0-9]{9}"))
        {
            phone.setError ( "Enter Valid Mobile Number" );
            phone.requestFocus ();
            return;
        }
        if (user_email.isEmpty ()) {
            email.setError ( "Field Can't be empty" );
            email.requestFocus ();
            return;
        } else {
            email.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( user_email ).matches ()) {
            email.setError ( "Please provide valid email" );
        } else {
            email.setError ( null );
        }
        if (user_password.isEmpty ()) {
            password.setError ( "Field Can't be empty" );
            password.requestFocus ();
            return;
        } else {
            password.setError ( null );
        }
        if (user_password.length () < 6) {
            password.setError ( "Min password length should be 6 character!" );
            password.requestFocus ();
            return;
        } else {
            password.setError ( null );
        }
        alertDialog.show ();

        mAuth.createUserWithEmailAndPassword ( user_email , user_password )
                .addOnCompleteListener ( this , new OnCompleteListener < AuthResult > () {
                    @Override
                    public void onComplete(@NonNull Task < AuthResult > task) {

                        if (task.isSuccessful ()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance ().getCurrentUser ();
                            AuthUserDetail authUserDetail = new AuthUserDetail ( user_name , user_age , user_email , user_phone );
                            user_ID = current_user.getUid ();
                            DocumentReference documentReference = db.collection ( "User" ).document ( user_ID );

                            documentReference.set ( authUserDetail ).addOnSuccessListener ( new OnSuccessListener < Void > () {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alertDialog.dismiss ();
                                    Log.d ( "YourData" , user_ID );
                                    startActivity ( new Intent ( getApplicationContext () , NotesActivity.class ) );
                                    finish ();

                                }
                            } ).addOnFailureListener ( new OnFailureListener () {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    alertDialog.dismiss ();
                                }
                            } );
                        } else {
                            alertDialog.dismiss ();
                            Toast.makeText ( signUpActivity.this , "Failed" + task.getException ().toString ()
                                    , Toast.LENGTH_LONG ).show ();

                        }
                    }
                } );
    }
}