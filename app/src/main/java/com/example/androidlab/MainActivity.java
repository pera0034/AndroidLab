package com.example.androidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent profilePage = new Intent(this, ProfileActivity.class);
        profilePage.putExtra("email", "qwerty@site.com");
        Button login = findViewById(R.id.login);
        login.setOnClickListener(click -> startActivity( profilePage ));

        SharedPreferences sharedPref = getSharedPreferences(
        getString(R.id.text1EmailAddress), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}