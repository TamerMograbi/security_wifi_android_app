package com.example.security_wifi;

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


public class FirstScenario extends ListActivity {

    Button button;

    EditText input;

    String password_input;

    AlertDialog password_query;

    String wifi_password;

    String selectedValue;

    /*
    1&2- We will start with no defense on both wifi lists.
    3- Blinking defense on wifi_names
    4- Color defense on wifi_names with red added to the first element on the list
    5- Color defense on wifi_names3
    6- QR code on wifi_names
     */

    static final String[] wifi_names =
            new String[]{"BoardAndBrew", "xfinitywifi", "BoardAndBrew", "public_wifi", "XFINITY", "Employees"};

    static final String[] wifi_names2 =
            new String[]{"BoardAndBrew_5G", "xfinitywifi", "BoardAndBrew", "XFINITY", "Employees"};

    static final String[] wifi_names3 =
            new String[]{"BoardAndBrew", "xfinitywifi", "BoardAndBrew", "BoardAndBrew_guest",  "Employees", "XFINITY"};

    static final int[] wifi_strengths =
            new int[]{3, 3, 2, 2, 2, 2};

    static final int[] wifi_strengths2 =
            new int[]{4, 3, 3, 2, 2, 2};

    static final int[] wifi_strengths3 =
            new int[]{4, 3, 3, 2, 2, 1};

    static final boolean[] wifi_private =
            new boolean[]{true, false, true, false, true, true};

    static final boolean[] wifi_private2 =
            new boolean[]{false, false, true, true, true};

    static final boolean[] wifi_private3 =
            new boolean[]{true, false, true, false, true, true};

    static final String[] wifi_passwords =
            new String[]{"games471", "", "games471", "", "gsafjaslfk", "gfdlskafdla"};

    static final String[] wifi_passwords2 =
            new String[]{"", "", "games471", "gsafjaslfk", "gfdlskafdla"};

    static final String[] wifi_passwords3 =
            new String[]{"games471", "", "games471", "", "gsafjaslfk", "gfdlskafdla"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario1);

        setListAdapter(new MobileArrayAdapter(this, wifi_names, wifi_strengths, wifi_private));
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

    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //i want this to start qr code scanner
                Intent intent = new Intent(FirstScenario.this, QrActivity.class);
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
        }

        else{
            wifi_password = wifi_passwords[position-1];
            password_query.show();
        }

    }
}
