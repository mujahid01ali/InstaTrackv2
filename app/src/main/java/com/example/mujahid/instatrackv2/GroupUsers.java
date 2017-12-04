package com.example.mujahid.instatrackv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupUsers extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton btnAddUser;
    ArrayList userList;
    private RecyclerView recyclerView;
    JSONArray jsonArray=null;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_users);
        btnAddUser=(FloatingActionButton) findViewById(R.id.btnAddUser);
        recyclerView=(RecyclerView) findViewById(R.id.recViewUsers);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        userList=new ArrayList<>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Users users = (Users) userList.get(position);
                         Toast.makeText(getApplicationContext(),"User Phone Number is "+ users.getPhone()+" and User id is "+users.getId() + " is selected!"+" And the Group ID is "+SharedPrefManager.getInstance(GroupUsers.this).getGroupId(), Toast.LENGTH_SHORT).show();

                    }
                })
        );
        btnAddUser.setOnClickListener(this);
        getUsers();
    }

    private void getUsers() {
        String url=Config.baseUrl+"getUsers.php?groupId="+SharedPrefManager.getInstance(GroupUsers.this).getGroupId();

        dialog=ProgressDialog.show(GroupUsers.this,"","Please Wait",true,true);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("null")) {
                    Toast.makeText(GroupUsers.this, "No User Found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("GroupUser",response);
                    userList.clear();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ob = jsonArray.getJSONObject(i);
                            userList.add(new Users(
                                    ob.getString("phone"),
                                    ob.getString("id").toString()
                            ));

                        }
                        UsersAdapter adapter = new UsersAdapter(GroupUsers.this, userList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(GroupUsers.this,"Connection error", Toast.LENGTH_SHORT).show();

            }
        });
        RequestHandler.getInstance(GroupUsers.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        Intent inAdd=new Intent(GroupUsers.this,AddUsers.class);
        startActivity(inAdd);
    }
}
