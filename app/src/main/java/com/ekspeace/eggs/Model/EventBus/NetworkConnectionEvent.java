package com.ekspeace.eggs.Model.EventBus;

public class NetworkConnectionEvent {
    private boolean isNetworkConnected;

    public NetworkConnectionEvent(boolean isNetworkConnected) {
        this.isNetworkConnected = isNetworkConnected;
    }

    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    public void setNetworkConnection(boolean isNetworkConnected) {
        this.isNetworkConnected = isNetworkConnected;
    }
}
