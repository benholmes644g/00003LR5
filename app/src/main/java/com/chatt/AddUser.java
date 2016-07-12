package com.chatt;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by mithramedia on 08/06/16.
 */
@ParseClassName("User")
public class AddUser extends ParseUser {

    public static final String username = "username";
    public static final String password = "password";
    public static final String status = "offline";

    public String getUsername() {
        return getString(username);
    }
//    public String getPassword() {
//        return getString(password);
//    }
    public String getStatus() {
        return getString(status);
    }

    public void setUsername(String username) {
        put(username, username);
    }
    public void setPassword(String password) {
        put(password, password);
    }
    public void setStatus(String status) {
        put(status, status);
    }
}
