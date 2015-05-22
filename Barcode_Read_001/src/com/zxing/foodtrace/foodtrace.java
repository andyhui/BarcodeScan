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
	 * Log打印标签
	 */
	private static final String BAIDU_MAP_KEY = "7856A2E0506129742F78B7F8A3FA26CD224C381F";
	private static final String TAG = foodtrace.class.getSimpleName();
	private static final String PACKAGE_NAME = foodtrace.class.getPackage()
			.getName();
	private String LocationResult = null;
	private String LastResults = null;

	// 地图控制
	// public MyLocationOverlay myPosition;
	// public MapController mapController;
	// public MapView myMapView = null;

	/**
	 * 定位SDK的核心类
	 */
	private LocationClient mLocationClient;

	/**
	 * 显示定位结果详细信息的View
	 */
	private TextView tvLocationResult;

	/**
	 * 标注我的位置的覆盖物 # class LocationOverlay extends MyLocationOverlay{}
	 */
	private MyLocationOverlay mLocationOverlay;

	/**
	 * 我的位置信息数据
	 */
	private LocationData locData;

	/**
	 * 定位结果处理器 # class MyLocationListener implements BDLocationListener{}
	 */
	private MyLocationListener mLocationListener;

	private View viewCache = null;

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View

	private MapController mMapController = null;

	// UI相关
	BMapManager mBMapManager = null;

	// 界面按钮
	TextView mTextView = null;
	TextView mTextView01 = null;
	Button mSendMsg = null;
	Button mScan = null;
	Button mGetGps = null;
	// ImageView image = null;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	// Service的基本变量
	GetGpsService mService;
	boolean mBound = false;

	GPSTracker gps;

	private static ExecutorService exec = Executors.newSingleThreadExecutor();

	// 检查GPS是否打开
	public void openGPSSettings() {
		Log.i(TAG, "openGPSSettings");
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Toast.makeText(this, "请开启GPS！", Toast.LENGTH_LONG).show();
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
		text.setText("Hi,请确认您已经打开gps!");
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.dog);

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		alertDialog = builder.create();
		// builder.show();
		alertDialog.show();
	}

	@Override
	// 菜单按钮
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
		// 注册监听函数
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption locationOption = new LocationClientOption();
		locationOption.setOpenGps(true);
		locationOption.setCoorType("bd09ll");
		locationOption.setPriority(LocationClientOption.GpsFirst);
		locationOption.setAddrType("all");
		locationOption.setProdName("通过GPS定位");
		mLocationClient.setLocOption(locationOption);

		Log.i(TAG, "BaiduMapMyLocationActivity 开启定位");
		mLocationClient.start();

		mLocationOverlay = new LocationOverlay(mMapView);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		// TextView titleTextView = (TextView) findViewById(R.id.title_text);
		// titleTextView.setText(string.scanbracode_title);
		// myMap();
		// onListener();

		// 条码识别按钮。
		// 发送信息按钮.
		// 获取GPS按钮。
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

	// 扫描条码
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

	// 显示GPS信息
	private class getGpsOnClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// LocationResult = mService.get();
			// mTextView01.setText(LocationResult);
			// System.out.println("通过Service获取位置信息" + LocationResult);

			gps = new GPSTracker(foodtrace.this);

			// check if GPS enabled
			if (gps.canGetLocation()) {

				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();

				LocationResult = "Your Location is - \nLat: " + latitude
						+ "\nLong: " + longitude;
				mTextView01.setText(LocationResult);
				System.out.println("通过Service获取位置信息" + LocationResult);
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

	// 发送信息给服务器
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
				// 获取条码内容
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
		// 或者下面这种方式
		// System.exit(0);
		// 建议用这种
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
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setMessage("确定退出系统吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("确定",
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

	// 绑定一个服务
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

			// TODO 为方便测试查看
			testLog(location);

			// 在地图上标注定位得到我当前的位置
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

		// 判断是否有定位精度半径
		if (location.hasRadius()) {
			// 获取定位精度半径，单位是米
			locData.accuracy = location.getRadius();
		}

		mLocationOverlay.setData(locData);
		mMapView.getOverlays().add(mLocationOverlay);
		mMapView.refresh();

		// 将我的当前位置移动到地图的中心点
		mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6)));

	}

	/**
	 * 打印测试LOG信息
	 * 
	 * @param location
	 */
	private void testLog(BDLocation location) {
		String province = location.getProvince(); // 获取省份信息
		String city = location.getCity(); // 获取城市信息
		String district = location.getDistrict(); // 获取区县信息

		Log.i(TAG, "province = " + province);
		Log.i(TAG, "city = " + city);
		Log.i(TAG, "district = " + district);

		int type = location.getLocType();
		Log.i(TAG, "当前定位采用的类型是：type = " + type);

		String coorType = location.getCoorType();
		Log.i(TAG, "坐标系类型:coorType = " + coorType);

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