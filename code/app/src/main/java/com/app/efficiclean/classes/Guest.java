package com.app.efficiclean.classes;

import java.io.Serializable;

public class Guest implements Serializable {

    private String forename;
    private String oneSignalKey;
    private String roomNumber;
    private String surname;

    public Guest() {
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getOneSignalKey() {
        return oneSignalKey;
    }

    public void setOneSignalKey(String oneSignalKey) {
        this.oneSignalKey = oneSignalKey;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
