package com.example1.archi.assign5;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DownloadCallback<String>,View.OnClickListener {

    final String APT_KEY = "AIzaSyABY0sTcgWao5QhrklYqHY6Pbrl8s-zmzw";
    final String SEARCH_TEXT = "&address=";
    String search_url ="https://www.googleapis.com/civicinfo/v2/representatives?key=";
    DownloadTask downloadTask;
    private static final String TAG="MainActivity";
    List<Goverment> govermentList;

    RecyclerView govermentRecycleView;
    GovermentViewHolder govermentViewHolder;
    GovermentAdaptor govermentAdaptor;
    CardView govermentCardView;
    String curState;
    Address currentLocObject = null;
    static final int ACTION_OFFICIAL_ACT_SHOW = 0;
    static final int NO_ITEM_CLICKED = -1;

    private static final int ACTIVITY_INTENT_OFFICIAL_ACT_SHOW = 0;
    private static final String NO_RESP_REC="No response received.";

    private Locator locator;
    String address;
    TextView locAddressTextView;
    Address localaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadTask = new DownloadTask(this);
        govermentCardView = (CardView)findViewById(R.id.cardViewGovOfcList);
        locAddressTextView = (TextView) findViewById(R.id.textView2);
      //  String api_search_url = search_url + APT_KEY +SEARCH_TEXT;
        setCurState(State.ON_CREATE);


        if(isOnline()) {
            locator = new Locator(this);
           /* locator.setUpLocationManager();
            locator.determineLocation();*/
            String api_search_url = search_url + APT_KEY +SEARCH_TEXT+address;
            Log.d(TAG,"Addres "+address);
            //locAddressTextView.setText(address);
            downloadTask.execute(api_search_url);
        }else{
            showCustomAlert("No Network Connection", "Data can not be fetched\n without network connection");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d("MainActivity","Menu inflater called ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.addNewStock :
                if(isOnline()) {
                    searchAddressDialog();
                }
                else {
                   showCustomAlert("No Network Connection", "Stocks Can not be added \n without network connection");
                }
            case R.id.about :
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void updateFromDownload(String result) {
     //Log.d(TAG," Result => "+result);
        if(result==null || result.length()<50 || result.equals(NO_RESP_REC)){
            showCustomAlert("No Result","Can not fetch data");
            return;
        }else {
            govermentList = readStockJsonData(result);
             localaddress = readLocationJsonData(result);
             Log.d(TAG," Local Address "+localaddress);
            locAddressTextView.setText(localaddress.getState()+","+localaddress.getCity()+","+localaddress.getZip());

                showGovListData(govermentList);
                showGovermentCrad();

        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        final String DEBUG_TAG = "NetworkStatusExample";
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isWifiConn = networkInfo.isConnected();
        boolean isMobileConn = networkInfo.isConnected();
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        return networkInfo;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {

    }

    private List<Goverment> readStockJsonData(String jsonData){
        List<Goverment> govList=null;
        try {
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes("UTF-8"));
            JasonFileHandler jasonFileHandler= new JasonFileHandler(this);
            govList =  jasonFileHandler.readData(inputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
        return govList;
    }

    private Address readLocationJsonData(String jsonData){
        Address address=null;
        try {
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes("UTF-8"));
            JasonFileHandler jasonFileHandler= new JasonFileHandler(this);
            address =  jasonFileHandler.readAddress(inputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
        return address;
    }

    public void searchAddressDialog(){
        final DownloadTask downloadTask;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter ZipCode, City or State");
        //builder.setIcon();
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        builder.setView(input);
        locator = new Locator(this);
        downloadTask = new DownloadTask(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String  m_Text = input.getText().toString();
                Log.d("MainActivity","test := "+m_Text);
                setCurState(State.NEW);
                if(isOnline()) {
                    locator.setUpLocationManager();
                    locator.determineLocation();
                    String api_search_url = search_url + APT_KEY +SEARCH_TEXT+m_Text;
                    downloadTask.execute(api_search_url);
                }else{
                    showCustomAlert("No Network Connection", "Data can not be fetched\n without network connection");
                }
                // mNetworkFragment = (NetworkFragment) NetworkFragment.getInstance(getSupportFragmentManager(), new_stock_search_url);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void showCustomAlert(String alertTitle, String alertMessage){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.activity_dialog_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView tittle = (TextView) promptView.findViewById(R.id.alertTitile);
        final TextView message = (TextView) promptView.findViewById(R.id.alertMsg);

        tittle.setText(alertTitle);
        message.setText(alertMessage);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    void showGovListData(List<Goverment> govermentList){
        Iterator<Goverment> govermentIterator = govermentList.iterator();
        while (govermentIterator.hasNext()){
            Log.d(TAG," Goverment Data "+govermentIterator.next());
        }
    }

    @Override
    public void onClick(View view) {
        int pos = govermentRecycleView.getChildLayoutPosition(view);
        Goverment goverment = govermentList.get(pos);
        Log.d(TAG,"Goverment officie selected => "+goverment.getOfficeName());
        startOfficialActivity(ACTION_OFFICIAL_ACT_SHOW,pos);
    }

    public String getCurState() {
        return curState;
    }

    public void setCurState(String curState) {
        this.curState = curState;
    }

    public void showGovermentCrad(){
        govermentRecycleView = (RecyclerView) findViewById(R.id.govRcyVw);
        govermentAdaptor = new GovermentAdaptor(this,govermentList);
        govermentRecycleView.setAdapter(govermentAdaptor);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        govermentRecycleView.setLayoutManager(layoutManager);
        govermentAdaptor.notifyDataSetChanged();
    }

    public void startAboutActivity(){
        Intent abtActyIntent = new Intent(this,AboutActivity.class);
        startActivity(abtActyIntent);
    }

    public void startOfficialActivity(int actionCode, int itemClickedPos){
        if(actionCode == ACTION_OFFICIAL_ACT_SHOW) {
            Goverment goverment;
            if(itemClickedPos!=NO_ITEM_CLICKED){
                goverment = govermentList.get(itemClickedPos);
                goverment.setLocalAddress(localaddress);
            }else{
                Log.d(TAG,"Something wrong check code ");
                return;
            }
            Intent officalActIntent = new Intent(this, OfficialActivity.class);
            officalActIntent.putExtra(getString(R.string.SerializeGovementObject),goverment);
            //startActivity(officalActIntent);
           startActivityForResult(officalActIntent,ACTION_OFFICIAL_ACT_SHOW);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTION_OFFICIAL_ACT_SHOW) {

            if (resultCode == RESULT_OK) {

            }
        }

    }
    interface State{
        public final static String ON_CREATE="ONCREATE";
        public final static String UPDATE = "UPDATE";
        public final static String NEW = "NEW";
    }
    /*************************************GeoLocation Chnages Start hear**********************************/
    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<android.location.Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                for (android.location.Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);
                    sb.append(ad.getLocality()+", "+ad.getAdminArea());
                    sb.append("$"+ad.getPostalCode());

                }
                Log.d(TAG, "Address iS ***********" + sb);
                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());
            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }
    public void setData(double lat, double lon) {
        address = doAddress(lat, lon);
        //currentaddress.setText(address.replace("$"," "));
        //Toast.makeText(this, address.substring(address.indexOf("$")), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }
/*    public void civic_Data_to_add( ArrayList<Official> civic_data) {

        officialList.clear();
        //Log.d("asdasdasdsadasdasd",Integer.toString(civic_data.size()));
        for(int i=0;i<civic_data.size();i++)
        {
            officialList.add(civic_data.get(i));
            address=civic_data.get(i).get_Locality();
            currentaddress.setText(address);
        }
        mAdapter.notifyDataSetChanged();
    }*/
    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }
    /*************************************GeoLocation Chnages End hear**********************************/
}
