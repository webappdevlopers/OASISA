package com.webapp.oasis.Customer;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.logging.type.LogSeverity;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityCutomerProfileBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class CustomerProfileActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private File actualImage;
    TextView address;
    ImageView back;
    ActivityCutomerProfileBinding binding;
    Bitmap bitmap;
    Button btn_vehicle_submit;
    private File compressedImage;
    String customerId;
    GoogleSignInAccount googleSignInAccount;
    String hash;
    ImageView image;
    String imagePath;
    ImageView imgedit;
    String imgs = "";
    String imgurl = "";
    String mCurrentPhotoPath;
    RequestQueue mRequestQueue;
    TextView mobile;
    TextView name;
    String profileImage;
    String roImage;
    SessionManager session;
    StringRequest stringRequest;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityCutomerProfileBinding.inflate(getLayoutInflater());
        this.googleSignInAccount = (GoogleSignInAccount) getIntent().getParcelableExtra(LoginFirstScreen.GOOGLE_ACCOUNT);
        setContentView((View) this.binding.getRoot());
        requestPermission();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.customerId = users.get(SessionManager.CustomerId);
        this.hash = users.get(SessionManager.KEY_HASH);
        this.address = (TextView) findViewById(R.id.address);
        this.name = (TextView) findViewById(R.id.name);
        this.mobile = (TextView) findViewById(R.id.mobile);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomerProfileActivity.this.startActivity(new Intent(CustomerProfileActivity.this, CustomerHomeActivity.class));
                CustomerProfileActivity.this.finish();
            }
        });
        update_driver_profile();
        this.image = (ImageView) findViewById(R.id.rluploadAdhaarcard);
        ImageView imageView2 = (ImageView) findViewById(R.id.imgedit);
        this.imgedit = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CustomerProfileActivity.this, CustomerEditProfileActivity.class);
                intent.putExtra("name", CustomerProfileActivity.this.name.getText().toString());
                intent.putExtra("mobile", CustomerProfileActivity.this.mobile.getText().toString());
                intent.putExtra("alternateMobileNo", CustomerProfileActivity.this.binding.alternateMobile.getText().toString());
                intent.putExtra("address", CustomerProfileActivity.this.binding.address.getText().toString());
                intent.putExtra("email", CustomerProfileActivity.this.binding.email.getText().toString());
                intent.putExtra("area", CustomerProfileActivity.this.binding.area.getText().toString());
                intent.putExtra("pincode", CustomerProfileActivity.this.binding.pincode.getText().toString());
                intent.putExtra("machineMake", CustomerProfileActivity.this.binding.machineMake.getText().toString());
                intent.putExtra("MachineModel", CustomerProfileActivity.this.binding.machineModel.getText().toString());
                intent.putExtra("ProfileImage", CustomerProfileActivity.this.profileImage);
                intent.putExtra("Roimage", CustomerProfileActivity.this.roImage);
                CustomerProfileActivity.this.startActivity(intent);
            }
        });
    }

    /* access modifiers changed from: private */
    public void update_driver_profile() {
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration").child(this.customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String alternateMobileNumber = (String) dataSnapshot.child("AlternateMobileNumber").getValue(String.class);
                    String area = (String) dataSnapshot.child("Area").getValue(String.class);
                    String pincode = (String) dataSnapshot.child("Pincode").getValue(String.class);
                    String machineMake = (String) dataSnapshot.child("MachineMake").getValue(String.class);
                    String machineModel = (String) dataSnapshot.child("Machine Model").getValue(String.class);
                    CustomerProfileActivity.this.profileImage = (String) dataSnapshot.child("ProfileImage").getValue(String.class);
                    CustomerProfileActivity.this.roImage = (String) dataSnapshot.child("RoImage").getValue(String.class);
                    CustomerProfileActivity.this.binding.mobile.setText((String) dataSnapshot.child("MobileNumber").getValue(String.class));
                    CustomerProfileActivity.this.binding.address.setText((String) dataSnapshot.child("Address").getValue(String.class));
                    CustomerProfileActivity.this.binding.email.setText((String) dataSnapshot.child("Email").getValue(String.class));
                    CustomerProfileActivity.this.binding.name.setText((String) dataSnapshot.child("Name").getValue(String.class));
                    if (!TextUtils.isEmpty(alternateMobileNumber)) {
                        CustomerProfileActivity.this.binding.alternateMobile.setText(alternateMobileNumber);
                    } else {
                        CustomerProfileActivity.this.binding.llAlternateMobile.setVisibility(View.GONE);
                        CustomerProfileActivity.this.binding.alternateView.setVisibility(View.GONE);
                    }
                    CustomerProfileActivity.this.binding.area.setText(area);
                    CustomerProfileActivity.this.binding.pincode.setText(pincode);
                    if (!TextUtils.isEmpty(machineMake)) {
                        CustomerProfileActivity.this.binding.machineMake.setText(machineMake);
                    } else {
                        CustomerProfileActivity.this.binding.llMakeMachine.setVisibility(View.GONE);
                        CustomerProfileActivity.this.binding.makeMachineView.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(machineModel)) {
                        CustomerProfileActivity.this.binding.machineModel.setText(machineModel);
                    } else {
                        CustomerProfileActivity.this.binding.llMachineModel.setVisibility(View.GONE);
                        CustomerProfileActivity.this.binding.machineModelView.setVisibility(View.GONE);
                    }
                    Glide.with((FragmentActivity) CustomerProfileActivity.this).load(CustomerProfileActivity.this.profileImage).into(CustomerProfileActivity.this.image);
                    if (CustomerProfileActivity.this.roImage != null) {
                        Glide.with((FragmentActivity) CustomerProfileActivity.this).load(CustomerProfileActivity.this.roImage).into(CustomerProfileActivity.this.binding.roIamge);
                        return;
                    }
                    CustomerProfileActivity.this.binding.roIamge.setVisibility(View.GONE);
                    CustomerProfileActivity.this.binding.rotext.setVisibility(View.GONE);
                }
            }

            public void onCancelled(DatabaseError error) {
                Log.w(Oscillator.TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                CustomerProfileActivity.this.update_driver_profile();
            }
        });
        dialogs.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, CustomerHomeActivity.class));
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission-group.CAMERA", "android.permission.CAMERA"}, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode != 1) {
            if (requestCode == 2 && resultCode == -1 && imageReturnedIntent != null) {
                try {
                    this.bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageReturnedIntent.getData());
                    Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image);
                    this.imgs = getStringImage(this.bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == -1) {
            if (requestCode == 1) {
                customdocCompressImage();
            }
            if (resultCode == 2) {
                String[] filePathColumn = {"_data"};
                Cursor cursor = getApplicationContext().getContentResolver().query(imageReturnedIntent.getData(), filePathColumn, (String) null, (String[]) null, (String) null);
                cursor.moveToFirst();
                this.imagePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                cursor.close();
                this.image.setImageBitmap(BitmapFactory.decodeFile(this.imagePath));
                Bitmap decodeFile = BitmapFactory.decodeFile(this.imagePath);
                this.bitmap = decodeFile;
                this.imgs = getStringImage(decodeFile);
            }
        }
    }

    public void customdocCompressImage() {
        if (this.actualImage != null) {
            try {
                this.compressedImage = new Compressor(getApplicationContext()).setMaxWidth(540).setMaxHeight(LogSeverity.ERROR_VALUE).setQuality(50).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(this.actualImage);
                setdocCompressedImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setdocCompressedImage() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image);
            String stringImage = getStringImage(this.bitmap);
            this.imgs = stringImage;
            Log.d("image base64 : ", stringImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image2 = File.createTempFile("JPEG_" + timeStamp + "_", ".jpeg", getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.mCurrentPhotoPath = image2.getAbsolutePath();
        return image2;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }
}
