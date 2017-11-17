package com.example.mujahid.instatrackv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    TextView textPhone;
    Button btnNext;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textPhone = (TextView) findViewById(R.id.etPhoneSignup);
        btnNext = (Button) findViewById(R.id.btnSignup);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url = Config.baseUrl+"signupOTP.php";
        if (textPhone.getText().toString().equals("")) {
            Toast.makeText(Signup.this, "Please Enter mobile number", Toast.LENGTH_LONG).show();
        } else {
            dialog=ProgressDialog.show(Signup.this,"","Please Wait...",false,false);
            StringRequest strRqstSignup = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        dialog.dismiss();
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                    } else if (response.contains("fail")  || response.contains("Error inserting into db")) {
                        dialog.dismiss();
                        Toast.makeText(Signup.this, "Some error occured", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();

                    Toast.makeText(Signup.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("phone", textPhone.getText().toString().trim());
                    return param;
                }
            };
            RequestHandler.getInstance(Signup.this).addToRequestQueue(strRqstSignup);
        }
    }
}
