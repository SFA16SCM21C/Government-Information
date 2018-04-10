package com.example1.archi.assign5;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class GovermentAdaptor extends RecyclerView.Adapter<GovermentViewHolder> {
    MainActivity mainActivity;
    Context context;
    List<Goverment> govermentList;
    private static String DOWN_POINTING_TRAINGLE="\u25BC";
    private static String UP_POINTING_TRAINGLE="▲";

    public GovermentAdaptor(MainActivity mainActivity, List<Goverment> govermentList) {
        this.mainActivity =  mainActivity;
        this.govermentList = govermentList;
    }
    @Override
    public GovermentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisItemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gov_list,
                parent, false);
        thisItemsView.setOnClickListener(mainActivity);

        return new GovermentViewHolder(thisItemsView);
    }

    @Override
    public void onBindViewHolder(GovermentViewHolder holder, int position) {
        String stock_indicator_traingle=null;
        Goverment goverment = govermentList.get(position);
        holder.officeNameTxtVw.setText(goverment.getOfficeName());
        holder.officialNameTxtVw.setText(goverment.getOfficial().getOfficialName());

    }

    @Override
    public int getItemCount() {
        return govermentList.size();
    }
    public String getFormattedDecimal(double d){
        return String.format("%.2f", d);
    }
}
