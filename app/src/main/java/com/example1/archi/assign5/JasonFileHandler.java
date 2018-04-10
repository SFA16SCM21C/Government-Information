package com.example1.archi.assign5;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JasonFileHandler {
    private final static String TAG="JasonFileHandler";
    private Map<Integer,Official> officialMap;
    private List<Goverment> govermentList;
    private Context context;
    interface ChannelConst{
        final static String GOOGLEPLUS="GooglePlus";
        final static String FACEBOOK="Facebook";
        final static String TWITTER="Twitter";
        final static String YOUTUBE="YouTube";
    }

    public JasonFileHandler(Context context) {
        this.context = context;
    }


    public List<Goverment> readData(InputStream in) throws IOException {
        JsonReader jsonReader1 = new JsonReader(new InputStreamReader(in, "UTF-8"));
        JsonReader jsonReader2 = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            readOfficialObject(jsonReader2);
        } finally {
            jsonReader2.close();
        }
        in.reset();
        try {
            govermentList = readOfficeObject(jsonReader1);
        } finally {
            jsonReader1.close();
        }
        return govermentList;
    }
    /*************************************Reading of offices Start******************************************/
    private List<Goverment> readOfficeObject(JsonReader reader) throws IOException {
        govermentList = new ArrayList<>();
        String offices=null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("offices")) {
                readOfficesArray(reader);
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return govermentList;
    }

    private void readOfficesArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
                readOfficeObjectList(reader);
        }
        reader.endArray();

    }

    private void readOfficeObjectList(JsonReader reader) throws IOException {
        String officeName=null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                officeName = reader.nextString();
            }else if (name.equals("officialIndices")) {
                readOfficeIndexArray(reader,officeName);
            }else{
                reader.skipValue();
            }
        }

        Log.d(TAG,"Office => "+officeName);
        reader.endObject();
    }

    private void readOfficeIndexArray(JsonReader reader, String officeName) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            int index = reader.nextInt();
            Official official = officialMap.get(index);
            govermentList.add(new Goverment(officeName,index,official));
            Log.d(TAG," Index "+index);
        }
        reader.endArray();
    }
    /*************************************Reading of offices End******************************************/

    /*************************************Reading of officials start******************************************/
    private void readOfficialObject(JsonReader reader) throws IOException {
        reader.beginObject();
        String officials=null;
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("officials")) {
                readOfficialsArray(reader);
            }else{
                reader.skipValue();
            }
        }
        Log.d(TAG,"Official => "+officials);
        reader.endObject();
    }

    private void readOfficialsArray(JsonReader reader) throws IOException {
        int count = 0;
        officialMap = new HashMap<>();
        reader.beginArray();
        while (reader.hasNext()) {
            Official official = readOfficialsObjectList(reader);
            officialMap.put(count++,official);
        }
        reader.endArray();

    }

    private Official readOfficialsObjectList(JsonReader reader) throws IOException {
         String officialName=null;
         Address address=null;
         String partyName=null;
         List<String> phNumList=null;
         List<String> emailList= null;
         List<String> urlList=null;
         String photoUrl=null;
         Channel channel=null;
         Official officail;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                officialName = reader.nextString();
            }else if (name.equals("address")) {
              address =  readOfficialAddressArray(reader);
            }else if (name.equals("party")) {
                partyName=reader.nextString();
            }else if (name.equals("phones")) {
               phNumList =  readPhoneNoList(reader);
            }else if (name.equals("emails")) {
              emailList=  readEmailList(reader);
            }else if (name.equals("urls")) {
              urlList =  readURLList(reader);
            }else if (name.equals("photoUrl")) {
               photoUrl = reader.nextString();
            }else if (name.equals("channels")) {
                channel = new Channel();
                readChannelArray(reader,channel);
            }else{
                reader.skipValue();
            }
        }
        Log.d(TAG,"Office => "+officialName);
        reader.endObject();
        officail = new Official(officialName,address,partyName,phNumList,emailList,urlList,photoUrl,channel);
        return officail;
    }


    private Address readOfficialAddressArray(JsonReader reader) throws IOException {
        Address address=null;
        reader.beginArray();
        while (reader.hasNext()) {
           address= readAddressObjData(reader);
        }
        reader.endArray();
        return address;
    }

    private Address readAddressObjData(JsonReader reader) throws IOException {
        String line1=null;
        String line2=null;
        String line3=null;
        String city=null;
        String state=null;
        String zip=null;
        Address address;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("line1")) {
                line1 = reader.nextString();
            }else if (name.equals("line2")) {
                line2 = reader.nextString();
            }else if (name.equals("line3")) {
                line3 = reader.nextString();
            }else if (name.equals("city")) {
                city = reader.nextString();
            }else if (name.equals("state")) {
                state = reader.nextString();
            }else if (name.equals("zip")) {
                zip = reader.nextString();
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        address=new Address(line1,line2,line3,city,state,zip);
        Log.d(TAG , " "+address);
        return address;
    }

    private List<String> readPhoneNoList(JsonReader reader) throws IOException {
        List<String> phNumList= new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            String phoneNum = reader.nextString();
            phNumList.add(phoneNum);
            Log.d(TAG,"Phoen No "+phoneNum);
        }
        reader.endArray();
        return phNumList;
    }
    private List<String> readEmailList(JsonReader reader) throws IOException {
        List<String> emailList= new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            String email = reader.nextString();
            emailList.add(email);
            Log.d(TAG,"Phoen No "+email);
        }
        reader.endArray();
        return emailList;
    }
    private List<String> readURLList(JsonReader reader) throws IOException {
        List<String> urlList= new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            String url = reader.nextString();
            urlList.add(url);
            Log.d(TAG,"url "+url);
        }
        reader.endArray();
        return urlList;
    }

    private void readChannelArray(JsonReader reader, Channel channel)throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            readChannelObjectData(reader,channel);
        }
        reader.endArray();
    }

    private void readChannelObjectData(JsonReader reader, Channel channel)throws IOException {
        String channelName="";
        String channelID="";
        reader.beginObject();
        while (reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("type")) {
                channelName = reader.nextString();
            }else if (name.equals("id")) {
                channelID = reader.nextString();
            }else{
                reader.skipValue();
            }
        }
        if(channelName.equalsIgnoreCase(ChannelConst.GOOGLEPLUS)){
            channel.setGoogleID(channelID);
        }else if(channelName.equalsIgnoreCase(ChannelConst.FACEBOOK)){
            channel.setFacebookID(channelID);
        }else if(channelName.equalsIgnoreCase(ChannelConst.TWITTER)){
            channel.setTwitterID(channelID);
        }else if(channelName.equalsIgnoreCase(ChannelConst.YOUTUBE)){
            channel.setYoutubeID(channelID);
        }
        reader.endObject();
    }

    /*************************************Reading of officials END******************************************/

    /*************************************Reading of NormalizedInput START******************************************/
    public Address readAddress(InputStream in) throws IOException {
        Address address=null;
        JsonReader jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try {
          address =   readNormalizedInputObject(jsonReader);
        } finally {
            jsonReader.close();
        }
        return address;
    }
    private Address readNormalizedInputObject(JsonReader reader)throws IOException {
        Address address=null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("normalizedInput")) {
              address =  readInputAddressObject(reader);
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return address;
    }

    private Address readInputAddressObject(JsonReader reader) throws IOException {
        String line1=null;
        String city=null;
        String state=null;
        String zip=null;
        Address address;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("line1")) {
                line1 = reader.nextString();
            }else if (name.equals("city")) {
                city = reader.nextString();
            }else if (name.equals("state")) {
                state = reader.nextString();
            }else if (name.equals("zip")) {
                zip = reader.nextString();
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        address=new Address(line1,null,null,city,state,zip);
        Log.d(TAG , " "+address);
        return address;
    }

    /*************************************Reading of NormalizedInput START******************************************/
}
