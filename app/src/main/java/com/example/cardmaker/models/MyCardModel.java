package com.example.cardmaker.models;

import android.net.Uri;

public class MyCardModel {
    private String image;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MyCardModel() {

    }

    public MyCardModel(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
