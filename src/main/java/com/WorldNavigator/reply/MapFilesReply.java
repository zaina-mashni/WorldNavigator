package com.WorldNavigator.reply;

import java.util.ArrayList;
import java.util.List;

public class MapFilesReply {
    public List<String> mapFiles;
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MapFilesReply(){
        mapFiles=new ArrayList<>();
    }
}
