package com.adm.studio.apiclient;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.adm.studio.apiclient.utils.Log;
import android.widget.Toast;
import android.os.SystemClock;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.ServiceConnection;

import android.content.ComponentName;
import com.adm.studio.apiclient.service.MainService;
import android.content.Intent;
import android.content.Context;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import android.widget.Button;
import com.adm.studio.apiclient.utils.Consent;
import android.widget.EditText;
import android.widget.RadioGroup;
import java.net.HttpURLConnection;
import com.adm.studio.apiclient.utils.HttpHelper;
import com.adm.studio.apiclient.utils.RequestPackage;
import android.view.View.OnLongClickListener;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.IntentFilter;
import com.adm.studio.apiclient.utils.NetworkHelper;
import android.text.method.ScrollingMovementMethod;
import android.view.View.OnCapturedPointerListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.adm.studio.apiclient.utils.HttpController;
import com.adm.studio.apiclient.viewmodel.MainServiceViewModel;
import java.util.HashMap;
import java.util.Map;
import com.adm.studio.apiclient.model.User;
import org.json.JSONObject;
import com.adm.studio.apiclient.utils.PreferencesHelper;
import android.app.Presentation;

public class MainActivity extends AppCompatActivity {

	public static final String TAG = "MainActivity";

	private StringBuilder mResponse = new StringBuilder();
	private StatusResuletView mView = new StatusResuletView();

	private boolean isNetworkOK;
	private boolean isSingleClick = false;
	private String mRequestMethord = "GET";

	//private PreferencesHelper mPreferenens;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);

		log("onCreate");
		findView();

		log(MainServiceViewModel.serviceRegister(this));
		PreferencesHelper.register(this);


		isNetworkOK = NetworkHelper.isNetworkAvailable(this);
		log("Device is online : " + isNetworkOK);


    }


	// === Ui Inin

	private DrawerLayout mDrawerLayout;
	private RadioGroup mRadioGroup;
	private EditText mEditTUrl, mEditTRequestParms;
	private TextView mTextVLog, mTextVStatus;
	private Button mBtnChangeUrl, mBtnSendRequest;
	private void findView() {

		// Toolbar
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
 		setSupportActionBar(toolbar);

		// DrawerLayout
		mDrawerLayout = findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this,
			mDrawerLayout,
			toolbar,
			R.string.navigation_drawer_open,
			R.string.navigation_drawer_close);

		mDrawerLayout.addDrawerListener(toggle);
		toggle.syncState();


		mRadioGroup = findViewById(R.id.radioG_request_type_mainA);


		mEditTUrl = findViewById(R.id.editT_url_mainA);
		mEditTUrl.setText("http://127.0.0.1:8000/api/posts/");

		mEditTRequestParms = findViewById(R.id.editT_request_parms_mainA);


		mTextVStatus = findViewById(R.id.textV_status_mainA);

		mTextVLog = findViewById(R.id.textV_log_mainA);
		//mTextVLog.setHorizontalScrollBarEnabled(true);
		//mTextVLog.set
		//mTextVLog.setMovementMethod(new ScrollingMovementMethod());

		mBtnSendRequest = findViewById(R.id.btn_send_request_mainA);


		setListener();
	}

	// === setLisetner
	private void setListener() {

		// radio group
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup p1, int radioButtonId) {

					switch (radioButtonId) {
						case R.id.radioB_get_mainA:
							mRequestMethord = Consent.GET;
							mEditTRequestParms.setVisibility(View.GONE);

							break;

						case R.id.radioB_post_mainA:
							mRequestMethord = RequestPackage.POST;
							mEditTRequestParms.setVisibility(View.VISIBLE);
							mEditTRequestParms.setText("email:roni@gmail.com|password:123456");

							break;

						case R.id.radioB_put_mainA:
							mRequestMethord = RequestPackage.PUT;
							mEditTRequestParms.setVisibility(View.VISIBLE);
							break;

						case R.id.radioB_delete_mainA:
							mRequestMethord = RequestPackage.DELETE;
							mEditTRequestParms.setVisibility(View.GONE);
							break;
					} // end switch
				}
			});



		mBtnSendRequest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {




					String url = mEditTUrl.getText().toString();
					String parmas = "";

					// check if url is empty
					if (url.equals("")) {
						log("Require url address!");
					}


					// check if request parmas 
					if (mEditTRequestParms.getVisibility() == View.VISIBLE)  {
						parmas = mEditTRequestParms.getText().toString();

						if (parmas.equals("")) {
							log("Require FormData");
						}						
					}

					RequestPackage requestPackage = new RequestPackage();

					requestPackage.setEndPoint(url);
					requestPackage.setMethod(mRequestMethord);
					requestPackage.setParmas(parmas);

					if (requestPackage.getEndPoint().contains("register")) {
						MainServiceViewModel.createUser(requestPackage);
						
					} else if (requestPackage.getEndPoint().contains("login")) {
						MainServiceViewModel.loginUser(requestPackage);

					} else if (requestPackage.getEndPoint().contains("logout")) {
						String token = PreferencesHelper.getString(Consent.ACCESS_TOKEN_KEY);
					    requestPackage.setAutorization(requestPackage.BEARER, token);
						
						MainServiceViewModel.logoutUser(requestPackage);

					} else {
						String token = PreferencesHelper.getString(Consent.ACCESS_TOKEN_KEY);
						requestPackage.setAutorization(requestPackage.BEARER, token);
						
						MainServiceViewModel.sendRequestToApi(requestPackage);

					}

				}
			});



		mTextVStatus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					String status = mTextVStatus.getText().toString();

					if (status.equals("Response")) {
						mTextVStatus.setText("Log");
						mTextVLog.setText(Log.getLogs.toString());
					} else if (status.equals("Log")) {
						mTextVStatus.setText("Response");
						mTextVLog.setText(mResponse.toString());
					}

				}
			});

		// heandle double click
		//boolean firstClick = false;

		mTextVLog.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {

					if (isSingleClick) {
						mTextVLog.setText("");
					}

					return true;
				}
			});// end


		mTextVLog.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

					if (!isSingleClick) {
						new Thread(new Runnable(){

								@Override
								public void run() {		
									isSingleClick = true;
									SystemClock.sleep(1000);
									isSingleClick = false;
								}
							}).start();
					}
				}
			}); // end

	}

	@Override
	public void onBackPressed() {

		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}


	@Override
	protected void onStart() {
		super.onStart();
		log("onStart");

		// BroadcastReciver register
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.registerReceiver(
			mReceiver, new IntentFilter(Consent.ACTION_SYNC_DATA_TO_UI));

		// AppService start
//		Intent i = new Intent(this, AppService.class);
//		bindService(i, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		log("onStop");

		// AppService stop
		unbindService(mConnection);

		// BroadcastReciver unregister
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.unregisterReceiver(mReceiver);

	}


	private class StatusResuletView {

		public void response(Object ob) {

			if (mTextVStatus != null && mTextVLog != null) {
				mTextVStatus.setText(Consent.RESPONSE);
				mTextVLog.setText(ob.toString());
			}
		}

		public void log(Object ob) {
			if (mTextVStatus != null && mTextVLog != null) {
				mTextVStatus.setText(Consent.LOG);
				mTextVLog.setText(ob.toString());
			}
		}
	}


	private void log(Object massage) {
		Log.d(TAG, massage);

		if (mTextVStatus != null && mTextVStatus.getText().toString().equals(Consent.LOG)) {
			mView.log(Log.getLogs.toString());
		}


	}
	// =========================  BroadcastReceiver  ========================
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context p1, Intent intent) {

            if (intent.hasExtra(Consent.SERVICE_PAYLOAD)) {
                String data = intent.getStringExtra(Consent.SERVICE_PAYLOAD);
                log(data);
                // showToast(data);

				mResponse = new StringBuilder();
				mResponse.append(data);
				mView.response(data);

            } else if (intent.hasExtra(Consent.SERVICE_EXCEPTION)) {
                String data = intent.getStringExtra(Consent.SERVICE_EXCEPTION);
                log(data);
				mResponse = new StringBuilder();
				mResponse.append(data);
				mView.response(data);

            }
		}
	};


	// =============== AppService
	private MainService mService;
	private boolean isServiceBind = false;
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName p1, IBinder service) {
			log("onServiceConnected");

			mService = ((MainService.LocalBinder)service).getService();
			isServiceBind = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName p1) {

			isServiceBind = false;
		}
	};



	// ====================
	private void createUser(RequestPackage requestPackage, String unformateParmas) {

		requestPackage.setParmas(unformateParmas);

		log(unformateParmas);

		Map<String, String> parmas = new HashMap();
		parmas = requestPackage.getParmas();

		String name = "";
		String email = "";
		String password = "";

		for (String key : parmas.keySet()) {

			String value = null;
			value = parmas.get(key);

			switch (key) {
				case "name":
					name = value;
					break;

				case "email":
					email = value;
					break;

				case "password":
					password = value;
					break;
			}
		}

		User user = new User(name, email, password);



	}

}
