package com.example.androidlab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /*Intent messagePage = new Intent(this, ChatRoomActivity.class);
        Button message = findViewById(R.id.button4);
        message.setOnClickListener(click -> startActivity( messagePage ));

        ImageButton imgButton = (ImageButton) findViewById(R.id.imageButton);
        Log.e("onCreate", "Activity: onCreate()");*/

        Intent weatherPage = new Intent(this, WeatherForecast.class);
        Button weather = findViewById(R.id.buttonWeather);
        weather.setOnClickListener(click -> startActivity( weatherPage ));
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart", "In function: onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "In function: onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "In function: onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "In function: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "In function: onDestroy()");
    }
}