package com.example.security_wifi;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    // Array of strings...
    String[] wifiArray = {"eduroam","Fios-P8AAE","NETGEAR-Guest","Cafe",
            "Cafe-guest","Cafe-5G","xfinity","HP printer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, wifiArray);

        ListView listView = (ListView) findViewById(R.id.wifi_list);
        Log.d("myTag", "this will show up in logcat");


        listView.setAdapter(adapter);
    }
}