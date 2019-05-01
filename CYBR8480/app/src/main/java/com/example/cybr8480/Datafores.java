package com.example.cybr8480;

class Datafores {

    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMagn() {
        return magn;
    }

    public void setMagn(String magn) {
        this.magn = magn;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    String magn;
    String acc;

    public  Datafores(StringBuffer data1)
    {
        this.data =data1.toString();
//        this.magn=magn;
//        this.acc=acc;
    }
}
