package com.theparkcorp.azureware3;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static java.util.UUID.fromString;

public class DataActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback{

    private static final String TAG = "BluetoothGattActivity";


    private static final String DEVICE_NAME = "CC2650 SensorTag";

    //ACC Serv UUID
    // private final static UUID UUID_ACC_SERV = fromString("f000aa10-0451-4000-b000-000000000000");
    // private final static UUID UUID_ACC_DATA = fromString("f000aa11-0451-4000-b000-000000000000");
    // private final static UUID UUID_ACC_CONF = fromString("f000aa12-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    // private final static UUID UUID_ACC_PERI = fromString("f000aa13-0451-4000-b000-000000000000");

    //Motion Service UUID
    private static final UUID MOTION_SERV = UUID.fromString("f000aa80-0451-4000-b000-000000000000");
    private static final UUID MOTION_DATA = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    private static final UUID MOTION_CONF = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    private static final UUID MOTION_CAL_CHAR = UUID.fromString("f000aa83-0451-4000-b000-000000000000");
    //Config Descriptor
    private static final UUID CONFIG_DESCRIPTOR = fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter;
    private SparseArray<BluetoothDevice> mDevices;

    private BluetoothGatt mConnectedGatt;

    private TextView mAccelDataX, mAccelDataY, mAccelDataZ;

    private TextView mScanInfo;

    private ProgressDialog mProgress;

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private static double accelX;
    private static double accelY;
    private static double accelZ;

    private static double[] xArrayData = new double[1000];
    private static double[] yArrayData = new double[1000];
    private static double[] zArrayData = new double[1000];

    private static int valCounter = 0;

    private static double zAxisAverage = 0;

    private static double baseAccelX;
    private static double baseAccelY;
    private static double baseAccelZ;

    private static double minBaseAccelX;
    private static double minBaseAccelY;
    private static double minBaseAccelZ;

    private static double avgX;
    private static double avgY;
    private static double avgZ;

    private static double finalX;
    private static double finalY;
    private static double finalZ;

    private static double finalZ2;

    private static int finalNum;
    private static int TremorRating;


    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLUETOOTH_LE is not supported for this device!", Toast.LENGTH_LONG).show();
            finish();
            Toast.makeText(this, "Checked Package Manager 1", Toast.LENGTH_SHORT).show();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect devices");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        mAccelDataX = (TextView) findViewById(R.id.mAccelDataX);
        mAccelDataY = (TextView) findViewById(R.id.mAccelDataY);
        mAccelDataZ = (TextView) findViewById(R.id.mAccelDataZ);

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();

        mDevices = new SparseArray<BluetoothDevice>();

        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BluetoothManager.getAdapter()==null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mScanInfo = (TextView) findViewById(R.id.ScanButtonText);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextScreenAuto = new Intent(DataActivity.this, ResultActivity.class);
                DataActivity.this.startActivity(nextScreenAuto);
                DataActivity.this.finish();
            }
        },25000);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
                Toast.makeText(this, "BT Requested", Toast.LENGTH_SHORT).show();
            }
        }

        clearDisplayValues();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
            finish();
            Toast.makeText(this, "BT Canceled", Toast.LENGTH_SHORT).show();
            return;
        } else if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION && resultCode == Activity.RESULT_CANCELED) {
            finish();
            Toast.makeText(this, "Location canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mProgress.dismiss();

        mHandler.removeCallbacks(mStopRunnable);
        mHandler.removeCallbacks(mStartRunnable);
        mBluetoothAdapter.stopLeScan(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mConnectedGatt != null){
            mConnectedGatt.disconnect();
            mConnectedGatt = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);

        for (int i=0; i < mDevices.size(); i++){
            BluetoothDevice device = mDevices.valueAt(i);
            menu.add(0, mDevices.keyAt(i), 0, device.getName());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){

            case R.id.action_scan:
                mDevices.clear();
                startScan();
                mScanInfo.setText("Press the 3 dots");
                return true;
            default:
                BluetoothDevice device = mDevices.get(item.getItemId());
                Log.i(TAG, "Connecting to " + device.getName());
                /*
                 * Make a connection with the device using the special LE-specific
                 * connectGatt() method, passing in a callback for GATT events
                 */
                mConnectedGatt = device.connectGatt(this, false, mGattCallback);
                //Display progress UI
                mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Connecting to " + device.getName() + "..."));
                return super.onOptionsItemSelected(item);

        }
    }

    private void clearDisplayValues(){

        mAccelDataX.setText("---");
        mAccelDataY.setText("---");
        mAccelDataZ.setText("---");
    }

    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };

    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };

    private void startScan(){
        mBluetoothAdapter.startLeScan(this);
        setProgressBarIndeterminateVisibility(true);

        mHandler.postDelayed(mStopRunnable, 2500);

    }

    private void stopScan(){
        mBluetoothAdapter.stopLeScan(this);
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi);
        if(DEVICE_NAME.equals(device.getName())){
            mDevices.put(device.hashCode(), device);
            invalidateOptionsMenu();
        }
    }
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        private int mState = 0;

        private void reset(){
            mState = 0;
        }

        private void advance(){
            mState++;
        }

        private void enableNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;

            switch (mState) {
                case 0:
                    Log.d(TAG, "Enabling Motion Services");
                    characteristic = gatt.getService(MOTION_SERV).getCharacteristic(MOTION_CONF);
                    characteristic.setValue(new byte[]{0x7F, 0x00});
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sesnors Enabled");
                    return;
            }

            gatt.writeCharacteristic(characteristic);
        }

        private void readNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;

            switch (mState) {
                case 0:
                    Log.d(TAG, "READING MOTION DATA");
                    characteristic = gatt.getService(MOTION_SERV).getCharacteristic(MOTION_DATA);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }
            gatt.readCharacteristic(characteristic);
        }

        private void setNotifySensor (BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Set Notify Motion Sensors");
                    characteristic = gatt.getService(MOTION_SERV).getCharacteristic(MOTION_DATA);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }
            gatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor desc = characteristic.getDescriptor(CONFIG_DESCRIPTOR);
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(desc);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "Connection State Change: "+status+" -> "+connectionState(newState));
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
                gatt.discoverServices();
                mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Discovering Services..."));
            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
                mHandler.sendEmptyMessage(MSG_CLEAR);
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                /*
                 * If there is a failure at any stage, simply disconnect
                 */
                gatt.disconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "Services Discovered: "+status);
            mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Enabling Sensors..."));
            reset();
            enableNextSensor(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (MOTION_DATA.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_MOTION, characteristic));
            }

            setNotifySensor(gatt);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            readNextSensor(gatt);
            Log.i(TAG, "DATA: " + characteristic);
            Log.i(TAG, "DATA: " + gatt);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(MOTION_DATA.equals(characteristic.getUuid())){
                mHandler.sendMessage(Message.obtain(null, MSG_MOTION, characteristic));
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            advance();
            enableNextSensor(gatt);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: "+rssi);
        }

        private String connectionState(int status){
            switch (status){
                case BluetoothProfile.STATE_CONNECTED:
                    return "Connected";
                case BluetoothProfile.STATE_DISCONNECTED:
                    return "Disconnected";
                case BluetoothProfile.STATE_CONNECTING:
                    return "Connecting";
                case BluetoothProfile.STATE_DISCONNECTING:
                    return "Disconnecting";
                default:
                    return String.valueOf(status);
            }
        }
    };

    private static final int MSG_MOTION = 101;
    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what){
                case MSG_MOTION:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null){
                        Log.w(TAG, "Error obtaining accelerometer values");
                        return;
                    }
                    updateAccelValues(characteristic);
                    break;
                case MSG_PROGRESS:
                    mProgress.setMessage((String) msg.obj);
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                    break;
                case MSG_DISMISS:
                    mProgress.hide();
                    break;
                case MSG_CLEAR:
                    clearDisplayValues();
                    break;
            }
        }
    };

    private void updateAccelValues (BluetoothGattCharacteristic c){

        accelX = SensorTagData.extractAccelX(c);
        accelY = SensorTagData.extraAccelY(c);
        accelZ = SensorTagData.extraAccelZ(c);

        xArrayData[valCounter] = accelX;
        Log.d(TAG, "valCounter = " + valCounter + "accelX = " + accelX);
        yArrayData[valCounter] = accelY;
        Log.d(TAG, "valCounter = " + valCounter + "accelY = " + accelY);
        zArrayData[valCounter] = accelZ;
        Log.d(TAG, "valCounter = " + valCounter + "accelZ = " + accelZ);
        valCounter++;

        mAccelDataX.setText(String.format("%.2f%%", accelX));
        mAccelDataY.setText(String.format("%.2f%%", accelY));
        mAccelDataZ.setText(String.format("%.2f%%", accelZ));
        mScanInfo.setText("Testing, Please wait...");

        zAxisAverage = ((zAxisAverage + accelZ)/2);
        Log.d(TAG, "valCounter = " + valCounter + "accelZ = " + accelZ + "zAxisAverage = " + zAxisAverage);

    }

    public static double[] retArray (){

        return zArrayData;
    }

    public static int getResult (double finalZ){

        finalZ = (zAxisAverage*100);
        finalZ2 = Math.abs(finalZ);
        Log.d(TAG,"finalZ2 = " + finalZ);

        Log.d(TAG, "finalZ2 = " + finalZ2);

        if (finalZ2 <= 0 && finalZ2 >= 2) {
            Log.d(TAG, "TremRat = 0, No tremor is available or tremor is negligible");
            TremorRating = 0;
        }else if (finalZ2 <= 11 && finalZ2 >= 3) {
            Log.d(TAG, "TremRat = 1, Slight Tremor is identified");
            TremorRating = 1;
        }else if (finalZ2 >= 12 && finalZ2 <= 16) {
            Log.d(TAG, "TremRat = 2, Mild Tremor is identified");
            TremorRating = 2;
        }else if (finalZ2 >= 17 && finalZ2 <= 26) {
            Log.d(TAG, "TremRat = 3, Moderate Tremor is identified");
            TremorRating = 3;
        }else if (finalZ2 >= 27) {
            Log.d(TAG, "TremRat = 4, Severe Tremor is identified");
            TremorRating = 4;
        }

        //finalNum = Integer.parseInt(TremorRating);

        return TremorRating;
    }

    public static int sendRes(){

        return TremorRating;
    }








/*
        int i;

        baseAccelX = 0;
        baseAccelY = 0;
        baseAccelZ = 0;

        minBaseAccelX = -0.01;
        minBaseAccelY = -0.01;
        minBaseAccelZ = -0.01;


        for (i=0; i<250; i++){
            if (baseAccelX > accelX){
                baseAccelX = accelX;
            }else if (minBaseAccelX < accelX){
                minBaseAccelX = accelX;
            }else{
                continue;
            }
        }

        for (i=0; i<250; i++){
            if (baseAccelY > accelY){
                baseAccelY = accelY;
            }else if (minBaseAccelY < accelY){
                minBaseAccelY = accelY;
            }else{
                continue;
            }
        }

        for (i=0; i<250; i++){
            if (baseAccelZ > accelZ){
                baseAccelZ = accelZ;
            }else if (minBaseAccelZ < accelZ){
                minBaseAccelZ = accelZ;
            }else{
                continue;
            }
        }

        avgX = baseAccelX - minBaseAccelX;
        avgY = baseAccelY - minBaseAccelY;
        avgZ = baseAccelZ - minBaseAccelZ ;

        finalX = avgX/2;
        finalY = avgY/2;
        finalZ = avgZ/2;

        return finalZ;
*/

}





