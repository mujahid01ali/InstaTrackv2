package com.example.mujahid.instatrackv2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mujahid on 12/1/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Contact> list;

    public ContactAdapter(Context mCtx, List<Contact> list){
        this.mCtx=mCtx;
        this.list=list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.contact_items_listview,null);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact= list.get(position);
        holder.phone.setText(contact.getPhone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public MyViewHolder(View itemView) {
            super(itemView);
            phone=(TextView) itemView.findViewById(R.id.phone);

        }
    }
}