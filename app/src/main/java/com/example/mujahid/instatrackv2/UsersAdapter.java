package com.example.mujahid.instatrackv2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mujahid on 12/5/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Users> userList;

    public UsersAdapter(Context mCtx,List<Users> userList){
        this.mCtx=mCtx;
        this.userList=userList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.users_list,null);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Users users=userList.get(position);
        holder.phone.setText(users.getPhone());
        holder.id.setText(users.getId());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone,id;
        public MyViewHolder(View itemView) {
            super(itemView);
            phone=(TextView) itemView.findViewById(R.id.userPhone);
            id=(TextView) itemView.findViewById(R.id.userId);
            id.setVisibility(View.GONE);

        }
    }
}
