package com.example1.archi.assign5;

import java.io.Serializable;


public class Goverment implements Serializable{
    private String officeName;
    private int officeIndex;
    private Official official;
    Address localAddress;

    public Goverment() {
    }

    public Goverment(String officeName, int officeIndex, Official official) {
        this.officeName = officeName;
        this.officeIndex = officeIndex;
        this.official = official;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getOfficeIndex() {
        return officeIndex;
    }

    public void setOfficeIndex(int officeIndex) {
        this.officeIndex = officeIndex;
    }

    public Official getOfficial() {
        return official;
    }

    public void setOfficial(Official official) {
        this.official = official;
    }

    public Address getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(Address localAddress) {
        this.localAddress = localAddress;
    }

    @Override
    public String toString() {
        return "\n Office Name "+officeName
                +"\n Office Index "+officeIndex
                +"\n Office ==> "+official;
    }
}
