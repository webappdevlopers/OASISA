package com.webapp.oasis.Driver.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webapp.oasis.Admin.Map.MapSingleDriverActivity;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Driver.DriverOrderActivity;
import com.webapp.oasis.Driver.ScanActivity;
import com.webapp.oasis.Model.DriverOrderDetailModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class DriverOrderListAdapter extends RecyclerView.Adapter<DriverOrderListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    private List<DriverOrderDetailModel> mUser;
    SessionManager session;
    String user_id, hash;
    public DriverOrderListAdapter(Context context, List<DriverOrderDetailModel> mUser) {

        this.mUser = mUser;
        this.context = context;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_order_list_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final DriverOrderDetailModel user_details = mUser.get(i);

        viewHolder.receiver_name.setText(user_details.getReceiver_name());
        viewHolder.mobile_no.setText(user_details.getMobile_no());
        viewHolder.source.setText(user_details.getSource());
        viewHolder.destination.setText(user_details.getDestination());
        viewHolder.weight.setText(user_details.getWeight());
        viewHolder.qty.setText(user_details.getQty());
        viewHolder.order_id.setText(user_details.getOrder_id());


        if (user_details.getStatus().equals("1"))
        {
            viewHolder.btn.setText("Submit");
            viewHolder.edtamount.setVisibility(View.VISIBLE);
            viewHolder.btn_scanqr.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else if (user_details.getStatus().equals("2"))
        {
            viewHolder.btn.setText("Pending");
            viewHolder.edtamount.setVisibility(View.GONE);
            viewHolder.btn_scanqr.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else  if(user_details.getStatus().equals("3")) {

            viewHolder.btn.setText("Verify");
            viewHolder.btn.setVisibility(View.GONE);
            viewHolder.edtamount.setVisibility(View.GONE);
            viewHolder.btn_scanqr.setVisibility(View.VISIBLE);
            viewHolder.btn_track.setVisibility(View.VISIBLE);
            viewHolder.call.setVisibility(View.VISIBLE);
            viewHolder.whatsapp.setVisibility(View.VISIBLE);
        }
        else if (user_details.getStatus().equals("4"))
        {
            viewHolder.btn.setText("Delivered");
            viewHolder.edtamount.setVisibility(View.GONE);
            viewHolder.btn_scanqr.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else if (user_details.getStatus().equals("5"))
        {
            viewHolder.btn.setText("Reject");
            viewHolder.edtamount.setVisibility(View.GONE);
            viewHolder.btn_scanqr.setVisibility(View.GONE);
            viewHolder.btn_track.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.whatsapp.setVisibility(View.GONE);
        }
        else if (user_details.getStatus().equals("6"))
        {
            viewHolder.btn.setText("On The Way");
            viewHolder.btn.setVisibility(View.GONE);
            viewHolder.edtamount.setVisibility(View.GONE);
            viewHolder.btn_scanqr.setVisibility(View.VISIBLE);
            viewHolder.btn_track.setVisibility(View.VISIBLE);
            viewHolder.call.setVisibility(View.VISIBLE);
            viewHolder.whatsapp.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(user_details.getParcel_img()).into(viewHolder.parcel_img);
        viewHolder.parcel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, See_Full_Image_F.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imageurl",user_details.getParcel_img());
                context.startActivity(intent);
            }
        });
        viewHolder.btn_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.btn.getText().toString().equals("Verify")) {
                    Intent intent = new Intent(context, ScanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("order_id", user_details.getOrder_id());
                    intent.putExtra("status", "6");
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, ScanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("order_id", user_details.getOrder_id());
                    intent.putExtra("status", "4");
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolder.btn.getText().toString().equals("Submit"))
                {
                    if (viewHolder.edtamount.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else {

                        driver_approve_order(user_details.getOrder_id(), viewHolder.edtamount.getText().toString());
                        //Toast.makeText(context, "Order is Delivered", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(context, "Order Is not approved by customer", Toast.LENGTH_SHORT).show();
                }
                else {
                    //change_order_status(user_details.getOrder_id());
                 /*   if (viewHolder.edtamount.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else {

                        driver_approve_order(user_details.getOrder_id(), viewHolder.edtamount.getText().toString());
                        //Toast.makeText(context, "Order is Delivered", Toast.LENGTH_SHORT).show();
                    }*/
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
        viewHolder.btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapSingleDriverActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("lat",user_details.getUser_lat());
                intent.putExtra("lon",user_details.getUser_lon());
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

        TextView receiver_name,mobile_no,source,destination,qty,weight,order_id;
        Button btn,btn_scanqr,btn_track;
        ImageView parcel_img;
        EditText edtamount;
        RelativeLayout call,whatsapp;

        public ViewHolder(View itemView) {
            super(itemView);

            whatsapp=itemView.findViewById(R.id.whatsapp);
            call=itemView.findViewById(R.id.call);
//            btn_scanqr=itemView.findViewById(R.id.btn_scanqr);
            btn_track=itemView.findViewById(R.id.btn_track);
            edtamount=itemView.findViewById(R.id.edtamount);
            order_id=itemView.findViewById(R.id.order_id);
            receiver_name=itemView.findViewById(R.id.receiver_name);
            mobile_no=itemView.findViewById(R.id.mobile_no);
            source=itemView.findViewById(R.id.source);
            destination=itemView.findViewById(R.id.destination);
            qty=itemView.findViewById(R.id.qty);
            weight=itemView.findViewById(R.id.weight);
            btn=itemView.findViewById(R.id.btn);
            parcel_img=itemView.findViewById(R.id.parcel_img);

        }
    }
    private void driver_approve_order(String order_id,String amount) {
        final ProgressDialog showMe = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
//        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.driver_approve_order,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        Log.d("server response : ", ServerResponse);

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, DriverOrderActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            showMe.dismiss();
                            Toast.makeText(context, "Something Went Wrong\n"+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        volleyError.printStackTrace();
                        showMe.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", user_id);
                params.put("hash", hash);
                params.put("order_id", order_id);
                params.put("amount", amount);

                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

}
