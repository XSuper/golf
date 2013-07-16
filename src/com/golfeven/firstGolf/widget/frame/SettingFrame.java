package com.golfeven.firstGolf.widget.frame;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.ui.LoginActivity;

public class SettingFrame extends LinearLayout{
	

	public SettingFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}


	private void initViews(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.frame_setting, this);
		View login = view.findViewById(R.id.frame_setting_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				
			}
		});
	}
	

}
