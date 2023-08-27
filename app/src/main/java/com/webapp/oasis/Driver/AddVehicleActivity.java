package com.webapp.oasis.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import id.zelory.compressor.Compressor;

import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

import static android.Manifest.permission_group.CAMERA;

public class AddVehicleActivity extends AppCompatActivity {

    EditText licence_no, vehical_no, costpkg, minodrpkg, maxodrpkg;
    ImageView back, image, list;
    Button btn_vehicle_submit;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String user_id, hash;

    String imgs = "";
    String imagePath;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private File actualImage, compressedImage;
    Bitmap bitmap;
    String mCurrentPhotoPath;
    private Spinner mySpinnerS, mySpinnerD;
    private ArrayList<String> flats1 = new ArrayList<String>();
    private ArrayList<String> flatids1 = new ArrayList<String>();
    private ArrayList<String> flats = new ArrayList<String>();
    private ArrayList<String> flatids = new ArrayList<String>();
    String spin_val1="null", spin_val2="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        requestPermission();

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);

        costpkg = findViewById(R.id.costpkg);
        minodrpkg = findViewById(R.id.minodrpkg);
        maxodrpkg = findViewById(R.id.maxodrpkg);

        list = findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddVehicleActivity.this, VehicleListActivity.class);
                startActivity(intent);
            }
        });
        licence_no = findViewById(R.id.licence_no);
        vehical_no = findViewById(R.id.vehical_no);
        btn_vehicle_submit = findViewById(R.id.btn_vehicle_submit);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btn_vehicle_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (licence_no.getText().toString().isEmpty() || licence_no.getText().toString().length()<15) {
                    Toast.makeText(AddVehicleActivity.this, "Enter Valid Licence Number", Toast.LENGTH_SHORT).show();
                } else if (vehical_no.getText().toString().isEmpty()  || vehical_no.getText().toString().length()<10) {
                    Toast.makeText(AddVehicleActivity.this, "Enter Valid Vehicle Number", Toast.LENGTH_SHORT).show();
                }
                else if (spin_val1.equals("null"))
                {
                    Toast.makeText(AddVehicleActivity.this, "Select Source", Toast.LENGTH_SHORT).show();
                }
                else  if (spin_val2.equals("null"))
                {
                    Toast.makeText(AddVehicleActivity.this, "Select Destination", Toast.LENGTH_SHORT).show();
                }
                else if (costpkg.getText().toString().isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Enter Cost Per Kg", Toast.LENGTH_SHORT).show();
                }
              /*  else if (minodrpkg.getText().toString().isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Enter Minimum order in Kg", Toast.LENGTH_SHORT).show();
                }
                else if (maxodrpkg.getText().toString().isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Enter Maximum Order in Kg", Toast.LENGTH_SHORT).show();
                }*/
                else if (imgs==null) {
                    Toast.makeText(AddVehicleActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    add_vehical();
                }
            }

        });

        image = findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddVehicleActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                actualImage = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                actualImage = null;
                                mCurrentPhotoPath = null;
                            }
                            if (actualImage != null) {
                                Uri photoURI = null;
                                if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)) {
                                    photoURI = FileProvider.getUriForFile(AddVehicleActivity.this,
                                            "itkida.rastasuraksha.app", actualImage);
                                } else {
                                    photoURI = Uri.fromFile(actualImage);
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, 1);
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        }
                    }
                });
                builder.show();
            }
        });
        mySpinnerS = (Spinner) findViewById(R.id.spngender);
        flats.add("Select Source");
        flatids.add("0");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_list_item_1, flats);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mySpinnerS.setAdapter(adapter);

        mySpinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!flats1.get(position).equals("Select Source")) {
                    spin_val1 = flatids.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mySpinnerD = (Spinner) findViewById(R.id.spngenderD);
        flats1.add("Select Destination");
        flatids1.add("0");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_list_item_1, flats1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mySpinnerD.setAdapter(adapter1);

        mySpinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!flats1.get(position).equals("Select Destination")) {
                    spin_val2 = flatids1.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Source();
        Destination();

    }
    public void Source() {

        final ProgressDialog showMe = new ProgressDialog(AddVehicleActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);

        String url = Config.city_list;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flats.clear();
                        flatids.clear();
                        flats.add("Select Source");
                        flatids.add("0");
                        showMe.dismiss();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            Log.d("Response city S list", response);

                            String status = j.getString("status");
                            if (status.equals("200")) {

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {
                                        JSONObject getOne = applist.getJSONObject(i);
                                        flats.add(getOne.getString("name"));
                                        flatids.add(getOne.getString("id"));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_list_item_1, flats);
                                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                    mySpinnerS.setAdapter(adapter);
                                }
                                showMe.dismiss();

                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showMe.dismiss();
                        Toast.makeText(AddVehicleActivity.this, "Internet not connected", Toast.LENGTH_LONG).show();
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
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void Destination() {

        final ProgressDialog showMe = new ProgressDialog(AddVehicleActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);

        String url = Config.city_list;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flats1.clear();
                        flatids1.clear();
                        flats1.add("Select Destination");
                        flatids1.add("0");
                        showMe.dismiss();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            Log.d("Response city D list", response);

                            String status = j.getString("status");
                            if (status.equals("200")) {

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {
                                        JSONObject getOne = applist.getJSONObject(i);
                                        flats1.add(getOne.getString("name"));
                                        flatids1.add(getOne.getString("id"));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_list_item_1, flats1);
                                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                    mySpinnerD.setAdapter(adapter);
                                }
                                showMe.dismiss();
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showMe.dismiss();
                        Toast.makeText(AddVehicleActivity.this, "Internet not connected", Toast.LENGTH_LONG).show();
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
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
    private void add_vehical() {

        final ProgressDialog showMe = new ProgressDialog(AddVehicleActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.add_vehical;
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();
                        Log.d("Login Server Response", ServerResponse);

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {

                                Intent intent = new Intent(AddVehicleActivity.this, DriverHomeActivity.class);
                                startActivity(intent);

                                Toast.makeText(AddVehicleActivity.this, "Vehicle Added", Toast.LENGTH_LONG).show();

                            }
                       else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(AddVehicleActivity.this, msg, Toast.LENGTH_LONG).show();
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
              //          Toast.makeText(AddVehicleActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", user_id);
                params.put("hash", hash);
                params.put("licence_no", licence_no.getText().toString());
                params.put("vehical_no", vehical_no.getText().toString());
                params.put("source", spin_val1);

                params.put("destination", spin_val2);
                params.put("costpkg", costpkg.getText().toString());
                params.put("minodrpkg", "1");
                params.put("maxodrpkg", maxodrpkg.getText().toString());
              //  params.put("discount", "0%");
                params.put("image", imgs);

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
        final Dialog dialogs = new Dialog(AddVehicleActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                add_vehical();
            }
        });
        dialogs.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddVehicleActivity.this,DriverHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AddVehicleActivity.this, new String[]
                {CAMERA, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (requestCode == 1) {
                        customdocCompressImage();
                    }
                    if (resultCode == 2) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(filePathColumn[0]);
                        imagePath = cursor.getString(index);
                        cursor.close();
                        image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                        bitmap = BitmapFactory.decodeFile(imagePath);
                        imgs = getStringImage(bitmap);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    if (imageReturnedIntent != null) {

                        Uri filePath = imageReturnedIntent.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                            Glide
                                    .with(this)
                                    .load(bitmap)
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.ic_camera_alt_black_24dp)
                                            .fitCenter())
                                    .into(image);

                            imgs = getStringImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
        }
    }

    public void customdocCompressImage() {
        if (actualImage == null) {
        } else {
            try {
                compressedImage = new Compressor(getApplicationContext())
                        .setMaxWidth(540)
                        .setMaxHeight(500)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(actualImage);

                setdocCompressedImage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void setdocCompressedImage() {
        bitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(actualImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            //   profile.setImageBitmap(bitmap);
            Glide
                    .with(this)
                    .load(bitmap)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_camera_alt_black_24dp)
                            .fitCenter())
                    .into(image);
            imgs = getStringImage(bitmap);

            Log.d("image base64 : ", imgs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}