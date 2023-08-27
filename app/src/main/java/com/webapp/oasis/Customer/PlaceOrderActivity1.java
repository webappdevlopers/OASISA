package com.webapp.oasis.Customer;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

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

import id.zelory.compressor.Compressor;

import com.webapp.oasis.Model.After_Place_Order_Successs_Model;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

import static android.Manifest.permission_group.CAMERA;

public class PlaceOrderActivity1 extends AppCompatActivity implements PaymentResultListener {

    String did, dmin, dmax;
    TextView dname, ddest;
    String source,destination;
    TextView tvtotal, tvgst, tvsubtotal;
    ImageView dimage, image;
    TextView edtqty;

    String user_id, hash, user_add, mob;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageView back;
    Float finalamount;
    String transactionid = "", amount = "";
    Button btn_place_order;

    int item_total_price = 0;
    String item_id2;
    ArrayList<After_Place_Order_Successs_Model> dm = new ArrayList<After_Place_Order_Successs_Model>();
    DatabaseHelper mydb;
    String order_id;
    int Final_amount, Nerkar_amont, Driver_amount;
    RadioButton radioCOD, radioONLINE;
    String payment_mtd = "0", trans_id = "-";
    EditText name, address, mobile;

    String imgs = "";
    String imagePath;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private File actualImage, compressedImage;
    Bitmap bitmap;
    String mCurrentPhotoPath;

    TextView weight,qty,custaddress,recaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order1);

        mydb = new DatabaseHelper(getApplicationContext());

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        user_add = users.get(session.KEY_PLACE);
        mob = users.get(session.KEY_MOB);

        weight = findViewById(R.id.weight);
        qty = findViewById(R.id.qty);
        custaddress = findViewById(R.id.custaddress);
        recaddress = findViewById(R.id.recaddress);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        radioCOD = findViewById(R.id.radioCOD);
        radioONLINE = findViewById(R.id.radioONLINE);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (R.id.radioCOD == checkedId) {
                        payment_mtd = "1";
                    } else if (R.id.radioONLINE == checkedId) {
                        payment_mtd = "2";
                    }
//                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }


        tvtotal = findViewById(R.id.tvtotal);
        tvgst = findViewById(R.id.tvgst);
        tvsubtotal = findViewById(R.id.tvsubtotal);
        btn_place_order = findViewById(R.id.btn_place_order);

        //driver details
        dname = findViewById(R.id.dname);
        ddest = findViewById(R.id.ddest);
        dimage = findViewById(R.id.dimage);

        Intent i = getIntent();
        did = (i.getStringExtra("did"));
        dname.setText(i.getStringExtra("dname"));
        ddest.setText(i.getStringExtra("ddest"));
        custaddress.setText(i.getStringExtra("source"));
        recaddress.setText(i.getStringExtra("destination"));
        source=(i.getStringExtra("source"));
        destination=(i.getStringExtra("destination"));
        Glide.with(getApplicationContext()).load(i.getStringExtra("dimage"))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(dimage);

        //item details

        edtqty = findViewById(R.id.edtqty);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);
        image = findViewById(R.id.ProfilePicture);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrderActivity1.this);
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
                                    photoURI = FileProvider.getUriForFile(PlaceOrderActivity1.this,
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
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(PlaceOrderActivity1.this, "Receiver Name!", Toast.LENGTH_SHORT).show();
                } else if (mobile.getText().toString().isEmpty()) {
                    Toast.makeText(PlaceOrderActivity1.this, "Receiver Mobile Number!", Toast.LENGTH_SHORT).show();
                } else if (qty.getText().toString().isEmpty()) {
                    Toast.makeText(PlaceOrderActivity1.this, "Quantity!", Toast.LENGTH_SHORT).show();
                } else if (weight.getText().toString().isEmpty()) {
                    Toast.makeText(PlaceOrderActivity1.this, "Weight!", Toast.LENGTH_SHORT).show();
                } else if (imgs.equals("")) {
                    Toast.makeText(PlaceOrderActivity1.this, "Parcel Image!", Toast.LENGTH_SHORT).show();
                } else {
                    place_order_for_driver();
                }

            }
        });


    }

    private void place_order_for_driver() {
        final ProgressDialog showMe = new ProgressDialog(PlaceOrderActivity1.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.place_order_for_driver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            dm.clear();
                            if (status.equals("200")) {
                                Toast.makeText(PlaceOrderActivity1.this, "Placed Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PlaceOrderActivity1.this,CustomerOrderDetailActivity.class);
                                startActivity(intent);

                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(PlaceOrderActivity1.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();
                            //      Toast.makeText(CelebrityListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        volleyError.printStackTrace();
                        Log.d("eerror", String.valueOf(volleyError));

                        showMe.dismiss();
                        NetworkDialoguser_reviewd_details();
                        // Toast.makeText(StampOfferActivity.this, "No Connection", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                Log.d("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                Log.d("user_id", user_id);
                Log.d("hash", hash);
                Log.d("receiver_name", name.getText().toString());
                Log.d("mobile", mobile.getText().toString());
                Log.d("driver_id", did);
                Log.d("source", source);
                Log.d("destination",destination);
                Log.d("qty", qty.getText().toString());
                Log.d("weight", weight.getText().toString());
                Log.d("receiver_address", recaddress.getText().toString());
                Log.d("parcel_img", imgs);
                Log.d("===============", "imgs");
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", user_id);
                params.put("hash", hash);
                params.put("receiver_name", name.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("driver_id", did);
                params.put("source", source);
                params.put("destination", destination);
                params.put("qty", qty.getText().toString());
                params.put("weight", weight.getText().toString());
                params.put("receiver_address", recaddress.getText().toString());
                params.put("parcel_img", imgs);
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

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(PlaceOrderActivity1.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                place_order_for_driver();
            }
        });
        dialogs.show();
    }


    @Override
    public void onPaymentSuccess(String s) {

        transactionid = s;
        // payment successfull pay_DGU19rDsInjcF2
        Log.e("TAG", " payment successfull " + s.toString());
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        //tvview.setText("");
        trans_id = s.toString();
        //success_payment(trans_id);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PlaceOrderActivity1.this, new String[]
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

    public String convertUrlToBase64(String url) {
        URL newurl;
        Bitmap bitmap;
        String base64 = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgs = base64;
        return base64;
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