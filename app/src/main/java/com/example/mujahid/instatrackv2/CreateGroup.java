package com.example.mujahid.instatrackv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateGroup extends AppCompatActivity implements View.OnClickListener {
    EditText etGroupName;
    Button btnCreateGroup;
    ProgressDialog dialog;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        etGroupName=(EditText)findViewById(R.id.etGroupName);
        btnCreateGroup=(Button)findViewById(R.id.submitCreate);
        btnCreateGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (etGroupName.getText().toString().equals("")){
            Toast.makeText(CreateGroup.this,"Enter Group Name",Toast.LENGTH_SHORT).show();
        }else{
            url=Config.baseUrl+"createGroup.php";
            dialog=ProgressDialog.show(CreateGroup.this,"","Please Wait",true,true);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    if (response.toString().contains("success")){
                        Toast.makeText(getApplicationContext(), "Group Created Successfully", Toast.LENGTH_LONG).show();
                        Intent intentG=new Intent(CreateGroup.this,MainActivity.class);
                        startActivity(intentG);
                        finish();
                    }else if (response.toString().contains("failure")) {
                        Toast.makeText(CreateGroup.this, "Please try Again", Toast.LENGTH_LONG).show();
                    } else if (response.toString().contains("failed")) {
                        Toast.makeText(CreateGroup.this, "Error Occured", Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(CreateGroup.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("phone", SharedPrefManager.getInstance(CreateGroup.this).getPhoneNumber().trim().toString());
                    param.put("groupName", etGroupName.getText().toString());
                    return param;
                }

            };
            RequestHandler.getInstance(CreateGroup.this).addToRequestQueue(stringRequest);
        }
    }
}
