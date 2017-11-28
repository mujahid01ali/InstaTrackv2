package com.example.mujahid.instatrackv2;

/**
 * Created by Mujahid on 11/28/2017.
 */

public class Group {
    private String name;
    private String gId;
    public Group(){

    }
    public Group(String name,String gId){
        this.name=name;
        this.gId=gId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getId(){
        return gId;
    }
    public void setId(String gId){
        this.gId=gId;
    }

}
