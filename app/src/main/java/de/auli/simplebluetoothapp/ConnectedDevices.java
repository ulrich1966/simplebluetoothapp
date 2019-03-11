package de.auli.simplebluetoothapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConnectedDevices extends AppCompatActivity {
    private  BluetoothService service;
    private  BluetoothService.BluetoothClient client;
    private  BluetoothService.BluetoothServer server;
    private TextView txtViewConnAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_devices);
    }

    public void send(View view) {
    }

    public void connAsClient(View view) {
    }

    public void connAsServer(View view) {
    }
}
