package com.ekspeace.eggs.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ekspeace.eggs.Model.User;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static Calendar currentDate = Calendar.getInstance();
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", Locale.getDefault());
    public static String UserEmailKey = "EMAIL_KEY";
    public static String UserPasswordKey = "PASSWORD_KEY";
    public static final double DELIVERY = 9.50;
    public static double PRICE = 12.00;

    public static Boolean isOnline(Context context)	{
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}
