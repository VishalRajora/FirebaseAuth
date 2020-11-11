package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {
    //View Casting
    TextInputLayout forgotPasswordEditText;
    Button forgotPasswordButton;

    //firebase
    FirebaseAuth mAuth;

    //alert Dialog
    AlertDialog alertDialog ,processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_password );

        mAuth = FirebaseAuth.getInstance ();

        forgotPasswordEditText = findViewById ( R.id.ForgotPassword_email );
        forgotPasswordButton = findViewById ( R.id.ForgotPassword_button );

        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( this );
        LayoutInflater layoutInflater = LayoutInflater.from ( this );
        View view = layoutInflater.inflate ( R.layout.progress_dialog , null );
        myDialog.setView ( view );
        processDialog = myDialog.create ();
        processDialog.setCancelable ( false );

        forgotPasswordButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                forgotPassword ();

            }
        } );
    }

    public void forgotPassword() {
        String forgotPassword = forgotPasswordEditText.getEditText ().getText ().toString ().trim ();
        processDialog.show ();

        if (forgotPassword.isEmpty ()) {
            forgotPasswordEditText.setError ( "Field Can't be empty" );
            forgotPasswordEditText.requestFocus ();
            return;
        } else {
            forgotPasswordEditText.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( forgotPassword ).matches ()) {
            forgotPasswordEditText.setError ( "Please provide valid email" );
        } else {
            forgotPasswordEditText.setError ( null );


            mAuth.sendPasswordResetEmail ( forgotPassword ).addOnCompleteListener ( new OnCompleteListener < Void > () {
                @Override
                public void onComplete(@NonNull Task < Void > task) {

                    if (task.isSuccessful ()) {
                        processDialog.dismiss ();

                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( Forgot_password.this );
                        myDialog.setMessage ( "Check your email to reset  your password " );
                        alertDialog = myDialog.create ();
                        alertDialog.setCancelable ( false );
                        alertDialog.getWindow ().setLayout ( 850 , 1200 );
                        myDialog.show ();

                    } else {
                        processDialog.dismiss ();
                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( Forgot_password.this );
                        myDialog.setMessage ( "Try again  Something wrong happened " );
                        alertDialog = myDialog.create ();
                        alertDialog.setCancelable ( false );
                        alertDialog.getWindow ().setLayout ( 850 , 1200 );
                        myDialog.show ();


                    }
                }
            } );
        }
    }
}