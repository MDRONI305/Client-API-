package com.adm.studio.apiclient.service;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import android.os.Binder;
import android.os.AsyncTask;
import android.net.Uri;
import java.io.InputStream;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.io.IOException;
import android.util.Log;
import com.adm.studio.apiclient.utils.Consent;
import com.adm.studio.apiclient.utils.RequestPackage;
import com.adm.studio.apiclient.utils.HttpHelper;
import com.adm.studio.apiclient.utils.CasheManager;
import com.adm.studio.apiclient.utils.HttpController;
import com.adm.studio.apiclient.utils.PreferencesHelper;
import org.json.JSONObject;

public class MainService extends Service {

	public static final String TAG = "AppService";
	public IBinder mBinder = new LocalBinder();
	//private PreferencesHelper mPreferencesHelper;

	@Override
	public void onCreate() {
		super.onCreate();

		//mPreferencesHelper = new PreferencesHelper(this);
	}


	@Override
	public IBinder onBind(Intent p1) {
		log("onBind");
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public MainService getService() {
			return MainService.this;
		}
	}


	@Override
	public boolean onUnbind(Intent intent) {
		log("onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		log("onRebind");
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}






	public void downloadDataToAPI(RequestPackage request) {


		BackgroundTask task = new BackgroundTask();
		task.execute(request);


	}


	private class BackgroundTask extends AsyncTask<RequestPackage, Integer, Integer> {




		@Override
		protected Integer doInBackground(RequestPackage... requests) {
			log("doInBackground");

			String data = ;

			try {




				data = HttpHelper.downloadUrl(requests[0]);
				syncDataToUI(data);

			} catch (Exception e) {
				e.printStackTrace();
				data = "ddddd" + e.getMessage();      
				syncDataToUI(data);

			} 

			return null;
		}
	}



	public void syncDataToUI(String data) {
		log("syncDataToUI");
		Intent intent = new Intent(Consent.ACTION_SYNC_DATA_TO_UI);
		intent.putExtra(Consent.SERVICE_PAYLOAD, data);	

		LocalBroadcastManager.getInstance(this)
			.sendBroadcast(intent);
	}

	private void syncDataToUI(Exception e) {
		log("syncDataToUI");
		Intent intent = new Intent(Consent.ACTION_SYNC_DATA_TO_UI);
		intent.putExtra(Consent.SERVICE_EXCEPTION, e.getMessage());  

		LocalBroadcastManager.getInstance(this)
			.sendBroadcast(intent);
	}





	private ImageDownloaderImp mImageDownloaderImp;
	public interface ImageDownloaderImp {
		void onImage(Bitmap image);
		void onError(String error);
	}

	public void getImage(String imageUrl, ImageDownloaderImp imp) {
		mImageDownloaderImp = imp;

		Uri uri = Uri.parse(imageUrl);
		String imageName = uri.getLastPathSegment();

		Bitmap imageBitmap = null;

		try {
			imageBitmap = CasheManager.getImage(MainService.this, imageName);
		} catch (Exception e) {

		}
		if (imageBitmap == null) {
			ImageDownloader donloader = new ImageDownloader();
			donloader.execute(imageUrl);

		} else {
			mImageDownloaderImp.onImage(imageBitmap);
		}
	}

	private class ImageDownloader extends AsyncTask<String, Integer, Boolean> {

		private Bitmap mBitmap = null;
		private String mError;

		@Override
		protected Boolean doInBackground(String[] urls) {
			log("doInBackground");

			boolean result = false;
			InputStream inputStream = null;

			try {

				URL imageUrl = new URL(urls[0]);
				inputStream = (InputStream) imageUrl.getContent();

				mBitmap = BitmapFactory.decodeStream(inputStream);

				Uri uri = Uri.parse(urls[0]);
				String imageName = uri.getLastPathSegment();
				result = CasheManager.putImage(MainService.this, mBitmap, imageName);

			} catch (Exception e) {
				mError = "doInBackground1 : " + e.getMessage();

			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						mError = "doInBackground1 : " + e.getMessage();
					} // end try catch
				}
			} // end finally


			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result && mImageDownloaderImp != null) {
				mImageDownloaderImp.onImage(mBitmap);
			} else {
				mImageDownloaderImp.onError(mError);
			}

		}


	}


	public void sendRequestToRestAPI(RequestPackage requestPackage) {

		HttpController httpController = new HttpController();

		httpController.addHttpRequest(requestPackage, new HttpController.OnHttpResponseListener() {
				@Override
				public void onResponseSuccess(String response) {
					//log(response);
					syncDataToUI(response);
				}

				@Override
				public void onResponseError(String error) {
					//log(error);
					syncDataToUI(error);
				}
			});
	}



	// ========================= Log

	public static final StringBuilder getLogMessage = new StringBuilder();
	public static void log(String tag, Object message) {
		Log.d(Consent.TAG, tag + " :\t\t" + message.toString());
		getLogMessage.append(tag + " :\t\t" + message.toString() + "\n");
	}

	// =============== Log
	private void log(Object message) {
		Log.d(Consent.TAG, TAG + message);
	}




	//========== api

	public String createUser(RequestPackage requestPackage) {

		final StringBuilder resultBuilder = new StringBuilder();

		HttpController.addHttpRequest(requestPackage, new HttpController.OnHttpResponseListener(){
				@Override	
				public void onResponseSuccess(String response) {

					String token = getToken(response);

					PreferencesHelper.put(Consent.ACCESS_TOKEN_KEY, token, PreferencesHelper.PUT_STRING);
					PreferencesHelper.put(Consent.USER_LOGGED_KEY, true, PreferencesHelper.PUT_BOOLEAN);

					resultBuilder.append(response);
					syncDataToUI("Register Successfully");
				}

				@Override	
				public void onResponseError(String error) {
					syncDataToUI(error);
					resultBuilder.append(error);

				}
			});


		return resultBuilder.toString();

	}


	public String loginUser(RequestPackage requestPackage) {

		final StringBuilder resultBuilder = new StringBuilder();

		HttpController.addHttpRequest(requestPackage, new HttpController.OnHttpResponseListener(){
				@Override	
				public void onResponseSuccess(String response) {

					String token = getToken(response);

					PreferencesHelper.put(Consent.ACCESS_TOKEN_KEY, token, PreferencesHelper.PUT_STRING);
					PreferencesHelper.put(Consent.USER_LOGGED_KEY, true, PreferencesHelper.PUT_BOOLEAN);


					resultBuilder.append(response);
					syncDataToUI("Login Successfully");
				}

				@Override	
				public void onResponseError(String error) {
					syncDataToUI(error);
					resultBuilder.append(error);

				}
			});


		return resultBuilder.toString();

	}

	public String logoutUser(RequestPackage requestPackage) {

		final StringBuilder resultBuilder = new StringBuilder();

		HttpController.addHttpRequest(requestPackage, new HttpController.OnHttpResponseListener(){
				@Override	
				public void onResponseSuccess(String response) {

					PreferencesHelper.delete(Consent.ACCESS_TOKEN_KEY);
					PreferencesHelper.put(Consent.USER_LOGGED_KEY, false, PreferencesHelper.PUT_BOOLEAN);

					resultBuilder.append("Logout Successfully");
					syncDataToUI(resultBuilder.toString());
				}

				@Override	
				public void onResponseError(String error) {
					syncDataToUI(error);
					resultBuilder.append(error);

				}
			});


		return resultBuilder.toString();

	}


	private static String getToken(String responseToken) {

		try {
			JSONObject ob = new JSONObject(responseToken);
			if (!ob.has("access_token")) {
				return "";
			}

			return ob.getString(Consent.ACCESS_TOKEN_KEY);

		} catch (Exception e) {
			return e.getMessage();
		}

	}



	public  String sendRequestToApi(RequestPackage requestPackage) {

		final StringBuilder resultBuilder = new StringBuilder();

		HttpController.addHttpRequest(requestPackage, new HttpController.OnHttpResponseListener(){
				@Override	
				public void onResponseSuccess(String response) {

					resultBuilder.append(response);
					syncDataToUI(response);
				}

				@Override	
				public void onResponseError(String error) {
					syncDataToUI(error);
					resultBuilder.append(error);
				}
			});

		return resultBuilder.toString();
	}




}

