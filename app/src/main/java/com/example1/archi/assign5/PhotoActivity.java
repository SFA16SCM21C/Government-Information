package com.example1.archi.assign5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class PhotoActivity extends AppCompatActivity {
    TextView aboutTxtView;
    ImageView officialImgView;
    ConstraintLayout phtActContraint;
    Goverment mainGoverment;
    TextView phtActLocTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        aboutTxtView = (TextView) findViewById(R.id.phtActAboutTv);
        officialImgView = (ImageView) findViewById(R.id.phtActImgVw);
        phtActContraint = (ConstraintLayout) findViewById(R.id.phtActContraint);
        phtActLocTv = (TextView) findViewById(R.id.phtActLocTv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent ofcActIntent = getIntent();

        Goverment goverment = (Goverment) ofcActIntent.getSerializableExtra(getString(R.string.SerializeGovementObject));
        Official official = goverment.getOfficial();
        mainGoverment = goverment;
        Address locaAdd = goverment.getLocalAddress();
        aboutTxtView .setText(getAboutOfficialString(goverment));
        phtActLocTv.setText(locaAdd.getCity()+","+locaAdd.getState()+","+locaAdd.getZip());
        loadImage(goverment);
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

    public void startOfficialActivity(Goverment goverment){
        Intent officalActIntent = new Intent(this, OfficialActivity.class);
        officalActIntent.putExtra(getString(R.string.SerializeGovementObject),goverment);
        startActivity(officalActIntent);
    }


    String getAboutOfficialString(Goverment goverment){
        String aboutString = "";
        aboutString = goverment.getOfficeName()
                + "\n"+goverment.getOfficial().getOfficialName();
        if(goverment.getOfficial().getPartyName()!=null && goverment.getOfficial().getPartyName().length() > 0){
            aboutString =aboutString + "\n("+goverment.getOfficial().getPartyName()+")";

            if(goverment.getOfficial().getPartyName().equals(OfficialActivity.PartyConstant.DEMOCRATIC)){
                int myColor = getResources().getColor(R.color.colorBlue);
                phtActContraint.setBackgroundColor(myColor);
            }else if (goverment.getOfficial().getPartyName().equals(OfficialActivity.PartyConstant.REPUBLICAN)){
                int myColor = getResources().getColor(R.color.colorRed);
                phtActContraint.setBackgroundColor(myColor);
            }
        }
        return aboutString;
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
                    .placeholder(R.drawable.hourglass)
                    .into(officialImgView);

        }
    }

}
