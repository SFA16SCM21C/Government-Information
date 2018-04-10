package com.example1.archi.assign5;

import java.io.Serializable;
import java.util.List;


public class Official implements Serializable{
    private String officialName;
    private Address address;
    private String email;
    private String partyName;
    private List<String> phNumList;
    private List<String> emailList;
    private List<String> urlList;
    private String photoUrl;
    private Channel channel;

    public Official() {
    }

    public Official(String officialName, Address address,  String partyName, List<String> phNumList, List<String> emailList, List<String> urlList, String photoUrl, Channel channel) {
        this.officialName = officialName;
        this.address = address;
        this.partyName = partyName;
        this.phNumList = phNumList;
        this.emailList = emailList;
        this.urlList = urlList;
        this.photoUrl = photoUrl;
        this.channel = channel;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public List<String> getPhNumList() {
        return phNumList;
    }

    public void setPhNumList(List<String> phNumList) {
        this.phNumList = phNumList;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Official Name "+officialName
                +"\n Adddress "+address
                +"\n Party Name "+partyName
                +"\n Phone Num "+phNumList
                +"\n Email List "+emailList
                +"\n URL List "+urlList
                +"\n photo URL "+photoUrl
                +"\n Channel "+channel;
    }
}
