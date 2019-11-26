package com.example.security_wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondPrompt extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_prompt);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        button = findViewById(R.id.nextButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SecondPrompt.this, SecondScenario.class);
                startActivity(intent);
            }

        });
    }
}
