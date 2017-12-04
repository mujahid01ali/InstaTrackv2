package com.example.mujahid.instatrackv2;

/**
 * Created by Mujahid on 12/5/2017.
 */

public class Users {
    private String phone;
    private String id;
    public Users(){

    }
    public Users(String phone,String id){
        this.phone=phone;
        this.id=id;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
}
