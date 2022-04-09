package com.adm.studio.apiclient.utils;
import android.util.Log;
import java.nio.charset.StandardCharsets;

public class Log {

    public static final String TAG = "adm.studio";
    public static final StringBuilder getLogs = new StringBuilder();


	public static final void d(String tag, Object message) {
		Log.d(TAG, tag + " : \t\t" + message.toString());
		getLogs.append(tag + " : \t\t" + message.toString() + "\n");
	
		
		
		String data = "{\n	\"employee\":{ \"name\":\"Emma\", \"age\":28, \"city\":\"Boston\" }\n} ";

		byte[] out = data.getBytes(StandardCharsets.UTF_8);
	}



}
