package com.example.security_wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SecondScenario extends ListActivity {


    EditText input;

    String password_input;

    AlertDialog password_query;

    String wifi_password;

    String selectedValue;

    int selectedStrength;

    private Socket mSocket;

    long tStart;

    {
        try {
            //try to visit this url on your laptop and see that clicking on a wifi updates
            //this page
            mSocket = IO.socket("https://murmuring-wildwood-99815.herokuapp.com/");
        } catch (URISyntaxException e) { Log.d("myTag", "failed to connect");}
    }


    static final String[] wifi_names =
            new String[]{"BoardAndBrew_5G", "xfinitywifi", "BoardAndBrew", "XFINITY", "Employees"};

    static final int[] wifi_strengths =
            new int[]{4, 3, 3, 2, 2, 2};

    static final boolean[] wifi_private =
            new boolean[]{false, false, true, true, true};

    static final String[] wifi_passwords =
            new String[]{"", "", "power1123", "gsafjaslfk", "gfdlskafdla"};

    static final int[] wifi_safety =
            new int[]{0,0,0,0,0,0};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario1);

        mSocket.connect();
        tStart = System.currentTimeMillis();

        setListAdapter(new MobileArrayAdapter(this, wifi_names, wifi_strengths, wifi_private, wifi_safety));
        TextView textView = new TextView(this);
        textView.setText("Available connections");
        textView.setHeight(200);
        textView.setTextSize(30);

        this.getListView().addHeaderView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password");

        input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("CONNECT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password_input = input.getText().toString();

                if(password_input.equals(wifi_password))
                {
                    sendElapsedToServer();
                    Toast.makeText(getApplicationContext(), "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SecondScenario.this, ThirdPrompt.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        password_query = builder.create();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Log.d("myTag", "position " + position);
        //get selected items
        if (position == 0) {
            return;
        }
        selectedValue = (String) getListAdapter().getItem(position - 1);

        selectedStrength = wifi_strengths[position - 1];

        if(!wifi_private[position-1]) {
            sendElapsedToServer();
            Toast.makeText(this, "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SecondScenario.this, ThirdPrompt.class);
            startActivity(intent);
        }

        else{
            wifi_password = wifi_passwords[position-1];
            password_query.show();
        }

    }

    public void sendElapsedToServer() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.d("myTag", "took  " + elapsedSeconds + " time to complete scenario 2");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("scenario 2 wifi", selectedValue + " " + selectedStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject.put("scenario 2 time",elapsedSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("elapsed",jsonObject);

    }

}
