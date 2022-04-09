package com.adm.studio.apiclient.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {
    
    public static final String TAG = "NetworkHelper";
    
    public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager manager = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);

		try {

			NetworkInfo info = manager.getActiveNetworkInfo();

			return info != null && info.isAvailable()|info.isConnected();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



}

