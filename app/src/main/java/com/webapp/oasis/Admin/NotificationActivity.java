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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.ImagesContract;
import com.webapp.oasis.Admin.Adapter.NotificationAdapter;
import com.webapp.oasis.Model.NotificationModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationActivity extends AppCompatActivity {
    ImageView back;

    /* renamed from: dm */
    ArrayList<NotificationModel> f19dm = new ArrayList<>();
    String getpostid;
    String hash_id;
    NotificationAdapter notificationAdapter;
    RecyclerView reviewrecycler;
    SessionManager session;
    SwipeRefreshLayout swipeRefreshLayout;
    String url;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_notification);
        SessionManager sessionManager = new SessionManager(this);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash_id = users.get(SessionManager.KEY_HASH);
        String str = this.user_id;
        Log.d(str, " " + this.hash_id);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NotificationActivity.this.onBackPressed();
            }
        });
        this.url = getIntent().getStringExtra(ImagesContract.URL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notificationrecycler);
        this.reviewrecycler = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.reviewrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.reviewrecycler.setItemAnimator(new DefaultItemAnimator());
        NotificationAdapter notificationAdapter2 = new NotificationAdapter(getApplicationContext(), this.f19dm);
        this.notificationAdapter = notificationAdapter2;
        this.reviewrecycler.setAdapter(notificationAdapter2);
        notification();
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                NotificationActivity.this.notification();
                NotificationActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void notification() {
        final ProgressDialog showMe = new ProgressDialog(this, 3);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        Volley.newRequestQueue(this).add(new StringRequest(1, this.url, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                showMe.dismiss();
                try {
                    JSONObject j = new JSONObject(ServerResponse);
                    Log.d("response noti", ServerResponse);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    NotificationActivity.this.f19dm.clear();
                    if (status.equals("200")) {
                        JSONArray applist = j.getJSONArray("data");
                        if (applist != null && applist.length() > 0) {
                            for (int i = 0; i < applist.length(); i++) {
                                JSONObject jsonObject = applist.getJSONObject(i);
                                NotificationModel rm = new NotificationModel();
                                rm.setId(jsonObject.getString("id"));
                                rm.setDate(jsonObject.getString("date"));
                                rm.setDriver_id(jsonObject.getString("driver_id"));
                                rm.setName(jsonObject.getString("name"));
                                rm.setPlace(jsonObject.getString(SessionManager.KEY_PLACE));
                                rm.setSadmin_approve_status(jsonObject.getInt("driver_status"));
                                NotificationActivity.this.f19dm.add(rm);
                                NotificationActivity.this.notificationAdapter = new NotificationAdapter(NotificationActivity.this, NotificationActivity.this.f19dm);
                                NotificationActivity.this.reviewrecycler.setAdapter(NotificationActivity.this.notificationAdapter);
                            }
                        }
                    } else if (status.equals("500")) {
                        NotificationActivity.this.session.logoutUser();
                        NotificationActivity.this.finish();
                        Toast.makeText(NotificationActivity.this, "You are Logged in to another device!", 1).show();
                    } else {
                        Toast.makeText(NotificationActivity.this, msg, 1).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showMe.dismiss();
                    Toast.makeText(NotificationActivity.this, "Something Went Wrong", 0).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                NotificationActivity.this.NetworkDialog();
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
                params.put("user_id", NotificationActivity.this.user_id);
                params.put(SessionManager.KEY_HASH, NotificationActivity.this.hash_id);
                return params;
            }
        });
    }

    /* access modifiers changed from: private */
    public void NetworkDialog() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                NotificationActivity.this.notification();
            }
        });
        dialogs.show();
    }
}
