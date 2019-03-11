package de.auli.simplebluetoothapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConnectedDevices extends AppCompatActivity {
    private BluetoothService service;
    private BluetoothService.BluetoothClient client;
    private BluetoothService.BluetoothServer server;
    private TextView txtViewConnAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_devices);
        service = BluetoothService.getInstance(this, new Handler());
        txtViewConnAs = findViewById(R.id.txt_connected_as);
    }

    public synchronized void send(View view) {
    }

    public synchronized void connAsClient(View view) {
        txtViewConnAs.setText("Verbunden als Client");
        String address = getIntent().getStringExtra("address");
        client = service.new BluetoothClient(address);
    }

    public synchronized void connAsServer(View view) {
        txtViewConnAs.setText("Verbunden als Server");
        BluetoothService.BluetoothServer server = service.new BluetoothServer();
   }
}
