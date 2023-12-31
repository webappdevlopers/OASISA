package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.webapp.oasis.Admin.Complaintdetails;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.HashMap;
import java.util.List;

public class AdminReportListAdapter extends RecyclerView.Adapter<AdminReportListAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    String hash;
    int lastPosition = -1;
    private List<AgentDriverOrderListModel> mUser;
    RequestQueue requestQueue;
    SessionManager session;
    StringRequest stringRequest;
    String user_id;

    public AdminReportListAdapter(Context context2, List<AgentDriverOrderListModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_TecnicianID);
        this.hash = users.get(SessionManager.KEY_HASH);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_order_driver_list_details, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final AgentDriverOrderListModel user_details = this.mUser.get(i);
        viewHolder.complaint.setText(user_details.getComplaint());
        viewHolder.service.setText(user_details.getService());
        viewHolder.customerMobileNo.setText(user_details.getCustomerMobileNumber());
        viewHolder.date.setText(user_details.getDate());
        viewHolder.timing.setText(user_details.getTiming());
        viewHolder.customerName.setText(user_details.getCustomertName());
        try {
            if (!user_details.getClosingDate().isEmpty()) {
                viewHolder.closingdatetime.setText("Closer Date : " + user_details.getClosingDate() + " " + user_details.getClosingTime());
            }else
                viewHolder.closingdatetime.setVisibility(View.GONE);
        }catch (Exception e){
            viewHolder.closingdatetime.setVisibility(View.GONE);
        }
        viewHolder.status.setText(user_details.getStatus());
        ((RequestBuilder) Glide.with(this.context).load(user_details.getImage()).placeholder((int) R.drawable.noimage)).into(viewHolder.parcel_img);

            if (!user_details.getStatus().equals("Done")) {
                ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
                params.height = 0;
                viewHolder.itemView.setLayoutParams(params);
            }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                    Intent intent = new Intent(AdminReportListAdapter.this.context, Complaintdetails.class);
                    intent.putExtra("mylist", user_details);
                    intent.putExtra("ComplaintId", user_details.getComplaintId());
                    intent.putExtra("CustomerId", user_details.getCustomerID());
//                intent.putExtra("getID", user_details.getId());
                    Log.d("getID", user_details.getCustomerID());

                    Log.d("getComplaintId", user_details.getComplaintId());
//                Log.d("getCustomerID",user_details.getCustomerID());
                    AdminReportListAdapter.this.context.startActivity(intent);
                }
        });

        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + user_details.getMobile_no()));
                AdminReportListAdapter.this.context.startActivity(intent);
            }
        });
        viewHolder.parcel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!user_details.getImage().isEmpty()) {
                        Log.d("user_details.getImage()", user_details.getImage());
                        Intent intent = new Intent(context, See_Full_Image_F.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("imageurl", user_details.getImage());
                        context.startActivity(intent);
                    }
                }catch (Exception e){

                }
            }
        });
    }

//    private boolean appInstalledOrNot(String uri) {
//        try {
//            this.context.getPackageManager().getPackageInfo(uri, 1);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_qrcode;
        RelativeLayout call;
        TextView complaint;
        TextView customerMobileNo;
        TextView customerName,closingdatetime;
        TextView date;
        TextView driver_name;
        TextView mobile_no;
        ImageView parcel_img;
        TextView service;
        TextView status;
        TextView timing;
        RelativeLayout whatsapp;

        public ViewHolder(View itemView) {
            super(itemView);
            this.customerName = (TextView) itemView.findViewById(R.id.etcutomerName_name);
            this.closingdatetime = (TextView) itemView.findViewById(R.id.closingdatetime);
            this.complaint = (TextView) itemView.findViewById(R.id.complaint);
            this.timing = (TextView) itemView.findViewById(R.id.ttv_timing);
            this.customerMobileNo = (TextView) itemView.findViewById(R.id.etcustomermobileNo);
            this.service = (TextView) itemView.findViewById(R.id.service);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.parcel_img = (ImageView) itemView.findViewById(R.id.parcel_img);
            this.whatsapp = (RelativeLayout) itemView.findViewById(R.id.whatsapp);
            this.call = (RelativeLayout) itemView.findViewById(R.id.call);
            this.status = (TextView) itemView.findViewById(R.id.order_status);
        }
    }
}
