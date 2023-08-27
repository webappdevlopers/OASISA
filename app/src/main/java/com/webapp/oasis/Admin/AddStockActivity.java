package com.webapp.oasis.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminStockListAdapter;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.databinding.ActivityAddStockBinding;

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
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AddStockActivity extends AppCompatActivity {
    ActivityAddStockBinding binding;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;

    /* renamed from: dm */
    ArrayList<ItemsModel> f14dm = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    RecyclerView mRecyclerView;
    AdminStockListAdapter myOrderAdapter;
    String csvFile;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddStockBinding inflate = ActivityAddStockBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        this.firebaseAuth = FirebaseAuth.getInstance();
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


        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuffer ItemsList2 = new StringBuffer();
                    AddStockActivity.this.f14dm.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String itemName = (String) itemSnapshot.child("ItemName").getValue(String.class);
                        String brandName = (String) itemSnapshot.child("BrandName").getValue(String.class);
                        String qty = (String) itemSnapshot.child("Qty").getValue(String.class);
                        String price = (String) itemSnapshot.child("Price").getValue(String.class);
                        Collections.sort(AddStockActivity.this.f14dm, new Comparator<ItemsModel>() {
                            @Override
                            public int compare(ItemsModel t1, ItemsModel t2) {
                                return (t2.getBrandName()).compareTo(t1.getBrandName());
                            }
                        });
                        AddStockActivity.this.f14dm.add(new ItemsModel(itemName, brandName, qty, price, (String) itemSnapshot.child("ItemID").getValue(String.class)));
                        AddStockActivity addStockActivity = AddStockActivity.this;
                        addStockActivity.myOrderAdapter = new AdminStockListAdapter(addStockActivity.getApplicationContext(), AddStockActivity.this.f14dm);
                        AddStockActivity.this.mRecyclerView.setAdapter(AddStockActivity.this.myOrderAdapter);
                        AddStockActivity.this.myOrderAdapter.notifyDataSetChanged();
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

        binding.rlexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    }
                }
                String currentTime = new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                csvFile = "OASIS/StockReport-" + currentDate +"-"+currentTime+ ".xls";
                createExcelSheet();

                Toast.makeText(AddStockActivity.this, "File Saved in OASIS folder in Phone Memory", Toast.LENGTH_LONG).show();

                //share direcly on whats app
//                Uri fileUri = FileProvider.getUriForFile(AddStockActivity.this, "com.webapp.oasis", new File(getApplicationContext().getExternalFilesDir("/").getAbsolutePath(), csvFile));
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("application/xls");
//                share.putExtra(Intent.EXTRA_STREAM, fileUri);
//                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(share, "Select"));
            }
        });
        this.binding.submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AddStockActivity.this.binding.edititemname.getText().toString().isEmpty()) {
                    Toast.makeText(AddStockActivity.this, "Enter Item Name", 0).show();
                } else if (AddStockActivity.this.binding.brandName.getText().toString().isEmpty()) {
                    Toast.makeText(AddStockActivity.this, "Enter Brand Name", 0).show();
                } else if (AddStockActivity.this.binding.qty.getText().toString().isEmpty()) {
                    Toast.makeText(AddStockActivity.this, "Enter Qty", 0).show();
                } else if (AddStockActivity.this.binding.price.getText().toString().isEmpty()) {
                    Toast.makeText(AddStockActivity.this, "Enter Price", 0).show();
                } else {
                    AddStockActivity.this.additem();
                }
            }
        });
        this.binding.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddStockActivity.this.onBackPressed();
            }
        });
    }

    public void additem() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        final String itemnameValue = this.binding.edititemname.getText().toString();
        String brandName = this.binding.brandName.getText().toString();
        String Qty = this.binding.qty.getText().toString();
        String price = this.binding.price.getText().toString();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS");
        Map<String, Object> data = new HashMap<>();
        data.put("ItemName", itemnameValue);
        data.put("BrandName", brandName);
        data.put("Qty", Qty);
        data.put("Price", price);
        String itemId = myRef.push().getKey();
        data.put("ItemID", itemId);
        myRef.child(itemId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    AddStockActivity.this.binding.edititemname.setText("");
                    AddStockActivity.this.binding.price.setText("");
                    AddStockActivity.this.binding.qty.setText("");
                    AddStockActivity.this.binding.brandName.setText("");
                    showMe.dismiss();
                    AddStockActivity addStockActivity = AddStockActivity.this;
                    Toast.makeText(addStockActivity, itemnameValue + " Added SuccessFully", 0).show();
                }
            }
        });
    }

    File directory, sd, file;
    WritableWorkbook workbook;

    void createExcelSheet() {

        sd = Environment.getExternalStorageDirectory();
        directory = new File(sd.getAbsolutePath());
        file = new File(directory, csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            createFirstSheet();
            //closing cursor
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createFirstSheet() {
        try {
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "ItemName"));
            sheet.addCell(new Label(1, 0, "BrandName"));
            sheet.addCell(new Label(2, 0, "Total Qty"));
            sheet.addCell(new Label(3, 0, "Price"));

            for (int i = 0; i <  AddStockActivity.this.f14dm.size(); i++) {
                sheet.addCell(new Label(0, i + 1,  AddStockActivity.this.f14dm.get(i).getItemName()));
                sheet.addCell(new Label(1, i + 1,  AddStockActivity.this.f14dm.get(i).getBrandName()));
                sheet.addCell(new Label(2, i + 1,  AddStockActivity.this.f14dm.get(i).getQty()));
                sheet.addCell(new Label(3, i + 1,  AddStockActivity.this.f14dm.get(i).getPrice()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
