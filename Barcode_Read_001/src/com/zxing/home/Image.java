package com.zxing.home;


import com.zxing.test.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Image extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		final ImageView splashImageView = (ImageView) findViewById(R.id.SplashScreen);
		splashImageView.setBackgroundResource(R.drawable.flag);
		final AnimationDrawable frameAnimation = (AnimationDrawable) splashImageView
				.getBackground();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent mainIntent = new Intent(Image.this,
						Tab.class);
				frameAnimation.start();
				Image.this.startActivity(mainIntent);
				Image.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

}
