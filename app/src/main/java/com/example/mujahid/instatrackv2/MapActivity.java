package com.example.mujahid.instatrackv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    boolean mapReady=false;
    Intent intent;
    protected int groupID;
    private MapFragment mapFragment;
    protected ArrayList<Location> positions;
    protected ArrayList<String> phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        groupID=1;
        positions=new ArrayList<>();
        phoneNumber=new ArrayList<>();
        intent=new Intent(this,LocationService.class);
        fetchData();
        mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onStart(){
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1234);

        }
        else{
            //startService(intent);
            //Toast.makeText(this,"Service started",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        if(requestCode==1234){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                //startService(intent);
                //Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap=googleMap;
        mapReady=true;
    }

    public void fetchData(){
        Toast.makeText(this,"Fetch Data started",Toast.LENGTH_SHORT).show();
        String url=Config.baseUrl2+"fetchData.php";

        StringRequest fetchRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Res",response);

                try{

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("users");
                    for(int i=0;i<jsonArray.length();i++){
                        double longitude;
                        double latitude;
                        String phone;
                        JSONObject object=jsonArray.getJSONObject(i);
                        longitude=object.getDouble("last_lng");
                        latitude=object.getDouble("last_lat");
                        phone=object.getString("phone");
                        Log.e("latitude",latitude+"");
                        Log.e("longitude",longitude+"");
                        Location loc=new Location("pos");
                        loc.setLongitude(longitude);
                        loc.setLatitude(latitude);
                        positions.add(loc);
                        phoneNumber.add(phone);
                    }
                    Log.e("Size",positions.size()+"");
                    addMarkers();



                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Log.e("Error","JSON exception");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("groupID",groupID+"");
                return param;
            }
        };
        fetchRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(MapActivity.this).addToRequestQueue(fetchRequest);

    }

    public void addMarkers(){
        Toast.makeText(this,"Add Marker started "+positions.size(),Toast.LENGTH_SHORT).show();

        gMap.clear();

        for(int i=0;i<positions.size();i++){
            Location location=positions.get(i);
            LatLng position=new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(position);
            markerOptions.title(phoneNumber.get(i));

            gMap.addMarker(markerOptions);

            if(i==0){
                CameraPosition cameraPosition=new CameraPosition(position,15,0,0);
                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }
    }

    public void refreshData(View view){
        positions.clear();
        phoneNumber.clear();

        fetchData();
        mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.gMap);
        mapFragment.getMapAsync(this);

    }

}
