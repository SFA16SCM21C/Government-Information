package com.example1.archi.assign5;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class OfficialActivity extends AppCompatActivity implements View.OnClickListener {
    TextView officialActLocTv;
    TextView aboutTxtView;
    TextView addressTxtView;
    TextView phNumTxtView;
    TextView emailTxtView;
    TextView webSiteTxtView;
    ImageView officialImgView;
    ImageView googleImgVw;
    ImageView twitterImgVw;
    ImageView facebookImgVw;
    ImageView youtubeImgVw;
    ConstraintLayout constraintLayout;
    Goverment govermentMain;
    final static String NO_DATA_PROVIDED="No Data Provided";
    public static final String TAG="OfficialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        officialActLocTv = (TextView) findViewById(R.id.officialActLocTv);

        aboutTxtView = (TextView) findViewById(R.id.ofcActAboutTv);
        addressTxtView = (TextView) findViewById(R.id.ofcActAddressTv);
        phNumTxtView = (TextView) findViewById(R.id.ofcActPhNumTv);
        emailTxtView = (TextView) findViewById(R.id.ofcActEmailTv);
        webSiteTxtView = (TextView) findViewById(R.id.ofcActWebsiteTv);

        officialImgView = (ImageView) findViewById(R.id.ofcActImgVw);
        googleImgVw = (ImageView)findViewById(R.id.googleImgVw);
        twitterImgVw = (ImageView) findViewById(R.id.twitterImgVw);
        facebookImgVw = (ImageView) findViewById(R.id.facebookImgVw);
        youtubeImgVw = (ImageView) findViewById(R.id.youImgVw);
        constraintLayout = (ConstraintLayout) findViewById(R.id.officalActContraint);
        officialImgView.setOnClickListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent ofcActIntent = getIntent();
        final Goverment goverment = (Goverment) ofcActIntent.getSerializableExtra(getString(R.string.SerializeGovementObject));
        govermentMain = goverment;
        Official official = goverment.getOfficial();
        Channel officialChannel = official.getChannel();
        Address localAdd  = goverment.getLocalAddress();

        String phoneNum= "";
        String email="";
        String webstite="";
        if(official.getPhNumList() !=null &&official.getPhNumList().size()>0){
            phoneNum = official.getPhNumList().get(0);
        }else{
            phoneNum = NO_DATA_PROVIDED;
        }

        if(official.getEmailList() != null && official.getEmailList().size() > 0){
            email = official.getEmailList().get(0);
        }else{
            email = NO_DATA_PROVIDED;
        }
        if(official.getUrlList() != null && official.getUrlList().size() > 0){
            webstite = official.getUrlList().get(0);
        }else{
            webstite = NO_DATA_PROVIDED;
        }
        officialActLocTv.setText(localAdd.getState()+","+localAdd.getCity()+","+localAdd.getZip());

        aboutTxtView .setText(getAboutOfficialString(goverment));
        addressTxtView.setText(getFormatedAddress(official.getAddress()));
        phNumTxtView.setText(phoneNum);
        emailTxtView.setText(email);
        webSiteTxtView.setText(webstite);

        Linkify.addLinks(addressTxtView , Linkify.MAP_ADDRESSES);
        Linkify.addLinks(phNumTxtView,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(emailTxtView,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(webSiteTxtView,Linkify.WEB_URLS);

        addressTxtView.setLinkTextColor(Color.parseColor("#ffffff"));
        phNumTxtView.setLinkTextColor(Color.parseColor("#ffffff"));
        emailTxtView.setLinkTextColor(Color.parseColor("#ffffff"));
        webSiteTxtView.setLinkTextColor(Color.parseColor("#ffffff"));

        setChannel(officialChannel);

        /*Picasso.with(getApplicationContext())
                .load(official.getPhotoUrl())
                .placeholder(R.drawable.hourglass)
                .fit()
                .error(R.drawable.profile)
                .into(officialImgView);*/
        loadImage(goverment);

        facebookImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookClicked(v,goverment);
            }
        });

        twitterImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterClicked(v,goverment);
            }
        });

        googleImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googlePlusClicked(v,goverment);
            }
        });

        youtubeImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubeClicked(v,goverment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                Intent clickedNoteIntent = new Intent();
                setResult(RESULT_OK, clickedNoteIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent clickedNoteIntent = new Intent();
        setResult(RESULT_OK, clickedNoteIntent);
        finish();
    }
    String getFormatedAddress(Address officialAddress){
        String addressString="";
        if(officialAddress !=null){
            if(officialAddress.getLine1() != null){
                addressString = officialAddress.getLine1()+"\n";
            }
            if(officialAddress.getLine2() != null){
                addressString +=  officialAddress.getLine2()+"\n";
            }
            if(officialAddress.getLine3() != null){
                addressString += officialAddress.getLine3()+"\n";
            }
            if(officialAddress.getCity() != null){
                addressString += officialAddress.getCity()+",";
            }
            if(officialAddress.getState() !=null){
                addressString += officialAddress.getState()+" ";
            }
            if(officialAddress.getZip() !=null){
                addressString +=officialAddress.getZip();
            }
        }
        return addressString;
    }

    void setChannel(Channel channel){
        if(channel !=null) {
            if (channel.getGoogleID() == null) {
                googleImgVw.setVisibility(View.INVISIBLE);
            }
            if (channel.getFacebookID() == null) {
                facebookImgVw.setVisibility(View.INVISIBLE);
            }
            if (channel.getTwitterID() == null) {
                twitterImgVw.setVisibility(View.INVISIBLE);
            }
            if (channel.getYoutubeID() == null) {
                youtubeImgVw.setVisibility(View.INVISIBLE);
            }
        }else{
            googleImgVw.setVisibility(View.INVISIBLE);
            facebookImgVw.setVisibility(View.INVISIBLE);
            twitterImgVw.setVisibility(View.INVISIBLE);
            youtubeImgVw.setVisibility(View.INVISIBLE);
        }
    }

    String getAboutOfficialString(Goverment goverment){
        String aboutString = "";
        aboutString = goverment.getOfficeName()
                + "\n"+goverment.getOfficial().getOfficialName();
        if(goverment.getOfficial().getPartyName()!=null && goverment.getOfficial().getPartyName().length() > 0){
            aboutString =aboutString + "\n("+goverment.getOfficial().getPartyName()+")";

            if(goverment.getOfficial().getPartyName().equals(PartyConstant.DEMOCRATIC)){
                int myColor = getResources().getColor(R.color.colorBlue);
                constraintLayout.setBackgroundColor(myColor);
            }else if (goverment.getOfficial().getPartyName().equals(PartyConstant.REPUBLICAN)){
                int myColor = getResources().getColor(R.color.colorRed);
                constraintLayout.setBackgroundColor(myColor);
            }
        }
        return aboutString;
    }
    @Override
    public void onClick(View v) {
        startPhotoActivity(govermentMain);
    }

    public void startPhotoActivity(Goverment goverment){
            Intent officalActIntent = new Intent(this, PhotoActivity.class);
            officalActIntent.putExtra(getString(R.string.SerializeGovementObject),goverment);
            startActivityForResult(officalActIntent,0);
    }

    interface PartyConstant{
        public static final String DEMOCRATIC= "Democratic";
        public static final String REPUBLICAN= "Republican";
    }

    public void facebookClicked(View v,Goverment goverment) {
        String FACEBOOK_URL = "https://www.facebook.com/" +goverment.getOfficial().getChannel().getFacebookID();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }else{
                urlToUse = "fb://page/" + goverment.getOfficial().getChannel().getFacebookID();;
            }
        }catch (PackageManager.NameNotFoundException e){
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v,Goverment goverment) {
        Intent intent = null;
        String name =  goverment.getOfficial().getChannel().getTwitterID();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) { // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(View v, Goverment goverment) {
        String name = goverment.getOfficial().getChannel().getGoogleID();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v, Goverment goverment) {
        String name = goverment.getOfficial().getChannel().getYoutubeID();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    private void loadImage(final Goverment goverment){
        final String photoUrl = goverment.getOfficial().getPhotoUrl();
        if ( photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");

                    picasso.load(changedUrl)
                            .fit()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.hourglass)
                            .into(officialImgView);
                }
            }).build();

            picasso.load(photoUrl)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.hourglass)
                    .into(officialImgView);

        } else {
            Picasso.with(this).load(photoUrl)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.profile)
                    .into(officialImgView);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

            }
        }

    }
}
