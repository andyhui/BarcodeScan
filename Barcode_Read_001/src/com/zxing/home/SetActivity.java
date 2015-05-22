package com.zxing.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxing.test.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class SetActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
    private boolean mImage = false;
    private boolean mDownload = false;
    private boolean mUpdate = false;
	
	private int off = R.drawable.radio_off;
	private int on = R.drawable.radio_on;

	ListView myListView;
	//MyListAdapter adapter;
	List<Map<String, Object>> list;
	int currentId = -1;

	private AlertDialog choseDlg;
	//private CheckBox m_CheckBox1;
	//private CheckBox m_CheckBox2;
	private Spinner m_spinner;

	private SimpleAdapter simpleadpter;
	private ArrayAdapter<String> arrayadapter;
	private String TAG = "MyList";

	private static final String[] m_Countries = { "睡眠", "退出" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		Log.i(TAG,"onCreate");
		myListView = (ListView) findViewById(R.id.my_list);
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		Map<String, Object> map4 = new HashMap<String, Object>();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    mImage = settings.getBoolean("image", false);
		mDownload = settings.getBoolean("download", false);
		mUpdate = settings.getBoolean("update", false);
		if(mImage)
		{
			map1.put("imge", on);
		}else{
			map1.put("imge", off);
		}
		map1.put("title", "图片");
		map1.put("info", "是否显示图片");
		if(mDownload)
		{
			map2.put("imge", on);
		}else{
			map2.put("imge", off);
		}
		map2.put("title", "下载");
		map2.put("info", "是否自动下载文件");
		if(mUpdate)
		{
			map3.put("imge", on);
		}else{
			map3.put("imge", off);
		}
		map3.put("title", "更新");
		map3.put("info", "是否定时自动更新");
		map4.put("imge", R.drawable.xuanze);
		map4.put("title", "睡眠模式");
		map4.put("info", "退出");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);

		simpleadpter = new SimpleAdapter(this, list, R.layout.list,
				new String[] { "imge", "title", "info" }, new int[] { R.id.img,
						R.id.title, R.id.info });
		myListView.setAdapter(simpleadpter);

		/*
		 * adapter = new MyListAdapter(MyList.this,list);
		 * myListView.setAdapter(adapter);
		 */
		// View mcontentView = LayoutInflater.from(this).inflate(R.layout.list,
		// null);
		// convertView=inflater.inflate(R.layout.list, null);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> map = list.get(position);
				if (map.get("imge").equals(R.drawable.radio_on)) {
					if (map.get("title").equals("图片")) {
						mImage = false;
					} else if (map.get("title").equals("下载")) {
						mDownload = false;
					} else if (map.get("title").equals("更新")) {
						mUpdate = false;
					}
					ChangeImg(position, false);
				} else if (map.get("title").equals("睡眠模式")) {
					choseDlg.show();
				} else {
					if (map.get("title").equals("图片")) {
						mImage = true;
					} else if (map.get("title").equals("下载")) {
						mDownload = true;
					} else if (map.get("title").equals("更新")) {
						mUpdate = true;
					}
					ChangeImg(position, true);
				}
				// TODO Auto-generated method stub
				/*
				 * if (position != currentId) { //ChangeImg(position, false);
				 * adapter.setCurrentId(position);
				 * adapter.notifyDataSetChanged();
				 * 
				 * //adapter.notifyDataSetChanged(); } else{
				 * adapter.setCurrentId(-1); adapter.notifyDataSetChanged(); }
				 */
			}
		});

		LayoutInflater inflater = LayoutInflater.from(SetActivity.this);
		/*
		 * final View checkbox = inflater.inflate(R.layout.checkbox, null);
		 * m_CheckBox1 = (CheckBox) checkbox.findViewById(R.id.checkbox01);
		 * m_CheckBox2 = (CheckBox) checkbox.findViewById(R.id.checkbox02);
		 * m_CheckBox1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		 * {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub
		 * 
		 * } });
		 * 
		 * m_CheckBox2.setOnCheckedChangeListener(new OnCheckedChangeListener()
		 * {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub
		 * 
		 * } });
		 */

		final View spinner = inflater.inflate(R.layout.spinner, null);
		m_spinner = (Spinner) spinner.findViewById(R.id.Spinner1);
		arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, m_Countries);
		m_spinner.setAdapter(arrayadapter);
		m_spinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		choseDlg = new AlertDialog.Builder(SetActivity.this).setTitle("睡眠模式")
				.setView(spinner).setPositiveButton("确定", null)
				.setNegativeButton("取消", null).create();
	}

	private void ChangeImg(int selectedItem, boolean b) {
		SimpleAdapter la = simpleadpter;
		Map<String, Object> map = (Map<String, Object>) la
				.getItem(selectedItem);
		if (b) {
			map.put("imge", R.drawable.radio_on);
		} else
			map.put("imge", R.drawable.radio_off);
		la.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("image", mImage);
			editor.putBoolean("download", mDownload);
			editor.putBoolean("update", mUpdate);
			editor.commit();*/
			Log.i(TAG ,"onkeyDown");
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onStart");
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onResume");
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onPause");
		super.onStop();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("image", mImage);
		editor.putBoolean("download", mDownload);
		editor.putBoolean("update", mUpdate);
		editor.commit();
		Log.i(TAG,"onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onDestroy");
		super.onDestroy();
	}
}
