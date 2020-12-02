package com.iettfd.tasks.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.iettfd.tasks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iettfd.tasks.util.WindowUtil.changePrimaryDark;
import static com.iettfd.tasks.util.WindowUtil.hideToolbar;

@SuppressLint("NonConstantResourceId")
public class WeatherFragment extends Fragment {

    @BindView(R.id.current_temp) TextView current_temp;
    @BindView(R.id.main_climate_text) TextView main_climate_text;
    @BindView(R.id.description_text) TextView description_text;
    @BindView(R.id.min_max_text) TextView min_max_text;
    @BindView(R.id.location_text) TextView location_text;
    @BindView(R.id.header_view) ImageView header_view;
    @BindView(R.id.date_day_text) TextView date_day_text;
    @BindView(R.id.sunrise_time) TextView sunrise_time;
    @BindView(R.id.sunset_time) TextView sunset_time;
    @BindView(R.id.wind_speed_text) TextView wind_speed_text;
    @BindView(R.id.pressure_text) TextView pressure_text;
    @BindView(R.id.humidity_text) TextView humidity_text;
    @BindView(R.id.visibility_text) TextView visibility_text;

    private final int HOUR_MIN = 0;
    private final int DAY_DATE = 1;
    private static final int REQUEST_LOCATION = 12;
    private LocationRequest locationRequest;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    private String mainWeather, description, placeName;
    private Integer pressure, humidity, visibility, sunrise, sunset, currentMilli;
    private Double temp, temp_min, temp_max, windSpeed;

    private String[] week = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    public WeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hideToolbar(getActivity());
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, root);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        changePrimaryDark(getActivity(), "#000000");
        locationCheckRequests();
        return root;
    }

    private void locationCheckRequests() {

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        if (!gps_enabled && !network_enabled) {

            new AlertDialog.Builder(getContext())
                    .setMessage("GPS not turned ON")
                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "Visit NOT updated", Toast.LENGTH_LONG).show();
                }
            }).show();
        }

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }else{
            fetchAPI();
        }
    }

    @SuppressLint("MissingPermission")
    private void fetchAPI() {
        requestQueue = Volley.newRequestQueue(getContext());
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(this);

                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int index = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(index).getLatitude();
                    double longitude = locationResult.getLocations().get(index).getLongitude();

                    String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + getString(R.string.api_key);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                placeName = String.valueOf(response.get("name"));
                                visibility = (Integer) response.get("visibility");
                                JSONObject weatherObj = response.getJSONArray("weather").getJSONObject(0);
                                mainWeather = String.valueOf(weatherObj.get("main"));
                                description = String.valueOf(weatherObj.get("description"));
                                JSONObject tempDetails = response.getJSONObject("main");
                                temp_min = (Double) tempDetails.get("temp_min");
                                temp_max = (Double) tempDetails.get("temp_max");
                                pressure = (Integer) tempDetails.get("pressure");
                                humidity = (Integer) tempDetails.get("humidity");
                                temp = (Double) tempDetails.get("temp");
                                JSONObject sysDetails = response.getJSONObject("sys");
                                sunrise = (Integer) sysDetails.get("sunrise");
                                sunset = (Integer) sysDetails.get("sunset");
                                JSONObject windDetails = response.getJSONObject("wind");
                                windSpeed = (Double) windDetails.get("speed");
                                currentMilli = (Integer) response.get("dt");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setUI();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    requestQueue.add(request);
                }
            }
        }, Looper.getMainLooper());
    }

    private void setUI() {
        location_text.setText(placeName);
        date_day_text.setText(convertTime(currentMilli, DAY_DATE));
        main_climate_text.setText(mainWeather);
        description_text.setText(description);
        min_max_text.setText(Math.round(temp_min - 273) + "° / " + Math.round(temp_max - 273) + "°");
        current_temp.setText(Math.round(temp - 273) + "°");
        sunrise_time.setText(convertTime(sunrise, HOUR_MIN));
        sunset_time.setText(convertTime(sunset, HOUR_MIN));
        pressure_text.setText(String.format("%.1f", pressure/1013.0));
        humidity_text.setText(String.valueOf(humidity));
        wind_speed_text.setText(String.valueOf(windSpeed));
        visibility_text.setText(String.valueOf(visibility/1000));

        if(currentMilli > sunset)
            header_view.setImageResource(R.drawable.header_night);

        progressDialog.dismiss();
    }

    private String convertTime(Integer milliTime, int option){
        String time = milliTime + "198";
        Date date = new Date(Long.parseLong(time));
        if(option == HOUR_MIN) {
            Format format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        }else{
            String day_date_month;
            day_date_month = week[date.getDay()] + ", " + date.getDate() + " " + month[date.getMonth()];
            return day_date_month;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchAPI();
            }else{
                Toast.makeText(getContext(), "Please allow all permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.stop();
        }
        changePrimaryDark(getActivity(), "#3700B3");
    }
}