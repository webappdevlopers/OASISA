package com.webapp.oasis.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.Driver.Adapter.DriverOrderListAdapter;
import com.webapp.oasis.Model.DriverOrderDetailModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class DriverOrderActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    DriverOrderListAdapter myOrderAdapter;

    String user_id, hash;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageView back;
    ArrayList<DriverOrderDetailModel> dm = new ArrayList<DriverOrderDetailModel>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        Log.d("userid",user_id);
        Log.d("hash",hash);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( DriverOrderActivity.this,DriverHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        mRecyclerView.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager12);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new DriverOrderListAdapter(getApplicationContext(),dm);
        mRecyclerView.setAdapter(myOrderAdapter);


        user_place_order_list();
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                user_place_order_list();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void user_place_order_list() {
        final ProgressDialog showMe = new ProgressDialog(DriverOrderActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.driver_order_detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            Log.d("driver_order_detail",ServerResponse);

                            dm.clear();
                            if (status.equals("200")) {

                                dm.clear();

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {

                                    for (int i = 0; i < applist.length(); i++) {

                                        JSONObject jsonObject = applist.getJSONObject(i);
                                        final DriverOrderDetailModel rm = new DriverOrderDetailModel();

                                        rm.setId(jsonObject.getString("id"));
                                        rm.setOrder_id(jsonObject.getString("order_id"));
                                        rm.setReceiver_name(jsonObject.getString("receiver_name"));
                                        rm.setMobile_no(jsonObject.getString("mobile_no"));
                                        rm.setReceiver_address(jsonObject.getString("receiver_address"));
                                        rm.setSource(jsonObject.getString("source"));
                                        rm.setDestination(jsonObject.getString("destination"));
                                        rm.setQty(jsonObject.getString("qty"));
                                        rm.setWeight(jsonObject.getString("weight"));
                                        rm.setParcel_img(jsonObject.getString("parcel_img"));
                                        rm.setUser_lat(jsonObject.getString("user_lat"));
                                        rm.setUser_lon(jsonObject.getString("user_lon"));
                                        rm.setStatus(jsonObject.getString("status"));

                                        dm.add(rm);
                                        myOrderAdapter = new DriverOrderListAdapter(getApplicationContext(), dm);
                                        mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model
                                    }
                                }
                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(DriverOrderActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();
                            //      Toast.makeText(CelebrityListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.d("eerror", String.valueOf(volleyError));
                        NetworkDialoguser_reviewd_details();

                        showMe.dismiss();
                        // Toast.makeText(StampOfferActivity.this, "No Connection", Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(DriverOrderActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(DriverOrderActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                user_place_order_list();
            }
        });
        dialogs.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( DriverOrderActivity.this,DriverHomeActivity.class);
        startActivity(intent);
        finish();
    }
}