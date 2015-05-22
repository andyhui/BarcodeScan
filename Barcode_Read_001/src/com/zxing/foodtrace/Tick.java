package com.zxing.foodtrace;

import android.R.color;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.zxing.test.R;
import com.zxing.test.R.string;

public class Tick extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sample);
		
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		TextView textView = (TextView)findViewById(R.id.title_text);
		textView.setText(string.quesion_title);
		
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.android_background);
        this.getWindow().setBackgroundDrawable(drawable);
	}

}
