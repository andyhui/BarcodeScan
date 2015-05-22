package com.zxing.home;

import com.winnower.tickActivity;
import com.zxing.test.R;
import com.zxing.test.R.string;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

public class Tab extends TabActivity {

	private TabHost myTabhost;
	private RadioGroup rgTab;
	private TextView textView = null;
	protected int myMenuSettingTag = 0;
	protected Menu myMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Context mcontext = Tab.this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.main_tab);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		rgTab = (RadioGroup) findViewById(R.id.rgTab);
		
		myTabhost = this.getTabHost();

		Resources rs = getResources();
		Drawable drawable = rs.getDrawable(R.drawable.android_background);
		myTabhost.setBackgroundDrawable(drawable);
		
		textView = (TextView) findViewById(R.id.title_text);

		// myTabhost.getTabContentView();
		/*
		 * LayoutInflater inflater = (LayoutInflater)
		 * getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
		 * inflater.inflate(R.layout.main_tab, myTabhost.getTabContentView(),
		 * true);
		 */
		// LayoutInflater.from(this).inflate(R.layout.main_tab,
		// myTabhost.getTabContentView(), true);
		// myTabhost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		//myTabhost.setBackgroundColor(Color.WHITE);

		myTabhost.addTab(myTabhost
				.newTabSpec("home")
		// make a new Tab
				.setIndicator("home",
						rs.getDrawable(R.drawable.tab_indicator))
				// set the Title and Icon
				.setContent(new Intent(Tab.this, Home.class)));
		// set the layout

		myTabhost.addTab(myTabhost
				.newTabSpec("comment")
				// make a new Tab
				.setIndicator("comment",
						rs.getDrawable(R.drawable.tab_indicator))
				// set the Title and Icon
				.setContent(new Intent(Tab.this, commentActivity.class)));

		myTabhost.addTab(myTabhost
				.newTabSpec("setting")
				// make a new Tab
				.setIndicator("setting",
						rs.getDrawable(R.drawable.tab_indicator))
				.setContent(new Intent(Tab.this, SetActivity.class)));

		myTabhost.addTab(myTabhost
				.newTabSpec("update")
				.setIndicator("update",
						rs.getDrawable(R.drawable.tab_indicator))
				.setContent(new Intent(Tab.this, settingList.class)));
		// set the Title and Icon
		// .setContent(R.id.widget_layout_green));

		// set the layout
		// set the layout
		myTabhost.setCurrentTab(0);
		textView.setText(string.home_title);
		
		rgTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.home:
					myTabhost.setCurrentTabByTag("home");
					textView.setText(string.home_title);
					break;
				case R.id.comment:
					myTabhost.setCurrentTabByTag("comment");
					textView.setText(string.comment_title);
					break;
				case R.id.setting:
					myTabhost.setCurrentTabByTag("setting");
					textView.setText(string.set_title);
					break;
				default:
					myTabhost.setCurrentTabByTag("home");
					break;

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Hold on to this
		myMenu = menu;
		myMenu.clear();// 清空MENU菜单
		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.total_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void press(View v) {
		// TODO Auto-generated method stub{
		Intent intent = new Intent();
		intent.setClass(this,tickActivity.class);
		startActivity(intent);
	}

}