package com.zxing.home;

import com.winnower.tickActivity;
import com.zxing.foodtrace.foodtrace;
import com.zxing.foodtrace.Tick;
import com.zxing.foodtrace.help;
import com.zxing.foodtrace.update;
import com.zxing.test.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class Home extends Activity{

	private ImageButton mButton01 = null;
	private ImageButton mButton02 = null;
	private ImageButton mButton03 = null;
	private ImageButton mButton04 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		
		mButton01 = (ImageButton)findViewById(R.id.shibie);
		mButton02 = (ImageButton) findViewById(R.id.help);
		mButton03 = (ImageButton) findViewById(R.id.update);
		mButton04 = (ImageButton) findViewById(R.id.send);
		mButton01.setOnClickListener(new myOnClickListener());
		mButton02.setOnClickListener(new myOnClickListener());
		mButton03.setOnClickListener(new myOnClickListener());
		mButton04.setOnClickListener(new myOnClickListener());
		
	}
	
	private class CheckOn implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class myOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v == mButton01){
				Intent intent = new Intent(Home.this,foodtrace.class);
				startActivity(intent);
			}else if(v == mButton02){
				Intent intent = new Intent(Home.this,help.class);
				startActivity(intent);
			}else if(v == mButton03){
				Intent intent = new Intent(Home.this,update.class);
				startActivityForResult(intent, 0);
			}else if(v == mButton04){
				Intent intent = new Intent(Home.this,tickActivity.class);
				startActivity(intent);
			}
			// TODO Auto-generated method stub
		}
		
	}

}
