package com.axolotl.yanews.retrofit.entity;

import org.parceler.Parcel;

/**
 * Created by axolotl on 16/9/30.
 */
@Parcel
public class Download {
    int progress;
    int currentFileSize;
    int totalFileSize;

    public Download() {
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }
}

