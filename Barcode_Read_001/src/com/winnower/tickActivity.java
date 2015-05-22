package com.winnower;

import com.zxing.foodtrace.pressActivity;
import com.zxing.test.R;
import com.zxing.test.R.string;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class tickActivity extends Activity{

	private Button submitButton = null;
	private Spinner genderSpinner = null;
    private Spinner ageSpinner = null;
	private ArrayAdapter<String> genderadapter = null;
	private ArrayAdapter<String> ageadapter = null;
	
	private static final String[] m_gender = {"性别","男","女"};
	private static final String[] m_age = {"年龄","18岁以下","18-48","48-78","78以上"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tick);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		Button IconButton = (Button)findViewById(R.id.Icon_bar);
		IconButton.setVisibility(View.GONE);
		TextView textView = (TextView)findViewById(R.id.title_text);
		textView.setText(string.tick);
		
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.color.white);
        this.getWindow().setBackgroundDrawable(drawable);
		
		submitButton = (Button)findViewById(R.id.submit);
		genderSpinner = (Spinner)findViewById(R.id.gender);
		ageSpinner = (Spinner)findViewById(R.id.age);
		
		submitButton.setOnClickListener(new OnClickListener());
		
		genderadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,m_gender);
		genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		genderSpinner.setAdapter(genderadapter);
		
		ageadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,m_age);
		ageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ageSpinner.setAdapter(ageadapter);
		
		genderSpinner.setOnItemSelectedListener(new OnItemListenSelect());
	}

	
	private class OnItemListenSelect implements Spinner.OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class OnClickListener implements Button.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
