package com.example.security_wifi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class QrActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;

    private Socket mSocket;

    long tStart;

    {
        try {
            //try to visit this url on your laptop and see that clicking on a wifi updates
            //this page
            mSocket = IO.socket("https://murmuring-wildwood-99815.herokuapp.com/");
        } catch (URISyntaxException e) { Log.d("myTag", "failed to connect");}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mSocket.connect();
        tStart = System.currentTimeMillis();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(QrActivity.this)
                                .setTitle("You connected to")
                                .setMessage("BoardAndBrew")
                                //.setMessage(result.getText())

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                //.setPositiveButton(android.R.string.yes, null)

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                //.setNegativeButton(android.R.string.no, null)
                                //.setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void sendElapsedToServer() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.d("myTag", "took  " + elapsedSeconds + " time to complete scenario 6");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("QR time",elapsedSeconds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("elapsed",jsonObject);

    }
}
