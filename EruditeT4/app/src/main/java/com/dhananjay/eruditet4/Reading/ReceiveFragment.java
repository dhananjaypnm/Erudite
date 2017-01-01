package com.dhananjay.eruditet4.Reading;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dhananjay.eruditet4.R;
import com.dhananjay.eruditet4.Reading.COReading;
import com.dhananjay.eruditet4.Reading.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiveFragment extends Fragment implements View.OnClickListener {

    private static final String TAG="ReceiveFragment";
    DatabaseHelper helper;
    Dao<COReading,Long> dao;
    List<COReading> vitalSignsReadingList;

    Button buttonReceive,scan,add;
    TextView valueReceive,averageReceive;

    BluetoothAdapter BTAdapter;
    boolean start=true;
    List<DeviceItem> deviceItemList;





    public ReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonReceive= (Button) view.findViewById(R.id.button_receive);
        valueReceive= (TextView) view.findViewById(R.id.value_receive);
        scan= (Button) view.findViewById(R.id.button_scan);
        add= (Button) view.findViewById(R.id.button_add);

        helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
        try {
            dao=helper.getDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        buttonReceive.setOnClickListener(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double val=0.0;
                val=Double.parseDouble(values[1]);
                long currTime=System.currentTimeMillis()/1000;
                COReading coReading=new COReading(currTime,String.valueOf(val));
                try {
                    valueReceive.setText("Received value: "+val);



                    List<COReading> checkingList=new ArrayList<COReading>();
                    QueryBuilder<COReading,Long> queryBuilder=helper.getDao().queryBuilder();
                    queryBuilder.where().eq("recordedTimestamp",currTime);
                    checkingList=queryBuilder.query();
                    if(checkingList.size()==0){
                        dao.createIfNotExists(coReading);
                        Toast.makeText(getContext(), "Reading Added", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: reading added "+coReading.value);
                    }else {
                        Log.d(TAG, "onClick: already added");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                if (start) {
                    scan.setText("cancel");

                    deviceItemList.clear();
                    getActivity().registerReceiver(bReciever, filter);
                    BTAdapter.startDiscovery();
                    Log.d(TAG, "onClick: started discovery");
                    start=false;
                } else {
                    scan.setText("scan");

                    getActivity().unregisterReceiver(bReciever);
                    BTAdapter.cancelDiscovery();
                    start=true;
                    Log.d(TAG, "onClick: stopped discovery");
                }
            }

        });

        deviceItemList = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                deviceItemList.add(newDevice);
                Log.d(TAG, "onViewCreated: "+newDevice.getDeviceName());
            }
        }else{
            Log.d(TAG, "onViewCreated: list size 0");
        }


    }

    BluetoothSocket btsoccet;


    public class ConnectThread extends Thread{
        BluetoothSocket bTSocket;
        BluetoothDevice bTDevice;
        public ConnectThread(BluetoothDevice bTDevice, UUID UUID) {
            BluetoothSocket tmp = null;
            this.bTDevice = bTDevice;

            try {
                tmp = this.bTDevice.createRfcommSocketToServiceRecord(UUID);
                Log.d(TAG, "ConnectThread: created socket");
            }
            catch (IOException e) {
                Log.d("CONNECTTHREAD", "Could not start listening for RFCOMM");
            }
            bTSocket = tmp;
        }

        public boolean connect(BluetoothDevice bTDevice, UUID mUUID) {
            try {
                bTSocket.connect();
                Log.d(TAG, "connect: connected socket");
                btsoccet=bTSocket;
                Log.d(TAG, "connect: connected");
                isConnected=true;
                Toast.makeText(getContext(), "connected", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connect: 00001101-0000-1000-8000-00805F9B34FB ");
                Toast.makeText(getContext(), "UUID :00001101-0000-1000-8000-00805F9B34FB", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
                try {
                    bTSocket.close();
                } catch(IOException close) {
                    Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                    return false;
                }
            }
            return true;
        }

        public boolean cancel() {
            try {
                bTSocket.close();
                Log.d(TAG, "cancel: socket not connected... cancelled");
                isConnected=false;
            } catch(IOException e) {
                Log.d("CONNECTTHREAD","Could not close connection:" + e.toString());
                return false;
            }
            return true;
        }
    }

    String[] values;

    public class ManageConnectThread extends Thread {

        public ManageConnectThread() { }

        public void sendData(BluetoothSocket socket, int data) throws IOException{
            Log.d(TAG, "sendData: sending data");
            ByteArrayOutputStream output = new ByteArrayOutputStream(4);
            output.write(data);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(output.toByteArray());
        }
        public int receiveData(BluetoothSocket socket) throws IOException{
            byte[] buffer = new byte[256];
            int bytes;
            ByteArrayInputStream input = new ByteArrayInputStream(buffer);
            InputStream inputStream = socket.getInputStream();

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "receiveData: "+readMessage);
                    values= readMessage.split("\\r?\\n");
                } catch (IOException e) {
                    break;
                }
            }


            Toast.makeText(getContext(), "receiving...", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "receiveData: "+inputStream.toString());
            return input.read();
        }
    }


    BluetoothDevice btdevice;
    UUID btuuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: received action"+action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "onReceive: found devices");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btdevice=device;
              /* UUID uuid=device.getUuids()[0].getUuid();
                btdevice=device;
                btuuid=uuid;
                Log.d(TAG, "onReceive: "+uuid);
                Toast.makeText(context, "uuid :"+uuid, Toast.LENGTH_SHORT).show();*/
                // Create a new device item
                Toast.makeText(context, "onReceive: "+device.getAddress(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "onReceive: "+device.getName(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onReceive: "+device.getAddress());
                Log.d(TAG, "onReceive: "+device.getName());
                Log.d(TAG, "onReceive: "+device.getUuids());
                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), "false");
                // Add it to our adapter
                deviceItemList.add(newDevice);
                Log.d(TAG, "onReceive: device added");
            }
        }
    };

    boolean isConnected=false;


    @Override
    public void onClick(View view) {

        ConnectThread connectThread=new ConnectThread(btdevice,btuuid);
        ManageConnectThread manageConnectThread=new ManageConnectThread();
        if(!isConnected){
            connectThread.connect(btdevice,btuuid);
        }else{
            connectThread.cancel();
        }
        Toast.makeText(getContext(), "connecting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onClick: connecting");
        try {
            manageConnectThread.receiveData(btsoccet);
            Log.d(TAG, "onClick: receiving data");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
