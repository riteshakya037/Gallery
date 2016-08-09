package com.blues.gallery;

public class MyNDK {
    static {
        System.loadLibrary("MyLibrary");
    }

    public native String getMyString();
    public native int GetSpotCount(String path);
}
