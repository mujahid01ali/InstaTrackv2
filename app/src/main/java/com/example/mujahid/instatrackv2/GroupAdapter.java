package com.example.mujahid.instatrackv2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Mujahid on 11/28/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Group> groupList;

    public interface OnItemLongClickListener{
        public boolean OnItemLongClicked(int position);
    }

    public GroupAdapter(Context mCtx,List<Group> groupList){
        this.mCtx=mCtx;
        this.groupList=groupList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.group_list_row,null);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Group group=groupList.get(position);
        holder.name.setText(group.getName());
        holder.gId.setText(group.getId());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){

                PopupMenu popupMenu=new PopupMenu(mCtx,holder.itemView);
                popupMenu.inflate(R.menu.group_options);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.deleteGroup:
                                //nnmn
                                break;
                            case R.id.viewInfo:
                                Intent intent=new Intent(mCtx,GroupUsers.class);
                                mCtx.startActivity(intent);
                                break;


                        }

                        return false;
                    }
                });
                popupMenu.show();

                return true;

            }

        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,gId;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
            gId=(TextView) itemView.findViewById(R.id.gId);
            gId.setVisibility(View.GONE);

            }
    }
}
