package com.example.carpoolapp;

import java.util.ArrayList;

public interface DataListener {
    void newDataReceived(ArrayList<User> user);
}
