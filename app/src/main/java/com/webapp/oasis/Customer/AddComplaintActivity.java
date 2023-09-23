package com.webapp.oasis.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.HttpHeaders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webapp.oasis.Admin.Complaintdetails;
import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityLogComplaintBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddComplaintActivity extends AppCompatActivity {
    String CustomerMoibleNo;
    String CustomerName;
    ActivityLogComplaintBinding binding;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    Calendar calendar;
    String customer_id;
    EditText customer_no_multi;
    DatePickerDialog datePicker;
    int dayOfMonth;
    Uri filePath;
    ArrayList<String> genderid = new ArrayList<>();
    ArrayList<String> gendername = new ArrayList<>();
    GoogleSignInAccount googleSignInAccount;
    String hash;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    String imageUrl;
    String imgs1 = "";
    String imgs2 = "";
    String imgs3 = "";
    String imgs4 = "";
    int month;
    SessionManager session;
    Spinner spinner_select_service;
    FirebaseStorage storage;
    StorageReference storageReference;
    int year;
    ArrayList<AgentListModel> technicianModel = new ArrayList<>();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLogComplaintBinding inflate = ActivityLogComplaintBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());

        requestPermission();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.customer_id = users.get(SessionManager.CustomerId);
        this.CustomerName = users.get("name");
        this.CustomerMoibleNo = users.get(SessionManager.KEY_MOB);
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;
        this.storageReference = instance.getReference();
        this.binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {

                            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            if (takePictureIntent.resolveActivity(AddComplaintActivity.this.getPackageManager()) != null) {
                                AddComplaintActivity.this.startActivityForResult(takePictureIntent, 2);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddComplaintActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                        }
                    }
                });
                builder.show();
            }
        });
        this.binding.selectImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            if (takePictureIntent.resolveActivity(AddComplaintActivity.this.getPackageManager()) != null) {
                                AddComplaintActivity.this.startActivityForResult(takePictureIntent, 2);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddComplaintActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                        }
                    }
                });
                builder.show();
            }
        });
        this.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AddComplaintActivity.this.binding.complaintEt.getText().toString().isEmpty()) {
                    Toast.makeText(AddComplaintActivity.this, "Enter Complaint", Toast.LENGTH_SHORT).show();
                } else if (AddComplaintActivity.this.spinner_select_service.getSelectedItem().toString().equals("Select Service")) {
                    Toast.makeText(AddComplaintActivity.this, "Select Service", Toast.LENGTH_SHORT).show();
                } else {
                    String obj = "EndDate";
                    String obj2 = "StartDate";
                    String obj3 = "ComplaintId";
                    if (AddComplaintActivity.this.binding.llStartDate.getVisibility() == View.VISIBLE && AddComplaintActivity.this.binding.llEndDate.getVisibility() == View.VISIBLE) {
                        String obj4 = "CustomerId";
                        if (AddComplaintActivity.this.binding.date.getText().toString().equals("Select Start Date")) {
                            Toast.makeText(AddComplaintActivity.this, "Select Start Date ", Toast.LENGTH_SHORT).show();
                        } else if (AddComplaintActivity.this.binding.enddate.getText().toString().equals("Select End Date")) {
                            Toast.makeText(AddComplaintActivity.this, "Select End Date", Toast.LENGTH_SHORT).show();
                        } else {
                            final ProgressDialog showMe = new ProgressDialog(AddComplaintActivity.this);
                            showMe.setMessage("Please wait");
                            showMe.setCancelable(true);
                            showMe.setCanceledOnTouchOutside(false);
                            showMe.show();
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
                            DatabaseReference myRef = database.getReference("Customer/Complaint");
                            Map<String, Object> data = new HashMap<>();
                            FirebaseDatabase firebaseDatabase = database;
                            data.put("Complaint", "Complaint :"+AddComplaintActivity.this.binding.complaintEt.getText().toString()+
                                    "\nAddress : "+AddComplaintActivity.this.binding.address.getText().toString()+
                                    "\nNumber : "+ AddComplaintActivity.this.binding.mobilenumber.getText().toString());
                            data.put("Service", AddComplaintActivity.this.spinner_select_service.getSelectedItem().toString());
                            data.put("Status", "Pending");
                            data.put(HttpHeaders.DATE, currentDate);
                            data.put("Timing", currentTime);
                            data.put("Image", AddComplaintActivity.this.imageUrl);
                            data.put("CustomerName", AddComplaintActivity.this.CustomerName);
                            data.put("CustomerMobileNumber", AddComplaintActivity.this.CustomerMoibleNo);
                            data.put(obj4, AddComplaintActivity.this.customer_id);
                            data.put("TechnicianId", "");
                            data.put("CustomerId", AddComplaintActivity.this.customer_id);
                            String complaintId = myRef.push().getKey();
                            data.put(obj3, complaintId);
                            if (AddComplaintActivity.this.binding.llStartDate.getVisibility() == View.VISIBLE && AddComplaintActivity.this.binding.llEndDate.getVisibility() == View.VISIBLE) {
                                data.put(obj2, AddComplaintActivity.this.binding.date.getText().toString());
                                data.put(obj, AddComplaintActivity.this.binding.enddate.getText().toString());
                            }
                            myRef.child(AddComplaintActivity.this.customer_id).child(complaintId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        showMe.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintActivity.this);
                                        builder.setMessage("Complaint Has Been Added Successfully");
                                        builder.setPositiveButton("GO TO COMPLAINT DETAILS PAGE", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                AddComplaintActivity.this.startActivity(new Intent(AddComplaintActivity.this, ComplaintStatusActivity.class));
                                                AddComplaintActivity.this.finish();
                                            }
                                        });
                                        builder.setNegativeButton("GO TO HOME PAGE", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                AddComplaintActivity.this.startActivity(new Intent(AddComplaintActivity.this, CustomerHomeActivity.class));
                                                AddComplaintActivity.this.finish();
                                            }
                                        });
                                        builder.create().show();
                                        Log.d(Oscillator.TAG, "Data uploaded successfully");
                                        return;
                                    }
                                    showMe.dismiss();
                                    Log.d(Oscillator.TAG, "Data upload failed");
                                }
                            });
                        }
                    } else {
                        final ProgressDialog showMe2 = new ProgressDialog(AddComplaintActivity.this);
                        showMe2.setMessage("Please wait");
                        showMe2.setCancelable(true);
                        showMe2.setCanceledOnTouchOutside(false);
                        showMe2.show();
                        String currentTime2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String currentDate2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        FirebaseDatabase database2 = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
                        DatabaseReference myRef2 = database2.getReference("Customer/Complaint");
                        Map<String, Object> data2 = new HashMap<>();
                        FirebaseDatabase firebaseDatabase2 = database2;
                        data2.put("Complaint", "Complaint :"+AddComplaintActivity.this.binding.complaintEt.getText().toString()+
                                "\nAddress : "+AddComplaintActivity.this.binding.address.getText().toString()+
                                "\nNumber : "+ AddComplaintActivity.this.binding.mobilenumber.getText().toString());
                        data2.put("Service", AddComplaintActivity.this.spinner_select_service.getSelectedItem().toString());
                        data2.put("Status", "Pending");
                        data2.put(HttpHeaders.DATE, currentDate2);
                        data2.put("Timing", currentTime2);
                        data2.put("Image", AddComplaintActivity.this.imageUrl);
                        data2.put("CustomerName", AddComplaintActivity.this.CustomerName);
                        data2.put("CustomerMobileNumber", AddComplaintActivity.this.CustomerMoibleNo);
                        data2.put("CustomerId", AddComplaintActivity.this.customer_id);
                        String complaintId2 = myRef2.push().getKey();
                        data2.put(obj3, complaintId2);
                        if (AddComplaintActivity.this.binding.llStartDate.getVisibility() == View.VISIBLE && AddComplaintActivity.this.binding.llEndDate.getVisibility() == View.VISIBLE) {
                            data2.put(obj2, AddComplaintActivity.this.binding.date);
                            data2.put(obj, AddComplaintActivity.this.binding.enddate);
                        }
                        myRef2.child(AddComplaintActivity.this.customer_id).child(complaintId2).setValue(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showMe2.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintActivity.this);
                                    builder.setMessage("Complaint Has Been Added Successfully");
                                    builder.setPositiveButton("GO TO COMPLAINT DETAILS PAGE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            AddComplaintActivity.this.startActivity(new Intent(AddComplaintActivity.this, ComplaintStatusActivity.class));
                                            AddComplaintActivity.this.finish();
                                        }
                                    });
                                    builder.setNegativeButton("GO TO HOME PAGE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            AddComplaintActivity.this.startActivity(new Intent(AddComplaintActivity.this, CustomerHomeActivity.class));
                                            AddComplaintActivity.this.finish();
                                        }
                                    });
                                    builder.create().show();
                                    Log.d(Oscillator.TAG, "Data uploaded successfully");
                                    return;
                                }
                                showMe2.dismiss();
                                Log.d(Oscillator.TAG, "Data upload failed");
                            }
                        });
                    }
                }
            }
        });
        this.binding.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddComplaintActivity.this.onBackPressed();
            }
        });
        this.spinner_select_service = (Spinner) findViewById(R.id.spncolor);
        this.gendername.add("Select Service");
        this.gendername.add("AMC");
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/AddServices").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot itemSnapshot = it.next();
                        Iterator<DataSnapshot> it2 = it;
                        gendername.add((String) itemSnapshot.child("ServiceName").getValue(String.class));
                        it = it2;
                    }
                    final List<String> technicianNames = new ArrayList<>();
                    technicianNames.add("Add Technician");
                    Iterator<AgentListModel> it3 = AddComplaintActivity.this.technicianModel.iterator();
                    while (it3.hasNext()) {
                        technicianNames.add(it3.next().getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddComplaintActivity.this, android.R.layout.simple_list_item_1, gendername);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    AddComplaintActivity.this.spinner_select_service.setAdapter(adapter);
                }
            }
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gendername);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.spinner_select_service.setAdapter(adapter);

        this.spinner_select_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                if (AddComplaintActivity.this.spinner_select_service.getSelectedItem().toString().equalsIgnoreCase("AMC")) {
                    AddComplaintActivity.this.binding.llStartDate.setVisibility(View.VISIBLE);
                    AddComplaintActivity.this.binding.llEndDate.setVisibility(View.VISIBLE);
                    return;
                }
                AddComplaintActivity.this.binding.llStartDate.setVisibility(View.GONE);
                AddComplaintActivity.this.binding.llEndDate.setVisibility(View.GONE);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.binding.llStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddComplaintActivity.this.calendar = Calendar.getInstance();
                AddComplaintActivity addComplaintActivity = AddComplaintActivity.this;
                addComplaintActivity.year = addComplaintActivity.calendar.get(1);
                AddComplaintActivity addComplaintActivity2 = AddComplaintActivity.this;
                addComplaintActivity2.month = addComplaintActivity2.calendar.get(2);
                AddComplaintActivity addComplaintActivity3 = AddComplaintActivity.this;
                addComplaintActivity3.dayOfMonth = addComplaintActivity3.calendar.get(5);
                AddComplaintActivity.this.datePicker = new DatePickerDialog(AddComplaintActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        TextView textView = AddComplaintActivity.this.binding.date;
                        textView.setText(day + "-" + (month + 1) + "-" + year);
                    }
                }, AddComplaintActivity.this.year, AddComplaintActivity.this.month, AddComplaintActivity.this.dayOfMonth);
                AddComplaintActivity.this.datePicker.show();
            }
        });
        this.binding.llEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(1);
                AddComplaintActivity.this.month = calendar.get(2);
                AddComplaintActivity.this.dayOfMonth = calendar.get(5);
                AddComplaintActivity.this.datePicker = new DatePickerDialog(AddComplaintActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        TextView textView = AddComplaintActivity.this.binding.enddate;
                        textView.setText(day + "-" + (month + 1) + "-" + year);
                    }
                }, year, AddComplaintActivity.this.month, AddComplaintActivity.this.dayOfMonth);
                AddComplaintActivity.this.datePicker.show();
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission-group.CAMERA", "android.permission.CAMERA", android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode != 1) {
            if (requestCode == 2 && imageReturnedIntent != null && resultCode == -1) {
                Bitmap imageBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                Glide.with((FragmentActivity) this).load(imageBitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.binding.uploadImage);
                uploadImage(bitmapToUri(imageBitmap));
            }else if (requestCode == 2 && imageReturnedIntent != null) {
                Bitmap imageBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                Glide.with((FragmentActivity) this).load(imageBitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.binding.uploadImage);
                uploadImage(bitmapToUri(imageBitmap));
            }
        } else if (resultCode == -1 && imageReturnedIntent != null) {
            this.filePath = imageReturnedIntent.getData();
            try {
                this.bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), this.filePath);
                Glide.with((FragmentActivity) this).load(this.bitmap1).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.binding.uploadImage);
                this.imgs1 = getStringImage1(this.bitmap1);
                uploadImage(this.filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri bitmapToUri(Bitmap imageBitmap) {
        File shareFile = new File(getApplicationContext().getExternalCacheDir(), "temp.png");
        try {
            FileOutputStream out = new FileOutputStream(shareFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(shareFile);
    }

    private void uploadImage(Uri filePath2) {
        if (filePath2 != null) {
            final ProgressDialog showMe = new ProgressDialog(this);
            showMe.setMessage("Image Upload is InProgress");
            showMe.setCancelable(true);
            showMe.setCanceledOnTouchOutside(false);
            showMe.show();
            StorageReference child = this.storageReference.child(this.customer_id);
            final StorageReference ref = child.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri downloadUri) {
                            showMe.dismiss();
                            AddComplaintActivity.this.imageUrl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    AddComplaintActivity addComplaintActivity = AddComplaintActivity.this;
                    Toast.makeText(addComplaintActivity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public String getStringImage1(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }
}
