package com.webapp.oasis;

import android.content.Context;

public class CacheClearer {
    public static void clearCache(Context context) {
        try {
            context.getCacheDir().delete();
            context.getExternalCacheDir().delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
