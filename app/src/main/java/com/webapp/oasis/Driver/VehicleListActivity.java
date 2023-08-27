package com.webapp.oasis.Driver;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.webapp.oasis.Driver.Adapter.VehicleListAdapter;
import com.webapp.oasis.Model.VehicleListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class VehicleListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    VehicleListAdapter myOrderAdapter;
    String user_id, hash;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageView back;
    ArrayList<VehicleListModel> dm = new ArrayList<VehicleListModel>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

            session = new SessionManager(getApplicationContext());
            final HashMap<String, String> users = session.getUserDetails();
            user_id = users.get(session.KEY_USERID);
            hash = users.get(session.KEY_HASH);
            Log.d("hash",hash);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.vehiclelistrecycler);
        mRecyclerView.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager12);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new VehicleListAdapter(getApplicationContext(),dm);
        mRecyclerView.setAdapter(myOrderAdapter);

        list_vehical();
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                list_vehical();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void list_vehical() {
        final ProgressDialog showMe = new ProgressDialog(VehicleListActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.    list_vehical,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        Log.d("list vehicla resp",ServerResponse);
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
                                        final VehicleListModel rm = new VehicleListModel();

                                        rm.setId(jsonObject.getString("id"));
                                        rm.setDate(jsonObject.getString("date"));
                                        rm.setVehical_no(jsonObject.getString("vehical_no"));
                                        rm.setLicence_no(jsonObject.getString("licence_no"));
                                        rm.setSource(jsonObject.getString("source"));
                                        rm.setDestination(jsonObject.getString("destination"));
                                        rm.setCostpkg(jsonObject.getString("costpkg"));
                                        rm.setMinodrpkg(jsonObject.getString("minodrpkg"));
                                        rm.setMaxodrpkg(jsonObject.getString("maxodrpkg"));
                                        rm.setImage(jsonObject.getString("image"));

                                        dm.add(rm);
                                        myOrderAdapter = new VehicleListAdapter(getApplicationContext(), dm);
                                        mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model
                                    }
                                }
                            }
                            else if(status.equals("500"))
                            {
                                session.logoutUser();
                                finish();
                                Toast.makeText(VehicleListActivity.this, "You are Logged in to another device!", Toast.LENGTH_LONG).show();
                            }
                            else {

                                // Showing Echo Response Message Coming From Server.
                                         Toast.makeText(VehicleListActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            showMe.dismiss();
                            //      Toast.makeText(CelebrityListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        volleyError.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(VehicleListActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(VehicleListActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                list_vehical();
            }
        });
        dialogs.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(VehicleListActivity.this,AddVehicleActivity.class);
        startActivity(intent);
        finish();
    }
}