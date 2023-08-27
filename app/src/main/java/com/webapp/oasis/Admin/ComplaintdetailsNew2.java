package com.webapp.oasis.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.SelectItemListAdapter;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.ReportComplaintModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Technician.technicianHomeActivity;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityComplaintdetails2Binding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ComplaintdetailsNew2 extends AppCompatActivity {
    String CustomerId;

    ActivityComplaintdetails2Binding binding;
    SelectItemListAdapter.CartListener cartListener;
    ArrayList<AdminItemListModel> f18dm = new ArrayList<>();
    public RecyclerView mRecyclerView;
    SelectItemListAdapter myOrderAdapter;
    SessionManager session;
    String technicianid;
    LinearLayout llproceed;
    DatabaseHelper mydb;
    public static TextView tltitem;
    public static TextView tltitemcost;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComplaintdetails2Binding inflate = ActivityComplaintdetails2Binding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.technicianid = users.get(SessionManager.KEY_TecnicianID);
        this.CustomerId = users.get(SessionManager.CustomerId);

        this.mydb = new DatabaseHelper(ComplaintdetailsNew2.this);
        this.mydb.delete();

        tltitem = findViewById(R.id.tltitem);
        tltitemcost = findViewById(R.id.tltitemcost);
        llproceed = findViewById(R.id.llproceed);
        llproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tltitem.getText().toString().equals("0 Items Added")) {
                    Toast.makeText(ComplaintdetailsNew2.this, "Please Select Item", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintdetailsNew2.this);
                    builder.setMessage("Collected Amount - "+tltitemcost.getText().toString()+" Rs.\nAre you sure want to Close Call ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    place_order();
                                    showCustomDialog();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        this.mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecyclerrrr);
//        this.mRecyclerView = recyclerView;
        this.mRecyclerView.setHasFixedSize(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SelectItemListAdapter selectItemListAdapter = new SelectItemListAdapter(ComplaintdetailsNew2.this, ComplaintdetailsNew2.this.f18dm, cartListener);
        this.myOrderAdapter = selectItemListAdapter;
        this.mRecyclerView.setAdapter(selectItemListAdapter);
        this.myOrderAdapter.notifyDataSetChanged();

        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ComplaintdetailsNew2.this.f18dm.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String itemName = (String) itemSnapshot.child("ItemName").getValue(String.class);
                        String brandName = (String) itemSnapshot.child("BrandName").getValue(String.class);
                        String qty = (String) itemSnapshot.child("Qty").getValue(String.class);
                        String price = (String) itemSnapshot.child("Price").getValue(String.class);
                        ComplaintdetailsNew2.this.f18dm.add(new AdminItemListModel(itemName, brandName, qty, price,qty, (String) itemSnapshot.child("ItemID").getValue(String.class)));
                        ComplaintdetailsNew2 complaintdetails = ComplaintdetailsNew2.this;
                        myOrderAdapter = new SelectItemListAdapter(ComplaintdetailsNew2.this, ComplaintdetailsNew2.this.f18dm, cartListener);
                        mRecyclerView.setAdapter(ComplaintdetailsNew2.this.myOrderAdapter);
                        mRecyclerView.getRecycledViewPool().clear();
                        myOrderAdapter.notifyDataSetChanged();
                        Log.d("TAG", "Item Name: " + itemName + ", Brand Name: " + brandName + ", Qty: " + qty + ", Price: " + price);
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

        Intent intent = getIntent();
//         user_details= intent.getStringExtra("mylist");
         ComplaintId= intent.getStringExtra("ComplaintId");
         CustomerIds= intent.getStringExtra("CustomerId");



    }
    String user_details;
    String ComplaintId;
    String CustomerIds;

    private void place_order() {
        List<AdminItemListModel> list = new ArrayList<>();
        list.addAll(mydb.getItemData());
        List<AdminItemListModel> listWithoutItemId = new ArrayList<>();
        for (int i= 0; i< list.size(); i++){
            AdminItemListModel emodel = new AdminItemListModel(list.get(i).getBrandName(),list.get(i).getItemName(),
                    list.get(i).getPrice(), list.get(i).getQty());
            listWithoutItemId.add(emodel);
        }
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("ItemsListWithoutItemId").setValue(listWithoutItemId);

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("ItemsList").setValue(list);

        for (int i= 0; i< list.size(); i++){
            FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS/")
                    .child(list.get(i).getItemID())
                    .child("Qty").setValue((Integer.parseInt(list.get(i).getItemQty()) - Integer.parseInt(list.get(i).getQty()))+"");
        }
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("Status").setValue("Done");
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String date1 = t.format("%Y/%m/%d");

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String var = dateFormat.format(date);
        String horafecha = var+ " - " + date1;

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("ClosingDate").setValue(date1+"");
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("ClosingTime").setValue(var+"");

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                .child(CustomerIds)
                .child(ComplaintId)
                .child("TotalAmount").setValue(tltitemcost.getText().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ComplaintdetailsNew2.this, technicianHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        Date currentTime = Calendar.getInstance().getTime();
        String s = String.valueOf(currentTime);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/OASIS.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    @SuppressLint("NewApi")
    private boolean checkIfAlreadyhavePermission() {

        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        }
        return false;
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String review = et_post.getText().toString();

                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Remark", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ComplaintdetailsNew2.this, "Remark Added Sucessfully", Toast.LENGTH_SHORT).show();
                    int secondsDelayed = 1;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Complaint/")
                                    .child(CustomerIds)
                                    .child(ComplaintId)
                                    .child("TechnicianRemark").setValue(review+"");
                            Intent intent = new Intent(ComplaintdetailsNew2.this, technicianHomeActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }, secondsDelayed * 2000);
                }
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
