package com.example.mujahid.instatrackv2;

/**
 * Created by Mujahid on 11/28/2017.
 */

public class Group {
    private String name,admin;
    public Group(){

    }
    public Group(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
}
