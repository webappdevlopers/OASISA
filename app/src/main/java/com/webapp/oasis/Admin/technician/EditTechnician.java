package com.webapp.oasis.Admin.technician;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

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

import com.android.volley.RequestQueue;
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
import com.webapp.oasis.databinding.FragmentEditTechnicianBinding;

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


public class EditTechnician extends AppCompatActivity {

    FragmentEditTechnicianBinding fragmentEditTechnicianBinding;
    FirebaseStorage storage;
    StorageReference storageReference;
    String licenceImageurl;
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
    ImageView list;
    String mCurrentPhotoPath, technicianId;
    RequestQueue mRequestQueue;
    CircularImageView upload_license;

    public EditTechnician() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentEditTechnicianBinding = FragmentEditTechnicianBinding.inflate(getLayoutInflater());
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;

        this.storageReference = instance.getReference();

        setContentView((View) fragmentEditTechnicianBinding.getRoot());
        Intent i = getIntent();
        fragmentEditTechnicianBinding.technicianName.setText(i.getStringExtra("name"));
        fragmentEditTechnicianBinding.technicianMoibleNo.setText(i.getStringExtra("mobile"));
        fragmentEditTechnicianBinding.technicainEmail.setText(i.getStringExtra("email"));
        fragmentEditTechnicianBinding.generatePassword.setText(i.getStringExtra("generated_password"));
        technicianId = i.getStringExtra("techician_id");

        //        fragmentEditTechnicianBinding.size.setText(i.getStringExtra("adhaarcard"));
//        fragmentEditTechnicianBinding.size.setText(i.getStringExtra("license"));
        Glide.with(getApplicationContext()).load(i.getStringExtra("adhaarcard")).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.adhaarcardImage);
        Glide.with(getApplicationContext()).load(i.getStringExtra("license")).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.licecnseImage);
        adhaarImageUrl = i.getStringExtra("adhaarcard");
        licenceImageurl = i.getStringExtra("license");
        fragmentEditTechnicianBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fragmentEditTechnicianBinding.uploadAdhaarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTechnician.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = actualImage = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = actualImage = null;
                                mCurrentPhotoPath = null;
                            }
                            if (actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(EditTechnician.this, BuildConfig.APPLICATION_ID, actualImage);
                                } else {
                                    photoURI = Uri.fromFile(actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
        fragmentEditTechnicianBinding.uploadLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTechnician.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = actualImage = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = actualImage = null;
                                mCurrentPhotoPath = null;
                            }
                            if (actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(EditTechnician.this, BuildConfig.APPLICATION_ID, actualImage);
                                } else {
                                    photoURI = Uri.fromFile(actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                startActivityForResult(intent, 3);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
                        }
                    }
                });
                builder.show();
            }
        });
        fragmentEditTechnicianBinding.btnUpdateTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentEditTechnicianBinding.technicianName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Item Name", Toast.LENGTH_SHORT).show();

                } else if (fragmentEditTechnicianBinding.technicianMoibleNo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile No", Toast.LENGTH_SHORT).show();

                } else if (fragmentEditTechnicianBinding.technicainEmail.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();

                } else if (fragmentEditTechnicianBinding.generatePassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Password for Technician Login", Toast.LENGTH_LONG).show();
                } else {
                    edit_technician();
                }
            }
        });
    }

    public void edit_technician() {
        final ProgressDialog showMe = new ProgressDialog(EditTechnician.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails");
        Map<String, Object> data = new HashMap<>();
        data.put("Name", fragmentEditTechnicianBinding.technicianName.getText().toString());
        data.put("Mobile", fragmentEditTechnicianBinding.technicianMoibleNo.getText().toString());
        data.put("email", fragmentEditTechnicianBinding.technicainEmail.getText().toString());
        data.put("AdhaarCard", this.adhaarImageUrl);
        data.put("License", this.licenceImageurl);
        data.put("Technician Password", fragmentEditTechnicianBinding.generatePassword.getText().toString());
        data.put("Technician ID", technicianId);
        data.put("isDelete", "false");

        myRef.child(technicianId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), fragmentEditTechnicianBinding.technicianName.getText().toString() + " Edited Successfully", Toast.LENGTH_LONG).show();
                    fragmentEditTechnicianBinding.technicianName.setText("");
                    fragmentEditTechnicianBinding.technicianMoibleNo.setText("");
                    fragmentEditTechnicianBinding.technicainEmail.setText("");
                    fragmentEditTechnicianBinding.generatePassword.setText("");
                    fragmentEditTechnicianBinding.adhaarcardImage.setImageResource(R.drawable.noimage);
                    fragmentEditTechnicianBinding.licecnseImage.setImageResource(R.drawable.noimage);

                    //                    Glide.with(getApplicationContext()).load(adhaarImageUrl).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.adhaarcardImage);
//                    Glide.with(getApplicationContext()).load(licenceImageurl).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.licecnseImage);
                    //
//                    if (fragmentEditTechnicianBinding.technicianName.getText().toString().isEmpty()) {
//                        fragmentEditTechnicianBinding.adhaarcardImage.setImageResource(R.drawable.noimage);
//                        fragmentEditTechnicianBinding.licecnseImage.setImageResource(R.drawable.noimage);
//
//                    }

                    //                    AddTechnicianActivity.this.upload_license.setImageResource(R.drawable.camera3);

//                    AddTechnicianActivity.this.generate_password.setText("");

                    showMe.dismiss();
                }
            }
        });
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
                            licenceImageurl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), 0).show();
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
        } else {
            Toast.makeText(this, "sdfdsfsf", Toast.LENGTH_SHORT).show();
        }
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
                        Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.licecnseImage);
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
                    Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.adhaarcardImage);
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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
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

    public void setdocCompressedImage() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.licecnseImage);
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

    public void setdocCompressedImageAdhaar() {
        this.bitmap = BitmapFactory.decodeFile(this.compressedImageAdhaar.getAbsolutePath());
        try {
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(this.actualImage));
            Glide.with((FragmentActivity) this).load(this.bitmap).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.ic_camera_alt_black_24dp)).fitCenter()).into((ImageView) fragmentEditTechnicianBinding.adhaarcardImage);
            this.imgs = getStringImage(this.bitmap);
            uploadImageAdhaar(bitmapToUri(this.bitmap));
            Log.d("image base64 : ", this.imgs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                            adhaarImageUrl = downloadUri.toString();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), 0).show();
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

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image2 = File.createTempFile("JPEG_" + timeStamp + "_", ".jpeg", getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.mCurrentPhotoPath = image2.getAbsolutePath();
        return image2;
    }
}