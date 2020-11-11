package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //views Casting
    TextInputLayout email, pasword;
    Button loginButtonPress;
    TextView createAccountTextView, forgetPasswordtextView;

    //Alert Dialog
    AlertDialog alertDialog;

    //firebase
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );


//        //initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance ();


        //Alert Dialog
        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( this );
        LayoutInflater layoutInflater = LayoutInflater.from ( this );
        View view = layoutInflater.inflate ( R.layout.progress_dialog , null );
        myDialog.setView ( view );
        alertDialog = myDialog.create ();
        alertDialog.setCancelable ( false );


        //View Casting
        email = this.findViewById ( R.id.Login_email );
        pasword = this.findViewById ( R.id.Login_password );

        //loginButton
        loginButtonPress = findViewById ( R.id.loginButton_login );
        loginButtonPress.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                appLogin ();

            }
        } );
//
//        //signupTextView
        createAccountTextView = findViewById ( R.id.signupbutton_login );
        createAccountTextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( MainActivity.this ,
                        signUpActivity.class ) );
                finish ();
            }
        } );

        //foregePasswordTextView
        forgetPasswordtextView = findViewById ( R.id.forgot_password );
        forgetPasswordtextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                startActivity ( new Intent ( MainActivity.this , Forgot_password.class ) );
                finish ();
            }
        } );


    }

    @Override
    protected void onStart() {
        super.onStart ();

        FirebaseUser currentUser = mAuth.getCurrentUser ();

        if (currentUser != null) {
            startActivity ( new Intent ( MainActivity.this , NotesActivity.class ) );
            finish ();
        }
    }

    private void appLogin() {
        String login_email = email.getEditText ().getText ().toString ().trim ();
        String login_password = pasword.getEditText ().getText ().toString ().trim ();

        if (login_email.isEmpty ()) {
            email.setError ( "Field can't be empty" );
            email.requestFocus ();
            return;
        } else {
            email.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( login_email ).matches ()) {
            email.setError ( "Enter Correct Email" );
            email.requestFocus ();
            return;
        } else {
            email.setError ( null );
        }
        if (login_password.isEmpty ()) {
            pasword.setError ( "Field can't be empty" );
            pasword.requestFocus ();
            return;
        } else {
            pasword.setError ( null );
        }
        if (login_password.length () < 6) {
            pasword.setError ( "Min password length should be 6 character!" );
            pasword.requestFocus ();
            return;
        } else {
            pasword.setError ( null );
        }

        alertDialog.show ();


        mAuth.signInWithEmailAndPassword ( login_email , login_password )
                .addOnCompleteListener ( new OnCompleteListener < AuthResult > () {
                    @Override
                    public void onComplete(@NonNull Task < AuthResult > task) {
                        if (task.isSuccessful ()) {
                            FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();

                            if (user.isEmailVerified ()) {
                                alertDialog.dismiss ();
                                startActivity ( new Intent ( MainActivity.this , NotesActivity.class ) );
                            } else {
                                alertDialog.dismiss ();
                                user.sendEmailVerification ();
                                MaterialAlertDialogBuilder myBuilderDialog = new MaterialAlertDialogBuilder ( MainActivity.this );
                                myBuilderDialog.setMessage ( "Check your email to verify your account! " );
                                AlertDialog myDialog = myBuilderDialog.create ();
                                myDialog.setCancelable ( true );
                                myDialog.show ();
                            }
                        } else {
                            Toast.makeText ( MainActivity.this , "Failed  Login" + task.getException ().getMessage () , Toast.LENGTH_LONG ).show ();
                            alertDialog.dismiss ();

                        }

                    }
                } );
    }


}

