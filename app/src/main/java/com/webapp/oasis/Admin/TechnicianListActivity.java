package com.webapp.oasis.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.technicianListAdapter;

import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TechnicianListActivity extends AppCompatActivity {

    /* renamed from: dm */
    ArrayList<AgentListModel> f20dm = new ArrayList<>();
    String hash;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    technicianListAdapter myOrderAdapter;
    SessionManager session;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_technician_list);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_TecnicianID);
        this.hash = users.get(SessionManager.KEY_HASH);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                onBackPressed();

            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 1, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TechnicianListActivity.this.f20dm.clear();
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot itemSnapshot = it.next();
                        AgentListModel agentListModel = new AgentListModel(
                                (String) itemSnapshot.child("Name").getValue(String.class),
                                (String) itemSnapshot.child("Mobile").getValue(String.class),
                                (String) itemSnapshot.child("Technician Password").getValue(String.class),
                                (String) itemSnapshot.child("email").getValue(String.class),
                                (String) itemSnapshot.child("AdhaarCard").getValue(String.class),
                                (String) itemSnapshot.child("License").getValue(String.class),
                                (String) itemSnapshot.child("Technician ID").getValue(String.class),
                                (String) itemSnapshot.child("isDelete").getValue(String.class)
                        );

                        // Check if isDelete is not "true" before adding to the list
                        if (!agentListModel.getIsDeleted().equals("true")) {
                            f20dm.add(agentListModel);
                        }

                        myOrderAdapter = new technicianListAdapter(getApplicationContext(), f20dm);
                        TechnicianListActivity.this.mRecyclerView.setAdapter(myOrderAdapter);
                        TechnicianListActivity.this.myOrderAdapter.notifyDataSetChanged();
                        showMe.dismiss();
                    }
                } else {
                    showMe.dismiss();
                }
            }

                public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                TechnicianListActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
