package com.crooks;

import java.util.ArrayList;

/**
 * Created by johncrooks on 6/7/16.
 */
public class User {
    String name, password;
    ArrayList<Restaurant> restaurants = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public User() {
    }
}
