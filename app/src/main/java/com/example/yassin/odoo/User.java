package com.example.yassin.odoo;

/**
 * Created by Yassin on 2017-12-01.
 */

class User {
    private String email;
    private String name;
    private String pass;
    private String permission;

    User(String email , String name , String pass ,String permission){
        this.email= email;
        this.name = name;
        this.pass = pass;
        this.permission = permission;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getPermission() {
        return permission;
    }
}
