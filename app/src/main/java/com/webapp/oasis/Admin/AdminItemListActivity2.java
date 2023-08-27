package com.webapp.oasis.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import com.webapp.oasis.Admin.Adapter.AdminItemListAdapter;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class AdminItemListActivity2 extends AppCompatActivity {
    RecyclerView mRecyclerView;
    AdminItemListAdapter myOrderAdapter;
    String user_id, hash;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageView back;
    ArrayList<AdminItemListModel> dm = new ArrayList<AdminItemListModel>();

    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_item_list2);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        mRecyclerView.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager12);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new AdminItemListAdapter(getApplicationContext(),dm);
        mRecyclerView.setAdapter(myOrderAdapter);

        item_list();
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                item_list();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void item_list() {
        final ProgressDialog showMe = new ProgressDialog(AdminItemListActivity2.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.item_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");


                            dm.clear();
                            if (status.equals("200")) {

                                dm.clear();

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {

                                    for (int i = 0; i < applist.length(); i++) {

                                        JSONObject jsonObject = applist.getJSONObject(i);
//                                        final AdminItemListModel rm = new AdminItemListModel();

//                                        rm.setId(jsonObject.getString("id"));
//                                        rm.setItem_id(jsonObject.getString("item_id"));
//                                        rm.setName(jsonObject.getString("name"));
////                                        rm.setPrice(jsonObject.getString("price"));
////                                        rm.setColor(jsonObject.getString("color"));
//                                        rm.setWeight(jsonObject.getString("weight"));
//                                        rm.setSize(jsonObject.getString("size"));
//                                        rm.setImage(jsonObject.getString("image"));
//
//                                        dm.add(rm);
//                                        myOrderAdapter = new AdminItemListAdapter(getApplicationContext(), dm);
//                                        mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model
                                    }
                                }
                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(AdminItemListActivity2.this, msg, Toast.LENGTH_LONG).show();
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
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(AdminItemListActivity2.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(AdminItemListActivity2.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                item_list();
            }
        });
        dialogs.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
           finish();
    }
}