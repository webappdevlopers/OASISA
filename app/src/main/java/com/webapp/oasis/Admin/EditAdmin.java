package com.webapp.oasis.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webapp.oasis.Admin.technician.EditTechnician;
import com.webapp.oasis.R;
import com.webapp.oasis.databinding.ActivityEditAdminBinding;
import com.webapp.oasis.databinding.FragmentEditTechnicianBinding;

import java.util.HashMap;
import java.util.Map;

public class EditAdmin extends AppCompatActivity {
    ActivityEditAdminBinding binding;
    String AdminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        binding.username.setText(i.getStringExtra("Admin_name"));
        binding.generatePassword.setText(i.getStringExtra("Admin_password"));
        AdminId = i.getStringExtra("Admin_ID");
        binding.btnPinSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_admin();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void edit_admin() {
        final ProgressDialog showMe = new ProgressDialog(EditAdmin.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials");
        Map<String, Object> data = new HashMap<>();
        data.put("UserName", binding.username.getText().toString());
        data.put("Password", binding.generatePassword.getText().toString());
        data.put("isDelete", "false");
        data.put("Admin ID", AdminId);

        myRef.child(AdminId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), binding.username.getText().toString() + " Edited Successfully", Toast.LENGTH_LONG).show();
                    binding.username.setText("");
                    binding.generatePassword.setText("");

                    showMe.dismiss();
                    onBackPressed();
                }
            }
        });
    }
}