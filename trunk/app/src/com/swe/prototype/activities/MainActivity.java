package com.swe.prototype.activities;

import com.swe.prototype.R;
import com.swe.prototype.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OnClickListener listnr = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_accounts);
				//Intent i = new Intent("AccountsActivity");
				//startActivity(i);
			}
		};
		Button btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(listnr);
	}

}