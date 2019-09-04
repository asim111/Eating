package com.example.asimkhan.eating.common;

import com.example.asimkhan.eating.Model.User;

public class Common {
    public static User currentuser;
    public static String convertcodetostatus(String status) {
        if(status.equals("0"))
            return "placed";
        else if(status.equals("1"))
            return "on my way";
        else
            return "shipped";

    }
}
