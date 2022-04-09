package com.adm.studio.apiclient.utils;
import android.content.SharedPreferences;
import android.content.Context;
import com.adm.studio.apiclient.R;

public class PreferencesHelper {

    public static final String TAG = "PreferencesHelper";
	public static final int PUT_STRING = 1;
	public static final int PUT_INT = 2;
	public static final int PUT_BOOLEAN = 3;
	public static final int PUT_FLOAT = 4;
	public static final int PUT_LOGNG = 5;



	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;
	
	


	public static boolean register(Context context) {
		
		
		mSharedPreferences = context
			.getSharedPreferences("adm", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		
		if(mEditor != null && mSharedPreferences != null){
			return true;
		}
		
		return false;
	}

	public static void put(String key, Object value, int type) {
		switch (type) {
			case PUT_STRING:
				mEditor.putString(key, value.toString()).commit();
				break;

			case PUT_INT:
				mEditor.putInt(key, (int)value).commit();
				break;

			case PUT_FLOAT:
				mEditor.putFloat(key, (float)value).commit();
				break;

			case PUT_LOGNG:
				mEditor.putLong(key, (long)value).commit();
				break;

			case PUT_BOOLEAN:
				mEditor.putBoolean(key, (boolean)value).commit();
				break;


		}
	}

	public static String getString(String key) {
		return mSharedPreferences.getString(key, "");
	}
	
	public static int delete(String key) {
		mEditor.remove(key);
	
		return -1;
	}





}
