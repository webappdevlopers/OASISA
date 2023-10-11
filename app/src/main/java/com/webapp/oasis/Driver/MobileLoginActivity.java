package com.webapp.oasis.Driver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.Notification.Config1;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

import static android.Manifest.permission_group.CAMERA;

public class MobileLoginActivity extends AppCompatActivity {


    Button signin;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    SessionManager session;
    EditText phonenumber;
    String newToken = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        session = new SessionManager(getApplicationContext());


        phonenumber = findViewById(R.id.phonenumber);

        signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (phonenumber.getText().toString().isEmpty() || phonenumber.length() != 10) {
                    Toast.makeText(MobileLoginActivity.this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    driver_login();
                }
            }
        });


        requestPermission();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MobileLoginActivity.this, new String[]
                {CAMERA, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }


    private void driver_login() {

        final ProgressDialog showMe = new ProgressDialog(MobileLoginActivity.this);
        showMe.setMessage("Verifying OTP please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.driver_logoin_verification;
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();
                        Log.d("Login Server Response", ServerResponse);

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {

                                String userid1 = j.getString("id");
                                String name1 = j.getString("name");
                                String mobile1 = j.getString("mobile");
                                String place1 = j.getString("source");
                                String hash1 = j.getString("hash");

                                session.createLoginSession(userid1, name1, mobile1, place1, hash1);
                                Intent intent = new Intent(MobileLoginActivity.this, DriverHomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(MobileLoginActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        driver_login();
                        showMe.dismiss();
                        Log.d("Error", String.valueOf(error));
//                        Toast.makeText(MobileLoginActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("mobtoken",newToken );

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("mobile", phonenumber.getText().toString());
                params.put("firebase_id", newToken);


                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }
}