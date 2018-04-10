package com.example1.archi.assign5;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


public class GovermentViewHolder extends RecyclerView.ViewHolder {
    public TextView officeNameTxtVw;
    public TextView officialNameTxtVw;

    public GovermentViewHolder(View itemView) {
        super(itemView);
        officeNameTxtVw = (TextView) itemView.findViewById(R.id.officeNameTxtvw);
        officialNameTxtVw = (TextView) itemView.findViewById(R.id.officailNameTxtVw);
    }
}
