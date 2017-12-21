package com.weather_condition;

import java.io.Serializable;

/**
 * Created by acer on 21.12.2017.
 */

public class Sehirler implements Serializable {
    private int SehirID;

    public int getSehirID() {
        return SehirID;
    }

    public void setSehirID(int sehirID) {
        SehirID = sehirID;
    }

    public String getSehirAdi() {
        return SehirAdi;
    }

    public void setSehirAdi(String sehirAdi) {
        SehirAdi = sehirAdi;
    }

    private String SehirAdi;
}
