package com.WorldNavigator.reply;

public class DefaultReply {
    public String value;
    public String status;

    public void setValue(String value) {
        this.value = value;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue(){
        return value;
    }

    public String getStatus() {
        return status;
    }
}
