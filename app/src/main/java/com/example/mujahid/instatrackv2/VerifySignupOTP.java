package com.example.mujahid.instatrackv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VerifySignupOTP extends AppCompatActivity implements View.OnClickListener {
    Button btnOtpCheckSignup;
    EditText etOtpSignup;
    ProgressDialog dialog;
    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_signup_otp);
        Bundle extra=getIntent().getExtras();
        phoneNum=extra.getString("phone");
        Toast.makeText(VerifySignupOTP.this,phoneNum.toString(),Toast.LENGTH_LONG).show();
        btnOtpCheckSignup = (Button) findViewById(R.id.btnSignupOtpCheck);
        etOtpSignup = (EditText) findViewById(R.id.etOtpVerifySignup);
        btnOtpCheckSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnOtpCheckSignup) {
            checkOtp();
        }
    }

    private void checkOtp() {
        String url = Config.baseUrl+"verifyLoginOTP.php";
        if (etOtpSignup.getText().toString().equals("")) {
            Toast.makeText(VerifySignupOTP.this, "Please Enter OTP", Toast.LENGTH_LONG).show();
        } else {
        dialog=ProgressDialog.show(VerifySignupOTP.this,"","Please Wait...",false,false);
        StringRequest strRqstVerifyOTP = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    dialog.dismiss();
                    Intent intent = new Intent(VerifySignupOTP.this, Login.class);
                    startActivity(intent);
                } else if (response.contains("fail")) {
                    dialog.dismiss();
                    Toast.makeText(VerifySignupOTP.this, "Incorrect OTP ", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Toast.makeText(VerifySignupOTP.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("otp", etOtpSignup.getText().toString().trim());
                param.put("phone",phoneNum.trim());
                return param;
            }
        };
        RequestHandler.getInstance(VerifySignupOTP.this).addToRequestQueue(strRqstVerifyOTP);
    }
    }

}
