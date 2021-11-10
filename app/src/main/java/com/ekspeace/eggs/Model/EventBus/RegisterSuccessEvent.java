package com.ekspeace.eggs.Model.EventBus;

public class RegisterSuccessEvent {
    private boolean isSuccess;

    public RegisterSuccessEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
