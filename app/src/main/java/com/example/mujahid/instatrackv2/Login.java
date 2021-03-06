package com.example.mujahid.instatrackv2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText etPhoneNum;
    TextView textCreate;
    Button btnSendOtp;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textCreate=(TextView) findViewById(R.id.textCreate);
        btnSendOtp=(Button) findViewById(R.id.btnGetOTP);
        etPhoneNum=(EditText) findViewById(R.id.etPhoneNum);
        btnSendOtp.setOnClickListener(this);
        textCreate.setOnClickListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},5678);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        if(requestCode==5678){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Location Permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view==btnSendOtp){
            sendOtp();
        }
        if(view==textCreate){
                    Intent intentCreate =new Intent(Login.this,Signup.class);
                    startActivity(intentCreate);
        }
    }

    public void sendOtp() {
        String url = Config.baseUrl+"loginOTP.php";
        if (etPhoneNum.getText().toString().equals("")) {
            Toast.makeText(Login.this, "Please Enter mobile number", Toast.LENGTH_LONG).show();
        } else {
            dialog=ProgressDialog.show(Login.this,"","Please Wait...",false,false);
            StringRequest loginRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        dialog.dismiss();
                        SharedPrefManager.getInstance(Login.this).login(etPhoneNum.getText().toString());
                        Intent intent = new Intent(Login.this, VerifySignupOTP.class);
                        intent.putExtra("phone",etPhoneNum.getText().toString().trim());
                        startActivity(intent);
                    } else if (response.contains("fail")) {
                        dialog.dismiss();
                        Toast.makeText(Login.this, "Unexpected Error", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();

                    Toast.makeText(Login.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("phone", etPhoneNum.getText().toString().trim());
                    return param;
                }
            };
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestHandler.getInstance(Login.this).addToRequestQueue(loginRequest);
        }
    }
}
