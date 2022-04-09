package com.adm.studio.apiclient.utils;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import android.util.Base64;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class HttpController {

    public static final String TAG = "HttpController";

	private static OnHttpResponseListener mListener;
	private static RequestPackage mRequestPackage = null;


	public static void addHttpRequest(final RequestPackage requestPackage,  OnHttpResponseListener listener) {
		mListener = listener;
		mRequestPackage = requestPackage;

		new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						String response = sendRequest(requestPackage);

						if (mListener != null) {
							mListener.onResponseSuccess(response);
						}


					} catch (Exception e) {
						if (mListener != null) {
							mListener.onResponseError(e.getMessage());
						}
					}// end try catch

				}// end run
			}).start();

	}

	private static String sendRequest(RequestPackage requestPackage) throws Exception {

		String response = "";
		InputStream inputStream = null;

		// check if mRequestPackage is information
		if (requestPackage == null) {
			throw new Exception("Error : requestPackage cano't be null");
		} else if (requestPackage.getEndPoint().length() == 0) {
			throw new Exception("Error : endpoint cano't be null");
		}

		String address = requestPackage.getEndPoint();
		String encodeParams = requestPackage.getEndPointParmas();

		// check if request tye GET than parmas encoded
		if (requestPackage.getMethod().equals("GET") && encodeParams.length() > 0) {
			address = String.format("%s?%s", address, encodeParams);
		}

		URL url = new URL(address);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(15000);
		connection.setConnectTimeout(10000);
		connection.setRequestMethod(requestPackage.getMethod());
		connection.setDoInput(true);

		// check if user authentacation
		if (requestPackage.getEncodeAuthorization().length() > 0) {
			connection.addRequestProperty("Content-Type", "application/json");
			connection.addRequestProperty("Authorization", requestPackage.getEncodeAuthorization());

			//connection.setRequestProperty("Authorization", "Bearer " + Consent.ACCESS_TOKEN); // successfully working
			//connection.setRequestProperty("Authorization", stringBuilder.toString());
		}

		// check if request type 
		if ((requestPackage.getMethod().equals("POST") | 
			requestPackage.getMethod().equals("PUT")) && encodeParams.length() > 0) {
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(requestPackage.getEndPointParmas());
			writer.flush();
			writer.close();
		}

		//finaly httpurlconnection is connect
		connection.connect();

		if (connection.getErrorStream() != null) {
			throw new Exception(readStream(connection.getErrorStream()));
		}

		inputStream = connection.getInputStream();
		response =  readStream(inputStream);

		if (inputStream != null) {
			inputStream.close();
		}

		return response;
	}


//	public void addOnHttpResponseListener(OnHttpResponseListener listener) {
//		mListener = listener;
//	}

	public static interface OnHttpResponseListener {

		public void onResponseSuccess(String response);
		public void onResponseError(String error);

	}

	public String getUrl() {
		String url = mRequestPackage.getEndPoint().equals("") ? "": mRequestPackage.getEndPoint();
		return url;
	}

	public String getEncodUrl() {

		String encodUrl = "";

		if (mRequestPackage.getEndPoint().length() == 0
			| mRequestPackage.getMethod().length() == 0
			| mRequestPackage.getEndPointParmas().length() == 0) {

			return "";
		}

		encodUrl = mRequestPackage.getEndPoint();

		// check if request method GET is url encoded
		if (mRequestPackage.getMethod().equals(RequestPackage.GET)) {
			encodUrl = String.format("%s?%s"
									 , mRequestPackage.getEndPoint()
									 , mRequestPackage.getEndPointParmas());
		}


		if (mRequestPackage.getMethod().equals(RequestPackage.POST)
			| mRequestPackage.getMethod().equals(RequestPackage.PUT)) {

		}


		return encodUrl;
	}


	// Authorization class
	public  class Authorization {

		public static final String BEARER = "Bearer";
		public static final String BASIC = "Basic";
		private String encodeAuthorizationValue;

		public Authorization(String authorizationType, String authorizationValue) {

			switch (authorizationType) {
				case BASIC:
					encodeAuthorizationValue = encodeBase64(authorizationValue);
					break;

				case BEARER:
					encodeAuthorizationValue = BEARER + " " + authorizationValue;
					break;
			}
		}

		public String getEncodeAuthorizationValue() {
			return encodeAuthorizationValue;
		}


		private String encodeBase64(String authorizationValue) {

			byte[] loginBytes = ("admin:lolx").getBytes();
			StringBuilder stringBuilder =
				new StringBuilder()
				.append("Basic ")
				.append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

			return stringBuilder.toString();
		}


	}

	private static String readStream(InputStream stream) throws IOException {

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

			return "null";

		} finally {

			if (out != null) {
				out.close();
			}
		}
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



	private static void log(Object ob) {

		Log.d(TAG, ob.toString());
		//System.out.println(ob.toString());

	}

}
