package com.webapp.oasis.Customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Admin.Map.MapSingleDriverActivity;
import com.webapp.oasis.Customer.PaymentCustomerActivity;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.UserOrderDetailModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class UserOrderListAdapter extends RecyclerView.Adapter<UserOrderListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    StringRequest stringRequest;
    RequestQueue requestQueue;
    private List<UserOrderDetailModel> mUser;
    SessionManager session;
    String user_id, hash;

    public UserOrderListAdapter(Context context, List<UserOrderDetailModel> mUser) {
        this.context = context;
        this.mUser = mUser;

        session = new SessionManager(context);
        HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);


    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final UserOrderDetailModel user_details = mUser.get(i);

        viewHolder.driver_name.setText(user_details.getDriver_name());
        viewHolder.mobile_no.setText(user_details.getMobile_no());
        viewHolder.date.setText(user_details.getDate());
        viewHolder.amount.setText(user_details.getAmount());
        if (viewHolder.amount.getText().toString().equals("0"))
        {
            viewHolder.llpayment.setVisibility(View.GONE);
        }
        viewHolder.order_id.setText(user_details.getOrder_id());
        viewHolder.gst.setText(user_details.getGst());
        viewHolder.total_amount.setText(user_details.getTotal_amount());

        if (user_details.getOrder_status().equals("1"))
        {
            viewHolder.order_status.setText("Pending");
            viewHolder.btn_qrcode.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
        }
        else if(user_details.getOrder_status().equals("2"))
        {
            viewHolder.order_status.setText("Approve");
            viewHolder.btn_qrcode.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.VISIBLE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else  if(user_details.getOrder_status().equals("3")) {

            viewHolder.order_status.setText("Verify");
            viewHolder.btn_qrcode.setVisibility(View.VISIBLE);
            viewHolder.btn_track.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.VISIBLE);
            viewHolder.whatsapp.setVisibility(View.VISIBLE);
        }
        else if (user_details.getOrder_status().equals("4"))
        {
            viewHolder.order_status.setText("Delivered");
            viewHolder.btn_qrcode.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
        }
        else if (user_details.getOrder_status().equals("5"))
        {
            viewHolder.order_status.setText("Reject");
            viewHolder.btn_qrcode.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else if (user_details.getOrder_status().equals("6"))
        {
            viewHolder.order_status.setText("On The Way");
            viewHolder.btn_qrcode.setVisibility(View.VISIBLE);
            viewHolder.btn_track.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.VISIBLE);
            viewHolder.whatsapp.setVisibility(View.VISIBLE);
        }
        viewHolder.btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapSingleDriverActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("lat",user_details.getLat());
                intent.putExtra("lon",user_details.getLon());
                context.startActivity(intent);
            }
        });
        viewHolder.btn_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, See_Full_Image_F.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imageurl",user_details.getQr_code());
                context.startActivity(intent);
            }
        });
        viewHolder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PaymentCustomerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("amount", user_details.getTotal_amount());
                Log.d("order_id",user_details.getOrder_id());
                intent.putExtra("amount",user_details.getTotal_amount());
                intent.putExtra("order_id",user_details.getOrder_id());
                context.startActivity(intent);
            }
        });
        viewHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                boolean isAppInstalled = appInstalledOrNot("com.whatsapp");
                if (isAppInstalled) {
                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);


                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91"+ viewHolder.mobile_no.getText().toString() +"&text=From OASIS GLOBE\n";
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store

                    Toast.makeText(context, "Application is not currently installed", Toast.LENGTH_SHORT).show();
                }
            }

        });
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + user_details.getMobile_no()));
                context.startActivity(intent);
            }
        });
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView driver_name,mobile_no,amount,date,order_status,order_id,gst,total_amount;
        Button btn_qrcode,btn_track,btn_pay;
        RelativeLayout whatsapp,call;
        LinearLayout llpayment;

        public ViewHolder(View itemView) {
            super(itemView);

            llpayment=itemView.findViewById(R.id.llpayment);
            gst=itemView.findViewById(R.id.gst);
            total_amount=itemView.findViewById(R.id.total_amount);
            order_id=itemView.findViewById(R.id.order_id);
            btn_pay=itemView.findViewById(R.id.btn_pay);
            whatsapp=itemView.findViewById(R.id.whatsapp);
            call=itemView.findViewById(R.id.call);
            order_status=itemView.findViewById(R.id.order_status);
            driver_name=itemView.findViewById(R.id.driver_name);
            mobile_no=itemView.findViewById(R.id.mobile_no);
            amount=itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.date);
            btn_qrcode=itemView.findViewById(R.id.btn_qrcode);
            btn_track=itemView.findViewById(R.id.btn_track);

        }
    }
}
