package com.adm.studio.apiclient.viewmodel;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.content.ComponentName;
import com.adm.studio.apiclient.service.MainService;
import android.content.Context;
import android.content.Intent;
import com.adm.studio.apiclient.utils.Log;
import com.adm.studio.apiclient.utils.PreferencesHelper;
import com.adm.studio.apiclient.utils.Consent;
import com.adm.studio.apiclient.R;
import com.adm.studio.apiclient.model.User;
import com.adm.studio.apiclient.utils.RequestPackage;
import com.adm.studio.apiclient.utils.HttpController;
import org.json.JSONObject;

public class MainServiceViewModel {

    public static final String TAG = "ServiceViewModel";
	private static final PreferencesHelper mPreferencesHelper = null;


	// ============= AppService 
	private static MainService mService;
	private static ServiceConnection  mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName p1, IBinder p2) {

			mService = ((MainService.LocalBinder)p2).getService();
			
			
			log("onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName p1) {
		}
	};

	public static boolean serviceRegister(Context context) {
		Intent serviceIntent = new Intent(context, MainService.class);
		return context.bindService(serviceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
	}


	public void onDistroy() {

	}

	private static void log(Object ob) {
		Log.d(TAG, ob == null ? "": ob.toString());
	}



	//========== api

	public static void createUser(RequestPackage requestPackage) {
		mService.createUser(requestPackage);
	}


	public static void loginUser(RequestPackage requestPackage) {
		mService.loginUser(requestPackage);
	}
	
	public static void logoutUser(RequestPackage requestPackage) {
		mService.logoutUser(requestPackage);
	}

	
	public static void sendRequestToApi(RequestPackage requestPackage) {
		mService.sendRequestToApi(requestPackage);
		
	}
	


}
