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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.logging.type.LogSeverity;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import id.zelory.compressor.Compressor;

public class AdmiItemActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /* access modifiers changed from: private */
    public File actualImage;
    ImageView back;
    Bitmap bitmap;
    Button btn_add_item;
    Button btnlist;
    private File compressedImage;
    Spinner destination;
    ArrayList<String> genderid = new ArrayList<>();
    ArrayList<String> gendername = new ArrayList<>();
    String gid;
    String gname = "null";
    String hash;
    ImageView image;
    String imagePath;
    String imgs = "";
    ImageView list;
    String mCurrentPhotoPath;
    RequestQueue mRequestQueue;
    EditText name;
    EditText price;
    SessionManager session;
    Button signin;
    EditText size;
    Spinner source;
    StringRequest stringRequest;
    String user_id;
    EditText weight;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_admi_item);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
//        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
        Log.d("User id", this.user_id);
        Log.d("Hash id", this.hash);
        this.name = (EditText) findViewById(R.id.name);
        this.price = (EditText) findViewById(R.id.price);
        this.weight = (EditText) findViewById(R.id.weight);
        this.size = (EditText) findViewById(R.id.size);
        this.source = (Spinner) findViewById(R.id.spncolor);
        this.btn_add_item = (Button) findViewById(R.id.btn_add_item);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdmiItemActivity.this.onBackPressed();
                AdmiItemActivity.this.finish();
            }
        });
        this.source = (Spinner) findViewById(R.id.spncolor);
        this.gendername.add("Select Color");
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
                if (!AdmiItemActivity.this.gendername.get(position).equals("Select Color")) {
                    AdmiItemActivity admiItemActivity = AdmiItemActivity.this;
                    admiItemActivity.gid = admiItemActivity.genderid.get(position);
                    if (AdmiItemActivity.this.gid == "1") {
                        AdmiItemActivity.this.gname = "Red";
                    }
                    if (AdmiItemActivity.this.gid == ExifInterface.GPS_MEASUREMENT_2D) {
                        AdmiItemActivity.this.gname = "Black";
                    }
                    if (AdmiItemActivity.this.gid == ExifInterface.GPS_MEASUREMENT_3D) {
                        AdmiItemActivity.this.gname = "Silver";
                    }
                    if (AdmiItemActivity.this.gid == "4") {
                        AdmiItemActivity.this.gname = "Orange";
                    }
                    if (AdmiItemActivity.this.gid == "5") {
                        AdmiItemActivity.this.gname = "Grey";
                    }
                    if (AdmiItemActivity.this.gid == "6") {
                        AdmiItemActivity.this.gname = "Blue";
                    }
                    Log.d("cid", AdmiItemActivity.this.gid);
                    Log.d("cname", AdmiItemActivity.this.gname);
                    return;
                }
                Toast.makeText(AdmiItemActivity.this, "Select Color", Toast.LENGTH_LONG);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Button button = (Button) findViewById(R.id.btnlist);
        this.btnlist = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdmiItemActivity.this.startActivity(new Intent(AdmiItemActivity.this, AdminAddServiceActivity.class));
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.rluploadAdhaarcard);
        this.image = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdmiItemActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Uri photoURI;
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            try {
                                File unused = AdmiItemActivity.this.actualImage = AdmiItemActivity.this.createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                File unused2 = AdmiItemActivity.this.actualImage = null;
                                AdmiItemActivity.this.mCurrentPhotoPath = null;
                            }
                            if (AdmiItemActivity.this.actualImage != null) {
                                if (Build.VERSION.SDK_INT > 19) {
                                    photoURI = FileProvider.getUriForFile(AdmiItemActivity.this, "itkida.rastasuraksha.app", AdmiItemActivity.this.actualImage);
                                } else {
                                    photoURI = Uri.fromFile(AdmiItemActivity.this.actualImage);
                                }
                                intent.putExtra("output", photoURI);
                                AdmiItemActivity.this.startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            AdmiItemActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                        }
                    }
                });
                builder.show();
            }
        });
        this.btn_add_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdmiItemActivity.this.name.getText().toString().isEmpty()) {
                    Toast.makeText(AdmiItemActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (AdmiItemActivity.this.price.getText().toString().isEmpty()) {
                    Toast.makeText(AdmiItemActivity.this, "Enter Price", Toast.LENGTH_SHORT).show();
                } else if (AdmiItemActivity.this.gname.equals("null")) {
                    Toast.makeText(AdmiItemActivity.this, "Select Color", Toast.LENGTH_SHORT).show();
                } else if (AdmiItemActivity.this.imgs == null) {
                    Toast.makeText(AdmiItemActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                } else if (AdmiItemActivity.this.weight.getText().toString().isEmpty()) {
                    Toast.makeText(AdmiItemActivity.this, "Enter Weight", Toast.LENGTH_SHORT).show();
                } else if (AdmiItemActivity.this.size.getText().toString().isEmpty()) {
                    Toast.makeText(AdmiItemActivity.this, "Enter Size", Toast.LENGTH_SHORT).show();
                } else {
                    AdmiItemActivity.this.add_item();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void add_item() {
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please Wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        this.mRequestQueue = Volley.newRequestQueue(this);
        StringRequest r3 = new StringRequest(1, Config.add_item, new Response.Listener<String>() {
            public void onResponse(String ServerResponse) {
                showMe.dismiss();
                Log.d("Login Server Response", ServerResponse);
                try {
                    JSONObject j = new JSONObject(ServerResponse);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (status.equals("200")) {
                        Toast.makeText(AdmiItemActivity.this, msg, Toast.LENGTH_LONG).show();
                        AdmiItemActivity.this.startActivity(new Intent(AdmiItemActivity.this, AdmiItemActivity.class));
                        AdmiItemActivity.this.finish();
                        return;
                    }
                    Toast.makeText(AdmiItemActivity.this, msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showMe.dismiss();
                AdmiItemActivity.this.NetworkDialog();
                Log.d("Error", String.valueOf(error));
                Toast.makeText(AdmiItemActivity.this, "Not connected to internet", Toast.LENGTH_LONG).show();
            }
        }) {
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", AdmiItemActivity.this.user_id);
                params.put(SessionManager.KEY_HASH, AdmiItemActivity.this.hash);
                params.put("name", AdmiItemActivity.this.name.getText().toString());
                params.put(FirebaseAnalytics.Param.PRICE, AdmiItemActivity.this.price.getText().toString());
                params.put("color", AdmiItemActivity.this.gname);
                params.put("weight", AdmiItemActivity.this.weight.getText().toString());
                params.put("size", AdmiItemActivity.this.size.getText().toString());
                params.put("image", AdmiItemActivity.this.imgs);
                return params;
            }
        };
        this.stringRequest = r3;
        r3.setTag("TAG");
        this.stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));
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
                AdmiItemActivity.this.add_item();
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
