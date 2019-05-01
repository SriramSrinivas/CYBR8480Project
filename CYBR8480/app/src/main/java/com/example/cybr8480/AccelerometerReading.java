package com.example.cybr8480;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccelerometerReading implements SensorEventListener  {

     private FileOutputStream outvalue;
    private TextView output;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private SensorEvent event;


    public AccelerometerReading( Sensor senAcceleromete, SensorManager senSensorManage)
    {
      //  output=textView;
       senSensorManager=senSensorManage;
        senAccelerometer=senAcceleromete;
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

//        outvalue=out;


    }





//     private long lastUpdate = 0;
//     private float last_x, last_y, last_z;
//     private static final int SHAKE_THRESHOLD = 600;





     @Override
     public void onSensorChanged (SensorEvent event) {

         Sensor mySensor = event.sensor;

         if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
             float x = event.values[0];
             float y = event.values[1];
             float z = event.values[2];

//             output.setText("x=" + x + "y=" + y + "z=" + z);
            // Log.i("acc",("x=" + x + "y=" + y + "z=" + z));
//             try {
//                 String value=String.valueOf(x)+","+String.valueOf(y)+","+ String.valueOf(z);
//               //  outvalue.write(value.getBytes());
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//             Log.i("acc", Float.toString(x) + Float.toString(y)+Float.toString(z));

         }
     }




    @Override
     public void onAccuracyChanged (Sensor sensor,int accuracy){

     }
 }

