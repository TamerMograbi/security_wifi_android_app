package com.example.security_wifi;

import android.os.Bundle;
import android.app.ListActivity;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import java.net.URISyntaxException;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;

public class MainActivity extends ListActivity {
    private Socket mSocket;
    {
        try {
            //try to visit this url on your laptop and see that clicking on a wifi updates
            //this page
            mSocket = IO.socket("https://murmuring-wildwood-99815.herokuapp.com/");
        } catch (URISyntaxException e) { Log.d("myTag", "failed to connect");}
    }

    static final String[] wifi_names =
            new String[]{"eduroam", "cafe", "cafe_guest", "cafe_5G"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();

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

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject colorsObj = new JSONObject();
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("color","red");
            jsonObject2 = new JSONObject();
            jsonObject2.put("color","green");
            jsonObject3 = new JSONObject();
            jsonObject3.put("color","blue");

            jsonArray.put(jsonObject1);
            jsonArray.put(jsonObject2);
            jsonArray.put(jsonObject3);

            colorsObj.put("colors", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("news",colorsObj);
//below is code for an http request. it works but using sockets is easier
/*        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://murmuring-wildwood-99815.herokuapp.com/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        Log.d("myTag", "Response is: "+ response.substring(0,404));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                Log.d("myTag", "That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);*/

    }
}