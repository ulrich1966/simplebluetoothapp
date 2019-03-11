package de.auli.simplebluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import java.util.UUID;

public class BluetoothService {
    private static BluetoothService bluetoothService;
    private Context context;
    private Handler handler;
    private BluetoothAdapter adapter;
    private TextView outputTxt;
    private static final UUID UUID_ = UUID.randomUUID();

    private BluetoothService(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized BluetoothService getInstance (Context context, Handler handler){
        if(bluetoothService == null){
            bluetoothService = new BluetoothService(context, handler);
        }
        return bluetoothService;
    }

    public class BluetoothServer extends Thread {

    }

    public class BluetoothClient extends Thread {

    }
}
