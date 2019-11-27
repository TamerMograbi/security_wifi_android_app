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

    int selectedStrength;

    private Socket mSocket;

    private int attacker_pos = 1;
    private int real_wifi_pos = 3;


    String[] correct_pattern_server = {"red","green","blue"};
    String[] wrong_pattern_server = {"MediumVioletRed","PowderBlue","LightCoral"};

    int[] correct_pattern_phone = {Color.parseColor("#FF0000"),Color.parseColor("#00FF00"),Color.parseColor("#0000FF")};
    int[] wrong_pattern_phone = {Color.parseColor("#C71585"),Color.parseColor("#B0E0E6"),Color.parseColor("#F08080")};

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
            new String[]{"secret150", "", "secret150", "", "gsafjaslfk", "gfdlskafdla"};

    static final int[] wifi_safety =
            new int[]{0,0,0,0,0,0};



    void sendBlinkRequestToServer(String color1,String color2, String color3) {
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject colorsObj = new JSONObject();
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("color",color1);
            jsonObject2 = new JSONObject();
            jsonObject2.put("color",color2);
            jsonObject3 = new JSONObject();
            jsonObject3.put("color",color3);

            jsonArray.put(jsonObject1);
            jsonArray.put(jsonObject2);
            jsonArray.put(jsonObject3);

            colorsObj.put("colors", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("news",colorsObj);
    }

    void makePhoneBlink(final View blinkerView,final int color1,final int color2,final int color3) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blinkerView.setBackgroundColor(color1);
                blinkerView.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blinkerView.setBackgroundColor(color2);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                blinkerView.setBackgroundColor(color3);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blinkerView.setVisibility(View.INVISIBLE);
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_scenario);
        mSocket.connect();
        tStart = System.currentTimeMillis();
        final View blinker = findViewById(R.id.blinker);

        blinker.setVisibility(View.INVISIBLE);

        final MobileArrayAdapter adapter = new MobileArrayAdapter(this, wifi_names, wifi_strengths, wifi_private, wifi_safety);
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
                if(position == attacker_pos) {
                    sendBlinkRequestToServer(wrong_pattern_server[0],wrong_pattern_server[1],wrong_pattern_server[2]);
                    makePhoneBlink(blinker,correct_pattern_phone[0],correct_pattern_phone[1],correct_pattern_phone[2]);
                    Toast.makeText(getApplicationContext(), "asking " +selectedValue + " to blink " , Toast.LENGTH_SHORT).show();
                }
                else if(position == real_wifi_pos) {
                    sendBlinkRequestToServer(correct_pattern_server[0],correct_pattern_server[1],correct_pattern_server[2]);
                    makePhoneBlink(blinker,correct_pattern_phone[0],correct_pattern_phone[1],correct_pattern_phone[2]);
                    Toast.makeText(getApplicationContext(), "asking " +selectedValue + " to blink " , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), selectedValue + " doesn't support blinking" , Toast.LENGTH_SHORT).show();
                }





                return true;

//                JSONObject jsonObject1 = new JSONObject();
//                JSONObject jsonObject2 = new JSONObject();
//                JSONObject jsonObject3 = new JSONObject();
//                JSONArray jsonArray = new JSONArray();
//                JSONObject colorsObj = new JSONObject();
//                try {
//                    jsonObject1 = new JSONObject();
//                    jsonObject1.put("color","red");
//                    jsonObject2 = new JSONObject();
//                    jsonObject2.put("color","green");
//                    jsonObject3 = new JSONObject();
//                    jsonObject3.put("color","blue");
//
//                    jsonArray.put(jsonObject1);
//                    jsonArray.put(jsonObject2);
//                    jsonArray.put(jsonObject3);
//
//                    colorsObj.put("colors", jsonArray);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                mSocket.emit("news",colorsObj);
                //blinker.setBackgroundColor(Color.parseColor("#00FF00"));

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        blinker.setBackgroundColor(Color.parseColor("#FF0000"));
//                        blinker.setVisibility(View.VISIBLE);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                blinker.setBackgroundColor(Color.parseColor("#00FF00"));
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        blinker.setBackgroundColor(Color.parseColor("#0000FF"));
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                blinker.setVisibility(View.INVISIBLE);
//                                            }
//                                        }, 1000);
//                                    }
//                                }, 1000);
//                            }
//                        }, 1000);
//                    }
//                }, 1000);
//
//                return true;
            }

        });
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
                    Toast.makeText(getApplicationContext(), "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
                    sendElapsedToServer();

                    Intent intent = new Intent(ThirdScenario.this, FourthPrompt.class);
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

    public void sendElapsedToServer() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.d("myTag", "took  " + elapsedSeconds + " time to complete scenario 3");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("scenario 3 wifi", selectedValue + " " + selectedStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("scenario 3 time",elapsedSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("elapsed",jsonObject);

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
            Toast.makeText(this, "connected to " + selectedValue, Toast.LENGTH_SHORT).show();
            sendElapsedToServer();
            Intent intent = new Intent(ThirdScenario.this, FourthPrompt.class);
            startActivity(intent);
        }

        else{
            wifi_password = wifi_passwords[position-1];
            password_query.show();
        }

    }
}
