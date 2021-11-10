package com.ekspeace.eggs.Model.EventBus;

public class AddQuantity {
    private boolean isAdded;

    public AddQuantity(boolean isAdded) {
        this.isAdded = isAdded;
    }
    public boolean isAdded() {
        return isAdded;
    }

    public void setAddQuantity(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
