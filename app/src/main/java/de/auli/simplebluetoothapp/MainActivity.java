package de.auli.simplebluetoothapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.tv.TvContract;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = AppCompatActivity.class.getName();
    private ListView liviDevices;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> list;
    private Set<BluetoothDevice> boundDivices;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals((intent.getAction()))){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String value = String.format("%s - %s - %s", device.getAddress(), device.getName(), device.createBond());
                Log.d(TAG, value);
                arrayAdapter.add(value);
                //if()
            }
    }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liviDevices = findViewById(R.id.livi_divices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        liviDevices.setAdapter(arrayAdapter);
        liviDevices.setOnItemClickListener((parent, view, position, id) -> {
            String address_plus = (String) parent.getItemAtPosition(position);
            String address = address_plus.substring(0, address_plus.indexOf("-")).trim();
            Intent intent = new Intent(MainActivity.this, ConnectedDevices.class);
            startActivity(intent);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            }
        }

        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, foundFilter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if((requestCode == 123) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED));
    }

    public void onClickOn(View view) {
        if(!bluetoothAdapter.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(turnOn);
            Toast.makeText(this,"Turned on" , Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this,"Already on" , Toast.LENGTH_SHORT);
        }
    }

    public void onClickOff(View view) {
        bluetoothAdapter.disable();
        Toast.makeText(this,"Turned off" , Toast.LENGTH_SHORT);
    }

    public void onClickVisible(View view) {
        Intent setVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        setVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,180 );
        startActivity(setVisible);
    }

    public void onClickDivices(View view) {
        boundDivices = bluetoothAdapter.getBondedDevices();
        arrayAdapter.clear();
        for (BluetoothDevice device : boundDivices){
            String value = String.format("%s - %s - %s", device.getAddress(), device.getName(), device.createBond());
            Log.d(TAG, value);
            arrayAdapter.add(value);
        }
    }

    public void onClickSearch(View view) {
        arrayAdapter.clear();
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
