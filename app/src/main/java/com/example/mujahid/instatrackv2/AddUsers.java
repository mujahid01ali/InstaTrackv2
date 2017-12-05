package com.example.mujahid.instatrackv2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    public ArrayList StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    ListAdapter listAdapter;
    public ArrayList contactsList;
    public ArrayList<String> finalList;
    JSONArray contacts=null;
    Cursor cursor ;
    String phonenumber ;
    Button button;
    ProgressDialog dialog;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        recyclerView=(RecyclerView) findViewById(R.id.recView1);
        recyclerView.setHasFixedSize(false);
        button=(Button)findViewById(R.id.button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        contactsList=new ArrayList<>();
        StoreContacts=new ArrayList<>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        EnableRuntimePermission();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Contact contact = (Contact) contactsList.get(position);
                            addUserToGroup(contact.getPhone());
                        //Toast.makeText(getApplicationContext(), "Group Id is"+SharedPrefManager.getInstance(AddUsers.this).getGroupId()+"    " +contact.getPhone(), Toast.LENGTH_SHORT).show();

                    }
                })
        );
        GetContactsIntoArrayList();
        getContact();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intersectionOfList();
            }
        });


    }

    private void addUserToGroup(String phone) {
       final String phNumber=phone;
        //Method for adding the member to groups
        dialog=ProgressDialog.show(AddUsers.this,"","Please Wait...",false,false);
        String url=Config.baseUrl+"addUserToGroup.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.toString().contains("already")){
                    Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_LONG).show();
                }else if (response.toString().contains("success")){
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                    Intent intentG=new Intent(AddUsers.this,GroupUsers.class);
                    startActivity(intentG);
                    finish();
                }else if (response.toString().contains("failure")) {
                    Toast.makeText(AddUsers.this, "Please try Again", Toast.LENGTH_LONG).show();
                } else if (response.toString().contains("failed")) {
                    Toast.makeText(AddUsers.this, "Connection Error", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(AddUsers.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("groupId",SharedPrefManager.getInstance(AddUsers.this).getGroupId());
                param.put("phone","+91"+phNumber);
                return param;
            }

        };
        RequestHandler.getInstance(AddUsers.this).addToRequestQueue(stringRequest);

    }

    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                AddUsers.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(AddUsers.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(AddUsers.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    private void getContact() {
        dialog=ProgressDialog.show(AddUsers.this,"","Fetching...",false,false);
        String url=Config.baseUrl+"getContacts.php";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.toString().contains("null")){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"No Recored Found",Toast.LENGTH_LONG).show();
                }
                else{
                    dialog.dismiss();
                    Log.d("Contact List",response);
                    contactsList.clear();

                    String phone;
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        contacts=jsonObject.getJSONArray("result");
                        for (int j=0;j<contacts.length();j++){
                            JSONObject a=contacts.getJSONObject(j);
                            phone=a.getString("phone");
                            String phNum=phone.replaceAll("[\\D+]","");
                            int len=phNum.length();
                            if (len==10){
                                contactsList.add(new Contact(
                                        phNum
                                ));
                            }else if(len>10) {
                                contactsList.add(new Contact(
                                        phNum.substring(len-10)
                                ));
                            }else{

                            }
                            /*arrayAdapter = new ArrayAdapter<String>(
                                    MainActivity.this,
                                    R.layout.contact_items_listview,
                                    R.id.name, contactsList
                            );

                            listView.setAdapter(arrayAdapter);*/



                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(AddUsers.this).addToRequestQueue(stringRequest);

    }



    private void GetContactsIntoArrayList() {
        StoreContacts.clear();

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            //name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phNum =phonenumber.replaceAll("[\\D+]", "");
            int len=phNum.length();
            if (len==10){
                StoreContacts.add(new Contact(phNum));
            }else if(len>10) {
                StoreContacts.add(new Contact(phNum.substring(len - 10)));
            }else{

            }
        }


        cursor.close();
    }


    private void intersectionOfList() {
        dialog=ProgressDialog.show(AddUsers.this,"","Wait...",false,false);
        StoreContacts.retainAll(contactsList);
        ContactAdapter adapter=new ContactAdapter(AddUsers.this,contactsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(AddUsers.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(AddUsers.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}
