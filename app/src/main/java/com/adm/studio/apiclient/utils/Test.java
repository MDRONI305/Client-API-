package com.adm.studio.apiclient.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import android.util.Base64;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Test {

	public static final String TAG = "HttpHelper";


	String text = "{\"status\":\"error\",\"message\":\"not found\"}";

	


	private static String jsonFormate(final String text) {
		StringBuilder b = new StringBuilder();

		int a = 0;

		for (char c : text.toCharArray()) {
			
			switch(c) {
				case '{':
					a++;
					 nl(a,b);
					break;
					
			}
			b.append(c);
		}

		return b.toString();
	}
	
	private static void nl(int indentLevel, StringBuilder stringBuilder) {
		stringBuilder.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			// Assuming indention using 2 spaces
			stringBuilder.append("  ");
		}
	}
}

