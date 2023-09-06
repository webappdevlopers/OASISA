package com.webapp.oasis.Retailer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class RetailerOtpActivity extends AppCompatActivity {

    Button verify;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;

    ImageView back;
    EditText otp;
    private FirebaseAuth mAuth;
    private String mVerificationId;

    String userid1,shop_name,name1,email,gst_no,shop_address,shop_image,retailer_status,hash,retailer_user_type,mobile1;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        session = new SessionManager(getApplicationContext());

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


        userid1=getIntent().getStringExtra("userid1");
        shop_name=getIntent().getStringExtra("shop_name");
        name1=getIntent().getStringExtra("name1");
        email=getIntent().getStringExtra("email");
        gst_no=getIntent().getStringExtra("gst_no");
        shop_address=getIntent().getStringExtra("shop_address");
        shop_image=getIntent().getStringExtra("shop_image");
        retailer_status=getIntent().getStringExtra("retailer_status");
        hash=getIntent().getStringExtra("hash");
        retailer_user_type=getIntent().getStringExtra("retailer_user_type");
        mobile1=getIntent().getStringExtra("mobile1");

        //sendVerificationCode(getIntent().getStringExtra("mobile1"));

        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty() || editText3.getText().toString().isEmpty()
                        || editText4.getText().toString().isEmpty() || editText5.getText().toString().isEmpty() || editText6.getText().toString().isEmpty()) {
                    Toast.makeText(RetailerOtpActivity.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
                } else {
                    verifyVerificationCode(editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() +
                            editText4.getText().toString() + editText5.getText().toString() + editText6.getText().toString());
                }
            }
        });
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                otp.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(RetailerOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                .addOnCompleteListener(RetailerOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            session.createLoginSessionRetailer(userid1, shop_name, name1, mobile1, email, gst_no, shop_address, shop_image, retailer_status, hash, retailer_user_type);

                            Intent intent = new Intent(RetailerOtpActivity.this, RetailerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();


                        } else {

                            Toast.makeText(RetailerOtpActivity.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                        }
                    }
                });
    }
}