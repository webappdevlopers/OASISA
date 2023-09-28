package com.webapp.oasis.Admin;

import static com.webapp.oasis.R.id.username;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminListAdapter;
import com.webapp.oasis.Admin.Adapter.AdminServiceListAdapter;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Admin_credentials extends AppCompatActivity {
    EditText username, generate_password;
    Button submit;
    RecyclerView recyclerView;

    ImageView back;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ItemsModel> f16dm = new ArrayList<>();
    AdminListAdapter myOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_credentials);
        username = findViewById(R.id.username);
        back = findViewById(R.id.back);
        generate_password = findViewById(R.id.generate_password);

        submit = findViewById(R.id.btn_pin_submit);
        recyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    f16dm.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        try {
                            String isDelete = (String) itemSnapshot.child("isDelete").getValue(String.class);
                            if (itemSnapshot.child("isDelete").exists()) {
                                if (isDelete.equals("false")) {
                                    f16dm.add(new ItemsModel((String) itemSnapshot.child("UserName").getValue(String.class), (String) itemSnapshot.child("Password").getValue(String.class), (String) itemSnapshot.child("Admin ID").getValue(String.class), (String) null, (String) null));
                                }
                            } else {
                                f16dm.add(new ItemsModel((String) itemSnapshot.child("UserName").getValue(String.class), (String) itemSnapshot.child("Password").getValue(String.class), (String) itemSnapshot.child("Admin ID").getValue(String.class), (String) null, (String) null));
                            }
                        }catch (Exception e){
                            f16dm.add(new ItemsModel((String) itemSnapshot.child("UserName").getValue(String.class), (String) itemSnapshot.child("Password").getValue(String.class), (String) itemSnapshot.child("Admin ID").getValue(String.class), (String) null, (String) null));
                        }
                        myOrderAdapter = new AdminListAdapter(getApplicationContext(), f16dm);
                        recyclerView.setAdapter(myOrderAdapter);
                        myOrderAdapter.notifyDataSetChanged();
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
        swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_AddMin();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void add_AddMin() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials");
        Map<String, Object> data = new HashMap<>();
        data.put("UserName", username.getText().toString());
        data.put("Password", generate_password.getText().toString());
        String AdminId = myRef.push().getKey();
        data.put("Admin ID", AdminId);
        data.put("isDelete", "false");

        myRef.child(AdminId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Add_Admin_credentials.this, "Admin added successfully", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    generate_password.setText("");
                    showMe.dismiss();
                }
            }
        });
    }

}