package com.blues.gallery.Adaptors;

import android.os.Parcel;
import android.os.Parcelable;

import com.blues.gallery.MyNDK;


public class ImageModel implements Parcelable {

    private String name, url;
    private boolean checkJpeg;

    public ImageModel() {

    }

    protected ImageModel(Parcel in) {
        name = in.readString();
        url = in.readString();
        checkJpeg = Boolean.parseBoolean(in.readString());
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        checkJpegPlus(this);
    }

    public boolean isCheckJpeg() {
        return checkJpeg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(String.valueOf(checkJpeg));
    }

    public void checkJpegPlus(ImageModel imageModel) {
//        return imageModel.getName().equals(AppConstant.overlayCheckText);
        MyNDK myNDK = new MyNDK();
        int nSpotCount = myNDK.GetSpotCount(imageModel.getUrl());

        checkJpeg = nSpotCount >= 0;
    }
}
