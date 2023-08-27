package com.webapp.oasis.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.logging.type.LogSeverity;
import com.webapp.oasis.BuildConfig;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityCustomerEditProfileBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class CustomerEditProfileActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    String ROImage;
    /* access modifiers changed from: private */
    public File actualImage;
    EditText addresset;
    ImageView back;
    ActivityCustomerEditProfileBinding binding;
    Bitmap bitmap;
    Button btn_add_item;
    String color;
    private File compressedImage;
    String customerId;
    String hash;
    ImageView image;
    String imagePath;
    String imageUrl;
    String imgs = "";
    String imgurl;
    String item_id;
    ImageView list;
    String mCurrentPhotoPath;
    RequestQueue mRequestQueue;
    EditText mobileet;
    EditText nameet;
    SessionManager session;
    Button signin;
    FirebaseStorage storage;
    StorageReference storageReference;
    StringRequest stringRequest;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCustomerEditProfileBinding inflate = ActivityCustomerEditProfileBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        requestPermission();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.customerId = users.get(SessionManager.CustomerId);
        this.hash = users.get(SessionManager.KEY_HASH);
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;
        this.storageReference = instance.getReference();
        this.nameet = (EditText) findViewById(R.id.name);
        this.mobileet = (EditText) findViewById(R.id.mobile);
        this.addresset = (EditText) findViewById(R.id.etadress);
        this.btn_add_item = (Button) findViewById(R.id.btn_edit);
        this.image = (ImageView) findViewById(R.id.rluploadAdhaarcard);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomerEditProfileActivity.this.onBackPressed();
                CustomerEditProfileActivity.this.startActivity(new Intent(CustomerEditProfileActivity.this, CustomerProfileActivity.class));
                CustomerEditProfileActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");
        String alternateMobileNo = intent.getStringExtra("alternateMobileNo");
        String address = intent.getStringExtra("address");
        String email = intent.getStringExtra("email");
        String area = intent.getStringExtra("area");
        String pincode = intent.getStringExtra("pincode");
        String machineMake = intent.getStringExtra("machineMake");
        String machineModel = intent.getStringExtra("MachineModel");
        final String profileImage = intent.getStringExtra("ProfileImage");
        final String ROImage2 = intent.getStringExtra("Roimage");
        this.nameet.setText(name);
        this.mobileet.setText(mobile);
        this.addresset.setText(address);
        this.binding.alternatemobile.setText(alternateMobileNo);
        this.binding.email.setText(email);
        this.binding.area.setText(area);
        this.binding.pincode.setText(pincode);
        this.binding.MachineMake.setText(machineMake);
        this.binding.machineModel.setText(machineModel);
        ((RequestBuilder) Glide.with((FragmentActivity) this).load(ROImage2).placeholder((int) R.drawable.ic_camera_alt_black_24dp)).into(this.image);
        this.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerEditProfileActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = CustomerEditProfileActivity.this.actualImage = CustomerEditProfileActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = CustomerEditProfileActivity.this.actualImage = null;
                                CustomerEditProfileActivity.this.mCurrentPhotoPath = null;
                            }
                            if (CustomerEditProfileActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(CustomerEditProfileActivity.this, BuildConfig.APPLICATION_ID, CustomerEditProfileActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(CustomerEditProfileActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                CustomerEditProfileActivity.this.startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            CustomerEditProfileActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
        this.btn_add_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CustomerEditProfileActivity.this.binding.mobile.getText().toString().isEmpty() || CustomerEditProfileActivity.this.binding.mobile.length() != 10) {
                    Toast.makeText(CustomerEditProfileActivity.this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (CustomerEditProfileActivity.this.binding.etadress.getText().toString().isEmpty()) {
                    Toast.makeText(CustomerEditProfileActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (CustomerEditProfileActivity.this.binding.area.getText().toString().isEmpty()) {
                    Toast.makeText(CustomerEditProfileActivity.this, "Enter Area", Toast.LENGTH_SHORT).show();
                } else if (CustomerEditProfileActivity.this.binding.pincode.getText().toString().isEmpty()) {
                    Toast.makeText(CustomerEditProfileActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration");
                    Map<String, Object> data = new HashMap<>();
                    data.put("MobileNumber", CustomerEditProfileActivity.this.binding.mobile.getText().toString());
                    data.put("AlternateMobileNumber", CustomerEditProfileActivity.this.binding.alternatemobile.getText().toString());
                    data.put("Address", CustomerEditProfileActivity.this.binding.etadress.getText().toString());
                    data.put("Area", CustomerEditProfileActivity.this.binding.area.getText().toString());
                    data.put("Pincode", CustomerEditProfileActivity.this.binding.pincode.getText().toString());
                    data.put("MachineMake", CustomerEditProfileActivity.this.binding.MachineMake.getText().toString());
                    data.put("Machine Model", CustomerEditProfileActivity.this.binding.machineModel.getText().toString());
                    data.put("Email", CustomerEditProfileActivity.this.binding.email.getText().toString());
                    data.put("Name", CustomerEditProfileActivity.this.binding.name.getText().toString());
                    data.put("ProfileImage", profileImage);
                    if (CustomerEditProfileActivity.this.imageUrl != null) {
                        data.put("RoImage", CustomerEditProfileActivity.this.imageUrl);
                    } else {
                        data.put("RoImage", ROImage2);
                    }
                    myRef.child(CustomerEditProfileActivity.this.customerId).setValue(data);
                    CustomerEditProfileActivity.this.startActivity(new Intent(CustomerEditProfileActivity.this, CustomerProfileActivity.class));
                    CustomerEditProfileActivity.this.finish();
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission-group.CAMERA", "android.permission.CAMERA"}, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode != 1) {
            if (requestCode == 2 && resultCode == -1 && imageReturnedIntent != null) {
                Uri filePath = imageReturnedIntent.getData();
                try {
                    this.bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                    Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image);
                    this.imgs = getStringImage(this.bitmap);
                    uploadImage(filePath);
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
                uploadImage(bitmapToUri(this.bitmap));
            }
        }
    }

    public String convertUrlToBase64(String url) {
        String base64 = "";
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            Bitmap bitmap2 = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            base64 = Base64.encodeToString(outputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.imgs = base64;
        return base64;
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

    private Uri bitmapToUri(Bitmap bitmap2) {
        File shareFile = new File(getApplicationContext().getExternalCacheDir(), "temp.png");
        try {
            FileOutputStream out = new FileOutputStream(shareFile);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(shareFile);
    }

    public void setdocCompressedImage() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into(this.image);
            this.imgs = getStringImage(this.bitmap);
            uploadImage(bitmapToUri(this.bitmap));
            Log.d("image base64 : ", this.imgs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public File createImageFile() throws IOException {
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

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void uploadImage(Uri filepath) {
        if (filepath != null) {
            final ProgressDialog showMe = new ProgressDialog(this);
            showMe.setMessage("Image Upload is InProgress");
            showMe.setCancelable(true);
            showMe.setCanceledOnTouchOutside(false);
            showMe.show();
            StorageReference child = this.storageReference.child(this.customerId);
            final StorageReference ref = child.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri downloadUri) {
                            showMe.dismiss();
                            CustomerEditProfileActivity.this.imageUrl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    CustomerEditProfileActivity customerEditProfileActivity = CustomerEditProfileActivity.this;
                    Toast.makeText(customerEditProfileActivity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
