package com.example.cybr8480;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SensorDataModel {


   @SerializedName("dataID")
    private int dataID;

    public int getDataID() {
        return dataID;
    }

    public void setDataID(int dataID) {
        this.dataID = dataID;
    }

//    public double getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(double temperature) {
//        this.temperature = temperature;
//    }

   // @SerializedName("data")
  //  private double temperature;
    @SerializedName("data")
    List<Datafores> data;

   public SensorDataModel(Integer dataID, List<Datafores> data)
   {
this.dataID=dataID;
this.data=  data;
   }

}
