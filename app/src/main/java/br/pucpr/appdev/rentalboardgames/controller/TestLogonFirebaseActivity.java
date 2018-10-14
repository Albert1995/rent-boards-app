package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import br.pucpr.appdev.rentalboardgames.R;

public class TestLogonFirebaseActivity extends AppCompatActivity {

    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_logon_firebase);
        fbAuth = FirebaseAuth.getInstance();
    }


    public void signOut(View v) {
        fbAuth.signOut();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }



}
