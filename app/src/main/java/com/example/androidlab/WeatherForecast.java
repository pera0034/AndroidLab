package com.example.androidlab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Values{
    public double uv;
    public String min;
    public String max;
    public String currentTemp;
    public String unit;
    public String icon;
    public String city;
    public char jsonData;
    public Bitmap currentWeather;
}

public class WeatherForecast extends AppCompatActivity {

    TextView currentTemp, minTemp, maxTemp, uvRating, currentCity;
    ImageView imageView;
    private ProgressBar progress;
    Integer count =1;

    String jsonURL = "https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentTemp = (TextView) findViewById(R.id.currentTemp);
        minTemp = (TextView) findViewById(R.id.minTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        uvRating = (TextView) findViewById(R.id.uvRating);
        currentCity = (TextView) findViewById(R.id.city);
        imageView = (ImageView) findViewById(R.id.imageView);

        progress = (ProgressBar) findViewById(R.id.myprogressbar);
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);

        ForecastQuery query = new ForecastQuery();
        query.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, Values> {

        public Values doInBackground(String ... args)
        {
            for (; count <= 100; count++) {
                try {
                    Thread.sleep(10);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Values w = new Values();
            try {
                // Extract XML File
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("temperature"))
                        {
                            w.currentTemp = xpp.getAttributeValue(null, "value");
                            w.min = xpp.getAttributeValue(null, "min");
                            w.max = xpp.getAttributeValue(null, "max");
                            w.unit = xpp.getAttributeValue(null, "unit");
                        }
                        else if(xpp.getName().equals("weather"))
                        {
                            w.icon = xpp.getAttributeValue(null, "icon");
                        }
                        else if(xpp.getName().equals("city"))
                        {
                            w.city = xpp.getAttributeValue(null, "name");
                        }
                    }
                    eventType = xpp.next();
                }

                // bitmap
                Bitmap bitmap = null;
                InputStream inputStream;
                try {
                    inputStream = new java.net.URL("https://openweathermap.org/img/w/" + w.icon + ".png").openStream();
                    w.currentWeather = BitmapFactory.decodeStream(inputStream);
                    //Log.e("Error", String.valueOf(w.currentWeather));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // end bitmap

                w.uv = loadJSON();
                return w;
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }
            return w;
        }

        protected void onPostExecute(String result) {
            progress.setVisibility(View.GONE);
        }

        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

        // Output to gui
        public void onPostExecute(Values fromDoInBackground)
        {
            imageView.setImageBitmap(fromDoInBackground.currentWeather);
            currentTemp.setText("Current Temperature: " +fromDoInBackground.currentTemp + " " +fromDoInBackground.unit);
            minTemp.setText("Min Temp: " +fromDoInBackground.min + " " +fromDoInBackground.unit);
            maxTemp.setText("Max Temp: " +fromDoInBackground.max + " " +fromDoInBackground.unit);
            uvRating.setText("UV Rating: " +fromDoInBackground.uv);
            currentCity.setText("" +fromDoInBackground.city);
        }

        public double loadJSON(){
            String uvValue = "";
            String jsonData = extractJson("uv");
            Log.i("JSON", jsonData);
            // Extract data
            try {
                JSONObject jObject = new JSONObject(jsonData);
                double value = jObject.getDouble("value");
                Log.i("JSON", String.valueOf(value));
                return value;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        // Extract json to get uv value
        public String extractJson(String type){
            URL url;
            HttpURLConnection urlConnection = null;
            String jsonData = "";

            try {
                if(type == ""){
                    url = new URL(jsonURL);
                }else{
                    url = new URL(jsonURL);
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    jsonData += (char) data;
                    data = isw.read();
                }
                return jsonData;

            }catch (Exception  e) {
                e.printStackTrace();
            }
            return jsonData;
        }
    }
}
