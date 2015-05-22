package com.zxing.foodtrace;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.winnower.tickActivity;
import com.zxing.test.R;
import com.zxing.test.R.string;

public class update extends pressActivity{

	Button mButton = null;
	Button mButtonOk = null;
	Intent intent = null;
	//Context context = this.getApplicationContext();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.updating);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		
		intent = this.getIntent();
		TextView textView = (TextView)findViewById(R.id.title_text);
		textView.setText(string.update_title);
		
		mButton = (Button)findViewById(R.id.updating);
		mButton.setOnClickListener(new ButtonClicked());
		
		mButtonOk = (Button)findViewById(R.id.ok);
		mButtonOk.setOnClickListener(new ButtonClicked());
		
		Resources res = getResources();
		//Drawable drawable = res.getDrawable(color.background_light);
		Drawable drawable = res.getDrawable(R.drawable.android_background);
        this.getWindow().setBackgroundDrawable(drawable);
	}

	private class ButtonClicked implements Button.OnClickListener{

		@Override
		public void onClick(View v) {
			if(v == mButton){
				Toast.makeText(update.this, "¸üÐÂÖÐ.......", Toast.LENGTH_LONG).show();
			}
			else if(v == mButtonOk){
				update.this.setResult(RESULT_OK,intent);
				update.this.finish();
			}
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void press(View v) {
		// TODO Auto-generated method stub{
		Intent intent = new Intent();
		intent.setClass(this,tickActivity.class);
		startActivity(intent);
	}
}
