package com.axolotl.yanews.event;

/**
 * Created by axolotl on 16/9/30.
 */

public class DownloadEvent {
    int progress;
    int currentFileSize;
    int totalFileSize;

    public DownloadEvent() {
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
