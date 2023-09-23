package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.webapp.oasis.Model.BannerPojo;
import com.webapp.oasis.R;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.annotations.NonNull;

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<BannerPojo> bannerArrayList;

    public BannerAdapter(Context context, ArrayList<BannerPojo> bannerArrayList) {
        this.context = context;
        this.bannerArrayList = bannerArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.banner, container, false);

        BannerPojo bannerPojo = bannerArrayList.get(position);

        ImageView imageView = view.findViewById(R.id.banner_imageview);


        Glide.with(context).load(bannerPojo.getBannerImage()).into(imageView);

        container.addView(view);


        return view;
    }

    @Override
    public int getCount() {

        return bannerArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}
