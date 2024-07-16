package com.dhee.abox;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NetActivity extends AppCompatActivity {

    private static final String TAG = "NetActivity";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        // Call the method to get IP addresses
        String[] ipAddresses = getDeviceIpAddress();

        // Display IPv4 and IPv6 addresses
        TextView textViewIPv4 = findViewById(R.id.textViewIPv4);
        TextView textViewIPv6 = findViewById(R.id.textViewIPv6);
        textViewIPv4.setText("IPv4 Address: " + ipAddresses[1]);
        textViewIPv6.setText("IPv6 Address: " + ipAddresses[0]);

        // Call the AsyncTask to ping the default gateway and display the result in ms

        // Display the current network device count
        int deviceCount = getNetworkDeviceCount();
        TextView textViewDeviceCount = findViewById(R.id.textViewDeviceCount);
        textViewDeviceCount.setText("Current Network Device Count: " + deviceCount);
    }


    private String[] getDeviceIpAddress() {
        String[] ipAddresses = new String[2];
        ipAddresses[0] = ""; // IPv6 address
        ipAddresses[1] = ""; // IPv4 address

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                List<InetAddress> addresses = Collections.list(iface.getInetAddresses());
                Iterator<InetAddress> addressIterator = addresses.iterator();
                while (addressIterator.hasNext()) {
                    InetAddress address = addressIterator.next();
                    if (!address.isLoopbackAddress()) {
                        String ip = address.getHostAddress();
                        if (ip.contains(":")) {  // IPv6 address
                            int delimIndex = ip.indexOf('%');  // remove the '%' suffix
                            ipAddresses[0] = delimIndex < 0 ? ip.toUpperCase() : ip.substring(0, delimIndex).toUpperCase();
                        } else {  // IPv4 address
                            ipAddresses[1] = ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipAddresses;
    }



    private int getNetworkDeviceCount() {
        int deviceCount = 0;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            deviceCount = interfaces.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceCount;
    }

}
