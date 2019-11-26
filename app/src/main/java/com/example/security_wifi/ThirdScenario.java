package com.example.security_wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ThirdScenario extends ListActivity {

    Button button;

    EditText input;

    String password_input;

    AlertDialog password_query;

    String wifi_password;

    String selectedValue;

    private Socket mSocket;

    View blinker;

    long tStart;

    {
        try {
            //try to visit this url on your laptop and see that clicking on a wifi updates
            //this page
            mSocket = IO.socket("https://murmuring-wildwood-99815.herokuapp.com/");
        } catch (URISyntaxException e) { Log.d("myTag", "failed to connect");}
    }

    static final String[] wifi_names =
            new String[]{"BoardAndBrew", "xfinitywifi", "BoardAndBrew", "public_wifi", "XFINITY", "Employees"};

    static final int[] wifi_strengths =
            new int[]{3, 3, 2, 2, 2, 2};

    static final boolean[] wifi_private =
            new boolean[]{true, false, true, false, true, true};

    static final String[] wifi_passwords =
            new String[]{"games471", "", "games471", "", "gsafjaslfk", "gfdlskafdla"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_scenario);
        mSocket.connect();
        tStart = System.currentTimeMillis();

        final View blinker = findViewById(R.id.blinker);
        blinker.setVisibility(View.INVISIBLE);

        final MobileArrayAdapter adapter = new MobileArrayAdapter(this, wifi_names, wifi_strengths, wifi_private);
        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                //adapter.highlightImageView(position);
                if(position == 0) {
                    return true;
                }
                String selectedValue = (String) getListAdapter().getItem(position-1);
                Toast.makeText(getApplicationContext(), "asking " +selectedValue + " to blink " , Toast.LENGTH_SHORT).show();

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
                //blinker.setBackgroundColor(Color.parseColor("#00FF00"));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blinker.setBackgroundColor(Color.parseColor("#FF0000"));
                        blinker.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                blinker.setBackgroundColor(Color.parseColor("#00FF00"));
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blinker.setBackgroundColor(Color.parseColor("#0000FF"));
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                blinker.setVisibility(View.INVISIBLE);
                                            }
                                        }, 1000);
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                }, 1000);

                return true;
            }

        });
        TextView textView = new TextView(this);
        textView.setText("Available connections");
        textView.setHeight(200);
        textView.setTextSize(30);

        this.getListView().addHeaderView(textView);
        addListenerOnButton();




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
                    Toast.makeText(getApplicationContext(), "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
                    sendElapsedToServer();

                    //Intent intent = new Intent(FirstScenario.this, SecondScenario.class);
                    //startActivity(intent);
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

    public void sendElapsedToServer() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.d("myTag", "took  " + elapsedSeconds + " time to complete scenario 3");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("scenario 3",elapsedSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("elapsed",jsonObject);

    }

    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //i want this to start qr code scanner
                Intent intent = new Intent(ThirdScenario.this, QrActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Log.d("myTag", "position " + position);
        //get selected items
        if (position == 0) {
            return;
        }
        selectedValue = (String) getListAdapter().getItem(position - 1);

        if(!wifi_private[position-1]) {
            Toast.makeText(this, "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
            sendElapsedToServer();
        }

        else{
            wifi_password = wifi_passwords[position-1];
            password_query.show();
        }

    }
}
