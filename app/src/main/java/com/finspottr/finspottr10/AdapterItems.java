package com.finspottr.finspottr10;

/**
 * Created by amers on 3/16/2017.
 */

public class AdapterItems {
    public  int id;
    public  String registrationnum;
    public  String manufacturer;
    public  String model;
    public  String ivImage;
    //for news details
    AdapterItems(int id, String registrationnum, String manufacturer, String model, String airline, String city, String province, String country, String date, String ivImage, String name)
    {
        this. id=id;
        this. registrationnum=registrationnum;
        this. manufacturer=manufacturer;
        this. model=model;
        this. ivImage=ivImage;
    }
}
