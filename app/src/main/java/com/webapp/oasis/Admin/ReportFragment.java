package com.webapp.oasis.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.FileProvider;
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
import com.google.gson.Gson;
import com.webapp.oasis.Admin.Adapter.AdminReportListAdapter;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.ReportComplaintModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentReportBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/* renamed from: com.webapp.oasis.Admin.ui.ReportFragment */
public class ReportFragment extends Fragment {

    String CustomerId;
    FragmentReportBinding binding;

    /* renamed from: dm */
    ArrayList<AgentDriverOrderListModel> f21dm = new ArrayList<>();
    String login_from;
    String logincode;
    RecyclerView mRecyclerView;
    AdminReportListAdapter myOrderAdapter;
    SessionManager session;
    String technicianId_session;
    String csvFile;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentReportBinding.inflate(getLayoutInflater());
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
//        this.binding.rllogout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentReportBinding.this.getContext());
//                builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        FragmentReportBinding.this.session.logoutUser();
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//                builder.create().show();
//            }
//        });

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it;
                String customerId;
                DataSnapshot customerSnapshot;
                Iterator<DataSnapshot> it2;
                Object r14 = null;
                Object r0 = null;
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
//                            Log.d("technicianId_session",FragmentReportBinding.this.technicianId_session);
                            it2 = it3;
                            customerSnapshot = customerSnapshot2;
                            customerId = customerId2;
                            it = it4;
                            DataSnapshot dataSnapshot2 = complaintSnapshot;
                            Object obj = complaintDataObject;
                            String obj2 = "CustomerID";
                            if (TextUtils.isEmpty(technicianId_session) || TextUtils.isEmpty(technicianId)) {
                                String obj3 = obj2;
                                if (TextUtils.isEmpty(technicianId_session)) {
                                    String complaint = (String) complaintData.get("Complaint");
                                    String image = (String) complaintData.get("Image");
                                    String str = (String) complaintData.get("StartDate");
                                    String str2 = (String) complaintData.get("EndDate");
                                    String str3 = (String) complaintData.get("TechnicianId");
//                                    r14 = this;
                                    Map<String, Object> map = complaintData;
                                    f21dm.add(new AgentDriverOrderListModel((String) complaintData.get("CustomerName"), (String) complaintData.get("CustomerMobileNumber"), complaint, (String) complaintData.get("Service"), (String) complaintData.get("Status"), image, (String) complaintData.get("Timing"), (String) complaintData.get(HttpHeaders.DATE), (String) complaintData.get("ComplaintId"), (String) complaintData.get("CustomerId"), (String) complaintData.get("ClosingDate"), (String) complaintData.get("ClosingTime"),(String) complaintData.get("TechnicianRemark")));
//                                    FragmentReportBinding FragmentReportBinding = FragmentReportBinding.this;
                                    String str4 = complaint;
                                    String str5 = image;
                                    Collections.sort(f21dm, new Comparator<AgentDriverOrderListModel>() {
                                                @Override
                                                public int compare(AgentDriverOrderListModel t1, AgentDriverOrderListModel t2) {
                                                    return (t2.getClosingDate()+t2.getClosingTime()).compareTo(t1.getClosingDate()+t1.getClosingTime());
                                                }
                                    });
                                            myOrderAdapter = new AdminReportListAdapter(getActivity(), f21dm);
                                    mRecyclerView.setAdapter(myOrderAdapter);
                                    myOrderAdapter.notifyDataSetChanged();
                                    showMe.dismiss();
                                } else {
                                    r14 = r0;
                                    Map<String, Object> map2 = complaintData;
                                }
                            } else if (technicianId.equals(technicianId_session)) {
                                String str6 = technicianId;
                                Log.d("str6", str6);
                                String timing = (String) complaintData.get("Timing");
                                String image2 = (String) complaintData.get("Image");
                                String str7 = (String) complaintData.get("StartDate");
                                String str8 = (String) complaintData.get("EndDate");
                                String str9 = (String) complaintData.get("TechnicianId");
                                f21dm.add(new AgentDriverOrderListModel((String) complaintData.get("CustomerName"), (String) complaintData.get("CustomerMobileNumber"), (String) complaintData.get("Complaint"), (String) complaintData.get("Service"), (String) complaintData.get("Status"), image2, timing, (String) complaintData.get(HttpHeaders.DATE), (String) complaintData.get("ComplaintId"), (String) complaintData.get("CustomerId"), (String) complaintData.get("ClosingDate"), (String) complaintData.get("ClosingTime"),(String) complaintData.get("TechnicianRemark")));
//                                FragmentReportBinding FragmentReportBinding2 = FragmentReportBinding.this;
                                String str10 = image2;
                                String str11 = timing;
                                myOrderAdapter = new AdminReportListAdapter(getActivity(), f21dm);
                                mRecyclerView.setAdapter(myOrderAdapter);
                                myOrderAdapter.notifyDataSetChanged();
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
                    showMe.dismiss();
                    r14 = r0;
                    Iterator<DataSnapshot> it5 = it3;
                    DataSnapshot dataSnapshot4 = customerSnapshot2;
                    String str12 = customerId2;
                }
                r14 = r0;
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        this.logincode.equals(ExifInterface.GPS_MEASUREMENT_2D);
        AdminReportListAdapter adminDriverListAdapter = new AdminReportListAdapter(getActivity(), this.f21dm);
        this.myOrderAdapter = adminDriverListAdapter;
        this.mRecyclerView.setAdapter(adminDriverListAdapter);

        binding.rlexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    }
                }

                String currentTime = new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                csvFile = "OASIS/Complaints-" + currentDate +"-"+currentTime+ ".xls";
                createExcelSheet();

                Toast.makeText(getActivity(), "File Saved in OASIS folder in Phone Memory", Toast.LENGTH_LONG).show();
                //Share directly on whats app
//                Uri fileUri = FileProvider.getUriForFile(getActivity(), "com.webapp.oasis", new File(getActivity().getExternalFilesDir("/").getAbsolutePath(), "OASIS.xls"));
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("application/xls");
//                share.putExtra(Intent.EXTRA_STREAM, fileUri);
//                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(share, "Select"));

            }
        });

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it3 = dataSnapshot.getChildren().iterator();
                while (it3.hasNext()) {
                    DataSnapshot customerSnapshot2 = it3.next();
                    Iterator<DataSnapshot> it4 = customerSnapshot2.getChildren().iterator();
                    while (it4.hasNext()) {
                        DataSnapshot complaintSnapshot = it4.next();
                        Object complaintDataObject = complaintSnapshot.getValue();
                        if (complaintDataObject instanceof Map) {
                            Map<String, Object> complaintData = (Map) complaintDataObject;
                            String Status = (String) complaintData.get("Status");
                            if (Status.equals("Done")) {

                                StringBuffer ItemsList2 = new StringBuffer();
                                String CustomerName = (String) complaintData.get("CustomerName");
                                String CustomerMobileNumber = (String) complaintData.get("CustomerMobileNumber");
                                String Complaint = (String) complaintData.get("Complaint");
                                String Date = (String) complaintData.get("Date");
                                String Timing = (String) complaintData.get("Timing");
                                String Service = (String) complaintData.get("Service");
                                String TechnicianName = (String) complaintData.get("TechnicianName");
                                String TechnicianMobile = (String) complaintData.get("TechnicianMobile");
                                String TechnicianEmail = (String) complaintData.get("TechnicianEmail");
                                String ClosingDate = (String) complaintData.get("ClosingDate");
                                String ClosingTime = (String) complaintData.get("ClosingTime");
                                String TotalAmount = (String) complaintData.get("TotalAmount");
                                Log.d("complaintData", complaintData.toString() + "");
                                String ItemsList="";
                                try {
                                    ItemsList= (String) complaintData.get("ItemsListWithoutItemId").toString();
                                    Log.d("ItemsList::", ItemsList + "");
                                }catch (Exception e){
                                    Log.d("Exception",e.toString());
                                }
                                Log.d("ItemsList::", ItemsList + "");

//                                try {
//                                    ItemsList2 = new StringBuffer();
//                                    ItemsList2.append(json);

                                    dm.clear();
//                                    JSONArray applist = new JSONArray(ItemsList);
//                                    if (applist != null && applist.length() > 0) {
//
//                                        for (int i = 0; i < applist.length(); i++) {
//
//                                            JSONObject jsonObject = applist.getJSONObject(i);
//                                            final AdminItemListModel rm = new AdminItemListModel();
//
//                                            ItemsList2.append("[");
//                                            ItemsList2.append("ItemName:" + jsonObject.getString("itemName"));
//                                            ItemsList2.append(", Brand:" + jsonObject.getString("brandName"));
//                                            ItemsList2.append(", Qty:" + jsonObject.getString("qty"));
//                                            ItemsList2.append(", Price:" + jsonObject.getString("price"));
//                                            ItemsList2.append("]    ");
//
//                                            dm.add(rm);
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    Log.d("ExceptionItemList", e.toString());
//                                    showMe.dismiss();
//                                    //      Toast.makeText(CelebrityListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                    e.printStackTrace();
//                                }
                                Log.d("dm", ItemsList2 + "");
                                Log.d("dmString", dm.toString());
                                Log.d("dmarray", dm.toArray().toString());
                                reportComplaintModel.add(new ReportComplaintModel(
                                        CustomerName, CustomerMobileNumber, Complaint, Date, Timing, Service, TechnicianName, TechnicianMobile, TechnicianEmail, ClosingDate,
                                        ClosingTime, TotalAmount, ItemsList.toString()
                                ));
                            }
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return this.binding.getRoot();
    }

    ArrayList<AdminItemListModel> dm = new ArrayList<>();

    ArrayList<ReportComplaintModel> reportComplaintModel = new ArrayList<>();

    File directory, sd, file;
    WritableWorkbook workbook;
    void createExcelSheet() {
        File externalFilesDir = getContext().getExternalFilesDir(null);
        if (externalFilesDir != null) {
            sd = Environment.getExternalStorageDirectory();
            directory = new File(sd.getAbsolutePath());
            file = new File(directory, csvFile);
            try {
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                workbook = Workbook.createWorkbook(file, wbSettings);
                createFirstSheet(workbook);
                //closing cursor
                workbook.write();
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void createFirstSheet(WritableWorkbook workbook) {
        try {
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "CustomerName"));
            sheet.addCell(new Label(1, 0, "CustomerMobileNumber"));
            sheet.addCell(new Label(2, 0, "Complaint"));
            sheet.addCell(new Label(3, 0, "Date"));
            sheet.addCell(new Label(4, 0, "Timing"));
            sheet.addCell(new Label(5, 0, "Service"));
            sheet.addCell(new Label(6, 0, "TechnicianName"));
            sheet.addCell(new Label(7, 0, "TechnicianMobile"));
            sheet.addCell(new Label(8, 0, "TechnicianEmail"));
            sheet.addCell(new Label(9, 0, "ClosingDate"));
            sheet.addCell(new Label(10, 0, "ClosingTime"));
            sheet.addCell(new Label(11, 0, "TotalAmount"));
            sheet.addCell(new Label(12, 0, "ItemsList"));

            for (int i = 0; i < reportComplaintModel.size(); i++) {
                sheet.addCell(new Label(0, i + 1, reportComplaintModel.get(i).getCustomerName()));
                sheet.addCell(new Label(1, i + 1, reportComplaintModel.get(i).getCustomerMobileNumber()));
                sheet.addCell(new Label(2, i + 1, reportComplaintModel.get(i).getComplaint()));
                sheet.addCell(new Label(3, i + 1, reportComplaintModel.get(i).getDate()));
                sheet.addCell(new Label(4, i + 1, reportComplaintModel.get(i).getTiming()));
                sheet.addCell(new Label(5, i + 1, reportComplaintModel.get(i).getService()));
                sheet.addCell(new Label(6, i + 1, reportComplaintModel.get(i).getTechnicianName()));
                sheet.addCell(new Label(7, i + 1, reportComplaintModel.get(i).getTechnicianMobile()));
                sheet.addCell(new Label(8, i + 1, reportComplaintModel.get(i).getTechnicianEmail()));
                sheet.addCell(new Label(9, i + 1, reportComplaintModel.get(i).getClosingDate()));
                sheet.addCell(new Label(10, i + 1, reportComplaintModel.get(i).getClosingTime()));
                sheet.addCell(new Label(11, i + 1, reportComplaintModel.get(i).getTotalAmount()));
                sheet.addCell(new Label(12, i + 1, reportComplaintModel.get(i).getItemsList().toString()));
                Log.d( (i + 1)+"", reportComplaintModel.get(i).getItemsList().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
