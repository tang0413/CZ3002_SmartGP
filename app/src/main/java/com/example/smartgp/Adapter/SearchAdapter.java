package com.example.smartgp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgp.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> clinicIdList;
    ArrayList<String> clinicNameList;
    ArrayList<String> addressList;
    ArrayList<String> phoneNumberList;
    ArrayList<String> openingHourList;

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView clinic_id, clinic_name, address, phoneNo, openHour, b_clinic_id, b_clinic_name, b_address, b_phoneNo, b_openHour;
        LinearLayout linearLayout;
        int pos = 0;

        public SearchViewHolder(View itemView, View itemView2) {
            super(itemView);
            clinic_id = (TextView) itemView.findViewById(R.id.clinic_ID);
            clinic_name = (TextView) itemView.findViewById(R.id.clinic_Name);
            address = (TextView) itemView.findViewById(R.id.clinic_Address);
            phoneNo = (TextView) itemView.findViewById(R.id.clinic_HP);
            openHour = (TextView) itemView.findViewById(R.id.open_Hour);

            b_clinic_id = (TextView) itemView2.findViewById(R.id.btm_clinic_ID);
            b_clinic_name = (TextView) itemView2.findViewById(R.id.btm_clinic_Name);
            b_address = (TextView) itemView2.findViewById(R.id.btm_clinic_Address);
            b_phoneNo = (TextView) itemView2.findViewById(R.id.btm_clinic_HP);
            b_openHour = (TextView) itemView2.findViewById(R.id.btm_open_Hour);

            itemView.setOnClickListener(this);
            System.out.println("print on slide up panel3");
            //select clinic
            /*linearLayout = itemView.findViewById(R.id.search_linear_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        b_clinic_id.setText(clinicIdList.get(pos));
                        b_clinic_name.setText(clinicNameList.get(pos));
                        b_address.setText(addressList.get(pos));
                        b_phoneNo.setText(phoneNumberList.get(pos));
                        b_openHour.setText(openingHourList.get(pos));
                    }
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            System.out.println("print on slide up panel");
            pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                System.out.println("current post" + pos);
                b_clinic_id.setText(clinicIdList.get(pos));
                b_clinic_name.setText(clinicNameList.get(pos));
                b_address.setText(addressList.get(pos));
                b_phoneNo.setText(phoneNumberList.get(pos));
                b_openHour.setText(openingHourList.get(pos));
            }
        }
    }

    public SearchAdapter(Context context, ArrayList<String> clinicIdList, ArrayList<String> clinicNameList, ArrayList<String> addressList, ArrayList<String> phoneNumberList, ArrayList<String> openingHourList) {
        this.context = context;
        this.clinicIdList = clinicIdList;
        this.clinicNameList = clinicNameList;
        this.addressList = addressList;
        this.phoneNumberList = phoneNumberList;
        this.openingHourList = openingHourList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        View view2 = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, parent, false);
        return new SearchAdapter.SearchViewHolder(view, view2);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        holder.clinic_id.setText(clinicIdList.get(position));
        holder.clinic_name.setText(clinicNameList.get(position));
        holder.address.setText(addressList.get(position));
        holder.phoneNo.setText(phoneNumberList.get(position));
        holder.openHour.setText(openingHourList.get(position));
        System.out.println("print on slide up panel2");
        /*holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                holder.b_clinic_id.setText(clinicIdList.get(pos));
                holder.b_clinic_name.setText(clinicNameList.get(pos));
                holder.b_address.setText(addressList.get(pos));
                holder.b_phoneNo.setText(phoneNumberList.get(pos));
                holder.b_openHour.setText(openingHourList.get(pos));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return clinicNameList.size();
    }
}
