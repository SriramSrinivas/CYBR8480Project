package com.example.cybr8480;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.example.cybr8480.ApiUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import static android.content.ContentValues.TAG;

public class MyService extends Service implements SensorEventListener{

    public int counter=0;
    private MyService mSensorService;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senSorValue1;
    private Sensor senSorValue2;
    private Sensor senSorValue3;
    private Sensor senSorValue4;
    private Sensor senSorValue5;
    private Sensor senSorValue6;
    private Sensor senSorValue7;
    private Sensor senSorValue8;
    private Sensor senSorValue9;
    private Sensor senSorValue10;
    private Sensor senSorValue11;


    private Button ambientBtn, lightBtn, pressureBtn, humidityBtn;
    private TextView ambientValue, lightValue, pressureValue, humidityValue;
    private String value;
    private SensorManager senseManage;
    private Sensor envSense;
    private TextView[] valueFields = new TextView[4];
    private final int AMBIENT=0;
    FileOutputStream fos = null;

    String nama = "datasensor.txt";
    File namafile = new File(nama);

    public MyService(Context applicationContext)  {
        super();
        Log.i("Here", "MyService: ");




    }

    public MyService()
    {

    }


    private void sendBroadcast( String value) {
        Intent intent = new Intent("myBroadcastIntent");
        intent.putExtra("someName", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();



        senseManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        envSense = senseManage.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        senAccelerometer = senseManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSorValue1=senseManage.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        senSorValue2=senseManage.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senSorValue3=senseManage.getDefaultSensor(Sensor.TYPE_LIGHT);
        senSorValue4=senseManage.getDefaultSensor(Sensor.TYPE_PRESSURE);
        senSorValue6=senseManage.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        senSorValue7=senseManage.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senSorValue8=senseManage.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senSorValue9=senseManage.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if(envSense==null)
            Toast.makeText(this.getApplicationContext(),
                    "Sorry - your device doesn't have an ambient temperature sensor!",
                    Toast.LENGTH_SHORT).show();
        else
            senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
        //schedule the timer, to wake up every 1 second


        if(senSorValue1!=null)
            senseManage.registerListener(this, senSorValue1, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue2!=null)
            senseManage.registerListener(this, senSorValue2, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue3!=null)
            senseManage.registerListener(this, senSorValue3, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue4!=null)
            senseManage.registerListener(this, senSorValue4, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue5!=null)
            senseManage.registerListener(this, senSorValue5, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue6!=null)
            senseManage.registerListener(this, senSorValue6, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue7!=null)
            senseManage.registerListener(this, senSorValue7, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue8!=null)
            senseManage.registerListener(this, senSorValue8, SensorManager.SENSOR_DELAY_NORMAL);
        if(senSorValue9!=null)
            senseManage.registerListener(this, senSorValue9, SensorManager.SENSOR_DELAY_NORMAL);
//        if(senSorValue10!=null)
//            senseManage.registerListener(this, senSorValue1, SensorManager.SENSOR_DELAY_NORMAL);
//        if(senSorValue11!=null)
//            senseManage.registerListener(this, senSorValue1, SensorManager.SENSOR_DELAY_NORMAL);
//        if(senSorValue1!=null)
//            senseManage.registerListener(this, senSorValue1, SensorManager.SENSOR_DELAY_NORMAL);
//


        senseManage.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        timer.schedule(timerTask, 10000, 10000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {


                Log.i("in timer", "in timer ++++  "+ (counter++));

                sendBroadcast(value);







                try {
                    FileOutputStream fos = openFileOutput(nama, MODE_APPEND);
                   String finalValue=value+"\n";
                    fos.write(value.getBytes());
                    value= new String();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



//                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//// set your desired log level
//                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//// add your other interceptors …
//
//// add logging as last interceptor
//                httpClient.addInterceptor(logging);
//                OkHttpClient.Builder client = new OkHttpClient.Builder();
//                client.addInterceptor(new Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
//                        Request request = chain.request();
//
//                        return chain.proceed(request);
//                    }
//                });
//                Gson gson = new GsonBuilder()
//                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                        .create();
//                String BASE_URL="http://ae32b47b.ngrok.io/";
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create(gson))
//                        .build();
//                SensorDataModel user = new SensorDataModel(123, 23.4);
//                ElasticService elasticService = retrofit.create(ElasticService.class);
//
//                Call<PUT> call = (Call<PUT
//                        >) elasticService.saveData(user);
////                try {
////                    call.execute().body();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//                call.enqueue(new Callback<PUT>() {
//                    @Override
//                    public void onResponse(Call<PUT> call, Response<PUT> response) {
//                        int statusCode = response.code();
//                        Log.i(TAG, "onResponse: "+statusCode+response.body() +response.errorBody() );
//
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<PUT> call, Throwable t) {
//                     Log.i("failure","fail ");
//                    }
//
//
//
//
//
//                });





                valueFields[AMBIENT]=ambientValue;













            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float sensorValue = event.values[0];
            value = String.valueOf(sensorValue) + ",";
            Log.i("env", String.valueOf(event.values[0]));

        }

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float sensorValue = event.values[0];
            value += String.valueOf(sensorValue) + ","+ String.valueOf(event.values[1])+ ","+String.valueOf(event.values[2]) ;
            Log.i("magn", String.valueOf(sensorValue));

        }

        if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
            float sensorValue = event.values[0];
            value += String.valueOf(sensorValue) + ",";
            Log.i("prox", String.valueOf(sensorValue));

        }

        if (mySensor.getType() == Sensor.TYPE_LIGHT) {
            float sensorValue = event.values[0];
            value += String.valueOf(sensorValue) + ",";
            Log.i("light", String.valueOf(event.values[0]));

        }

        if (mySensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float sensorValue = event.values[0];
            value += String.valueOf(sensorValue) + ",";
            Log.i("humi", String.valueOf(event.values[0]));

        }

        if (mySensor.getType() == Sensor.TYPE_PRESSURE) {
            float sensorValue = event.values[0];
            value += String.valueOf(sensorValue) + ",";
            Log.i("pressure", String.valueOf(event.values[0]));

        }



        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            value+=String.valueOf(x)+","+String.valueOf(y)+","+String.valueOf(z);

//             output.setText("x=" + x + "y=" + y + "z=" + z);
            // Log.i("acc",("x=" + x + "y=" + y + "z=" + z));
//             try {
//                 String value=String.valueOf(x)+","+String.valueOf(y)+","+ String.valueOf(z);
//               //  outvalue.write(value.getBytes());
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
             Log.i("acc", Float.toString(x) + Float.toString(y)+Float.toString(z));

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String accuracyMsg = "";
        switch(accuracy){
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyMsg="Sensor has high accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyMsg="Sensor has medium accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyMsg="Sensor has low accuracy";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                accuracyMsg="Sensor has unreliable accuracy";
                break;
            default:
                break;
        }


    }
}
