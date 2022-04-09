package com.adm.studio.apiclient.utils;
import android.util.Base64;

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


	private static String encodeBase64(String authorizationValue) {

		byte[] loginBytes = ("admin:lolx").getBytes();
		StringBuilder stringBuilder =
			new StringBuilder()
			.append("Basic ")
			.append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

		return stringBuilder.toString();
	}


}
