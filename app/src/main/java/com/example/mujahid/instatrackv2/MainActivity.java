package com.example.mujahid.instatrackv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton btnGroupCreate;
    public String url=Config.baseUrl+"getGroups.php?phone="+SharedPrefManager.getInstance(MainActivity.this).getPhoneNumber().toString().trim();
    ArrayList groupList;
    private RecyclerView recyclerView;
    JSONArray jsonArray=null;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGroupCreate=(FloatingActionButton) findViewById(R.id.btnCreateGroup);
        recyclerView=(RecyclerView) findViewById(R.id.rcViewGroups);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        groupList=new ArrayList<>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Group group = (Group) groupList.get(position);
                        SharedPrefManager.getInstance(MainActivity.this).groupId(group.getId());
                        Intent intent=new Intent(MainActivity.this,GroupUsers.class);
                        startActivity(intent);
                       // Toast.makeText(getApplicationContext(),"group Name is "+ group.getName()+" and Group id is "+group.getId() + " is selected!", Toast.LENGTH_SHORT).show();

                    }
                })
        );


        getGroups();

        btnGroupCreate.setOnClickListener(this);
    }


    private void getGroups() {

        dialog=ProgressDialog.show(MainActivity.this,"","Please Wait",true,true);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("null")) {
                    Toast.makeText(MainActivity.this, "No Recored Found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("MainActivity",response);
                    groupList.clear();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ob = jsonArray.getJSONObject(i);
                            groupList.add(new Group(
                                    ob.getString("name"),
                                    ob.getString("id").toString()
                            ));

                        }
                        GroupAdapter adapter = new GroupAdapter(MainActivity.this, groupList);
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
                Toast.makeText(MainActivity.this,"Connection error", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPrefManager.getInstance(MainActivity.this).logOut();
                Intent in=new Intent(MainActivity.this,Login.class);
                startActivity(in);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,CreateGroup.class);
        startActivity(intent);

    }

}
