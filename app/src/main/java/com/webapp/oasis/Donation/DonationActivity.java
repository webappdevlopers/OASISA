package com.webapp.oasis.Donation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class DonationActivity extends AppCompatActivity implements PaymentResultListener {
    Button btn_pay;
    SessionManager session;
    EditText edtamount;
    String user_id, amount = "0";

    StringRequest stringRequest;
    RequestQueue mRequestQueue;

    ImageView back;
    String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        session = new SessionManager(DonationActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);


        edtamount = findViewById(R.id.edtamount);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btn_pay = findViewById(R.id.btn_pay);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startPayment();
                if (edtamount.getText().toString().isEmpty()) {
                    Toast.makeText(DonationActivity.this, "Amount is empty", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(edtamount.getText().toString()) < 1) {
                    Toast.makeText(DonationActivity.this, "Minimum Amount is 1 Rs", Toast.LENGTH_LONG).show();
                } else {
                    startPayment();
                }
            }
        });
    }

    public void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "OASIS GLOBE App");
            options.put("description", "Donation");
            options.put("currency", "INR");
            //      String payment = "1000000";
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total;// = Double.parseDouble(payment);
            total = Double.parseDouble(edtamount.getText().toString()) * 100;

            Log.d("Amount", String.valueOf(total));
            Log.d("Amount 1 ", amount);

            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    String transactionid;

    @Override
    public void onPaymentSuccess(String s) {
        // payment successfull pay_DGU19rDsInjcF2
        Log.e("TAG", " payment successfull " + s.toString());
        //Toast.makeText(this, "Payment successfully done! " + s, Toast.LENGTH_SHORT).show();

        transactionid = s;
        session.createamoutsession(edtamount.getText().toString());
//        submitAction();
        add_more_credits();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }


    private void NetworkDialog1() {
        final Dialog dialogs = new Dialog(DonationActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                add_more_credits();
            }
        });
        dialogs.show();
    }

    public void add_more_credits() {

        final ProgressDialog showMe = new ProgressDialog(DonationActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.donation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {

                                //  Toast.makeText(GotoPremiumActivity.this, msg, Toast.LENGTH_LONG).show();
                                submitAction();
                                //startActivity(new Intent(GotoPremiumActivity.this, TabMainAPF.class));

                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(DonationActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        volleyError.printStackTrace();
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            // Is thrown if there's no network connection or server is down
                            // We return to the last fragment
                            Log.d("eerror", String.valueOf(volleyError));
                            NetworkDialog1();
                            if (getFragmentManager().getBackStackEntryCount() != 0) {
                                getFragmentManager().popBackStack();
                            }

                        } else {
                            // Is thrown if there's no network connection or server is down
                            Log.d("eerror", String.valueOf(volleyError));
                            NetworkDialog1();
                            // We return to the last fragment
                            if (getFragmentManager().getBackStackEntryCount() != 0) {
                                getFragmentManager().popBackStack();
                            }
                        }
                        showMe.dismiss();

//                        Toast.makeText(A1_LoginActivity.this,"Error:"+ volleyError, Toast.LENGTH_SHORT).show();
                        //    Toast.makeText(A1_LoginActivity.this, "No Connection", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", user_id);
                params.put("amount", edtamount.getText().toString());
                params.put("tran_id", transactionid);
                params.put("description", "-");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DonationActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void submitAction() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogPaymentSuccess();
            }
        }, 1000);
    }

    private void showDialogPaymentSuccess() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogPaymentSuccessFragment newFragment = new DialogPaymentSuccessFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
