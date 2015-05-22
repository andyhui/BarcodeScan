package com.zxing.foodtrace;

import com.winnower.tickActivity;
import com.zxing.test.R;
import com.zxing.test.R.string;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class help extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.help);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		TextView titleTextView = (TextView)findViewById(R.id.title_text);
		titleTextView.setText(string.help_title);
		
		TextView textView = (TextView)findViewById(R.id.mTextView);
		ImageView imageView = (ImageView)findViewById(R.id.image);
		
		textView.setText(R.string.help_message);
		imageView.setImageResource(R.drawable.help);
		
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.android_background);
        this.getWindow().setBackgroundDrawable(drawable);
		
		
	}
	public void press(View v) {
		// TODO Auto-generated method stub{
		Intent intent = new Intent();
		intent.setClass(this,tickActivity.class);
		startActivity(intent);
	}
}
