package com.webapp.oasis.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.Donation.DialogPaymentSuccessFragment;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class PaymentCustomerActivity extends AppCompatActivity implements PaymentResultListener {

    String amount,order_id;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String user_id, hash;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_customer);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);


        amount = getIntent().getStringExtra("amount");
        order_id = getIntent().getStringExtra("order_id");

        startPayment();
    }

    String transactionid;

    @Override
    public void onPaymentSuccess(String s) {

        transactionid = s;
        // payment successfull pay_DGU19rDsInjcF2
        Log.e("TAG", " payment successfull " + s.toString());
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        //tvview.setText("");
        payment();

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

    public void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "OASIS GLOBE");
            options.put("description", "Parcel Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            double total;// = Double.parseDouble(payment);
            total = Double.parseDouble(amount) * 100;

            Log.d("Amount", String.valueOf(total));
            Log.d("Amount 1 ", amount);

            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("contact", "");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void payment() {

        final ProgressDialog showMe = new ProgressDialog(PaymentCustomerActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
            String url = Config.payment;
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();
                        Log.d("payment response", ServerResponse);

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {
                                session.createamoutsession(amount);

                                submitAction();
                          /*      Intent intent = new Intent(PaymentCustomerActivity.this, CustomerOrderDetailActivity.class);
                                startActivity(intent);
*/
                                //Toast.makeText(PaymentCustomerActivity.this, "Vehicle Added", Toast.LENGTH_LONG).show();

                            }
                            else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(PaymentCustomerActivity.this, msg, Toast.LENGTH_LONG).show();
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
                        Log.d("Error", String.valueOf(error));
                        Toast.makeText(PaymentCustomerActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                        NetworkDialog();
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

                Log.d("Payment Data",user_id+" "+order_id+" "+amount+" "+transactionid+" "+hash);
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");

                params.put("user_id", user_id);
                params.put("hash", hash);
                params.put("order_id", order_id);
                params.put("paid_amount", amount);
                params.put("trans_id", transactionid);

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(PaymentCustomerActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                payment();
            }
        });
        dialogs.show();
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

}
