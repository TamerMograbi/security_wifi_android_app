package com.example.security_wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final int[] strength;
    private final boolean[] locked;
    private final int[] wifi_safety;

    public MobileArrayAdapter(Context context, String[] values, int[] strength, boolean[] locked, int[] wifi_safety) {
        super(context, R.layout.list_wifis, values);
        this.context = context;
        this.values = values;
        this.strength = strength;
        this.locked = locked;
        this.wifi_safety = wifi_safety;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_wifis, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        ImageView lockImageView = rowView.findViewById(R.id.lock);
        ImageView dotImageView = rowView.findViewById(R.id.colorDot);
        textView.setText(values[position]);
        lockImageView.setImageResource(0); //we clear the default image which is whatever is in list_wifis.xml

        // Change icon based on name
        String s = values[position];
        int wifi_strength = strength[position];
        boolean isLocked = locked[position];
        int safety = wifi_safety[position];

        System.out.println(s);

        /*
        if (s.equals("eduroam")) {
            imageView.setImageResource(R.drawable.signal1_bar_wifi);
            lockImageView.setImageResource(R.drawable.lock);
        } else if (s.equals("cafe")) {
            imageView.setImageResource(R.drawable.signal4_bar_wifi);
        } else if (s.equals("cafe_guest")) {
            imageView.setImageResource(R.drawable.signal1_bar_wifi);
            lockImageView.setImageResource(R.drawable.lock);
        } else {
            imageView.setImageResource(R.drawable.signal0_bar_wifi);
        }
         */

        switch (wifi_strength){
            case 0:
                imageView.setImageResource(R.drawable.signal0_bar_wifi);
                break;

            case 1:
                imageView.setImageResource(R.drawable.signal1_bar_wifi);
                break;

            case 2:
                imageView.setImageResource(R.drawable.signal2_bar_wifi);
                break;

            case 3:
                imageView.setImageResource(R.drawable.signal3_bar_wifi);
                break;

            case 4:
                imageView.setImageResource(R.drawable.signal4_bar_wifi);
                break;
        }

        if(isLocked)
        {
            lockImageView.setImageResource(R.drawable.lock);
        }

        switch (safety){
            case 0:
                break;

            case 1:
                dotImageView.setImageResource(R.drawable.reddot);
                break;

            case 2:
                dotImageView.setImageResource(R.drawable.yellowdot);
                break;

            case 3:
                dotImageView.setImageResource(R.drawable.greendot);
                break;

        }

        return rowView;
    }
}
