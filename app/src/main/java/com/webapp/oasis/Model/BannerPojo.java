package com.webapp.oasis.Model;

import com.google.errorprone.annotations.Keep;

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
