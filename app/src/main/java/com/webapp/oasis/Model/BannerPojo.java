package com.webapp.oasis.Model;

import androidx.annotation.Keep;

@Keep
public class BannerPojo {

    private String BannerImage;

    public BannerPojo() {

    }

    public String getBannerImage() {
        return BannerImage;
    }

    public void setBannerImage(String bannerImage) {
        BannerImage = bannerImage;
    }
}
