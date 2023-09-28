package com.webapp.oasis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.logging.type.LogSeverity;
import com.webapp.oasis.Admin.AdminHomePage;
import com.webapp.oasis.Customer.CustomerHomeActivity;
import com.webapp.oasis.Customer.CustomerPlaceOrderActivity;
import com.webapp.oasis.Driver.MobileLoginActivity;
import com.webapp.oasis.Model.TechnicianCredentiansModel;
import com.webapp.oasis.Notification.Config1;
import com.webapp.oasis.Retailer.RetailerOtpActivity;
import com.webapp.oasis.Technician.technicianHomeActivity;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityLoginBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import id.zelory.compressor.Compressor;


public class LoginActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    String Mobile;
    String Technician_ID;
    String Technician_Password;
    /* access modifiers changed from: private */
    public ArrayList<TechnicianCredentiansModel> Techniciandetails = new ArrayList<>();
    EditText acc_number;
    EditText address;
    ActivityLoginBinding binding;
    Bitmap bitmap;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    LinearLayout driverlayout;
    Button driverlogin;
    Uri filePath;
    /* access modifiers changed from: private */
    public ArrayList<String> flatids1 = new ArrayList<>();
    private ArrayList<String> flatidsD = new ArrayList<>();
    private ArrayList<String> flatidsS = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> flats1 = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> flatsD = new ArrayList<>();
    GoogleSignInAccount googleSignInAccount;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String imagePath4;
    String imageUrl;
    String imgs1 = "";
    String imgs2 = "";
    String imgs3 = "";
    String imgs4 = "";
    boolean isInvalid = false;
    EditText licence_no;
    LinearLayout lladdress;
    LinearLayout lladminId;
    LinearLayout lladminpassword;
    String login_from;
    String logincode;
    private FirebaseAuth mAuth;
    String mCurrentPhotoPath;
    String mCurrentPhotoPath1;
    String mCurrentPhotoPath2;
    String mCurrentPhotoPath3;
    String mCurrentPhotoPath4;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RequestQueue mRequestQueue;
    private Spinner mySpinner;
    private Spinner mySpinnerD;
    private Spinner mySpinnerS;
    private Spinner mySpinnerV;
    EditText name;
    String newToken = null;
    EditText password;
    EditText phonenumber;
    SessionManager session;
    Button signin;
    String spin_val1;
    String spin_val2;
    String spin_valD = "null";
    String spin_valS = "null";
    String spin_valV = "null";
    private Spinner spnprototype;
    FirebaseStorage storage;
    StorageReference storageReference;
    StringRequest stringRequest;
    TextView tvdemo;
    EditText vehical_no;
    private ArrayList<String> vehicleid = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> vehiclename = new ArrayList<>();
    public static String userType;
    String Admin_id;
    String isdelete;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding inflate = ActivityLoginBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        this.mAuth = FirebaseAuth.getInstance();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        this.logincode = sessionManager.getUserDetails().get(SessionManager.KEY_LoginCode);
        this.googleSignInAccount = (GoogleSignInAccount) getIntent().getParcelableExtra(LoginFirstScreen.GOOGLE_ACCOUNT);
        this.login_from = getIntent().getStringExtra("Login_From");
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;
        this.storageReference = instance.getReference();
        this.driverlogin = (Button) findViewById(R.id.driverlogin);
        this.vehical_no = (EditText) findViewById(R.id.vehical_no);
        this.licence_no = (EditText) findViewById(R.id.licence_no);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lladminId);
        this.lladminId = linearLayout;
        linearLayout.setVisibility(View.GONE);
        this.driverlayout = (LinearLayout) findViewById(R.id.driverlayout);
        this.tvdemo = (TextView) findViewById(R.id.tvdemo);
        this.lladdress = (LinearLayout) findViewById(R.id.lladdress);
        this.lladminpassword = (LinearLayout) findViewById(R.id.lladminpassword);
        this.signin = (Button) findViewById(R.id.signin);
        this.acc_number = (EditText) findViewById(R.id.acc_number);
        this.password = (EditText) findViewById(R.id.password);
        this.name = (EditText) findViewById(R.id.name);
        this.phonenumber = (EditText) findViewById(R.id.phonenumber);
        this.address = (EditText) findViewById(R.id.etadress);
        this.driverlayout.setVisibility(View.GONE);
        if (this.login_from.equalsIgnoreCase("Admin")) {
            this.tvdemo.setText("Admin Login");
            this.signin.setText("SIGN IN");
        } else if (this.login_from.equalsIgnoreCase("Technician")) {
            this.tvdemo.setText("Technician Login");
            this.signin.setText("SIGN IN");
        } else if (this.login_from.equalsIgnoreCase("Customer")) {
            this.tvdemo.setText("Customer SignUp");
            this.lladminpassword.setVisibility(View.GONE);
            this.binding.llAlternateMobileNumber.setVisibility(View.VISIBLE);
            this.lladdress.setVisibility(View.VISIBLE);
            this.binding.llMake.setVisibility(View.VISIBLE);
            this.binding.llArea.setVisibility(View.VISIBLE);
            this.binding.llUploadImage.setVisibility(View.VISIBLE);
            this.binding.lladdress.setVisibility(View.VISIBLE);
            this.binding.llPincode.setVisibility(View.VISIBLE);
            this.binding.llModelNumber.setVisibility(View.VISIBLE);
        } else if (this.login_from.equalsIgnoreCase("Super Admin")) {
            this.lladdress.setVisibility(View.GONE);
            this.signin.setText("SIGN IN");
            this.tvdemo.setText("Super Admin Login");
        }
        this.driverlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MobileLoginActivity.class));
            }
        });
        this.signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LoginActivity.this.login_from.equalsIgnoreCase("Super Admin")) {

                    if (LoginActivity.this.phonenumber.getText().toString().isEmpty() || LoginActivity.this.phonenumber.length() != 10
                    ) {
                        Toast.makeText(LoginActivity.this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (LoginActivity.this.binding.password.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    } else {

                        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("SuperAdmin/SuperAdmin Credentials").addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        DataSnapshot itemSnapshot = it.next();
                                        String UserName = (String) itemSnapshot.child("UserName").getValue(String.class);
                                        String Password = (String) itemSnapshot.child("Password").getValue(String.class);
                                            Techniciandetails.add(new TechnicianCredentiansModel(UserName, Password));
                                            if (LoginActivity.this.binding.phonenumber.getText().toString().equalsIgnoreCase(UserName) && LoginActivity.this.binding.password.getText().toString().equalsIgnoreCase(Password)) {
                                                LoginActivity.this.isInvalid = true;
                                                break;
                                            }

                                    }
                                    if (LoginActivity.this.isInvalid) {
                                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, AdminHomePage.class));
                                        userType = "Super Admin";
                                        LoginActivity.this.session.LoginCode("6");

                                       // LoginActivity.this.session.createLoginSessionAdmin(Admin_id);
                                        LoginActivity.this.finish();
                                        return;

                                    }

                                    Toast.makeText(LoginActivity.this, "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(LoginActivity.this, "Please Ask Admin to Generate Password", Toast.LENGTH_SHORT).show();
                            }

                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TAG", "Failed to read value.", databaseError.toException());
                            }
                        });
                    }

                } else if (LoginActivity.this.login_from.equalsIgnoreCase("Admin")) {
                    if (LoginActivity.this.phonenumber.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (LoginActivity.this.binding.password.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials").addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        DataSnapshot itemSnapshot = it.next();
                                        String UserName = (String) itemSnapshot.child("UserName").getValue(String.class);
                                        String Password = (String) itemSnapshot.child("Password").getValue(String.class);
                                        Admin_id = (String) itemSnapshot.child("Admin ID").getValue(String.class);
                                        isdelete = (String) itemSnapshot.child("isDelete").getValue(String.class);
                                        Techniciandetails.add(new TechnicianCredentiansModel(UserName, Password));
                                        if (LoginActivity.this.binding.phonenumber.getText().toString().equalsIgnoreCase(UserName) && LoginActivity.this.binding.password.getText().toString().equalsIgnoreCase(Password)) {
                                            LoginActivity.this.isInvalid = true;
                                            break;
                                        }
                                    }
                                    try {
                                        if (isdelete.equals("false")) {
                                            if (LoginActivity.this.isInvalid) {
                                                LoginActivity.this.startActivity(new Intent(LoginActivity.this, AdminHomePage.class));
                                                userType = "Admin";
                                                LoginActivity.this.session.LoginCode("5");
                                                LoginActivity.this.session.createLoginSessionAdmin(Admin_id);
                                                LoginActivity.this.finish();
                                                return;
                                            }
                                        }
                                    }catch (Exception e){
                                        if (LoginActivity.this.isInvalid) {
                                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, AdminHomePage.class));
                                            userType = "Admin";
                                            LoginActivity.this.session.LoginCode("5");
                                            LoginActivity.this.session.createLoginSessionAdmin(Admin_id);
                                            LoginActivity.this.finish();
                                            return;
                                        }
                                    }


                                    Toast.makeText(LoginActivity.this, "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(LoginActivity.this, "Please Ask Admin to Generate Password", Toast.LENGTH_SHORT).show();
                            }

                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TAG", "Failed to read value.", databaseError.toException());
                            }
                        });


                    }
                } else if (LoginActivity.this.login_from.equalsIgnoreCase("Technician")) {
                    FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    DataSnapshot itemSnapshot = it.next();
                                    String str = (String) itemSnapshot.child("Name").getValue(String.class);
                                    LoginActivity.this.Mobile = (String) itemSnapshot.child("Mobile").getValue(String.class);
                                    LoginActivity.this.Technician_Password = (String) itemSnapshot.child("Technician Password").getValue(String.class);
                                    isdelete = (String) itemSnapshot.child("isDelete").getValue(String.class);
                                    String str2 = (String) itemSnapshot.child("email").getValue(String.class);
                                    String str3 = (String) itemSnapshot.child("AdhaarCard").getValue(String.class);
                                    String str4 = (String) itemSnapshot.child("License").getValue(String.class);
                                    LoginActivity.this.Technician_ID = (String) itemSnapshot.child("Technician ID").getValue(String.class);
                                    LoginActivity.this.Techniciandetails.add(new TechnicianCredentiansModel(LoginActivity.this.Mobile, LoginActivity.this.Technician_Password));
                                    if (LoginActivity.this.binding.phonenumber.getText().toString().equalsIgnoreCase(LoginActivity.this.Mobile) && LoginActivity.this.binding.password.getText().toString().equalsIgnoreCase(LoginActivity.this.Technician_Password)) {
                                        LoginActivity.this.isInvalid = true;
                                        break;
                                    }
                                }

                                try{
                                    if(!isdelete.equals("true")){
                                        if (LoginActivity.this.isInvalid) {
                                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, technicianHomeActivity.class));
                                            LoginActivity.this.finish();
                                            LoginActivity.this.session.LoginCode(ExifInterface.GPS_MEASUREMENT_2D);
                                            LoginActivity.this.session.createLoginSessionTecnician(LoginActivity.this.Technician_ID);
                                            return;
                                        }
                                    }
                                }catch (Exception e){
                                    if (LoginActivity.this.isInvalid) {
                                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, technicianHomeActivity.class));
                                        LoginActivity.this.finish();
                                        LoginActivity.this.session.LoginCode(ExifInterface.GPS_MEASUREMENT_2D);
                                        LoginActivity.this.session.createLoginSessionTecnician(LoginActivity.this.Technician_ID);
                                        return;
                                    }
                                }
                                Toast.makeText(LoginActivity.this, "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(LoginActivity.this, "Please Ask Admin to Generate Password", Toast.LENGTH_SHORT).show();
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("TAG", "Failed to read value.", databaseError.toException());
                        }
                    });
                } else if (!LoginActivity.this.login_from.equalsIgnoreCase("Customer")) {

                } else {
                    if (LoginActivity.this.phonenumber.getText().toString().isEmpty() || LoginActivity.this.phonenumber.length() != 10) {
                        Toast.makeText(LoginActivity.this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (LoginActivity.this.binding.etadress.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                    } else if (LoginActivity.this.binding.etArea.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter Area", Toast.LENGTH_SHORT).show();
                    } else if (LoginActivity.this.binding.etPincode.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        final ProgressDialog showMe = new ProgressDialog(LoginActivity.this);
                        showMe.setMessage("Please wait");
                        showMe.setCancelable(true);
                        showMe.setCanceledOnTouchOutside(false);
                        showMe.show();
                        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration");
                        Map<String, Object> data = new HashMap<>();
                        data.put("MobileNumber", LoginActivity.this.binding.phonenumber.getText().toString());
                        data.put("AlternateMobileNumber", LoginActivity.this.binding.anotherMobileNumber.getText().toString());
                        data.put("Address", LoginActivity.this.binding.etadress.getText().toString());
                        data.put("Area", LoginActivity.this.binding.etArea.getText().toString());
                        data.put("Pincode", LoginActivity.this.binding.etPincode.getText().toString());
                        data.put("MachineMake", LoginActivity.this.binding.etMake.getText().toString());
                        data.put("Machine Model", LoginActivity.this.binding.etModelNo.getText().toString());
                        data.put("Email", LoginActivity.this.googleSignInAccount.getEmail());
                        data.put("Name", LoginActivity.this.googleSignInAccount.getDisplayName());
                        if (LoginActivity.this.googleSignInAccount.getPhotoUrl() != null) {
                            data.put("ProfileImage", LoginActivity.this.googleSignInAccount.getPhotoUrl().toString());
                        }
                        data.put("RoImage", LoginActivity.this.imageUrl);
                        myRef.child(LoginActivity.this.googleSignInAccount.getId()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(Oscillator.TAG, "Data uploaded successfully");
                                    showMe.dismiss();
                                    LoginActivity.this.session.LoginCode(ExifInterface.GPS_MEASUREMENT_3D);
                                    LoginActivity.this.session.useridsession(LoginActivity.this.googleSignInAccount.getId());
                                    LoginActivity.this.session.createLoginSession((String) null, LoginActivity.this.googleSignInAccount.getDisplayName(), LoginActivity.this.binding.phonenumber.getText().toString(), "", "");
                                    Intent intent = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                                    intent.putExtra(LoginFirstScreen.GOOGLE_ACCOUNT, LoginActivity.this.googleSignInAccount);
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                    return;
                                }
                                Log.d(Oscillator.TAG, "Data upload failed");
                            }
                        });
                    }
                }
            }
        });

        //displayFirebaseRegId();
        this.mySpinner = (Spinner) findViewById(R.id.spngender);
        this.flats1.add("Select Admin");
        this.flatids1.add("0");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, flats1);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.mySpinner.setAdapter(adapter);

        this.mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((String) LoginActivity.this.flats1.get(position)).equals("Select Admin")) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.spin_val1 = (String) loginActivity.flatids1.get(position);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.ProfilePicture1);
        this.image1 = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            if (takePictureIntent.resolveActivity(LoginActivity.this.getPackageManager()) != null) {
                                LoginActivity.this.startActivityForResult(takePictureIntent, 2);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            LoginActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                        }
                    }
                });
                builder.show();
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.ProfilePicture2);
        this.image2 = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
            }
        });
        ImageView imageView3 = (ImageView) findViewById(R.id.ProfilePicture3);
        this.image3 = imageView3;
        imageView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 3);
            }
        });
        ImageView imageView4 = (ImageView) findViewById(R.id.ProfilePicture4);
        this.image4 = imageView4;
        imageView4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
            }
        });
        this.mySpinnerD = (Spinner) findViewById(R.id.spngenderD);
        this.flatsD.add("Select Destination");
        this.flatidsD.add("0");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, flatsD);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.mySpinnerD.setAdapter(adapter1);

        this.mySpinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((String) LoginActivity.this.flatsD.get(position)).equals("Select Destination")) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.spin_valD = (String) loginActivity.flatsD.get(position);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mySpinnerV = (Spinner) findViewById(R.id.spngenderV);
        this.vehiclename.add("Vehicle Type");
        this.vehicleid.add("0");
        this.vehiclename.add("Bike");
        this.vehicleid.add("1");
        this.vehiclename.add("Car");
        this.vehicleid.add(ExifInterface.GPS_MEASUREMENT_2D);
        this.vehiclename.add("Rickshaw");
        this.vehicleid.add(ExifInterface.GPS_MEASUREMENT_3D);
        this.vehiclename.add("Bus");
        this.vehicleid.add("4");
        this.vehiclename.add("Train");
        this.vehicleid.add("5");
        this.vehiclename.add("By Walk");
        this.vehicleid.add("6");
        ArrayAdapter<String> adapterV = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, vehiclename);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.mySpinnerV.setAdapter(adapterV);

        this.mySpinnerV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((String) LoginActivity.this.vehiclename.get(position)).equals("Vehicle Type")) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.spin_valV = (String) loginActivity.vehiclename.get(position);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        requestPermission();
//        checkIfAlreadyhavePermission();
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission-group.CAMERA", "android.permission.CAMERA", android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode != 1) {
            if (requestCode != 2) {
                if (requestCode != 3) {
                    if (requestCode == 4 && resultCode == -1 && imageReturnedIntent != null) {
                        try {
                            this.bitmap4 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageReturnedIntent.getData());
                            Glide.with((FragmentActivity) this).load(this.bitmap4).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image4);
                            this.imgs4 = getStringImage1(this.bitmap4);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == -1 && imageReturnedIntent != null) {
                    try {
                        this.bitmap3 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageReturnedIntent.getData());
                        Glide.with((FragmentActivity) this).load(this.bitmap3).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image3);
                        this.imgs3 = getStringImage3(this.bitmap3);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } else if (imageReturnedIntent != null && resultCode == -1) {
                Bitmap imageBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                Glide.with((FragmentActivity) this).load(imageBitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image1);
                uploadImage(bitmapToUri(imageBitmap));
            }
        } else if (resultCode == -1 && imageReturnedIntent != null) {
            this.filePath = imageReturnedIntent.getData();
            try {
                this.bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), this.filePath);
                Glide.with((FragmentActivity) this).load(this.bitmap1).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image1);
                this.imgs1 = getStringImage1(this.bitmap1);
                uploadImage(this.filePath);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }


    private Uri bitmapToUri(Bitmap bitmap5) {
        File shareFile = new File(getApplicationContext().getExternalCacheDir(), "temp.png");
        try {
            FileOutputStream out = new FileOutputStream(shareFile);
            bitmap5.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(shareFile);
    }

    public String getStringImage1(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public String getStringImage3(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public String getStringImage4(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    /* access modifiers changed from: private */


    /* access modifiers changed from: private */
    public void login() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        this.mRequestQueue = Volley.newRequestQueue(this);
        StringRequest r3 = new StringRequest(1, Config.admin_login, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                String str = ServerResponse;
                showMe.dismiss();
                Log.d("Login Server Response", str);
                try {
                    JSONObject j = new JSONObject(str);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (status.equals("200")) {
                        String userid1 = j.getString("id");
                        String name1 = j.getString("name");
                        String mobile1 = j.getString("mobile");
                        String place1 = j.getString(SessionManager.KEY_PLACE);
                        String hash1 = j.getString(SessionManager.KEY_HASH);
                        LoginActivity.this.session.createAdminLoginRole(j.getString("user_type"));
                        LoginActivity.this.session.createLoginSession(userid1, name1, mobile1, place1, hash1);
                        if (LoginActivity.this.logincode.equals("1")) {
                            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra("name", LoginActivity.this.name.getText().toString());
                            intent.putExtra("mobile", LoginActivity.this.phonenumber.getText().toString());
                            intent.putExtra(SessionManager.KEY_PLACE, LoginActivity.this.address.getText().toString());
                            intent.putExtra("admincode", LoginActivity.this.password.getText().toString());
                            intent.putExtra("newToken", LoginActivity.this.newToken);
                            LoginActivity.this.startActivity(intent);
                        }
                        return;
                    }
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                showMe.dismiss();
                LoginActivity.this.NetworkDialog();
                Log.d("Error", String.valueOf(error));
                Toast.makeText(LoginActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("name", LoginActivity.this.name.getText().toString());
                params.put("mobile", LoginActivity.this.phonenumber.getText().toString());
                params.put("admin_code", LoginActivity.this.password.getText().toString());
                params.put("firebase_id", LoginActivity.this.newToken);
                return params;
            }
        };
        this.stringRequest = r3;
        r3.setTag("TAG");
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, 1.0f));
        this.mRequestQueue.add(this.stringRequest);
    }

    /* access modifiers changed from: private */
    public void NetworkDialog() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                LoginActivity.this.login();
            }
        });
        dialogs.show();
    }

    /* access modifiers changed from: private */
    public void retailer_login() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        this.mRequestQueue = Volley.newRequestQueue(this);
        StringRequest r3 = new StringRequest(1, Config.retailer_login, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                String str = ServerResponse;
                showMe.dismiss();
                Log.d("Login Server Response", str);
                try {
                    JSONObject j = new JSONObject(str);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (status.equals("200")) {
                        String userid1 = j.getString("id");
                        String shop_name = j.getString("shop_name");
                        String name1 = j.getString("name");
                        String mobile1 = j.getString("mobile");
                        String email = j.getString("email");
                        String gst_no = j.getString("gst_no");
                        String shop_address = j.getString("shop_address");
                        String shop_image = j.getString("shop_image");
                        String retailer_status = j.getString("retailer_status");
                        String hash = j.getString(SessionManager.KEY_HASH);
                        String retailer_user_type = j.getString("retailer_user_type");
                        String str2 = mobile1;
                        JSONObject jSONObject = j;
                        LoginActivity.this.session.createAdminLoginRole("4");
                        String str3 = status;
                        Intent intent = new Intent(LoginActivity.this, RetailerOtpActivity.class);
                        intent.putExtra("userid1", userid1);
                        intent.putExtra("shop_name", shop_name);
                        intent.putExtra("name1", name1);
                        intent.putExtra("email", email);
                        intent.putExtra("gst_no", gst_no);
                        intent.putExtra("mobile1", LoginActivity.this.phonenumber.getText().toString());
                        intent.putExtra("shop_address", shop_address);
                        intent.putExtra("shop_image", shop_image);
                        intent.putExtra("retailer_status", retailer_status);
                        intent.putExtra(SessionManager.KEY_HASH, hash);
                        intent.putExtra("retailer_user_type", retailer_user_type);
                        LoginActivity.this.startActivity(intent);
                        return;
                    }
                    String str4 = status;
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                showMe.dismiss();
                LoginActivity.this.NetworkDialog2();
                Log.d("Error", String.valueOf(error));
                Toast.makeText(LoginActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("name", LoginActivity.this.name.getText().toString());
                params.put("mobile", LoginActivity.this.phonenumber.getText().toString());
                params.put("firebase_id", LoginActivity.this.newToken);
                return params;
            }
        };
//        this.stringRequest = r3;
//        r3.setTag("TAG");
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, 1.0f));
        this.mRequestQueue.add(this.stringRequest);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, LoginFirstScreen.class));
    }

    /* access modifiers changed from: private */
    public void NetworkDialog2() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                LoginActivity.this.retailer_login();
            }
        });
        dialogs.show();
    }

    private void uploadImage(Uri filepath) {
        if (filepath != null) {
            final ProgressDialog showMe = new ProgressDialog(this);
            showMe.setMessage("Image Upload is InProgress");
            showMe.setCancelable(true);
            showMe.setCanceledOnTouchOutside(false);
            showMe.show();
            StorageReference child = this.storageReference.child(this.googleSignInAccount.getId());
            final StorageReference ref = child.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri downloadUri) {
                            showMe.dismiss();
                            LoginActivity.this.imageUrl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    LoginActivity loginActivity = LoginActivity.this;
                    Toast.makeText(loginActivity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener((OnProgressListener) new OnProgressListener<UploadTask.TaskSnapshot>() {
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                    Double.isNaN(bytesTransferred);
                    double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                    Double.isNaN(totalByteCount);
                    double d = (bytesTransferred * 100.0d) / totalByteCount;
                }
            });
        }
    }
}
