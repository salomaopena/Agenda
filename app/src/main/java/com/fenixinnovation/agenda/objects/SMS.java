package com.fenixinnovation.agenda.objects;

import java.util.Objects;

public class SMS {

    private long id;
    private String address;
    private String msg;
    private String readState; //"0" Para sms nao lida e "1" sms lida
    private long time;
    private String folderName;
    private int color;

    public SMS() {
    }

    public SMS(long id, String address, String msg, String readState, long time, String folderName, int color) {
        this.id = id;
        this.address = address;
        this.msg = msg;
        this.readState = readState;
        this.time = time;
        this.folderName = folderName;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMS sms = (SMS) o;
        return Objects.equals(address, sms.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
