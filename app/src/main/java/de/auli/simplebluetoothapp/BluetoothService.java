package de.auli.simplebluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = BluetoothService.class.getSimpleName();
    private static BluetoothService bluetoothService;
    private Context context;
    private Handler handler;
    private BluetoothAdapter adapter;
    private TextView txtOutput;
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
        private final BluetoothServerSocket serverSocket;
        BluetoothServerSocket tmp;

        public BluetoothServer(){
            try {
                BluetoothServerSocket tmp = adapter.listenUsingInsecureRfcommWithServiceRecord("Server", UUID_);
            } catch (IOException e) {
                Log.e(TAG, "Server nicht bereit", e);
            }
            serverSocket = tmp;
        }

        public void run(){
            BluetoothSocket socket;
            InputStream is;
            int bytes;
            while(true){
                final byte[] buffer = new byte[1024];
                try {
                    socket = serverSocket.accept();
                    is = socket.getInputStream();
                    bytes = is.read(buffer);
                    handler.post(() -> txtOutput.append(new String(buffer) + "\n"));
                } catch (IOException e){
                    Log.e(TAG, "Verbindung fehlgeschlagen", e);
                }
            }
        }
    }

    public class BluetoothClient extends Thread {
        private final BluetoothDevice device;
        private final BluetoothSocket socket;

        public BluetoothClient(String address) {
            device = adapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID_);
            } catch (IOException e) {
                Log.e(TAG, "Socketfehler", e);
            }
            socket = tmp;
        }

        public void run(){
            try {
                OutputStream os = socket.getOutputStream();
                EditText input = ((Activity)context).findViewById(R.id.txt_in);
                byte[] buffer = input.getText().toString().getBytes();
                os.write(buffer);
                os.flush();
                os.close();
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
    }
}
