package com.example.algo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.io.IOException;

public class pulserate extends AppCompatActivity {

     private TextView pulse_rate;
     private Button show_pulse;
    private static final String ACTION_USB_PERMISSION = "com.example.USB_PERMISSION";
    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private PendingIntent permissionIntent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize UI components
        pulse_rate=findViewById(R.id.pulse_rate_disp);
        show_pulse=findViewById(R.id.button);

        View showPulseButton = null;
        showPulseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPulseRateFromarduino();
            }

            private void readPulseRateFromarduino() {
            }
        });

        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_pulserate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        class MainActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);


                usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                registerReceiver(usbReceiver, filter);

                discoverDevice();
            }

            private void discoverDevice() {
                for (UsbDevice device : usbManager.getDeviceList().values()) {
                    // Optionally check for specific device IDs
                    usbDevice = device;
                    usbManager.requestPermission(device, permissionIntent);
                }
            }

            private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (ACTION_USB_PERMISSION.equals(action)) {
                        synchronized (this) {
                            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                if (device != null) {
                                    // Call method to set up device communication
                                    setupDeviceCommunication(device);
                                }
                            } else {
                                Toast.makeText(context, "Permission denied for device " + device, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (device != null) {
                            // Request permission again if the device is attached after app start
                            usbManager.requestPermission(device, permissionIntent);
                        }
                    } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (device != null) {
                            // Handle USB device detach
                            closeDeviceConnection(device);
                        }
                    }
                }
            };

            private void setupDeviceCommunication(UsbDevice device) {
                UsbDeviceConnection connection = usbManager.openDevice(device);
                if (connection != null) {
                    // Communicate with the device, setup endpoints, etc.
                    readFromDevice(connection);
                }
            }

            private void readFromDevice(UsbDeviceConnection connection) {
                // Assuming a specific endpoint and interface are known
                // This part is very device-specific and needs to be tailored to your device
                UsbDevice device = findUsbDevice(usbManager);
                UsbInterface usbInterface = device.getInterface(0);
                int inEndpointAddress = 0x81; // Example endpoint address for bulk transfer
                byte[] newbuffer = new byte[1024];
                int timeout = 5000;
                ByteBuffer buffer = ByteBuffer.allocate(64);
                UsbEndpoint endpoint = usbInterface.getEndpoint(0);
                int bytesRead = connection.bulkTransfer(endpoint, buffer.array(), buffer.capacity(), timeout);


                if (bytesRead > 0) {
                    String receivedData = new String(buffer.array(), 0, bytesRead);
                    Log.i("MainActivity", "Data received: " + receivedData);
                }
            }

            private UsbDevice findUsbDevice(UsbManager usbManager) {
            return usbDevice;}

            private void closeDeviceConnection(UsbDevice device) {
                // Cleanup or notify user about device detachment
                Toast.makeText(this, "USB device detached: " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
                unregisterReceiver(usbReceiver);
            }
        }

    }
}