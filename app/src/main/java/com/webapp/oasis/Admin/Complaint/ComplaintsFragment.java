package com.webapp.oasis.Admin.Complaint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.net.HttpHeaders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminDriverListAdapter;
import com.webapp.oasis.Customer.ComplaintStatusActivity;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.ComplaintStatusModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentComplaintsBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: com.webapp.oasis.Admin.ui.Complaint.ComplaintsFragment */
public class ComplaintsFragment extends Fragment {
    String CustomerId;
    FragmentComplaintsBinding binding;

    /* renamed from: dm */
    ArrayList<AgentDriverOrderListModel> f21dm = new ArrayList<>();
    String login_from;
    String logincode;
    RecyclerView mRecyclerView;
    AdminDriverListAdapter myOrderAdapter;
    SessionManager session;
    String technicianId_session;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentComplaintsBinding.inflate(getLayoutInflater());
        SessionManager sessionManager = new SessionManager(getActivity());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.logincode = users.get(SessionManager.KEY_LoginCode);
        this.CustomerId = users.get(SessionManager.CustomerId);
        this.technicianId_session = users.get(SessionManager.KEY_TecnicianID);
        this.f21dm.clear();
        RecyclerView recyclerView = (RecyclerView) this.binding.getRoot().findViewById(R.id.driverlistrecycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final ProgressDialog showMe = new ProgressDialog(getContext());
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        this.binding.rllogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintsFragment.this.getContext());
                builder.setMessage("Are you sure you want to logout ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ComplaintsFragment.this.session.logoutUser();
                    }
                }).setNegativeButton("No", (dialog, id) -> dialog.cancel());
                builder.create().show();
            }
        });

        this.logincode.equals(ExifInterface.GPS_MEASUREMENT_2D);
        AdminDriverListAdapter adminDriverListAdapter = new AdminDriverListAdapter(getActivity(), this.f21dm);
        this.myOrderAdapter = adminDriverListAdapter;
        this.mRecyclerView.setAdapter(adminDriverListAdapter);

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it;
                String customerId;
                DataSnapshot customerSnapshot;
                Iterator<DataSnapshot> it2;
                Object r14 = null;
                Object  r0 = null;
                Iterator<DataSnapshot> it3 = dataSnapshot.getChildren().iterator();
                while (it3.hasNext()) {
                    DataSnapshot customerSnapshot2 = it3.next();
                    String customerId2 = customerSnapshot2.getKey();
                    Iterator<DataSnapshot> it4 = customerSnapshot2.getChildren().iterator();
                    while (it4.hasNext()) {
                        DataSnapshot complaintSnapshot = it4.next();
                        Object complaintDataObject = complaintSnapshot.getValue();
                        if (complaintDataObject instanceof Map) {
                            Map<String, Object> complaintData = (Map) complaintDataObject;
                            String technicianId = (String) complaintData.get("TechnicianId");
//                            Log.d("technicianId",technicianId);
//                            Log.d("technicianId_session",ComplaintsFragment.this.technicianId_session);
                            it2 = it3;
                            customerSnapshot = customerSnapshot2;
                            customerId = customerId2;
                            it = it4;
                            DataSnapshot dataSnapshot2 = complaintSnapshot;
                            Object obj = complaintDataObject;
                            String obj2 = "CustomerID";
                            if (TextUtils.isEmpty(ComplaintsFragment.this.technicianId_session) || TextUtils.isEmpty(technicianId)) {
                                String obj3 = obj2;
                                if (TextUtils.isEmpty(ComplaintsFragment.this.technicianId_session)) {
                                    ComplaintsFragment.this.binding.rllogout.setVisibility(View.GONE);
                                    String complaint = (String) complaintData.get("Complaint");
                                    String image = (String) complaintData.get("Image");
                                    String str = (String) complaintData.get("StartDate");
                                    String str2 = (String) complaintData.get("EndDate");
                                    String str3 = (String) complaintData.get("TechnicianId");
//                                    r14 = this;
                                    Map<String, Object> map = complaintData;
                                    ComplaintsFragment.this.f21dm.add(new AgentDriverOrderListModel((String) complaintData.get("CustomerName"), (String) complaintData.get("CustomerMobileNumber"), complaint, (String) complaintData.get("Service"), (String) complaintData.get("Status"), image, (String) complaintData.get("Timing"), (String) complaintData.get(HttpHeaders.DATE), (String) complaintData.get("ComplaintId")
                                            , (String) complaintData.get("CustomerId"),"","", (String) complaintData.get("TechnicianRemark")));
                                    ComplaintsFragment complaintsFragment = ComplaintsFragment.this;
                                    String str4 = complaint;
                                    String str5 = image;

                                    Collections.sort(ComplaintsFragment.this.f21dm, new Comparator<AgentDriverOrderListModel>() {
                                        @Override
                                        public int compare(AgentDriverOrderListModel t1, AgentDriverOrderListModel t2) {
                                            return (t2.getDate()+t2.getTiming()).compareTo(t1.getDate()+t1.getTiming());
                                        }
                                    });
                                    try {
                                        complaintsFragment.myOrderAdapter = new AdminDriverListAdapter(complaintsFragment.getActivity(), ComplaintsFragment.this.f21dm);
                                        ComplaintsFragment.this.mRecyclerView.setAdapter(ComplaintsFragment.this.myOrderAdapter);
                                        ComplaintsFragment.this.myOrderAdapter.notifyDataSetChanged();
                                    }catch (Exception e){}
                                    showMe.dismiss();
                                } else {
                                    r14 = r0;
                                    Map<String, Object> map2 = complaintData;
                                }
                            } else if (technicianId.equals(ComplaintsFragment.this.technicianId_session)) {
                                String str6 = technicianId;
                                Log.d("str6",str6);
                                ComplaintsFragment.this.binding.rllogout.setVisibility(View.VISIBLE);
                                String timing = (String) complaintData.get("Timing");
                                String image2 = (String) complaintData.get("Image");
                                String str7 = (String) complaintData.get("StartDate");
                                String str8 = (String) complaintData.get("EndDate");
                                String str9 = (String) complaintData.get("TechnicianId");
                                ComplaintsFragment.this.f21dm.add(new AgentDriverOrderListModel((String) complaintData.get("CustomerName"), (String) complaintData.get("CustomerMobileNumber"), (String) complaintData.get("Complaint"), (String) complaintData.get("Service"), (String) complaintData.get("Status"), image2, timing, (String) complaintData.get(HttpHeaders.DATE), (String) complaintData.get("ComplaintId"), (String) complaintData.get("CustomerId"),"","",(String) complaintData.get("TechnicianRemark")));
                                ComplaintsFragment complaintsFragment2 = ComplaintsFragment.this;
                                String str10 = image2;
                                String str11 = timing;
                                try {
                                complaintsFragment2.myOrderAdapter = new AdminDriverListAdapter(complaintsFragment2.getActivity(), ComplaintsFragment.this.f21dm);
                                ComplaintsFragment.this.mRecyclerView.setAdapter(ComplaintsFragment.this.myOrderAdapter);
                                ComplaintsFragment.this.myOrderAdapter.notifyDataSetChanged();
                                }catch (Exception e){}
                                showMe.dismiss();
                                r14 = r0;
                            } else {
                                r14 = r0;
                            }
                        } else {
                            r14 = r0;
                            it2 = it3;
                            customerSnapshot = customerSnapshot2;
                            customerId = customerId2;
                            it = it4;
                            DataSnapshot dataSnapshot3 = complaintSnapshot;
                            Object obj4 = complaintDataObject;
                        }
                        r0 = r14;
                        it3 = it2;
                        customerSnapshot2 = customerSnapshot;
                        customerId2 = customerId;
                        it4 = it;
                    }
                    r14 = r0;
                    Iterator<DataSnapshot> it5 = it3;
                    DataSnapshot dataSnapshot4 = customerSnapshot2;
                    String str12 = customerId2;
                }
                r14 = r0;
                showMe.dismiss();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return this.binding.getRoot();
    }
}
