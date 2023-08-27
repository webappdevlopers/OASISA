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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.webapp.oasis.Admin.Adapter.AdminDriverListAdapter;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminDriverListActivity extends AppCompatActivity {
    ImageView back;

    /* renamed from: dm */
    ArrayList<AgentDriverOrderListModel> f15dm = new ArrayList<>();
    String hash;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    AdminDriverListAdapter myOrderAdapter;
    SessionManager session;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    String url = "";
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_admin_driver_list);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
        Log.d("userId", this.user_id);
        Log.d(SessionManager.KEY_HASH, this.hash);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdminDriverListActivity.this.onBackPressed();
                AdminDriverListActivity.this.finish();
            }
        });
        this.url = getIntent().getStringExtra(ImagesContract.URL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.driverlistrecycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        AdminDriverListAdapter adminDriverListAdapter = new AdminDriverListAdapter(getApplicationContext(), this.f15dm);
        this.myOrderAdapter = adminDriverListAdapter;
        this.mRecyclerView.setAdapter(adminDriverListAdapter);
//        user_place_order_list();
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
//                AdminDriverListActivity.this.user_place_order_list();
                AdminDriverListActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
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
//                AdminDriverListActivity.this.user_place_order_list();
            }
        });
        dialogs.show();
    }
}
