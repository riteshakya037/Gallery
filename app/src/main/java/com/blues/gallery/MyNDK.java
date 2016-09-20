package com.blues.gallery;

public class MyNDK {
    static {
        System.loadLibrary("MyLibrary");
    }

    public native String getMyString();
    public native int GetSpotCount(String path);
    public native int InsertMoment(String path, String UDID, String moment, String description, String language);
    public native String GetMoment(String path, String momenttype);
}
