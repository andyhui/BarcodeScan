package com.zxing.foodtrace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.http.service.NewService;
import com.winnower.tickActivity;
import com.zxing.activity.CaptureActivity;
import com.zxing.gps.GPSTracker;
import com.zxing.gps.GetGpsService;
import com.zxing.gps.GetGpsService.LocalBinder;
import com.zxing.test.R;

public class foodtrace extends Activity {
	/** Called when the activity is first created. */
	/**
	 * Log��ӡ��ǩ
	 */
	private static final String BAIDU_MAP_KEY = "7856A2E0506129742F78B7F8A3FA26CD224C381F";
	private static final String TAG = foodtrace.class.getSimpleName();
	private static final String PACKAGE_NAME = foodtrace.class.getPackage()
			.getName();
	private String LocationResult = null;
	private String LastResults = null;

	// ��ͼ����
	// public MyLocationOverlay myPosition;
	// public MapController mapController;
	// public MapView myMapView = null;

	/**
	 * ��λSDK�ĺ�����
	 */
	private LocationClient mLocationClient;

	/**
	 * ��ʾ��λ�����ϸ��Ϣ��View
	 */
	private TextView tvLocationResult;

	/**
	 * ��ע�ҵ�λ�õĸ����� # class LocationOverlay extends MyLocationOverlay{}
	 */
	private MyLocationOverlay mLocationOverlay;

	/**
	 * �ҵ�λ����Ϣ����
	 */
	private LocationData locData;

	/**
	 * ��λ��������� # class MyLocationListener implements BDLocationListener{}
	 */
	private MyLocationListener mLocationListener;

	private View viewCache = null;

	// ��ͼ��أ�ʹ�ü̳�MapView��MyLocationMapViewĿ������дtouch�¼�ʵ�����ݴ���
	// ���������touch�¼���������̳У�ֱ��ʹ��MapView����
	MapView mMapView = null; // ��ͼView

	private MapController mMapController = null;

	// UI���
	BMapManager mBMapManager = null;

	// ���水ť
	TextView mTextView = null;
	TextView mTextView01 = null;
	Button mSendMsg = null;
	Button mScan = null;
	Button mGetGps = null;
	// ImageView image = null;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	// Service�Ļ�������
	GetGpsService mService;
	boolean mBound = false;

	GPSTracker gps;

	private static ExecutorService exec = Executors.newSingleThreadExecutor();

	// ���GPS�Ƿ��
	public void openGPSSettings() {
		Log.i(TAG, "openGPSSettings");
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPSģ������", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Toast.makeText(this, "�뿪��GPS��", Toast.LENGTH_LONG).show();
			Dialog();
		}
	}

	private void Dialog() {
		// Context mContext = this.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog,
				(ViewGroup) findViewById(R.id.layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Hi,��ȷ�����Ѿ���gps!");
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.dog);

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		alertDialog = builder.create();
		// builder.show();
		alertDialog.show();
	}

	@Override
	// �˵���ť
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int item_id = item.getItemId();
		switch (item_id) {
		/*
		 * case R.id.menu_add: break;
		 */
		case R.id.menu_exit:
			onStop();
			onDestroy();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.exit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(BAIDU_MAP_KEY, null);
		setContentView(R.layout.main);

		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();

		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		mLocationClient = new LocationClient(this);

		locData = new LocationData();

		mLocationListener = new MyLocationListener();
		// ע���������
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption locationOption = new LocationClientOption();
		locationOption.setOpenGps(true);
		locationOption.setCoorType("bd09ll");
		locationOption.setPriority(LocationClientOption.GpsFirst);
		locationOption.setAddrType("all");
		locationOption.setProdName("ͨ��GPS��λ");
		mLocationClient.setLocOption(locationOption);

		Log.i(TAG, "BaiduMapMyLocationActivity ������λ");
		mLocationClient.start();

		mLocationOverlay = new LocationOverlay(mMapView);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		// TextView titleTextView = (TextView) findViewById(R.id.title_text);
		// titleTextView.setText(string.scanbracode_title);
		// myMap();
		// onListener();

		// ����ʶ��ť��
		// ������Ϣ��ť.
		// ��ȡGPS��ť��
		Log.i(TAG, "onCreate");
		// openGPSSettings();
		mSendMsg = (Button) findViewById(R.id.sendmsg);
		mScan = (Button) findViewById(R.id.ScanAnything);
		mGetGps = (Button) findViewById(R.id.GetGps);

		mTextView = (TextView) findViewById(R.id.mTextView);
		mTextView01 = (TextView) findViewById(R.id.mTextView01);
		// image = (ImageView) findViewById(R.id.image);

		mSendMsg.setOnClickListener(new mSendMsgOnClickListener());
		mScan.setOnClickListener(new scanOnClickListenner());
		mGetGps.setOnClickListener(new getGpsOnClickListener());

		// image.setImageResource(R.drawable.map01);

		exec.execute(new mRunnable());
	}

	class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

	}

	// ɨ������
	private class scanOnClickListenner implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Intent intent = new
			// Intent("com.google.zxing.client.android.SCAN");
			Intent intent = new Intent(foodtrace.this, CaptureActivity.class);
			startActivityForResult(intent, 0);

		}
	}

	protected void onActivityResult1(int requstcode, int resultCode, Intent data) {

	}

	// ��ʾGPS��Ϣ
	private class getGpsOnClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// LocationResult = mService.get();
			// mTextView01.setText(LocationResult);
			// System.out.println("ͨ��Service��ȡλ����Ϣ" + LocationResult);

			gps = new GPSTracker(foodtrace.this);

			// check if GPS enabled
			if (gps.canGetLocation()) {

				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();

				LocationResult = "Your Location is - \nLat: " + latitude
						+ "\nLong: " + longitude;
				mTextView01.setText(LocationResult);
				System.out.println("ͨ��Service��ȡλ����Ϣ" + LocationResult);
				// \n is for new line
				Toast.makeText(
						getApplicationContext(),
						"Your Location is - \nLat: " + latitude + "\nLong: "
								+ longitude, Toast.LENGTH_LONG).show();
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}
		}
	}

	// ������Ϣ��������
	private class mSendMsgOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (LocationResult != null && LastResults != null) {
				boolean result = NewService.save(LocationResult, LastResults);
				if (result) {
					Toast.makeText(getApplicationContext(), R.string.seccuss, 1)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.error, 1)
							.show();
				}
			} else
				Toast.makeText(getApplicationContext(), R.string.nodata, 1)
						.show();
			return;
		}
	}

	class mRunnable implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			Message message = mHandler.obtainMessage();
			mHandler.sendMessage(message);
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {

				Bundle bundle = intent.getExtras();
				String scanResult = bundle.getString("result");
				// resultTextView.setText(scanResult);

				// String contents = intent.getStringExtra("SCAN_RESULT");
				// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// ��ȡ��������
				// LastResults = "Format: " + format + "\nContents: " +
				// contents;
				/*
				 * mHandler.sendMessage(mHandler.obtainMessage(ResultsFlag,
				 * LastResults));
				 */
				LastResults = scanResult;
				showDialog(R.string.result_succeeded, LastResults);
				mTextView.setText(LastResults);
			} else if (resultCode == RESULT_CANCELED) {
				showDialog(R.string.result_failed,
						getString(R.string.result_failed_why));
			}
		}
	}

	private void showDialog(int title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		openGPSSettings();
		Intent intent = new Intent(this, GetGpsService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mBound) {
			this.unbindService(conn);
			mBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// �����������ַ�ʽ
		// System.exit(0);
		// ����������
		// android.os.Process.killProcess(android.os.Process.myPid());
		Log.i(TAG, "BaiduMapMyLocationActivity onDestroy() start");

		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(mLocationListener);
		mMapView.destroy();

		super.onDestroy();

		Log.i(TAG, "BaiduMapMyLocationActivity onDestroy() end");
	}

	/*
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * mMapView.onPause(); if (mBMapMan != null) { mBMapMan.stop(); }
	 * super.onPause(); }
	 * 
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * mMapView.onResume(); if (mBMapMan != null) { mBMapMan.start(); }
	 * super.onResume(); }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setMessage("ȷ���˳�ϵͳ��")
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// ��һ������
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mBound = false;
		}
	};

	class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}

			Log.i(TAG, "BaiduMapMyLocationActivity onReceiveLocation()");

			// TODO Ϊ������Բ鿴
			testLog(location);

			// �ڵ�ͼ�ϱ�ע��λ�õ��ҵ�ǰ��λ��
			markLocation(location);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	private void markLocation(BDLocation location) {
		locData.latitude = location.getLatitude();
		locData.longitude = location.getLongitude();
		locData.direction = location.getDerect();

		// �ж��Ƿ��ж�λ���Ȱ뾶
		if (location.hasRadius()) {
			// ��ȡ��λ���Ȱ뾶����λ����
			locData.accuracy = location.getRadius();
		}

		mLocationOverlay.setData(locData);
		mMapView.getOverlays().add(mLocationOverlay);
		mMapView.refresh();

		// ���ҵĵ�ǰλ���ƶ�����ͼ�����ĵ�
		mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6)));

	}

	/**
	 * ��ӡ����LOG��Ϣ
	 * 
	 * @param location
	 */
	private void testLog(BDLocation location) {
		String province = location.getProvince(); // ��ȡʡ����Ϣ
		String city = location.getCity(); // ��ȡ������Ϣ
		String district = location.getDistrict(); // ��ȡ������Ϣ

		Log.i(TAG, "province = " + province);
		Log.i(TAG, "city = " + city);
		Log.i(TAG, "district = " + district);

		int type = location.getLocType();
		Log.i(TAG, "��ǰ��λ���õ������ǣ�type = " + type);

		String coorType = location.getCoorType();
		Log.i(TAG, "����ϵ����:coorType = " + coorType);

		float derect = location.getDerect();
		Log.i(TAG, "derect = " + derect);
	}

	public void press(View v) {
		// TODO Auto-generated method stub{
		Intent intent = new Intent();
		intent.setClass(this, tickActivity.class);
		startActivity(intent);
	}
}