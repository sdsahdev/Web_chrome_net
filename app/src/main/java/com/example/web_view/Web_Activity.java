package com.example.web_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URL;

public class Web_Activity extends AppCompatActivity {
WebView webapp;
    private static final int INTERNET_STATE = 0;
    private TextView txtInternet;
    ProgressBar progressDialog;
//    , txtPing, txtMetered;
    private MainViewModel viewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webapp = findViewById(R.id.webapp);
        txtInternet = findViewById(R.id.tx1);
        progressDialog = findViewById(R.id.progressDialog);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeData();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void observeData() {

        viewModel.observeLiveInternet().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                requestPermissions(viewModel.checkPermission());
                if (value) {
                    txtInternet.setVisibility(View.GONE);

                    String url = "https://www.google.com/";
                    webapp.loadUrl(url);
                    webapp.getSettings().setJavaScriptEnabled(true);
                    webapp.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            progressDialog.setVisibility(View.GONE);
                        }
                    });
                    webapp.setVisibility(View.VISIBLE);
                } else {
                    txtInternet.setText("Network Connection is not available");
                    txtInternet.setVisibility(View.VISIBLE);
                    webapp.setVisibility(View.GONE);
                }
            }
        });

//        viewModel.observeLivePing().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean value) {
//                requestPermissions(viewModel.checkPermission());
//                if (value) {
//                    txtPing.setText("Google Successfully Ping");
//                    txtMetered.setVisibility(View.VISIBLE);
//                } else {
//                    txtPing.setText("Google Unreachable ping");
//                    txtMetered.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        viewModel.observeLiveMeter().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean value) {
//                if (value) {
//                    txtMetered.setText("Network Connection is Metered");
//                } else {
//                    txtMetered.setText("Network Connection is not Metered");
//                }
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            viewModel.unregisterConnectivity();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        requestPermissions(viewModel.checkPermission());
        try {
            viewModel.registerConnectivity();
            viewModel.checkConnections();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }
    }

    private void requestPermissions(boolean permissionGranted) {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_WIFI_STATE,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                    },
                    INTERNET_STATE
            );
        }
    }

    private void showAppPermissionSettings() {
        Toast.makeText(this,"Internet Permissions Disabled",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
