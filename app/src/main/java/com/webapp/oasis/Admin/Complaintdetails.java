package com.webapp.oasis.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.HttpHeaders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.BillSelectedItemListAdapter;
import com.webapp.oasis.Admin.Adapter.SelectItemListAdapter;
import com.webapp.oasis.Customer.CustomerHomeActivity;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Technician.technicianHomeActivity;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityComplaintdetailsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Complaintdetails extends AppCompatActivity {
    String CustomerId;
    ActivityComplaintdetailsBinding binding;
    SelectItemListAdapter.CartListener cartListener;

    /* renamed from: dm */
    ArrayList<AdminItemListModel> f18dm = new ArrayList<>();
    //    ArrayList<ItemsModel> f18dm = new ArrayList<>();
    ArrayList<String> gendername = new ArrayList<>();
    String logincode;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    //    SelectItemListAdapter myOrderAdapter;
    BillSelectedItemListAdapter myOrderAdapter;
    SessionManager session;
    Spinner spinner_select_service;
    ArrayList<AgentListModel> technicianModel = new ArrayList<>();
    String technicianid;
    ArrayList<ItemsModel> f14dm = new ArrayList<>();

    LinearLayout llproceed, technician_details;
    DatabaseHelper mydb;
    public static TextView tltitem;
    public static TextView tltitemcost;
    int flag = 0;
    String technicianId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComplaintdetailsBinding inflate = ActivityComplaintdetailsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.technicianid = users.get(SessionManager.KEY_TecnicianID);
        this.CustomerId = users.get(SessionManager.CustomerId);

        this.mydb = new DatabaseHelper(Complaintdetails.this);
        this.mydb.delete();
        tltitemcost = findViewById(R.id.tltitemcost);
        tltitem = findViewById(R.id.tltitem);
        technician_details = findViewById(R.id.technician_details);
        llproceed = findViewById(R.id.llproceed);
        llproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tltitem.getText().toString().equals("0 Items Added")) {
                    Toast.makeText(Complaintdetails.this, "Please Select Item", Toast.LENGTH_SHORT).show();
                } else {
//                    place_order();
                }
            }
        });
        this.mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecyclerrrr);
        this.mRecyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.stopScroll();
try {
    final ProgressDialog showMe = new ProgressDialog(this);
    showMe.setMessage("Please wait");
    showMe.setCancelable(true);
    showMe.setCanceledOnTouchOutside(false);
    showMe.show();
    String ItemList1 = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
            .child(getIntent().getStringExtra("CustomerId"))
            .child(getIntent().getStringExtra("ComplaintId"))
            .child("ItemsList").toString();
    Log.d("ItemList1", ItemList1.toString());
    FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
            .child(getIntent().getStringExtra("CustomerId"))
            .child(getIntent().getStringExtra("ComplaintId"))
            .child("ItemsList").addValueEventListener(new ValueEventListener() {
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("dataSnapshot1", dataSnapshot.toString());
            if (dataSnapshot.exists()) {
                Log.d("dataSnapshot", dataSnapshot.toString());
                Complaintdetails.this.f18dm.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Log.d("itemSnapshot", itemSnapshot.toString());
                    String itemName = (String) itemSnapshot.child("itemName").getValue(String.class);
                    String brandName = (String) itemSnapshot.child("brandName").getValue(String.class);
                    String qty = (String) itemSnapshot.child("qty").getValue(String.class);
                    String price = (String) itemSnapshot.child("price").getValue(String.class);
                    Complaintdetails.this.f18dm.add(new AdminItemListModel(itemName, brandName, qty, price, qty, (String) itemSnapshot.child("itemID").getValue(String.class)));
                    Complaintdetails complaintdetails = Complaintdetails.this;
                    myOrderAdapter = new BillSelectedItemListAdapter(Complaintdetails.this, Complaintdetails.this.f18dm);
                    mRecyclerView.setAdapter(Complaintdetails.this.myOrderAdapter);
                    myOrderAdapter.notifyDataSetChanged();

                    Log.d("TAG", "Item Name: " + itemName + ", Brand Name: " + brandName + ", Qty: " + qty + ", Price: " + price);
                    showMe.dismiss();
                }
                return;
            } else {
                Log.d("TAGElse", "NotExists");
            }
            showMe.dismiss();
        }

        public void onCancelled(DatabaseError databaseError) {
            Log.w("TAG", "Failed to read value.", databaseError.toException());
        }
    });
}catch (Exception e){}


        Intent intent = getIntent();
//        String user_details1= intent.getStringExtra("mylist");
        String ComplaintId = intent.getStringExtra("ComplaintId");
        String CustomerId = intent.getStringExtra("CustomerId");
        this.binding.changeStatusAdditem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Complaintdetails.this, ComplaintdetailsNew2.class);
//                intent.putExtra("mylist", user_details1);
                intent.putExtra("ComplaintId", ComplaintId);
                intent.putExtra("CustomerId", CustomerId);
                Log.d("CustomerId", CustomerId);
                Log.d("ComplaintId", ComplaintId);
                Complaintdetails.this.startActivity(intent);
            }
        });

        this.binding.changeStatusInprogress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Complaintdetails.this);
                builder.setMessage("Are you sure you want Change Status to InProgress ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Complaintdetails.this.mRecyclerView.setVisibility(View.GONE);
                        Complaintdetails.this.binding.changeStatusInprogress.setVisibility(View.GONE);
                        Complaintdetails.this.binding.changeStatusAdditem.setVisibility(View.VISIBLE);
                        Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
                        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                                .child(getIntent().getStringExtra("CustomerId"))
                                .child(getIntent().getStringExtra("ComplaintId"))
                                .child("Status").setValue("InProgress");

                        Toast.makeText(Complaintdetails.this, "Status Change to InProgress", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Complaintdetails.this, technicianHomeActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        try {
            DatabaseReference database = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                    .child(getIntent().getStringExtra("CustomerId"))
                    .child(getIntent().getStringExtra("ComplaintId"))
                    .child("TechnicianId");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        Log.d("TechnicianId", snapshot.getValue(String.class) + "");
                        technicianId = snapshot.getValue(String.class);
                        Log.d("technicianId", technicianId + "");

                    } catch (Exception e) {
                        Log.d("Exception TechnicianId", e.toString());
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        } catch (Exception e) {
        }

        try {
            DatabaseReference database = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                    .child(getIntent().getStringExtra("CustomerId"))
                    .child(getIntent().getStringExtra("ComplaintId"))
                    .child("TotalAmount");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        Log.d("TotalAmount", snapshot.getValue(String.class) + "");
                        Complaintdetails.this.binding.tltitemcost.setText(snapshot.getValue(String.class));
                    } catch (Exception e) {
                        Log.d("Exception TechnicianId", e.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        } catch (Exception e) {

        }

        Complaintdetails.this.binding.llproceed.setVisibility(View.GONE);

        final AgentDriverOrderListModel user_details = (AgentDriverOrderListModel) getIntent().getSerializableExtra("mylist");
        if (user_details.getStatus() != null) {
            if (TextUtils.isEmpty(this.technicianid) && user_details.getStatus().equalsIgnoreCase("Done") || !this.CustomerId.isEmpty()) {
                Complaintdetails.this.binding.tolalAmt.setVisibility(View.VISIBLE);
                Complaintdetails.this.binding.itemsDetails.setVisibility(View.VISIBLE);
                Complaintdetails.this.binding.llproceed.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Complaintdetails.this.binding.llAddTechnician.setVisibility(View.GONE);
                try {
                    Log.d("user_details.getTechnicianRemark()", "-" + user_details.getTechnicianRemark());
                    Complaintdetails.this.binding.technicianRemark.setText(user_details.getTechnicianRemark());
                } catch (Exception e) {
                }
                getTechnicianDetails();
                if (!this.CustomerId.isEmpty()) {
                    technician_details.setVisibility(View.GONE);
                }

            } else if (TextUtils.isEmpty(Complaintdetails.this.technicianid) && user_details.getStatus().equalsIgnoreCase("InProgress")) {
                Complaintdetails.this.binding.llAddTechnician.setVisibility(View.GONE);
                Complaintdetails.this.binding.itemsDetails.setVisibility(View.GONE);
                Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
                Complaintdetails.this.mRecyclerView.setVisibility(View.GONE);
                Complaintdetails.this.binding.changeStatusInprogress.setVisibility(View.GONE);

                getTechnicianDetails();
            } else if (TextUtils.isEmpty(Complaintdetails.this.technicianid) && user_details.getStatus().equalsIgnoreCase("Pending")) {
                Complaintdetails.this.binding.llAddTechnician.setVisibility(View.VISIBLE);
                Complaintdetails.this.binding.technicianDetails.setVisibility(View.GONE);
                Complaintdetails.this.binding.itemsDetails.setVisibility(View.GONE);
                Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
                Complaintdetails.this.mRecyclerView.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(Complaintdetails.this.technicianid) && user_details.getStatus().equalsIgnoreCase("Assign")) {
                Complaintdetails.this.binding.llAddTechnician.setVisibility(View.GONE);
                Complaintdetails.this.binding.technicianDetails.setVisibility(View.VISIBLE);
                Complaintdetails.this.mRecyclerView.setVisibility(View.VISIBLE);
                Complaintdetails.this.binding.itemsDetails.setVisibility(View.GONE);
                Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);

                getTechnicianDetails();

            } else if (!TextUtils.isEmpty(Complaintdetails.this.technicianid)) {
                if (user_details.getStatus().equalsIgnoreCase("InProgress")) {
                    Complaintdetails.this.mRecyclerView.setVisibility(View.GONE);
                    this.binding.llAddTechnician.setVisibility(View.GONE);
                    this.binding.changeStatusAdditem.setVisibility(View.GONE);
                    this.binding.technicianDetails.setVisibility(View.GONE);
                    this.binding.itemsDetails.setVisibility(View.GONE);
                    Complaintdetails.this.binding.changeStatusAdditem.setVisibility(View.VISIBLE);
                    Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
                    Complaintdetails.this.binding.changeStatusInprogress.setVisibility(View.GONE);
                } else if (user_details.getStatus().equalsIgnoreCase("Assign")) {
                    Complaintdetails.this.binding.llAddTechnician.setVisibility(View.GONE);
                    Complaintdetails.this.binding.technicianDetails.setVisibility(View.GONE);
                    Complaintdetails.this.binding.itemsDetails.setVisibility(View.GONE);
                    Complaintdetails.this.mRecyclerView.setVisibility(View.GONE);
                    Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
                    Complaintdetails.this.binding.changeStatusInprogress.setVisibility(View.VISIBLE);
                    flag = 1;
                }
            }
        }
        Complaintdetails.this.binding.customerName.setText(user_details.getCustomertName());
        Complaintdetails.this.binding.customerMobileNo.setText(user_details.getCustomerMobileNumber());
        Complaintdetails.this.binding.complaint.setText(user_details.getComplaint());
        Complaintdetails.this.binding.service.setText(user_details.getService());
        Complaintdetails.this.binding.timing.setText(user_details.getTiming());
        Complaintdetails.this.binding.date.setText(user_details.getDate());
        Complaintdetails.this.binding.status.setText(user_details.getStatus());
        Log.d("user_details.getTechnicianRemark()", "-" + user_details.getTechnicianRemark());
        Complaintdetails.this.binding.technicianRemark.setText(user_details.getTechnicianRemark());
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    Complaintdetails.this.f18dm.clear();
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot itemSnapshot = it.next();
                        Iterator<DataSnapshot> it2 = it;
//                        AgentListModel agentListModel = r4;
                        DataSnapshot dataSnapshot2 = itemSnapshot;
                        ArrayList<AgentListModel> arrayList = Complaintdetails.this.technicianModel;
                        AgentListModel agentListModel2 = new AgentListModel((String) itemSnapshot.child("Name").getValue(String.class), (String) itemSnapshot.child("Mobile").getValue(String.class), (String) itemSnapshot.child("Technician Password").getValue(String.class), (String) itemSnapshot.child("email").getValue(String.class), (String) itemSnapshot.child("AdhaarCard").getValue(String.class), (String) itemSnapshot.child("License").getValue(String.class), (String) itemSnapshot.child("Technician ID").getValue(String.class), (String) itemSnapshot.child("isDelete").getValue(String.class));
                        arrayList.add(agentListModel2);
                        it = it2;
                    }
                    final List<String> technicianNames = new ArrayList<>();
                    technicianNames.add("Add Technician");
                    Iterator<AgentListModel> it3 = Complaintdetails.this.technicianModel.iterator();
                    while (it3.hasNext()) {
                        technicianNames.add(it3.next().getName());
                    }
                    final List<String> technicianId = new ArrayList<>();
                    technicianId.add("-");
                    Iterator<AgentListModel> it4 = Complaintdetails.this.technicianModel.iterator();
                    while (it4.hasNext()) {
                        technicianId.add(it4.next().getTechnician_id());
                    }
                    final List<String> technicianMobile = new ArrayList<>();
                    technicianMobile.add("Add technicianMobile");
                    Iterator<AgentListModel> it5 = Complaintdetails.this.technicianModel.iterator();
                    while (it5.hasNext()) {
                        technicianMobile.add(it5.next().getMobile());
                    }
                    final List<String> technicianEmail = new ArrayList<>();
                    technicianEmail.add("Add technicianEmail");
                    Iterator<AgentListModel> it6 = Complaintdetails.this.technicianModel.iterator();
                    while (it6.hasNext()) {
                        technicianEmail.add(it6.next().getEmail());
                    }
                    Complaintdetails complaintdetails = Complaintdetails.this;
                    complaintdetails.spinner_select_service = (Spinner) complaintdetails.findViewById(R.id.add_technician);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Complaintdetails.this, android.R.layout.simple_list_item_1, technicianNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    Complaintdetails.this.spinner_select_service.setAdapter(adapter);
                    Complaintdetails.this.spinner_select_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (!Complaintdetails.this.spinner_select_service.getSelectedItem().toString().equalsIgnoreCase("Add Technician")) {
                                final String selectedTechnicianName = (String) technicianNames.get(position);
                                Log.d("selectedTechnicianName", selectedTechnicianName);
                                Log.d("position", position + "");
                                String format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                String format2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint");
                                Map<String, Object> data = new HashMap<>();
                                data.put("Complaint", user_details.getComplaint());
                                data.put("Service", user_details.getService());
                                data.put("Status", "Assign");
                                data.put(HttpHeaders.DATE, user_details.getDate());
                                data.put("Timing", user_details.getTiming());
                                data.put("Image", user_details.getImage());
                                data.put("CustomerName", user_details.getCustomertName());
                                data.put("CustomerMobileNumber", user_details.getCustomerMobileNumber());
                                data.put("TechnicianId", (String) technicianId.get(position));
                                data.put("TechnicianName", selectedTechnicianName);
                                Log.d("technicianMobile", (String) technicianMobile.get(position) + "");
                                data.put("TechnicianMobile", (String) technicianMobile.get(position));
                                data.put("TechnicianEmail", (String) technicianEmail.get(position));
                                data.put("ComplaintId", user_details.getComplaintId());
                                data.put("CustomerId", user_details.getCustomerID());
                                myRef.child(user_details.getCustomerID()).child(user_details.getComplaintId()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Complaintdetails.this);
                                            builder.setMessage("Complaint Has Been Assign to " + selectedTechnicianName);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Complaintdetails.this.startActivity(new Intent(Complaintdetails.this, Complaintdetails.class));
                                                    Complaintdetails.this.finish();
                                                }
                                            });
                                            builder.setNegativeButton("GO TO HOME PAGE", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Complaintdetails.this.startActivity(new Intent(Complaintdetails.this, CustomerHomeActivity.class));
                                                    Complaintdetails.this.finish();
                                                }
                                            });
                                            builder.create().show();
                                            Log.d(Oscillator.TAG, "Data uploaded successfully");
                                            return;
                                        }
                                        Log.d(Oscillator.TAG, "Data upload failed");
                                    }
                                });
                                return;
                            }
                            Toast.makeText(Complaintdetails.this, "Assign Complaint to Technician", Toast.LENGTH_SHORT).show();
                        }

                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        Complaintdetails.this.binding.changeStatusDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Complaintdetails.this);
                builder.setMessage("Are you sure you want Change Status to Done ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

//        Complaintdetails.this.binding.tolalAmt.setVisibility(View.GONE);
//        mRecyclerView.setVisibility(View.GONE);
    }

    private void getTechnicianDetails() {
        try {
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    final ProgressDialog showMe1 = new ProgressDialog(Complaintdetails.this);
                    showMe1.setMessage("Please wait");
                    showMe1.setCancelable(true);
                    showMe1.setCanceledOnTouchOutside(false);
                    showMe1.show();
                    FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                while (it.hasNext()) {
                                    DataSnapshot itemSnapshot = it.next();
                                    AgentListModel agentListModel2 = new AgentListModel((String) itemSnapshot.child("Name").getValue(String.class), (String) itemSnapshot.child("Mobile").getValue(String.class), (String) itemSnapshot.child("Technician Password").getValue(String.class), (String) itemSnapshot.child("email").getValue(String.class), (String) itemSnapshot.child("AdhaarCard").getValue(String.class), (String) itemSnapshot.child("License").getValue(String.class), (String) itemSnapshot.child("Technician ID").getValue(String.class) , (String) itemSnapshot.child("isDelete").getValue(String.class));
                                    if (agentListModel2.getTechnician_id().equals(technicianId)) {

                                        Complaintdetails.this.binding.technicianname.setText(agentListModel2.getName());
                                        Complaintdetails.this.binding.techniciannumber.setText(agentListModel2.getMobile());
                                    }
                                    showMe1.dismiss();
                                }
                                return;
                            }
                            showMe1.dismiss();
                        }
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("TAG", "Failed to read value.", databaseError.toException());
                        }
                    });
                }
            }, secondsDelayed * 2000);
        }catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (flag==1){
////                    complaintdetails.myOrderAdapter = new SelectItemListAdapter(Complaintdetails.this, Complaintdetails.this.f18dm);
////                    Complaintdetails.this.mRecyclerView.setAdapter(Complaintdetails.this.myOrderAdapter);
////                    Complaintdetails.this.myOrderAdapter.notifyDataSetChanged();
//                }
//            }
//        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!TextUtils.isEmpty(Complaintdetails.this.technicianid)) {
            Intent intent = new Intent(Complaintdetails.this, technicianHomeActivity.class);
            startActivity(intent);
        } else {
            finish();
        }

    }
}
