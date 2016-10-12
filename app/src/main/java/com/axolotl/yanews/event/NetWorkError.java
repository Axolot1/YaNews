package com.axolotl.yanews.event;

/**
 * Created by axolotl on 16/9/29.
 */

public class NetWorkError {
    String channel;

    public NetWorkError(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
