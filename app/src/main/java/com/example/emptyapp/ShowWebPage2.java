package com.example.emptyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emptyapp.R;
import com.example.emptyapp.ShowWebPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class ShowWebPage2 extends AppCompatActivity
{

    private static BluetoothSocket mmSocket;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice bluetoothDevice ;
    OutputStream mmOutStream = null;
    InputStream mmInputStream = null;
    int bytes = 50;
    int no_data_counter = 0;

    //String bluetooth_message = "SONG AND LINDA";

    boolean deviceFound;
    boolean alreadyConnected;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;

    int line_counter = 0;
    int Availability = 0;
    String JK;

    int FN;
    int AV;
    String url;
    String results2[] = new String[500];
    int id;
    public Menu menu;
    MenuItem menuItem;
    String Request_Mark;

    ShowWebPage.Singleton MY_URL = ShowWebPage.Singleton.getInstance();
    ShowWebPage.Singleton AV_INFO = ShowWebPage.Singleton.getInstance();
    ShowWebPage.Singleton FLOOR_NUMBER = ShowWebPage.Singleton.getInstance();

    private void checkBTPermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
            {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }
        else
        {
            Log.d("the app", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private void makeDiscoverable()
    {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Log.i("Log", "Discoverable ");
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                //Found, add to a device list
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d("Broadcast Received", "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);

                if(device.getName() != null && device.getName().contains("raspberrypi"))
                {
                    Log.d("BluetoothDevice ", "The bluetooth device seen is " + device.getName());
                    bluetoothDevice = device;

                    try {
                        createBond(bluetoothDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

        }
    };

    public void Discover_Devices()
    {
        Log.d("The app", "btnDiscover: Looking for unpaired devices.");

        if(bluetoothAdapter .isDiscovering()){
            bluetoothAdapter .cancelDiscovery();
            Log.d("The app", "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            bluetoothAdapter .startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(myReceiver, discoverDevicesIntent);
        }
        if(!bluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(myReceiver, discoverDevicesIntent);

        }
    }

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    Log.d("mBroadcastReceiver4", "BroadcastReceiver: BOND_BONDED.");

                    pairedDevices = bluetoothAdapter.getBondedDevices(); //recheck for the paired devices
                    findDevice(); //find all the paired devices and list them
                    BTConnect(); //find the raspberrypi , connect and send the data
                    unpairDevice(mDevice);

                }
                //case2: creating a bone
                else if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d("mBroadcastReceiver4", "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                else if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d("mBroadcastReceiver4", "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        System.out.println("Pairing status = "+ returnValue);
        return returnValue.booleanValue();
    }

    private void unpairDevice (BluetoothDevice device)
    {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("unpairDevice", e.getMessage());
        }
    }

    private void findDevice()
    {

        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                if (device.getName().equals("raspberrypi"))
                {
                    bluetoothDevice = device;
                    deviceFound = true;
                    System.out.println("raspberry pi found status = "+ deviceFound);
                    break;
                }
            }
        }
    }

    public void BTConnect()
    {

        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        int no_of_times_data_sent = 0;

        // If there are paired devices
        if (pairedDevices.size() > 0) {

            // Loop through paired devices
            for (BluetoothDevice device2 : pairedDevices)
            {

                if ((device2.getName().equals("raspberrypi")) && no_of_times_data_sent<1)
                {

                    try
                    {
                        // Get a BluetoothSocket to connect with the given BluetoothDevice.
                        // MY_UUID is the app's UUID string, also used in the server code.
                        if(mmSocket == null || mmSocket.getRemoteDevice() == null ||
                                !mmSocket.getRemoteDevice().getAddress().equals(device2.getAddress())) {
                            mmSocket = device2.createRfcommSocketToServiceRecord(MY_UUID);
                            alreadyConnected = false;


                        }
                        else
                        {
                            alreadyConnected = true;
                            Toast.makeText(ShowWebPage2.this,"Already Connect = true!",Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (IOException e)
                    {
                        Log.e("BTConnect()", "Socket's create() method failed", e);
                    }


                    if(!alreadyConnected)
                    {
                        // Cancel discovery because it otherwise slows down the connection.
                        bluetoothAdapter.cancelDiscovery();

                        try
                        {
                            // Connect to the remote device through the socket. This call blocks
                            // until it succeeds or throws an exception.
                            mmSocket.connect();
                            if (mmSocket.isConnected()) {


                                mmOutStream = mmSocket.getOutputStream();
                                mmOutStream.write(Request_Mark.getBytes());


                                byte[] buffer = new byte[50];
                                mmInputStream = mmSocket.getInputStream();
                                bytes = mmInputStream.read(buffer);
                                String confirmation_message = new String(buffer, 0, bytes);


                                if (confirmation_message.contains("Message received"))

                                    Toast.makeText(ShowWebPage2.this, "Connection successful!", Toast.LENGTH_LONG).show();
                                else
                                {
                                    Toast.makeText(ShowWebPage2.this, "Unsuccessful Connection!", Toast.LENGTH_LONG).show();
                                    System.out.println("Message from raspberrypi : " + confirmation_message);

                                }


                            }
                        }
                        catch (IOException connectException)
                        {
                            // Unable to connect; close the socket and return.
                            try
                            {
                                mmSocket.close();

                            } catch (IOException closeException)
                            {
                                Log.e("BTConnect()", "Could not close the client socket", closeException);

                            }
                        }
                    }
                    no_of_times_data_sent++;
                }
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        Log.d("hey", "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(myReceiver);
        unregisterReceiver(mBroadcastReceiver4);

    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        this.menu = menu;

        menuItem = menu.findItem(R.id.UseRobotButton);

        AV = AV_INFO.getAV_INFO();
        FN = FLOOR_NUMBER.getFLOOR_NUMBER();


        if ((AV>0) && (FN == 2))
        {
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.robotvectorfacetransparent));
            System.out.println(" Button Functionality ON !");

        }

        return super.onCreateOptionsMenu(menu);
    }



    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        id = item.getItemId();
        String Book_url = MY_URL.getString();


        if (id == R.id.UseRobotButton)
        {

                AV = AV_INFO.getAV_INFO();
                FN = FLOOR_NUMBER.getFLOOR_NUMBER();


                if ((AV>0) && (FN == 2))
                {
                    Toast.makeText(this, "Sending data...", Toast.LENGTH_SHORT).show();

                    lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
                    mBTDevices = new ArrayList<>();

                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(myReceiver, discoverDevicesIntent);
                    makeDiscoverable();

                    //Broadcasts when bond state changes (ie:pairing)
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                    registerReceiver(mBroadcastReceiver4, filter);

                    Discover_Devices(); //create bond in myReciever class
                }

                else
                {
                    if ((AV>0))
                        Toast.makeText(this, "The book is not on the 2nd floor of the University Library!", Toast.LENGTH_LONG).show();

                    else
                        Toast.makeText(this, "This book is unavailable!", Toast.LENGTH_LONG).show();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page2);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null)
        {
            url = bundle.getString("url");
            results2 = getIntent().getStringArrayExtra("RES");

        }

        else
        {
            Toast errorToast = Toast.makeText(ShowWebPage2.this, "Error sending data!", Toast.LENGTH_SHORT);
            errorToast.show();
        }

        WebView webView =  findViewById(R.id.webView);
        TextView contentView =  findViewById(R.id.contentView);


        WebSettings webSettings = webView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        try {
            Method m = WebSettings.class.getMethod("setMixedContentMode", int.class);
            if ( m == null )
            {
                Log.e("WebSettings", "Error getting setMixedContentMode method");
            }
            else {
                m.invoke(webSettings, 2); // 2 = MIXED_CONTENT_COMPATIBILITY_MODE
                Log.i("WebSettings", "Successfully set MIXED_CONTENT_COMPATIBILITY_MODE");
            }
        }
        catch (Exception ex) {
            Log.e("WebSettings", "Error calling setMixedContentMode: " + ex.getMessage(), ex);
        }


        class MyJavaScriptInterface
        {


            private TextView contentView;



            public MyJavaScriptInterface(TextView aContentView)
            {
                try
                {
                    synchronized(this)
                    {
                        wait(300);
                    }
                }

                catch(InterruptedException ex)
                {

                }
                contentView = aContentView;
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processContent(String aContent)
            {
                final String content = aContent;
                contentView.post(new Runnable()
                {
                    public void run()
                    {


                        contentView.setText(content);
                        System.out.print(content);

                        BufferedReader br = new BufferedReader(new StringReader(content));

                        String result[] = new String[500];
                        String line = "";


                        while (true)
                        {
                            try
                            {
                                if (!((line = br.readLine()) != null))
                                    break;
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }


                            result[line_counter] = line;
                            line_counter++;
                        }

                        //Toast.makeText(MainActivity.this,result[62],Toast.LENGTH_LONG).show();

                        for(int cc = 0; cc < line_counter; cc++)
                        {


                            if (result[cc].contains("서명/저자\t"))
                            {
                                JK = result[cc].replace("서명/저자\t","");
                                JK=JK.substring(0,JK.indexOf(" /"));


                                for(int b = 0; b < results2.length; b++)
                                {
                                    if(results2[b] != null)
                                    {

                                        if (results2[b].contains(JK) || ((Arrays.asList(result).contains("Status    Available"))) )
                                        {
                                            //Toast.makeText(ShowWebPage2.this, "found it at "+ b +" = " + results2[b], Toast.LENGTH_LONG).show();

                                            for(int bb = 0; bb < 5; bb++)
                                            {
                                                if ((results2[b+bb].contains("] Available")) || (Arrays.asList(result).contains("Status Available")))
                                                {
                                                    invalidateOptionsMenu();
                                                    Availability++;
                                                    AV_INFO.setAV_INFO(Availability);
                                                    Toast.makeText(ShowWebPage2.this, "This item is in the library", Toast.LENGTH_LONG).show();
                                                    Request_Mark = result[cc + 1].replace("청구기호\t","");
                                                    //Toast.makeText(ShowWebPage2.this, Request_Mark, Toast.LENGTH_LONG).show();


                                                    if (Arrays.asList(result).contains("Collected location\t2층 인문학자료실"))
                                                    {

                                                        Toast.makeText(ShowWebPage2.this, "You can now use the library robot!", Toast.LENGTH_LONG).show();
                                                        FLOOR_NUMBER.setFLOOR_NUMBER(2);
                                                    }
                                                    else
                                                    {
                                                        FLOOR_NUMBER.setFLOOR_NUMBER(0);
                                                    }

                                                }

                                            }
                                        }

                                    }

                                }

                            }

                            else

                            {

                            }
                        }






                    }
                });
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processHTML(String html)
            {
                //Html extract here

                final String content = html;
                contentView.post(new Runnable()
                {
                    public void run()
                    {
                        contentView.setText(content);
                        System.out.println(content);


                    }
                });

            }
        }




        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                try
                {
                    synchronized(this)
                    {
                        wait(1000);
                    }
                }

                catch(InterruptedException ex)
                {

                }


                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");

            }
        });

        webView.clearCache(true);
        webView.loadUrl(url);

    }
}
