package com.example.cybr8480;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.biometrics.BiometricPrompt;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {


    private final static int ALL_PERMISSIONS_RESULT = 101;
    private MyService mSensorService;
    private ESservice eSensorService;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    Intent mServiceIntent;
    Intent eServiceIntent;
    private TextView ambientValue, lightValue, pressureValue, humidityValue;
    Context ctx;
    TextView tvLatitude, tvLongitude, tvTime;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    LocationTrack locationTrack;
    public Context getCtx() {
        return ctx;
    }
private static  String[] INITIAL_PERMS={
  Manifest.permission.ACCESS_COARSE_LOCATION,
  Manifest.permission.ACCESS_COARSE_LOCATION
};




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        mSensorService = new MyService(getCtx());
        eSensorService= new ESservice(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        eServiceIntent= new Intent(getCtx(), eSensorService.getClass());
//
//        permissions.add(ACCESS_FINE_LOCATION);
//        permissions.add(ACCESS_COARSE_LOCATION);
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"BBBB",Toast.LENGTH_LONG).show();
        }else{
            requestPermissions(INITIAL_PERMS,1);
        }



        permissionsToRequest = findUnAskedPermissions(permissions);
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);

        }

        if (!isMyServiceRunning(eSensorService.getClass())) {
            startService(eServiceIntent);

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        ambientValue = (TextView)findViewById(R.id.ambient_text);
        TextView output = (TextView) findViewById(R.id.outputValue);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String someValue = intent.getStringExtra("someName");
                Log.i("l", "onReceive: I am here");
              ambientValue.setText(someValue);
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter("myBroadcastIntent"));


        try {
            getDataFromAccelerometer();
        } catch (IOException e) {
            e.printStackTrace();
        }


        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvTime = (TextView) findViewById(R.id.tvTime);

         getLocation();




    }







    private void getLocation() {
        locationTrack = new LocationTrack(MainActivity.this);



        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            tvLongitude.setText(Double.toString(longitude));
           tvLatitude.setText(Double.toString(latitude));
            Log.i("long",Double.toString(longitude));
        }else
        {
            locationTrack.showSettingsAlert();
        }
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void getDataFromAccelerometer() throws IOException {
        TextView output = (TextView) findViewById(R.id.outputValue);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        String nama = "datasensor.txt";
        File namafile = new File(nama);
        FileOutputStream fos = openFileOutput(nama, Context.MODE_PRIVATE);




        //use it here.



        //AccelerometerReading accl = new AccelerometerReading( senAccelerometer, senSensorManager);

    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        stopService(eServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }


}
