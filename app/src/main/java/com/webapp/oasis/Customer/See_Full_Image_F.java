package com.webapp.oasis.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.webapp.oasis.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class See_Full_Image_F extends AppCompatActivity {

    ImageButton close_gallery;
    ImageView single_image;
    String image_url = "";
    ProgressBar p_bar;
    int width, height;
    ImageView whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see__full__image__f);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Intent i = getIntent();
        image_url = i.getStringExtra("imageurl");
        Log.d("image url", image_url);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkIfAlreadyhavePermission();
        }
        whatsapp = findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);

                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                Intent share = new Intent(Intent.ACTION_SEND);

                share.setType("image/*");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        b, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/SafeParcel.png"));
                startActivity(Intent.createChooser(share, "Select"));
            }
        });
        close_gallery = findViewById(R.id.close_gallery);
        close_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        p_bar = findViewById(R.id.p_bar);

        single_image = findViewById(R.id.single_image);

        p_bar.setVisibility(View.GONE);
        Glide.with(getApplicationContext()).load(image_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(single_image);
        PhotoViewAttacher photoAttacher;
        photoAttacher= new PhotoViewAttacher(single_image);
        photoAttacher.update();
    }
    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        Date currentTime = Calendar.getInstance().getTime();
        String s = String.valueOf(currentTime);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/SafeParcel.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    @SuppressLint("NewApi")
    private boolean checkIfAlreadyhavePermission() {

        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        }
        return false;
    }
}


