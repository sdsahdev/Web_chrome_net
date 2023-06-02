package com.example.web_view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class Network_reciver extends BroadcastReceiver {
    private final com.example.web_view.MainViewModel MainViewModel;
    public MainViewModel mainviewmodel;

public  Network_reciver(MainViewModel mainViewmodel){
    this.MainViewModel = mainViewmodel;

}

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            MainViewModel.checkConnections();
        }
    }
}
