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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminServiceListAdapter;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityAddServiceBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminAddServiceActivity extends AppCompatActivity {
    ImageView back;
    ActivityAddServiceBinding binding;

    /* renamed from: dm */
    ArrayList<ItemsModel> f16dm = new ArrayList<>();
    String hash;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    AdminServiceListAdapter myOrderAdapter;
    SessionManager session;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddServiceBinding inflate = ActivityAddServiceBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_TecnicianID);
        this.hash = users.get(SessionManager.KEY_HASH);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdminAddServiceActivity.this.onBackPressed();
            }
        });
        this.binding.btnAddService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdminAddServiceActivity.this.binding.etaddservice.getText().toString().isEmpty()) {
                    Toast.makeText(AdminAddServiceActivity.this, "Enter Service", Toast.LENGTH_SHORT).show();
                } else {
                    AdminAddServiceActivity.this.add_service();
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/AddServices").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AdminAddServiceActivity.this.f16dm.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        AdminAddServiceActivity.this.f16dm.add(new ItemsModel((String) itemSnapshot.child("ServiceName").getValue(String.class), (String) null, (String) null, (String) null, (String) null));
                        AdminAddServiceActivity adminStockListActivity = AdminAddServiceActivity.this;
                        adminStockListActivity.myOrderAdapter = new AdminServiceListAdapter(adminStockListActivity.getApplicationContext(), AdminAddServiceActivity.this.f16dm);
                        AdminAddServiceActivity.this.mRecyclerView.setAdapter(AdminAddServiceActivity.this.myOrderAdapter);
                        AdminAddServiceActivity.this.myOrderAdapter.notifyDataSetChanged();
                        showMe.dismiss();
                    }
                    return;
                }
                showMe.dismiss();
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
                AdminAddServiceActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void add_service() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/AddServices");
        Map<String, Object> data = new HashMap<>();
        data.put("ServiceName", this.binding.etaddservice.getText().toString());

        myRef.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    AdminAddServiceActivity adminStockListActivity = AdminAddServiceActivity.this;
                    Toast.makeText(adminStockListActivity, AdminAddServiceActivity.this.binding.etaddservice.getText().toString() + " Added Successfully", Toast.LENGTH_SHORT).show();
                    AdminAddServiceActivity.this.binding.etaddservice.setText("");
                    showMe.dismiss();
                }
            }
        });
    }
}
