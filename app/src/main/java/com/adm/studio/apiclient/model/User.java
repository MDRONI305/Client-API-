package com.adm.studio.apiclient.model;

import org.json.JSONObject;

public class User {

	private String name;
	private String email;
	private String pasword;
	private Profile profile;


	public User(String name, String email, Profile profile) {
		this.name = name;
		this.email = email;
		this.profile = profile;
	}
	public User(String name, String email, String pasword, Profile profile) {
		this.name = name;
		this.email = email;
		this.pasword = pasword;
		this.profile = profile;
	}

	public User(String name, String email, String pasword) {
		this.name = name;
		this.email = email;
		this.pasword = pasword;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public String getPasword() {
		return pasword;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}

	public static class Profile {

		private String accessToken;

		public Profile() {}
		public Profile(String accessToken) {
			this.accessToken = accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getAccessToken() {
			return accessToken;
		}

		@Override
		public String toString() {
			String object = "";

			try {
				JSONObject profile = new JSONObject();
				profile.accumulate("accessToken", getAccessToken());
				object = profile.toString();

			} catch (Exception e) {

			}
			return object;
		}
	}

	@Override
	public String toString() {

		String object = "";

		try {

			JSONObject user = new JSONObject();
			user.accumulate("name", getName());
			user.accumulate("email", getEmail());
			user.accumulate("password", getPasword());

			user.accumulate("profile", getProfile().toString());

			object = user.toString();

		} catch (Exception e) {

		}

		return object;
	}
}

