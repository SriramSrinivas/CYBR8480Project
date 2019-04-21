package com.example.cybr8480;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class AccelerometerReading implements SensorEventListener  {


    private TextView output;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;


    public AccelerometerReading(TextView textView, Sensor senAcceleromete, SensorManager senSensorManage)
    {
        output=textView;
       senSensorManager=senSensorManage;
        senAccelerometer=senAcceleromete;
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }





//     private long lastUpdate = 0;
//     private float last_x, last_y, last_z;
//     private static final int SHAKE_THRESHOLD = 600;





     @Override
     public void onSensorChanged (SensorEvent event){

         Sensor mySensor = event.sensor;

         if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
             float x = event.values[0];
             float y = event.values[1];
             float z = event.values[2];

             output.setText("x=" + x + "y=" + y + "z=" + z);

         }

     }

     @Override
     public void onAccuracyChanged (Sensor sensor,int accuracy){

     }
 }

