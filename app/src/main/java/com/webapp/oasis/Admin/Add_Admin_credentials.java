package com.webapp.oasis.Admin;

import static com.webapp.oasis.R.id.username;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webapp.oasis.R;

import java.util.HashMap;
import java.util.Map;

public class Add_Admin_credentials extends AppCompatActivity {
    EditText username, generate_password;
    Button submit;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_credentials);
        username = findViewById(R.id.username);
        generate_password = findViewById(R.id.generate_password);
        submit = findViewById(R.id.btn_pin_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_technician();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void add_technician() {
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