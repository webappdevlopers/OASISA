package com.webapp.oasis.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.webapp.oasis.Admin.Adapter.AdminDriverListAdapter;
import com.webapp.oasis.Admin.Complaintdetails;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.ComplaintStatusModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ComplaintStatusListAdapter extends RecyclerView.Adapter<ComplaintStatusListAdapter.ViewHolder> {
    private Context context;
    String customer_id;
    String hash;
    int lastPosition = -1;
    private List<ComplaintStatusModel> mUser;
    SessionManager session;
    String user_id;

    public ComplaintStatusListAdapter(Context context2, List<ComplaintStatusModel> mUser2, String customer_id2) {
        this.customer_id = customer_id2;
        this.mUser = mUser2;
        this.context = context2;
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_order_list_details, parent, false));
    }

    public int getItemViewType(int position) {
        return position;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ComplaintStatusModel user_details = this.mUser.get(i);
        viewHolder.service.setText(user_details.getService());
        viewHolder.date_new.setText(user_details.getDate());
        viewHolder.timing.setText(user_details.getTiming());
        viewHolder.status.setText(user_details.getStatus());
        if(viewHolder.status.getText().toString().equals("Assign")){
            viewHolder.status.setText("Assigned");
        }
        viewHolder.complaint.setText(user_details.getComplaint());
        if (user_details.getStartDate() != null) {
            viewHolder.linearLayout_start_date.setVisibility(View.VISIBLE);
            viewHolder.start_amc_Date.setText(user_details.getStartDate());
        }
        if (user_details.getEndDate() != null) {
            viewHolder.linearLayout_end_date.setVisibility(View.VISIBLE);
            viewHolder.end_amc_date.setText(user_details.getEndDate());
        }
        ((RequestBuilder) Glide.with(this.context).load(user_details.getImage()).placeholder((int) R.drawable.noimage)).into(ViewHolder.parcel_img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (user_details.getStatus().equals("Done")){
                    AgentDriverOrderListModel user_details1 = new AgentDriverOrderListModel();
                    user_details1.setCustomertName(user_details.getCustomerName());
                    user_details1.setCustomerMobileNumber(user_details.getCustomerMobileNumber());
                    user_details1.setComplaint(user_details.getComplaint());
                    user_details1.setService(user_details.getService());
                    user_details1.setTiming(user_details.getTiming());
                    user_details1.setDate(user_details.getDate());
                    user_details1.setStatus(user_details.getStatus());
                    user_details1.setTechnicianRemark(user_details.getTechnicianRemark());

                    Intent intent = new Intent(context, Complaintdetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mylist", user_details1);
                    intent.putExtra("ComplaintId", user_details.getComplaintId());
                    intent.putExtra("CustomerId", user_details.getCustomerID());
                    Log.d("getID", user_details.getCustomerID()+"");
                    Log.d("getComplaintId", user_details.getComplaintId()+"");
                    context.startActivity(intent);
                }
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static ImageView parcel_img;
        Button btn;
        Button btn_scanqr;
        Button btn_track;
        TextView complaint;
        TextView date_new;
        EditText edtamount;
        TextView end_amc_date;
        LinearLayout linearLayout_end_date;
        LinearLayout linearLayout_start_date;
        TextView service;
        TextView start_amc_Date;
        TextView status;
        TextView timing;
        TextView weight;

        public ViewHolder(View itemView) {
            super(itemView);
            this.complaint = (TextView) itemView.findViewById(R.id.complaint);
            this.service = (TextView) itemView.findViewById(R.id.service);
            this.date_new = (TextView) itemView.findViewById(R.id.date_new);
            this.timing = (TextView) itemView.findViewById(R.id.timing);
            this.start_amc_Date = (TextView) itemView.findViewById(R.id.start_amc_date);
            this.end_amc_date = (TextView) itemView.findViewById(R.id.end_amc_date);
            this.btn_track = (Button) itemView.findViewById(R.id.btn_track);
            this.edtamount = (EditText) itemView.findViewById(R.id.edtamount);
            this.linearLayout_start_date = (LinearLayout) itemView.findViewById(R.id.ll_start_date);
            this.linearLayout_end_date = (LinearLayout) itemView.findViewById(R.id.ll_end_date);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.weight = (TextView) itemView.findViewById(R.id.weight);
            this.btn = (Button) itemView.findViewById(R.id.btn);
            parcel_img = (ImageView) itemView.findViewById(R.id.parcel_img);
        }
    }
}
