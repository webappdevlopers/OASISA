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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

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

import id.zelory.compressor.Compressor;

import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

import static android.Manifest.permission_group.CAMERA;

public class DriverEditProfileActivity extends AppCompatActivity {

    Button signin;
    Button btn_add_item;
    EditText name,mobile,place;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String user_id, hash;
    SessionManager session;

    String imgs = "";
    String imagePath;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private File actualImage, compressedImage;
    Bitmap bitmap;
    String mCurrentPhotoPath;
    ImageView back, image, list;
    String color,imgurl,item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_profile);

        requestPermission();

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        Log.d("User id",user_id);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        place = findViewById(R.id.place);

        btn_add_item = findViewById(R.id.btn_edit);
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                startActivity(new Intent(DriverEditProfileActivity.this,DriverProfileActivity.class));
                finish();
            }
        });


        Intent i = getIntent();
        name.setText(i.getStringExtra("name"));
        mobile.setText(i.getStringExtra("mobile"));
        place.setText(i.getStringExtra("place"));
        Glide.with(getApplicationContext()).load(i.getStringExtra("image"))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);
        convertUrlToBase64(i.getStringExtra("image"));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverEditProfileActivity.this);
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
                                    photoURI = FileProvider.getUriForFile(DriverEditProfileActivity.this,
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
        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty())
                {
                    Toast.makeText(DriverEditProfileActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if (mobile.getText().toString().isEmpty())
                {
                    Toast.makeText(DriverEditProfileActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else if (place.equals("null"))
                {
                    Toast.makeText(DriverEditProfileActivity.this, "Enter Place", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (imgs == null) {
                        imgs = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5OjcBCgoKDQwNGg8PGjclHyU3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3N//AABEIAIsAiwMBIgACEQEDEQH/xAAbAAEAAgMBAQAAAAAAAAAAAAAABAUBAgMGB//EADUQAAICAAQEBAQDCAMAAAAAAAABAgMEBREhEjFBUSIyYXETUoGRQoLRFTRTYnKhsfAUIzP/xAAWAQEBAQAAAAAAAAAAAAAAAAAAAQL/xAAYEQEBAQEBAAAAAAAAAAAAAAAAAREhMf/aAAwDAQACEQMRAD8A+4gAAAAAAAAwayurh57Ir3YG4OH/AC8P/Gh9zaOJolyug/zAdQYTTWqexkAAAAAAAAAAAABpbZGqDlN6JAbNpLVvRepBxGZQi3GpcT+boQsXi54h6LWNfSP6kYuDtbirrfPY/ZbI5GAVAABG8LJ1vWE5R9mTKMynDRXLjXdbMgGQr0FN0Lo8Vck1/dHQ87XZKqanW2pLqXGDxccQtHtYua7+qJYqUACAAAAAAw3otXyKTG4l4ixpPSEeS7+pNzW9wrVUH4pc/YqSwAAVlkm4bL5WJTtfBF8l1ZpltCuv1lvGG/1LolWIqy/DJeRv8zON+Wwa1pk4vs90WAIrztlcqpuE46NGhc5lQrKONeaG/wBCmNIG0JOElKL0a3TNQBfYTELEVKXKS2kvU7lFgb/gXxbfhltIvSVQAEAA0ulwUzl2i2BR4uz4uJnP10XscR0BpkAAFnk2nDb31RZFJl96pv8AFtGWz9C7JWgAEHPEafAs15cD/wAHni4zO9Qp+GvNP+yKcsSgAKgy9wNvxcNCT5rZ/Qoi0yeXgsh2af8Av2FVYgAyocMb+62/0nc5YqPFhrV/KwPPgLdA0yBvQBrUKxqTcLmMqkoWJzguXdHCjC23r/rjt8z5Ev8AZMuHVWri7abASY5nhn+KS/Kcb81glpTBt95dCO8rv7x+jOkMqse05xivTcnFQJ2SnOUpycpPqNUS78ttrTcfGvTn9iI4ac00/UqMmAtkABYZP/62/wBKK8ssnjvbL2QoswAZUMNJpp8mZAHnbYOu2cH0ehoT81p4bValtLZ+5ANIFjgcDxJWXrbpHucstoV1vFPyw307suSWjCSS0WyMgEUAAAi4rB14ha6KM/mJQA87bXOqbhYtGjQucxw8baXNaKUFrr3RTGkC5yuvgw2vzPUqaa3bbGuPOTPQQioQUYrRJaJEpGwAIoAAOeIqjdVKuXJr7MobISqm4TXiT3PREXHYRYiGsdFYls+/oyyiootnTYpwe66dGXeGxEMRDWPNc49iilCUJOMk1Jc0xGUoSUoScWuqLiPRgrKMz02vjr/NH9CZXi6LPLZH2exlXcGqnF8pJ/U1nfVDz2RXuwOhiUlGLlJpJdWQrsyqjtWnN/ZFdiMTbe/HLw9IrkXB3x+Md+sK9VWub7kIE/AYP4jVlq8HNRfUviO2WYZwh8aa8UvKuyLAAyoAAAAAAADhicLXiI+JaSXKS5oqcRg7aG9YuUfmRegujzQL23B0WvVwSfeOxGnlUfwWte61LqKsFh+y5/xY/Y2jlT/Fb9ojTqtN66p2y4a4uT9C2ry6iG74pv1ZKhCMIqMEkl0RNEHC5fGDU7vFJco9EWABFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH/2Q==";
                    }
                    update_driver_profile();
                }
            }
        });
    }
    private void update_driver_profile() {

        final ProgressDialog showMe = new ProgressDialog(DriverEditProfileActivity.this);
        showMe.setMessage("Please Wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        String url = Config.update_driver_profile;
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

                                Toast.makeText(DriverEditProfileActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DriverEditProfileActivity.this, DriverProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(DriverEditProfileActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMe.dismiss();
                        NetworkDialog();
                        Log.d("Error", String.valueOf(error));
                        Toast.makeText(DriverEditProfileActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", user_id);
                params.put("hash", hash);
                params.put("name", name.getText().toString());
                params.put("place", place.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("driver_image", imgs);

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        mRequestQueue.add(stringRequest);

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(DriverEditProfileActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                update_driver_profile();
            }
        });
        dialogs.show();
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(DriverEditProfileActivity.this, new String[]
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DriverEditProfileActivity.this,DriverProfileActivity.class));
        finish();
    }
}