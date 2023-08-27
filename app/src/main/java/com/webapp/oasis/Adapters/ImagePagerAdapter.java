package com.webapp.oasis.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;

public class ImagePagerAdapter extends PagerAdapter {
    private int[] imageIds;

    public ImagePagerAdapter(int[] imageIds2) {
        this.imageIds = imageIds2;
    }

    public int getCount() {
        return this.imageIds.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(this.imageIds[position]);
        container.addView(imageView);
        return imageView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
