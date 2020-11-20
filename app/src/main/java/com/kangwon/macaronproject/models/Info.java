package com.kangwon.macaronproject.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Info {

    public String username;
    public String phone;
    public boolean isowner;

    public Info(){

    }

    public Info( String username, String phone, boolean isowner){
        this.username = username;
        this.phone = phone;
        this.isowner = isowner;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("phone",phone);
        result.put("isowner",isowner);

        return result;
    }
}
