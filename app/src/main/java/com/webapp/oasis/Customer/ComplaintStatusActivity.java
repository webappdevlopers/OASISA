package com.webapp.oasis.Customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.common.net.HttpHeaders;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Customer.Adapter.ComplaintStatusListAdapter;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.ComplaintStatusModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityComplaintstatusBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ComplaintStatusActivity extends AppCompatActivity {
    ImageView back;
    ActivityComplaintstatusBinding binding;
    String customer_id;

    /* renamed from: dm */
    ArrayList<ComplaintStatusModel> f22dm = new ArrayList<>();
    String hash;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    ComplaintStatusListAdapter myOrderAdapter;
    SessionManager session;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComplaintstatusBinding inflate = ActivityComplaintstatusBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.customer_id = users.get(SessionManager.CustomerId);
        this.hash = users.get(SessionManager.KEY_HASH);
//        Log.d(SessionManager.KEY_USERID, this.user_id);
        Log.d(SessionManager.KEY_HASH, this.hash);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintStatusActivity.this.onBackPressed();
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
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
        final DatabaseReference myRef = database.getReference("Customer/Complaint/" + this.customer_id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myRef.addChildEventListener(new ChildEventListener() {
                        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                            if (dataSnapshot.exists()) {
                                Object value = dataSnapshot.getValue();
                                if (value instanceof Map) {
                                    Map<String, Object> data = (Map) value;

                                    if (data != null) {
                                        String complaint = (String) data.get("Complaint");
                                        Object obj = value;
                                        Object value2 = value;
                                        String str = complaint;
                                        ArrayList<ComplaintStatusModel> arrayList = ComplaintStatusActivity.this.f22dm;
                                        ComplaintStatusModel complaintStatusModel = new ComplaintStatusModel(complaint, (String) data.get("Service"),
                                                (String) data.get("Status"), (String) data.get(HttpHeaders.DATE), (String) data.get("Timing"),
                                                (String) data.get("Image"), (String) data.get("StartDate"), (String) data.get("EndDate")
                                                , (String) data.get("CustomerId"), (String) data.get("ComplaintId")
                                                , (String) data.get("CustomerName"), (String) data.get("CustomerMobileNumber"), (String) data.get("TechnicianRemark"));
                                        arrayList.add(complaintStatusModel);

                                        Collections.sort(ComplaintStatusActivity.this.f22dm, new Comparator<ComplaintStatusModel>() {
                                            @Override
                                            public int compare(ComplaintStatusModel t1, ComplaintStatusModel t2) {
                                                return (t1.getDate()+t1.getTiming()).compareTo(t2.getDate()+t2.getTiming());
                                            }
                                        });
                                        ComplaintStatusActivity.this.myOrderAdapter = new ComplaintStatusListAdapter(ComplaintStatusActivity.this.getApplicationContext(), ComplaintStatusActivity.this.f22dm, ComplaintStatusActivity.this.customer_id);
                                        ComplaintStatusActivity.this.mRecyclerView.setAdapter(ComplaintStatusActivity.this.myOrderAdapter);

                                        showMe.dismiss();
                                        return;
                                    }
                                    showMe.dismiss();
                                    ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                                    return;
                                }
                                showMe.dismiss();
                                ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                                Log.w(Oscillator.TAG, "Data is not a Map");
                                return;
                            }
                            showMe.dismiss();
                            ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                        }

                        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                            showMe.dismiss();
                            ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                        }

                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            showMe.dismiss();
                            ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                        }

                        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                            showMe.dismiss();
                            ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                        }

                        public void onCancelled(DatabaseError error) {
                            showMe.dismiss();
                            ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
                            Log.w(Oscillator.TAG, "Failed to read value.", error.toException());
                        }
                    });
                    return;
                }
                showMe.dismiss();
                ComplaintStatusActivity.this.binding.noDataFound.setVisibility(View.VISIBLE);
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        this.swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                ComplaintStatusActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }
}
