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

    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_wifis, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_wifis, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        if (s.equals("eduroam")) {
            imageView.setImageResource(R.drawable.wifi_one_bar);
        } else if (s.equals("cafe")) {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        } else if (s.equals("cafe_guest")) {
            imageView.setImageResource(R.drawable.wifi_one_bar);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        return rowView;
    }
}
