package com.webapp.oasis.Notification;

import java.util.Random;

public class Config1 {

    public static final String TOPIC_GLOBAL = "global";


    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";


    /*public static final int NOTIFICATION_ID = 100;*/
    public static final int NOTIFICATION_ID = new Random().nextInt(3000);
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
