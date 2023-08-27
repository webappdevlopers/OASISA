package com.webapp.oasis.Admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.webapp.oasis.Admin.Adapter.AdminInvoiceListAdapter;
import com.webapp.oasis.Model.InvoiceListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AminInvoiceListActivity extends AppCompatActivity {
    ImageView back;

    /* renamed from: dm */
    ArrayList<InvoiceListModel> f17dm = new ArrayList<>();
    String hash;
    RecyclerView mRecyclerView;
    AdminInvoiceListAdapter myOrderAdapter;
    SessionManager session;
    SwipeRefreshLayout swipeRefreshLayout;
    String url = "";
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_amin_invoice_list);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AminInvoiceListActivity.this.onBackPressed();
                AminInvoiceListActivity.this.finish();
            }
        });
        Log.d("userId", this.user_id);
        Log.d(SessionManager.KEY_HASH, this.hash);
        this.url = getIntent().getStringExtra(ImagesContract.URL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        AdminInvoiceListAdapter adminInvoiceListAdapter = new AdminInvoiceListAdapter(getApplicationContext(), this.f17dm);
        this.myOrderAdapter = adminInvoiceListAdapter;
        this.mRecyclerView.setAdapter(adminInvoiceListAdapter);
        invoice();
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                AminInvoiceListActivity.this.invoice();
                AminInvoiceListActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void invoice() {
        final ProgressDialog showMe = new ProgressDialog(this, 3);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        StringRequest r3 = new StringRequest(1, this.url, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                showMe.dismiss();
                try {
                    JSONObject j = new JSONObject(ServerResponse);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    Log.d("user_place_order_list", ServerResponse);
                    AminInvoiceListActivity.this.f17dm.clear();
                    if (status.equals("200")) {
                        AminInvoiceListActivity.this.f17dm.clear();
                        JSONArray applist = j.getJSONArray("data");
                        if (applist != null && applist.length() > 0) {
                            for (int i = 0; i < applist.length(); i++) {
                                JSONObject jsonObject = applist.getJSONObject(i);
                                InvoiceListModel rm = new InvoiceListModel();
                                rm.setDriver_name(jsonObject.getString("driver_name"));
                                rm.setMobile_no(jsonObject.getString("mobile_no"));
                                rm.setAddress(jsonObject.getString("address"));
                                rm.setSource(jsonObject.getString(FirebaseAnalytics.Param.SOURCE));
                                rm.setDestination(jsonObject.getString(FirebaseAnalytics.Param.DESTINATION));
                                rm.setReceiver_name(jsonObject.getString("receiver_name"));
                                rm.setOrder_date(jsonObject.getString("order_date"));
                                rm.setFinal_cost(jsonObject.getString("final_cost"));
                                rm.setOrder_status(jsonObject.getString("order_status"));
                                rm.setInvoice_no(jsonObject.getString("invoice_no"));
                                rm.setInvoice(jsonObject.getString("invoice"));
                                AminInvoiceListActivity.this.f17dm.add(rm);
                                AminInvoiceListActivity.this.myOrderAdapter = new AdminInvoiceListAdapter(AminInvoiceListActivity.this.getApplicationContext(), AminInvoiceListActivity.this.f17dm);
                                AminInvoiceListActivity.this.mRecyclerView.setAdapter(AminInvoiceListActivity.this.myOrderAdapter);
                            }
                        }
                        return;
                    }
                    Toast.makeText(AminInvoiceListActivity.this, msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    showMe.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("eerror", String.valueOf(volleyError));
                AminInvoiceListActivity.this.NetworkDialoguser_reviewd_details();
                showMe.dismiss();
            }
        }) {
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", AminInvoiceListActivity.this.user_id);
                params.put(SessionManager.KEY_HASH, AminInvoiceListActivity.this.hash);
                return params;
            }
        };
        r3.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));
        Volley.newRequestQueue(this).add(r3);
    }

    /* access modifiers changed from: private */
    public void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                AminInvoiceListActivity.this.invoice();
            }
        });
        dialogs.show();
    }
}
