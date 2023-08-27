package com.webapp.oasis.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.logging.type.LogSeverity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.webapp.oasis.BuildConfig;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class AddTechnicianActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /* access modifiers changed from: private */
    public File actualImage;
    CircularImageView adhaarCard;
    String adhaarImageUrl;
    ImageView back;
    Bitmap bitmap;
    Button btn_pin_submit;
    private File compressedImage;
    private File compressedImageAdhaar;
    EditText email;
    EditText generate_password;
    String hash;
    RelativeLayout image;
    String imagePath;
    String imgs = "";
    String licenceImageurl;
    ImageView list;
    String mCurrentPhotoPath;
    RequestQueue mRequestQueue;
    EditText mobile;
    EditText name;
    RelativeLayout rl_upload_license;
    SessionManager session;
    FirebaseStorage storage;
    StorageReference storageReference;
    StringRequest stringRequest;
    CircularImageView upload_license;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_add_technician);
        this.adhaarCard = (CircularImageView) findViewById(R.id.upload_adhaar_card);
        this.upload_license = (CircularImageView) findViewById(R.id.upload_license);
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;
        this.storageReference = instance.getReference();
        this.rl_upload_license = (RelativeLayout) findViewById(R.id.rl_upload_license);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
//        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
//        Log.d(SessionManager.KEY_USERID, this.user_id);
        Log.d(SessionManager.KEY_HASH, this.hash);
        ImageView imageView = (ImageView) findViewById(R.id.list);
        this.list = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddTechnicianActivity.this.startActivity(new Intent(AddTechnicianActivity.this, TechnicianListActivity.class));
            }
        });
        this.btn_pin_submit = (Button) findViewById(R.id.btn_pin_submit);
        this.name = (EditText) findViewById(R.id.name);
        this.mobile = (EditText) findViewById(R.id.mobile);
        this.generate_password = (EditText) findViewById(R.id.generate_password);
        this.email = (EditText) findViewById(R.id.email);
        this.image = (RelativeLayout) findViewById(R.id.rluploadAdhaarcard);
        ImageView imageView2 = (ImageView) findViewById(R.id.back);
        this.back = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddTechnicianActivity.this.onBackPressed();
            }
        });
        this.btn_pin_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AddTechnicianActivity.this.name.getText().toString().isEmpty()) {
                    Toast.makeText(AddTechnicianActivity.this, "Enter Technician Name", Toast.LENGTH_LONG).show();
                } else if (AddTechnicianActivity.this.mobile.getText().toString().isEmpty()) {
                    Toast.makeText(AddTechnicianActivity.this, "Enter Technician Mobile Number", Toast.LENGTH_LONG).show();
                } else if (AddTechnicianActivity.this.generate_password.getText().toString().isEmpty()) {
                    Toast.makeText(AddTechnicianActivity.this, "Enter Password for Technician Login", Toast.LENGTH_LONG).show();
                } else if (AddTechnicianActivity.this.email.getText().toString().isEmpty()) {
                    Toast.makeText(AddTechnicianActivity.this, "Enter Technician Email", Toast.LENGTH_LONG).show();
                } else {
                    AddTechnicianActivity.this.add_technician();
                }
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rluploadAdhaarcard);
        this.image = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTechnicianActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AddTechnicianActivity.this.actualImage = AddTechnicianActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AddTechnicianActivity.this.actualImage = null;
                                AddTechnicianActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AddTechnicianActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AddTechnicianActivity.this, BuildConfig.APPLICATION_ID, AddTechnicianActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AddTechnicianActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AddTechnicianActivity.this.startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddTechnicianActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
        this.adhaarCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTechnicianActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AddTechnicianActivity.this.actualImage = AddTechnicianActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AddTechnicianActivity.this.actualImage = null;
                                AddTechnicianActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AddTechnicianActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AddTechnicianActivity.this, BuildConfig.APPLICATION_ID, AddTechnicianActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AddTechnicianActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AddTechnicianActivity.this.startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddTechnicianActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
        this.rl_upload_license.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTechnicianActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AddTechnicianActivity.this.actualImage = AddTechnicianActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AddTechnicianActivity.this.actualImage = null;
                                AddTechnicianActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AddTechnicianActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AddTechnicianActivity.this, BuildConfig.APPLICATION_ID, AddTechnicianActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AddTechnicianActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AddTechnicianActivity.this.startActivityForResult(intent, 3);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddTechnicianActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
                        }
                    }
                });
                builder.show();
            }
        });
        this.upload_license.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTechnicianActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AddTechnicianActivity.this.actualImage = AddTechnicianActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AddTechnicianActivity.this.actualImage = null;
                                AddTechnicianActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AddTechnicianActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AddTechnicianActivity.this, BuildConfig.APPLICATION_ID, AddTechnicianActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AddTechnicianActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AddTechnicianActivity.this.startActivityForResult(intent, 3);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AddTechnicianActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void add_technician() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails");
        Map<String, Object> data = new HashMap<>();
        data.put("Name", this.name.getText().toString());
        data.put("Mobile", this.mobile.getText().toString());
        data.put("Technician Password", this.generate_password.getText().toString());
        data.put("email", this.email.getText().toString());
        data.put("AdhaarCard", this.adhaarImageUrl);
        data.put("License", this.licenceImageurl);
        String technicianId = myRef.push().getKey();
        data.put("Technician ID", technicianId);
        myRef.child(technicianId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    AddTechnicianActivity addTechnicianActivity = AddTechnicianActivity.this;
                    Toast.makeText(addTechnicianActivity, AddTechnicianActivity.this.name.getText().toString() + " Added Successfully", Toast.LENGTH_LONG).show();
                    AddTechnicianActivity.this.name.setText("");
                    AddTechnicianActivity.this.mobile.setText("");
                    AddTechnicianActivity.this.generate_password.setText("");
                    AddTechnicianActivity.this.email.setText("");
                    AddTechnicianActivity.this.adhaarCard.setImageResource(R.drawable.camera3);
                    AddTechnicianActivity.this.upload_license.setImageResource(R.drawable.camera3);
                    showMe.dismiss();
                }
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
                AddTechnicianActivity.this.add_technician();
            }
        });
        dialogs.show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission-group.CAMERA", "android.permission.CAMERA"}, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode != 1) {
            if (requestCode != 2) {
                if (requestCode != 3) {
                    if (requestCode != 4) {
                        return;
                    }
                } else if (resultCode == -1) {
                    if (requestCode == 3) {
                        customdocCompressImage();
                    }
                    if (resultCode == 2) {
                        String[] filePathColumn = {"_data"};
                        Cursor cursor = getApplicationContext().getContentResolver().query(imageReturnedIntent.getData(), filePathColumn, (String) null, (String[]) null, (String) null);
                        cursor.moveToFirst();
                        this.imagePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                        cursor.close();
                        this.adhaarCard.setImageBitmap(BitmapFactory.decodeFile(this.imagePath));
                        Bitmap decodeFile = BitmapFactory.decodeFile(this.imagePath);
                        this.bitmap = decodeFile;
                        this.imgs = getStringImage(decodeFile);
                    }
                }
                if (resultCode == -1 && imageReturnedIntent != null) {
                    Uri filePath = imageReturnedIntent.getData();
                    try {
                        this.bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                        Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.upload_license);
                        this.imgs = getStringImage(this.bitmap);
                        uploadImageLicence(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == -1 && imageReturnedIntent != null) {
                Uri filePath2 = imageReturnedIntent.getData();

                try {
                    this.bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath2);
                    Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.adhaarCard);
                    this.imgs = getStringImage(this.bitmap);
                    uploadImageAdhaar(filePath2);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (resultCode == -1) {
            if (requestCode == 1) {
                customdocCompressImageAdhaar();
            }
            if (resultCode == 2) {
                String[] filePathColumn2 = {"_data"};
                Cursor cursor2 = getApplicationContext().getContentResolver().query(imageReturnedIntent.getData(), filePathColumn2, (String) null, (String[]) null, (String) null);
                cursor2.moveToFirst();
                this.imagePath = cursor2.getString(cursor2.getColumnIndex(filePathColumn2[0]));
                cursor2.close();
                this.adhaarCard.setImageBitmap(BitmapFactory.decodeFile(this.imagePath));
                Bitmap decodeFile2 = BitmapFactory.decodeFile(this.imagePath);
                this.bitmap = decodeFile2;
                this.imgs = getStringImage(decodeFile2);
            }
        }
    }

    public void customdocCompressImageAdhaar() {
        if (this.actualImage != null) {
            try {
                this.compressedImageAdhaar = new Compressor(getApplicationContext()).setMaxWidth(540).setMaxHeight(LogSeverity.ERROR_VALUE).setQuality(50).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(this.actualImage);
                setdocCompressedImageAdhaar();
            } catch (IOException e) {
                e.printStackTrace();
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

    public void setdocCompressedImageAdhaar() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImageAdhaar.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.adhaarCard);
            this.imgs = getStringImage(this.bitmap);
            uploadImageAdhaar(bitmapToUri(this.bitmap));
            Log.d("image base64 : ", this.imgs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setdocCompressedImage() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) this.upload_license);
            this.imgs = getStringImage(this.bitmap);
            uploadImageLicence(bitmapToUri(this.bitmap));
            Log.d("image base64 : ", this.imgs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    private void uploadImageAdhaar(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog showMe = new ProgressDialog(this);
            showMe.setMessage("Image Upload is InProgress");
            showMe.setCancelable(true);
            showMe.setCanceledOnTouchOutside(false);
            showMe.show();
            StorageReference child = this.storageReference.child("Techinician");
            final StorageReference ref = child.child("images" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri downloadUri) {
                            showMe.dismiss();
                            AddTechnicianActivity.this.adhaarImageUrl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    AddTechnicianActivity addTechnicianActivity = AddTechnicianActivity.this;
                    Toast.makeText(addTechnicianActivity, "Failed " + e.getMessage(), 0).show();
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

    private void uploadImageLicence(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog showMe = new ProgressDialog(this);
            showMe.setMessage("Image Upload is InProgress");
            showMe.setCancelable(true);
            showMe.setCanceledOnTouchOutside(false);
            showMe.show();
            StorageReference child = this.storageReference.child("Techinician");
            final StorageReference ref = child.child("images" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri downloadUri) {
                            showMe.dismiss();
                            AddTechnicianActivity.this.licenceImageurl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    AddTechnicianActivity addTechnicianActivity = AddTechnicianActivity.this;
                    Toast.makeText(addTechnicianActivity, "Failed " + e.getMessage(), 0).show();
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
