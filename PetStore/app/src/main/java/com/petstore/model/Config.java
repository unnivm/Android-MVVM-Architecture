package com.petstore.model;

public class Config {
    private boolean isChatEnabled;
    private boolean isCallEnabled;
    private String workingHours;

    public boolean isChatEnabled() {
        return isChatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        isChatEnabled = chatEnabled;
    }

    public boolean isCallEnabled() {
        return isCallEnabled;
    }

    public void setCallEnabled(boolean callEnabled) {
        isCallEnabled = callEnabled;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
}
