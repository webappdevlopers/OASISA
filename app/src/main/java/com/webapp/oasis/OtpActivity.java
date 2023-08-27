package com.webapp.oasis;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.webapp.oasis.Admin.AdmiinHomeScreen;
import com.webapp.oasis.Customer.CustomerHomeActivity;
import com.webapp.oasis.Driver.DriverHomeActivity;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class OtpActivity extends AppCompatActivity {

    Button verify;
    SessionManager session;
    String logincode, statuscode = "1";
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String userid, name, mobile, place, newToken, admincode, driver_id, spin_val1, vehical_no,licence_no;
    ImageView back;
    EditText otp;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private CountDownTimer countDownTimer;
    private TextView tv_coundown, resend;
    String source,destination,licphoto,vehphoto,rcbook,cancelcheck,vehicletype,acc_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        logincode = users.get(session.KEY_LoginCode);

        if (logincode.equals("2")) {
            statuscode = "0";
        }

        tv_coundown =findViewById(R.id.timer);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);

        editText1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText1.getText().toString().length() == 1) {
                    editText1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText2.requestFocus();
                }
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText2.getText().toString().length() == 0) {
                    editText1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText2.getText().toString().length() == 1) {
                    editText2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText3.requestFocus();
                }
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText3.getText().toString().length() == 0) {

                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText3.getText().toString().length() == 1) {
                    editText3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText4.requestFocus();
                }
            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText4.getText().toString().length() == 0) {
                    editText4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText4.getText().toString().length() == 1) {
                    editText4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText5.requestFocus();
                }

            }
        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText5.getText().toString().length() == 0) {
                    editText5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText5.getText().toString().length() == 1) {
                    editText5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText6.requestFocus();
                }
            }
        });
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText6.getText().toString().length() == 0) {
                    editText6.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                    editText5.requestFocus();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable editable) {

                editText6.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bordergrey));
                editText6.setShowSoftInputOnFocus(false);
            }
        });
        otp = (EditText) findViewById(R.id.otp);

        mAuth = FirebaseAuth.getInstance();

        try {
        name = getIntent().getStringExtra("name");
        } catch (Exception e) {
            Log.d("name", String.valueOf(e));
        }
        try {
            newToken = getIntent().getStringExtra("newToken");
        } catch (Exception e) {
            Log.d("newToken", String.valueOf(e));
        }
        try {
            admincode = getIntent().getStringExtra("admincode");
        } catch (Exception e) {
            Log.d("admincode", String.valueOf(e));
        }
        try {
            place = getIntent().getStringExtra("address");
        } catch (Exception e) {
            Log.d("Value", String.valueOf(e));
        }
        try {
            vehical_no = getIntent().getStringExtra("vehical_no");
        } catch (Exception e) {
            Log.d("vehical_no", String.valueOf(e));
        }
        try {
            spin_val1 = getIntent().getStringExtra("spin_val1");
        } catch (Exception e) {
            Log.d("spin_val1", String.valueOf(e));
        }
        try {
            source = getIntent().getStringExtra("source");
        } catch (Exception e) {
            Log.d("Value", String.valueOf(e));
        }
        try {
            destination = getIntent().getStringExtra("destination");
        } catch (Exception e) {
            Log.d("destination", String.valueOf(e));
        }
        try {
            licphoto = getIntent().getStringExtra("licphoto");
        } catch (Exception e) {
            Log.d("licphoto", String.valueOf(e));
        }
        try {
            vehphoto = getIntent().getStringExtra("vehphoto");
        } catch (Exception e) {
            Log.d("vehphoto", String.valueOf(e));
        }
        try {
            rcbook = getIntent().getStringExtra("rcbook");
        } catch (Exception e) {
            Log.d("rcbook", String.valueOf(e));
        }
        try {
            cancelcheck = getIntent().getStringExtra("cancelcheck");
        } catch (Exception e) {
            Log.d("cancelcheck", String.valueOf(e));
        }
        try {
            mobile = getIntent().getStringExtra("mobile");
        } catch (Exception e) {
            Log.d("mobile", String.valueOf(e));
        }
        try {
            vehicletype = getIntent().getStringExtra("vehicletype");
        } catch (Exception e) {
            Log.d("vehicletype", String.valueOf(e));
        }
        try {
            acc_number = getIntent().getStringExtra("acc_number");
        } catch (Exception e) {
            Log.d("acc_number", String.valueOf(e));
        }


        sendVerificationCode(mobile);

        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty() || editText3.getText().toString().isEmpty()
                        || editText4.getText().toString().isEmpty() || editText5.getText().toString().isEmpty() || editText6.getText().toString().isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
                } else {
                    verifyVerificationCode(editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() +
                            editText4.getText().toString() + editText5.getText().toString() + editText6.getText().toString());
                }
            }
        });
        countDownTimer();
    }
    private void countDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 1, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                tv_coundown.setText(text);
            }
            @Override
            public void onFinish() {
                tv_coundown.setText("60 Sec");
          //      Toast.makeText(OtpActivity.this, "Code is Resend !!!", Toast.LENGTH_SHORT).show();
//                resend.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                otp.setText(code);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_id", userid);

                editor.commit();
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            statuscode = "1";

                            if (logincode.equals("1") || logincode.equals("4")) {
                                Intent intent = new Intent(OtpActivity.this, AdmiinHomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else if (logincode.equals("3")) {
                                user_login();
                            } else if (logincode.equals("2")) {
                                driver_login();
                            }

                        } else {

                            Toast.makeText(OtpActivity.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                        }
                    }
                });
    }

    private void user_login() {

        final ProgressDialog showMe = new ProgressDialog(OtpActivity.this);
        showMe.setMessage("Verifying OTP please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.user_login;
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
                                String place1 = j.getString("place");
                                String hash1 = j.getString("hash");

                                //Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();
                                session.createLoginSession(userid1, name1, mobile1, place1, hash1);

                                if (logincode.equals("3")) {

                                    Intent intent = new Intent(OtpActivity.this, CustomerHomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMe.dismiss();
                        NetworkDialog();
                        Log.d("Error", String.valueOf(error));
                        Toast.makeText(OtpActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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


                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("place", place);
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

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(OtpActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                user_login();
            }
        });
        dialogs.show();
    }

    //Driver login

    private void driver_login() {

        final ProgressDialog showMe = new ProgressDialog(OtpActivity.this);
        showMe.setMessage("Verifying OTP please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.driver_login;
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
                                driver_id = j.getString("id");
                                String name1 = j.getString("name");
                                String mobile1 = j.getString("mobile");
                                String place1 = j.getString("source");
                                String hash1 = j.getString("hash");

                                //Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();
                                session.createLoginSession(userid1, name1, mobile1, place1, hash1);
                                Intent intent = new Intent(OtpActivity.this, DriverHomeActivity.class);
                                startActivity(intent);
                                finish();
                                //notification_to_admin();



                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();
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
                        showMe.dismiss();
                        NetworkDialogDriver();
                        Log.d("Error", String.valueOf(error));
                        Toast.makeText(OtpActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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

                Log.d("name", name);
                Log.d("mobile", mobile);
                Log.d("user_type", logincode);
                Log.d("admin_code", admincode);
                Log.d("newToken", newToken);
                Log.d("place", place);
                Log.d("statuscode", statuscode);
                Log.d("admin_id", spin_val1);
//                Log.d("vehical_no", vehical_no);
//                Log.d("licence_no", licence_no);

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("address", place);
                params.put("source", source);
                params.put("admin_id", spin_val1);
                params.put("destination", destination);
                params.put("firebase_id", newToken);
                params.put("licphoto", licphoto);
                params.put("vehphoto", vehphoto);
                params.put("rcbook", rcbook);
                params.put("cancelcheck", cancelcheck);
                params.put("vehicle_type", vehicletype);
                params.put("acc_number", acc_number);

          /*      params.put("vehical_no", vehical_no);
                params.put("licence_no", licence_no);*/

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void notification_to_admin() {

        final ProgressDialog showMe = new ProgressDialog(OtpActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.notification_to_admin;
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

                                    Intent intent = new Intent(OtpActivity.this, DriverHomeActivity.class);
                                    startActivity(intent);
                                    finish();

                            } else {
                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMe.dismiss();
                        NetworkDialogDriver();
                        Log.d("Error", String.valueOf(error));
                        Toast.makeText(OtpActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("driver_id", driver_id);

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void NetworkDialogDriver() {
        final Dialog dialogs = new Dialog(OtpActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                driver_login();
            }
        });
        dialogs.show();
    }
}