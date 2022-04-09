package com.adm.studio.apiclient.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import android.util.Base64;

public class RequestPackage {

	private static final String TAG = "RequestPackage";

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";

	public static final String BEARER = "Bearer";
	public static final String BASIC = "Basic";



	private String endPoint;
	private String methord = "GET";
	private String authorization;
	private Map<String, String> parmas = new HashMap<>();

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getEndPoint() {
		return endPoint == null ? "": endPoint;
	}

	public void setMethod(String methor) {
		this.methord = methor;
	}

	public String getMethod() {
		return methord;
	}


	public void setAutorization(String authorizationType, String authorizationValue) {
		switch (authorizationType) {
			case BASIC:
				this.authorization = encodeBase64(authorizationValue);
				break;

			case BEARER:
				this.authorization = BEARER + " " + authorizationValue;
				break;
		}
	}

	public String getEncodeAuthorization() {

		return authorization == null ? "": authorization ;
	}

	private String encodeBase64(String authorizationValue) {

		byte[] loginBytes = ("admin:lolx").getBytes();
		StringBuilder stringBuilder =
			new StringBuilder()
			.append("Basic ")
			.append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

		return stringBuilder.toString();
	}


	public Map<String, String> getParmas() {
		return parmas;
	}

	public void setParmas(String key, String value) {
		this.parmas.put(key, value);
	}

	String getEndPointParmas() {

		StringBuilder sb = new StringBuilder();

		for (String key : parmas.keySet()) {

			String value = null;

			try {
				value = URLEncoder.encode(parmas.get(key), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(key).append("=").append(value);
		}

		return sb.toString();
	}

	public int setParmas(String unformateedParmasString) {

		String parmasKey = "";
		String parmasValue = "";

		StringBuilder parmasBuilder = new StringBuilder();

		for (char charFromUnformattedParmas : (unformateedParmasString + '|').toCharArray()) {
			switch (charFromUnformattedParmas) {
				case ':':
					parmasKey = parmasBuilder.toString();
					parmasBuilder = new StringBuilder();

					System.out.println("key : " + parmasKey);
					System.out.println("bulder : " + parmasBuilder.toString());

					break;

				case '|':
					parmasValue = parmasBuilder.toString();
					parmasBuilder = new StringBuilder();

					System.out.println("value : " + parmasValue);
					System.out.println("builder :" + parmasBuilder.toString());

					setParmas(parmasKey, parmasValue);
					break;

				default:
					parmasBuilder.append(charFromUnformattedParmas);
			}
		}

		System.out.println(getEndPointParmas());

		return -1;
	}


	boolean contains(String character) {
		return getEndPoint().contains(character);
	}


}
