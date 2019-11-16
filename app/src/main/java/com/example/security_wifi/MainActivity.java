package com.example.security_wifi;

import android.os.Bundle;
import android.app.ListActivity;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends ListActivity {

    static final String[] wifi_names =
            new String[]{"eduroam", "cafe", "cafe_guest", "cafe_5G"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new MobileArrayAdapter(this, wifi_names));
        TextView textView = new TextView(this);
        textView.setText("Available connections");
        textView.setHeight(200);
        textView.setTextSize(30);

        this.getListView().addHeaderView(textView);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Log.d("myTag", "position " + position);
        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position-1);
        Toast.makeText(this, "connected to " + selectedValue, Toast.LENGTH_SHORT).show();

    }
}