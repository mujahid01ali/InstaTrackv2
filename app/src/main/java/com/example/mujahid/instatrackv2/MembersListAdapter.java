package com.example.mujahid.instatrackv2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by n5050 on 11/30/2017.
 */

public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.MembersViewHolder> {

    View view;
    private Context context;
    private ArrayList<String> phoneNumber;

    public MembersListAdapter(Context context,ArrayList<String> phoneNumber){
        this.context=context;
        this.phoneNumber=phoneNumber;
    }


    public class MembersViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MembersViewHolder(View view){
            super(view);
            textView= (TextView) view.findViewById(R.id.memberPhone);

        }

    }


    public MembersViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        context=viewGroup.getContext();
        view= LayoutInflater.from(context).inflate(R.layout.members_list_row,viewGroup,false);
        MembersViewHolder membersViewHolder=new MembersViewHolder(view);

        return membersViewHolder;

    }

    public void onBindViewHolder(MembersViewHolder membersViewHolder,int i){

        String phone=phoneNumber.get(i);
        membersViewHolder.textView.setText(phone);
    }

    public int getItemCount(){
        if(phoneNumber==null){
            return 0;
        }
        else{
            return phoneNumber.size();
        }
    }
}
