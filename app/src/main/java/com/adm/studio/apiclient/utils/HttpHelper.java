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
import org.json.JSONObject;

public class HttpHelper {

	public static final String TAG = "HttpHelper";

	public static String downloadUrl(RequestPackage requestPackage) throws Exception {

		// Authorization: Base {base64_encode(username:password)}

		/*byte[] loginBytes = ("admin:lolx").getBytes();
		 StringBuilder stringBuilder =
		 new StringBuilder()
		 .append("Basic ")
		 .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

		 String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9sb2dpbiIsImlhdCI6MTY0OTI1NDc1MywiZXhwIjoxNjQ5MjU4MzUzLCJuYmYiOjE2NDkyNTQ3NTMsImp0aSI6ImNOVHpuZHZOMUxCMmVuM3kiLCJzdWIiOjcsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.iKVLK4i-VIe4S8F7zJcZ-A8zdBy5TQQlHpK3njJyLCM";

		 byte[] loginBytes = (token).getBytes();
		 StringBuilder stringBuilder =
		 new StringBuilder()
		 .append("Baerer ")
		 .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));*/

		InputStream inputStream = null;
		String response = "";
		String address = requestPackage.getEndPoint();
		String encodeParams = requestPackage.getEndPointParmas();

		// check if request tye GET than parmas encoded
		if (requestPackage.getMethod().equals("GET") && encodeParams.length() > 0) {
			address = String.format("%s?%s", address, encodeParams);
		}

		try {

			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// check if user authentacation
			if (Consent.LOGGING_IN) {
				connection.addRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Authorization", "Bearer " + Consent.ACCESS_TOKEN); // successfully working
				//connection.setRequestProperty("Authorization", stringBuilder.toString());
			}

			// httpurlconnectipn set arrtibute
			connection.setReadTimeout(15000);
			connection.setConnectTimeout(10000);
			connection.setRequestMethod(requestPackage.getMethod());
			connection.setDoInput(true);

			if ((requestPackage.getMethod().equals("POST") | 
				requestPackage.getMethod().equals("PUT")) && encodeParams.length() > 0) {
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
				writer.write(requestPackage.getEndPointParmas());
				writer.flush();
				writer.close();
			}


			connection.connect();
			int responseCode = connection.getResponseCode();


			// httpurlconnectipn error
			if (connection.getErrorStream() != null) {
				return "Error : " + prettyPrintJSON(readStream(connection.getErrorStream()));
			}

			inputStream = connection.getInputStream();
		    response = prettyPrintJSON(readStream(inputStream));

			if (responseCode != 200) {
				return "Erreor : " + response;
			}

			log("Request Url : " + requestPackage.getEndPoint());
			log("Request Params : " + requestPackage.getEndPointParmas());
			log("Request Type : " + requestPackage.getMethod());
			log("Response Code : " + responseCode);
			log("Response : " + response);


			if (requestPackage.contains("/register") | requestPackage.contains("/login")) {

				JSONObject ob = new JSONObject(response);
				if (!ob.has("access_token")) {
					return "Access Token not fpund : " + response;
				}

				Consent.ACCESS_TOKEN = ob.getString("access_token");
				Consent.LOGGING_IN = true;

				String status = requestPackage.contains("/register") ?
					requestPackage.contains("/login") ?
					"fail!" :
					Consent.REGISTRATION_LOG_MESSAGE : 
					Consent.LOGIN_LOG_MESSAGE;

				return "Response : \n[\n" + status + "\n" + response + "\n]";

			} else if (requestPackage.contains("/logout")) {
				Consent.LOGGING_IN = false;
			} 

			return "Response : \n" + response;

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}
		} // end finally
	}

	private static String readStream(InputStream stream) throws IOException {

		log("readStream");

		byte[] buffer = new byte[1024];
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		BufferedOutputStream out = null;

		try {

			int length = 0;
			out = new BufferedOutputStream(byteArray);

			while ((length = stream.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
			// log(byteArray.toString());
			return prettyPrintJSON(byteArray.toString());

		} catch (IOException e) {
			e.printStackTrace();
			log(e.getMessage());
			return "null";

		} finally {

			if (out != null) {
				out.close();
			}
		}
	}

	private static void log(Object ob) {

		Log.d(TAG, ob.toString());
		//System.out.println(ob.toString());

	}

	/**
	 * A simple implementation to pretty-print JSON file.
	 *
	 * @param unformattedJsonString
	 * @return
	 */
	private static String prettyPrintJSON(String unformattedJsonString) {
		StringBuilder prettyJSONBuilder = new StringBuilder();
		int indentLevel = 0;
		boolean inQuote = false;
		for (char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
			switch (charFromUnformattedJson) {
				case '"':
					// switch the quoting status
					inQuote = !inQuote;
					prettyJSONBuilder.append(charFromUnformattedJson);
					break;
				case ' ':
					// For space: ignore the space if it is not being quoted.
					if (inQuote) {
						prettyJSONBuilder.append(charFromUnformattedJson);
					}
					break;
				case '{':
				case '[':
					// Starting a new block: increase the indent level
					prettyJSONBuilder.append(charFromUnformattedJson);
					indentLevel++;
					appendIndentedNewLine(indentLevel, prettyJSONBuilder);
					break;
				case '}':
				case ']':
					// Ending a new block; decrese the indent level
					indentLevel--;
					appendIndentedNewLine(indentLevel, prettyJSONBuilder);
					prettyJSONBuilder.append(charFromUnformattedJson);
					break;
				case ',':
					// Ending a json item; create a new line after
					prettyJSONBuilder.append(charFromUnformattedJson);
					if (!inQuote) {
						appendIndentedNewLine(indentLevel, prettyJSONBuilder);
					}
					break;
				default:
					prettyJSONBuilder.append(charFromUnformattedJson);
			}
		}
		return prettyJSONBuilder.toString();
	}

	/**
	 * Print a new line with indention at the beginning of the new line.
	 *
	 * @param indentLevel
	 * @param stringBuilder
	 */
	private static void appendIndentedNewLine(int indentLevel, StringBuilder stringBuilder) {
		stringBuilder.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			// Assuming indention using 2 spaces
			stringBuilder.append("  ");
		}
	}

	//======================================================



}

