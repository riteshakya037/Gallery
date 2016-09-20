package com.blues.gallery.Adaptors;

import android.os.Parcel;
import android.os.Parcelable;

import com.blues.gallery.MyNDK;


public class ImageModel implements Parcelable {

    private String name, url;
    private boolean checkJpeg;
    private String jpegPlusEventTag;
    private String jpegPlusLocationTag;
    static MyNDK myNDK;

    public ImageModel() {

    }

    protected ImageModel(Parcel in) {
        name = in.readString();
        url = in.readString();
        checkJpeg = in.readByte() != 0;
        jpegPlusEventTag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeByte((byte) (checkJpeg ? 1 : 0));
        dest.writeString(jpegPlusEventTag);
    }

    @Override
    public int describeContents() {
        return 0;
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
    }

    public void setCheckJpeg(boolean checkJpeg) {
        this.checkJpeg = checkJpeg;
    }

    public boolean isCheckJpeg() {
        return checkJpeg;
    }


    public String getJpegPlusEventTag() {
        return jpegPlusEventTag;
    }

    public void setJpegPlusEventTag(String jpegPlusEventTag) {
        this.jpegPlusEventTag = jpegPlusEventTag;
    }

    public String getJpegPlusLocationTag() {
        return jpegPlusLocationTag;
    }

    public void setJpegPlusLocationTag(String jpegPlusLocationTag) {
        this.jpegPlusLocationTag = jpegPlusLocationTag;
    }

    private void checkJpegPlus(ImageModel imageModel) {
//        return imageModel.getName().equals(AppConstant.overlayCheckText);
        if (myNDK == null)
            myNDK = new MyNDK();
        int nSpotCount = myNDK.GetSpotCount(imageModel.getUrl());

        checkJpeg = nSpotCount >= 0;
        if (checkJpeg) {
            setJpegPlusEventTag(myNDK.GetMoment(imageModel.getUrl(), "event"));
            setJpegPlusLocationTag(myNDK.GetMoment(imageModel.getUrl(), "location"));
        }
    }

    public void checkJpeg() {
        checkJpegPlus(this);
    }
}
