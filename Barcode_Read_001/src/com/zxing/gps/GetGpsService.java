package com.zxing.gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.http.service.newGetjson;
import com.zxing.foodtrace.foodtrace;
import com.zxing.test.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GetGpsService extends Service {

	// private final String action = "android.action.locationchangerd";
	private final IBinder mBinder = new LocalBinder();
	private final Random mGenerator = new Random();

	public static final int LENGTH_LONG = 5;
	private static final String TAG = GetGpsService.class.getSimpleName();

	private String latLongString;
	private String LocationResult;
	private Location mylocation;
	private NotificationManager mNotificationManager;
	private Notification notification;
	private Intent mIntent;
	private PendingIntent contentIntent;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		get();
		//String content = LocationResult;
		/*mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		String Text = LocationResult;
		String title = "GPS";
		//String content = LocationResult;
		int drawable = R.drawable.sanjingshou;
		notification = new Notification(drawable, getString(R.string.notification),System.currentTimeMillis());
		mIntent = new Intent(this, help.class);
		contentIntent = PendingIntent.getActivity(this, 0,mIntent, 0);*/
		System.out.println("已经起作用了");
		
		/*notification.setLatestEventInfo(this, title, content, contentIntent);
		mNotificationManager.notify(1, notification);
		
		setNotifacation(LocationResult, "GPS", LocationResult,R.drawable.sanjingshou);*/
		setNotifacation(LocationResult);
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBind");
		return mBinder;
	};

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "UNBIND");
		return super.onUnbind(intent);
	}

	public class LocalBinder extends Binder {
		public GetGpsService getService() {
			return GetGpsService.this;
		}
	}

	public int getRandomNumber() {
		return mGenerator.nextInt();

	}

	public String get() {
		LocationManager locationManager = null;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);
		Criteria creteria = new Criteria();
		creteria.setAccuracy(Criteria.ACCURACY_FINE);
		creteria.setAltitudeRequired(false);
		creteria.setBearingRequired(false);
		creteria.setCostAllowed(false);
		creteria.setPowerRequirement(creteria.POWER_LOW);

		String provider = locationManager.getBestProvider(creteria, true);
		System.out.println(provider);

		Location mlocation01 = locationManager.getLastKnownLocation(provider);
		System.out.println(TAG + mlocation01);
		Location location = mlocation01;

		if (location == null) {
			Criteria mcreteria = new Criteria();
			mcreteria.setPowerRequirement(mcreteria.POWER_LOW);
			String myProvider = LocationManager.NETWORK_PROVIDER;
			Location mLocation02 = locationManager
					.getLastKnownLocation(myProvider);
			location = mLocation02;
			provider = myProvider;
			System.out.println("gps没有找到地址" + "没有找到地址");
		}

		String result = updateToNewLocation(location);

		locationManager.requestLocationUpdates(provider, 900000, 100, new myLocationListener());

		return result;
	}

	private String updateToNewLocation(Location location) {
		// TODO Auto-generated method stub

		String addressString = null;
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			latLongString = "(" + (float)latitude +","+ (float)longitude +")";

			Geocoder gc = new Geocoder(this, Locale.CHINA);

			try {
				List<Address> addresses = gc.getFromLocation(latitude,
						longitude, 1);
				StringBuilder sb = new StringBuilder();
				System.out.println(addresses.size());
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
						sb.append(address.getAddressLine(i));
					}
					sb.append(address.getThoroughfare());
					addressString = sb.toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("错误");
				e.printStackTrace();
			}
			if (addressString != null) {
				return LocationResult = "位置:" + latLongString + addressString;
			} else {
				newGetjson json = new newGetjson();
				boolean flag = json.GetData(latitude, longitude);
				if (flag) {
					String result = json.getResult();
					return LocationResult = "位置:" + latLongString + result;
				}
			}
		} else {
			return latLongString = "没有找到坐标\n";
		}
		return LocationResult = "位置:" + latLongString + addressString;
	}

	public class myLocationListener implements LocationListener {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			// updateToNewLocation(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String mylocation = updateToNewLocation(location);
			setNotifacation(LocationResult);
		}
	}

	private void setNotifacation(String content) {
		/*Notification notification = new Notification(drawable, Text,
				System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getService(this, 0,
				new Intent(this, MyZxingTestActivity.class), 0);*/
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		/*String Text = LocationResult;
		String title = "GPS";*/
		//String content = LocationResult;
		int drawable = R.drawable.title2;
		notification = new Notification(drawable, getString(R.string.notification),System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		mIntent = new Intent(this, foodtrace.class);
		contentIntent = PendingIntent.getActivity(this, 0,mIntent, 0);
		notification.setLatestEventInfo(this, getString(R.string.notification), content, contentIntent);
		mNotificationManager.notify(1, notification);
	}


}
