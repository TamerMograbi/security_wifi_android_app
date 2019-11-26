package com.example.security_wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FourthPrompt extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_prompt);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        button = findViewById(R.id.nextButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(FourthPrompt.this, FourthScenario.class);
                startActivity(intent);
            }

        });
    }
}
