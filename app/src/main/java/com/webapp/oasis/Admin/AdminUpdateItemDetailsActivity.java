package com.webapp.oasis.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.logging.type.LogSeverity;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityAdminUpdateItemDetailsBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import id.zelory.compressor.Compressor;

public class AdminUpdateItemDetailsActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /* access modifiers changed from: private */
    public File actualImage;
    ImageView back;
    ActivityAdminUpdateItemDetailsBinding binding;
    Bitmap bitmap;
    EditText brandname;
    Button btn_add_item;
    String color;
    private File compressedImage;
    Context context;
    Spinner destination;
    ArrayList<String> genderid = new ArrayList<>();
    ArrayList<String> gendername = new ArrayList<>();
    String gid;
    String gname = "null";
    String hash;
    ImageView image;
    String imagePath;
    String imgs = "";
    String imgurl;
    String item_id;
    ImageView list;
    String mCurrentPhotoPath;
    RequestQueue mRequestQueue;
    EditText name;
    EditText qty;
    SessionManager session;
    Button signin;
    EditText size;
    Spinner source;
    StringRequest stringRequest;
    String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminUpdateItemDetailsBinding inflate = ActivityAdminUpdateItemDetailsBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        this.name = (EditText) findViewById(R.id.itemname);
        this.brandname = (EditText) findViewById(R.id.brandname);
        this.qty = (EditText) findViewById(R.id.qty);
        this.size = (EditText) findViewById(R.id.size);
        this.source = (Spinner) findViewById(R.id.spncolor);
        this.btn_add_item = (Button) findViewById(R.id.btn_add_item);
        this.image = (ImageView) findViewById(R.id.rluploadAdhaarcard);
        this.back = (ImageView) findViewById(R.id.back);
        Intent i = getIntent();
        this.name.setText(i.getStringExtra("name"));
        this.brandname.setText(i.getStringExtra("brandname"));
        this.qty.setText(i.getStringExtra(DatabaseHelper.COL_3));
        this.size.setText(i.getStringExtra("size"));
        this.name.setText(i.getStringExtra("name"));
        this.gname = i.getStringExtra("color");
        this.item_id = i.getStringExtra("ItemID");
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
        this.btn_add_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdminUpdateItemDetailsActivity.this.binding.itemname.getText().toString().isEmpty()) {
                    Toast.makeText(AdminUpdateItemDetailsActivity.this, "Enter Item Name", Toast.LENGTH_SHORT).show();
                } else if (AdminUpdateItemDetailsActivity.this.binding.brandname.getText().toString().isEmpty()) {
                    Toast.makeText(AdminUpdateItemDetailsActivity.this, "Enter Brand Name", Toast.LENGTH_SHORT).show();
                } else if (AdminUpdateItemDetailsActivity.this.binding.qty.getText().toString().isEmpty()) {
                    Toast.makeText(AdminUpdateItemDetailsActivity.this, "Enter Qty", Toast.LENGTH_SHORT).show();
                } else if (AdminUpdateItemDetailsActivity.this.binding.size.getText().toString().isEmpty()) {
                    Toast.makeText(AdminUpdateItemDetailsActivity.this, "Enter Price", Toast.LENGTH_SHORT).show();
                } else {
                    additem();
                }
            }

            private void additem() {
                final ProgressDialog showMe = new ProgressDialog(AdminUpdateItemDetailsActivity.this);
                showMe.setMessage("Please wait");
                showMe.setCancelable(true);
                showMe.setCanceledOnTouchOutside(false);
                showMe.show();
                final String itemnameValue = AdminUpdateItemDetailsActivity.this.binding.itemname.getText().toString();
                String brandName = AdminUpdateItemDetailsActivity.this.binding.brandname.getText().toString();
                String Qty = AdminUpdateItemDetailsActivity.this.binding.qty.getText().toString();
                String price = AdminUpdateItemDetailsActivity.this.binding.size.getText().toString();
                DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS");
                Map<String, Object> data = new HashMap<>();
                data.put("ItemName", itemnameValue);
                data.put("BrandName", brandName);
                data.put("Qty", Qty);
                data.put("Price", price);
                data.put("ItemID", AdminUpdateItemDetailsActivity.this.item_id);
                myRef.child(AdminUpdateItemDetailsActivity.this.item_id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            showMe.dismiss();
                            Intent intent = new Intent(AdminUpdateItemDetailsActivity.this.getApplicationContext(), AddStockActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            AdminUpdateItemDetailsActivity.this.getApplicationContext().startActivity(intent);
                            AdminUpdateItemDetailsActivity adminUpdateItemDetailsActivity = AdminUpdateItemDetailsActivity.this;
                            Toast.makeText(adminUpdateItemDetailsActivity, itemnameValue + " Edit SuccessFully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdminUpdateItemDetailsActivity.this.onBackPressed();
                AdminUpdateItemDetailsActivity.this.finish();
            }
        });
        this.source = (Spinner) findViewById(R.id.spncolor);
        this.gendername.add(this.gname);
        this.genderid.add("0");
        this.gendername.add("Red");
        this.genderid.add("1");
        this.gendername.add("Black");
        this.genderid.add(ExifInterface.GPS_MEASUREMENT_2D);
        this.gendername.add("Silver");
        this.genderid.add(ExifInterface.GPS_MEASUREMENT_3D);
        this.gendername.add("Orange");
        this.genderid.add("4");
        this.gendername.add("Grey");
        this.genderid.add("5");
        this.gendername.add("Blue");
        this.genderid.add("6");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  this.gendername);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        this.source.setAdapter(adapter);
        this.source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!AdminUpdateItemDetailsActivity.this.gendername.get(position).equals(AdminUpdateItemDetailsActivity.this.gname)) {
                    AdminUpdateItemDetailsActivity adminUpdateItemDetailsActivity = AdminUpdateItemDetailsActivity.this;
                    adminUpdateItemDetailsActivity.gid = adminUpdateItemDetailsActivity.genderid.get(position);
                    if (AdminUpdateItemDetailsActivity.this.gid == "1") {
                        AdminUpdateItemDetailsActivity.this.gname = "Red";
                    }
                    if (AdminUpdateItemDetailsActivity.this.gid == ExifInterface.GPS_MEASUREMENT_2D) {
                        AdminUpdateItemDetailsActivity.this.gname = "Black";
                    }
                    if (AdminUpdateItemDetailsActivity.this.gid == ExifInterface.GPS_MEASUREMENT_3D) {
                        AdminUpdateItemDetailsActivity.this.gname = "Silver";
                    }
                    if (AdminUpdateItemDetailsActivity.this.gid == "4") {
                        AdminUpdateItemDetailsActivity.this.gname = "Orange";
                    }
                    if (AdminUpdateItemDetailsActivity.this.gid == "5") {
                        AdminUpdateItemDetailsActivity.this.gname = "Grey";
                    }
                    if (AdminUpdateItemDetailsActivity.this.gid == "6") {
                        AdminUpdateItemDetailsActivity.this.gname = "Blue";
                    }
                    Log.d("cid", AdminUpdateItemDetailsActivity.this.gid);
                    Log.d("cname", AdminUpdateItemDetailsActivity.this.gname);
                    return;
                }
                Toast.makeText(AdminUpdateItemDetailsActivity.this, "Select Color", Toast.LENGTH_SHORT);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUpdateItemDetailsActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AdminUpdateItemDetailsActivity.this.actualImage = AdminUpdateItemDetailsActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AdminUpdateItemDetailsActivity.this.actualImage = null;
                                AdminUpdateItemDetailsActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AdminUpdateItemDetailsActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AdminUpdateItemDetailsActivity.this, "itkida.rastasuraksha.app", AdminUpdateItemDetailsActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AdminUpdateItemDetailsActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AdminUpdateItemDetailsActivity.this.startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AdminUpdateItemDetailsActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void add_item() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Updating Coordinates");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        this.mRequestQueue = Volley.newRequestQueue(this);
        StringRequest r3 = new StringRequest(1, Config.update_item, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                showMe.dismiss();
                Log.d("Login Server Response", ServerResponse);
                try {
                    JSONObject j = new JSONObject(ServerResponse);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (status.equals("200")) {
                        Toast.makeText(AdminUpdateItemDetailsActivity.this, msg, Toast.LENGTH_LONG).show();
                        AdminUpdateItemDetailsActivity.this.startActivity(new Intent(AdminUpdateItemDetailsActivity.this, AdminAddServiceActivity.class));
                        AdminUpdateItemDetailsActivity.this.finish();
                        return;
                    }
                    Toast.makeText(AdminUpdateItemDetailsActivity.this, msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                showMe.dismiss();
                AdminUpdateItemDetailsActivity.this.NetworkDialog();
                Log.d("Error", String.valueOf(error));
                Toast.makeText(AdminUpdateItemDetailsActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                AdminUpdateItemDetailsActivity adminUpdateItemDetailsActivity = AdminUpdateItemDetailsActivity.this;
                adminUpdateItemDetailsActivity.item_id = adminUpdateItemDetailsActivity.item_id.substring(1, AdminUpdateItemDetailsActivity.this.item_id.length());
                Log.d("data item", AdminUpdateItemDetailsActivity.this.name.getText().toString() + " \n" + AdminUpdateItemDetailsActivity.this.brandname.getText().toString() + " \n" + AdminUpdateItemDetailsActivity.this.gname + " \n" + AdminUpdateItemDetailsActivity.this.qty.getText().toString() + " \n" + AdminUpdateItemDetailsActivity.this.size.getText().toString() + "\n" + AdminUpdateItemDetailsActivity.this.item_id + " \n" + AdminUpdateItemDetailsActivity.this.imgs + " \n" + AdminUpdateItemDetailsActivity.this.item_id);
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", AdminUpdateItemDetailsActivity.this.user_id);
                params.put(SessionManager.KEY_HASH, AdminUpdateItemDetailsActivity.this.hash);
                params.put("name", AdminUpdateItemDetailsActivity.this.name.getText().toString());
                params.put(FirebaseAnalytics.Param.PRICE, AdminUpdateItemDetailsActivity.this.brandname.getText().toString());
                params.put("color", AdminUpdateItemDetailsActivity.this.gname);
                params.put("weight", AdminUpdateItemDetailsActivity.this.qty.getText().toString());
                params.put("size", AdminUpdateItemDetailsActivity.this.size.getText().toString());
                params.put("image", AdminUpdateItemDetailsActivity.this.imgs);
                params.put("item_id", AdminUpdateItemDetailsActivity.this.item_id);
                return params;
            }
        };
        this.stringRequest = r3;
        r3.setTag("TAG");
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 1, 1.0f));
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
                AdminUpdateItemDetailsActivity.this.add_item();
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
        finish();
    }
}
