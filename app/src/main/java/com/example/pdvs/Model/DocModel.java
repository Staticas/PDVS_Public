package com.example.pdvs.Model;

import android.graphics.Bitmap;

public class DocModel {
    private int id, status;
    private String docInfo;

    private byte[] imageView;

    public byte[] getImageView() {
        return imageView;
    }

    public void setImageView(byte[] imageView) {
        this.imageView = imageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDocInfo() {
        return docInfo;
    }

    public void setDocInfo(String docInfo) {
        this.docInfo = docInfo;
    }
}
