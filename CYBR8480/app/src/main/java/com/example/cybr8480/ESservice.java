package com.example.cybr8480;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import static android.content.ContentValues.TAG;

public class ESservice extends Service {

    public ESservice(Context ctx) {
        super();
        Log.i("Here", "MyService: ");
    }
    public ESservice()
    {

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        startTimer();
        return START_STICKY;
    }
    private Timer timer;
    private void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 10000, 10000);
    }
    private TimerTask timerTask;
    public int escounter=0;
    private void initializeTimerTask() {

        timerTask = new TimerTask() {
                public void run() {


                    Log.i("in timer", "in es  "+ (escounter++));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .create();
                    String BASE_URL="http://3e325242.ngrok.io/";
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();


                      String nama="datasensor.txt";
                    FileInputStream fis;
                    StringBuffer fileContent = new StringBuffer("");
                    try {
                        fis = openFileInput("datasensor.txt");

                        byte[] buffer = new byte[1024];
                         int n;

                        while ((n = fis.read(buffer)) != -1)
                        {

                            Log.i("here",String.valueOf(buffer));
                            fileContent.append(new String(buffer, 0, n));
                            fileContent.append("\n");
                        }

                        Log.i("filecontent", String.valueOf(fileContent));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }



//                    FileInputStream is = null;
//                    BufferedReader reader;
//                    final File file = new File("datasensor.txt");
//
//                    if (file.exists()) {
//                        try {
//                            is = new FileInputStream(file);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        reader = new BufferedReader(new InputStreamReader(is));
//                        String line = null;
//                        try {
//                            line = reader.readLine();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        while(line != null){
//                            Log.d("StackOverflow", line);
//                            try {
//                                line = reader.readLine();
//                                Log.i("value line", line);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }






                    Datafores valq= new Datafores(fileContent);
                    List<Datafores> els= Arrays.asList(valq);
                    SensorDataModel user = new SensorDataModel(1116, els);
                    ElasticService elasticService = retrofit.create(ElasticService.class);

                    Call<POST> call = (Call<POST>) elasticService.saveData(user);
//                try {
//                    call.execute().body();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                    call.enqueue(new Callback<POST>() {
                        @Override
                        public void onResponse(Call<POST> call, Response<POST> response) {
                            int statusCode = response.code();
                            Log.i(TAG, "onResponse: "+statusCode+response.body() +response.errorBody() );



                        }

                        @Override
                        public void onFailure(Call<POST> call, Throwable t) {
                            Log.i("failure","fail ");
                        }





                    });


                }
            };
        }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
