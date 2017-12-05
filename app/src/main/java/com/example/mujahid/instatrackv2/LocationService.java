package com.example.mujahid.instatrackv2;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by n5050 on 11/27/2017.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    protected GoogleApiClient googleApiClient;
    protected Location location;
    protected LocationRequest locationRequest;
    double longitude;
    double latitude;


    public void buildGoogleApiClient(){
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onCreate() {
        Toast.makeText(this,"oncreate",Toast.LENGTH_SHORT).show();
        super.onCreate();
        buildGoogleApiClient();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Not Available",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!googleApiClient.isConnected()){
                googleApiClient.connect();
            }
            Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();

        }
        return START_STICKY;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
        else{
            Toast.makeText(this,"Location permission not available",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Could not establish connection",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();


        String url=Config.baseUrl+"updateLocation.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> param = new HashMap<>();
                param.put("phone","+918218426482");
                param.put("longitude",longitude+"");
                param.put("latitude",latitude+"");

                return param;
            }

        };
        RequestHandler.getInstance(LocationService.this).addToRequestQueue(stringRequest);

    }
}
