package com.example.androidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResources().getString(R.string.toast_message);

        CheckBox box = (CheckBox)findViewById(R.id.checkbox);

        box.setOnCheckedChangeListener((CompoundButton cb, boolean b) -> {
            /*Snackbar.setAction is having an error.*/
            Snackbar snackbar = Snackbar.setAction("Undo", click ->cb.setChecked(b));
        });
    }
}