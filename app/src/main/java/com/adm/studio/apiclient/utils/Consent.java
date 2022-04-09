package com.adm.studio.apiclient.utils;

public class Consent {
	public static final String TAG = "adm.studio";
    
    public static final String BASE_URL = "http://localhost:8000/api/";
    public static final String RESPONSE = "Response";
	public static final String LOG = "Log";
    

	public static final String TAG_MAIN_ACTIVITY = "MainActivity";
	public static final String TAG_APP_SERVICE = "AppService";
    public static final String TAG_HTTP_HELPER = "HttpHelper";
    public static final String TAG_CACHE_IMAGE_MANAGER = "CacheImageManager";

    public static final String ACTION_SYNC_DATA_TO_UI = "ui_data_sync";

	public static final String SERVICE_PAYLOAD = "service_payload";
	public static final String SERVICE_MESSAGE = "service_message";
    public static final String SERVICE_REQUEST_package = "service_request_package";

    public static final String SERVICE_EXCEPTION = "service_exception";

	//public static final String JSON_URL_1 = "http://192.168.0.102:8080/json/itemsfeed.php";
	public static final String JSON_URL = "http://0.0.0.0:8080/json/itemsfeed.php";
	public static final String XML_URL = "http://0.0.0.0:8080/xml/itemsfeed.php";
    public static final String IMAGE_URL_1 = "http://0.0.0.0:8080/images/bahawalpur.jpg";
    public static final String IMAGE_URL_2 = "http://0.0.0.0:8080/images/hyderabad.jpg";

	public static final String JSON_PLACE_HOLDER = "https://jsonplaceholder.typicode.com/posts";

    //public static final String BASE_URL = "http://localhost:8000/";
	
	public static final String REGISTRATION_LOG_MESSAGE = "{\n  \"status\":\"success\",\n  \"message\":\"Registration successfully!\"'\n}";
	public static final String LOGIN_LOG_MESSAGE = "{\n  \"status\":\"success\",\n  \"message\":\"Login successfully!\"\n}";

	public static boolean LOGGING_IN = false;
	public static String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9sb2dpbiIsImlhdCI6MTY0OTI2MjEzOCwiZXhwIjoxNjQ5MjY1NzM4LCJuYmYiOjE2NDkyNjIxMzgsImp0aSI6IkJ1VkZoMlR5OVR4ejEya00iLCJzdWIiOjcsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.P9xoRiukhyl6WullUtRKf62DNXkXLjaLLIVWQsGGza0";
	public static String ACCESS_TOKEN_KEY = "access_token";
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";

	public static final String USER_KEY = "user_key";
	public static final String USER_LOGGED_KEY = "user_logged_key";
	}
