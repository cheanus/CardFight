package com.qianxia.cardfight;

import android.app.*;
import android.os.*;
import android.view.View.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
	Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Button button_start = (Button)findViewById(R.id.main_button_start);
		Button button_help = (Button)findViewById(R.id.main_button_help);
		Button button_reference = (Button)findViewById(R.id.main_button_reference);
		button_start.setOnClickListener(this);
		button_help.setOnClickListener(this);
		button_reference.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		intent = null;
		switch (v.getId()) {
			case R.id.main_button_start:
				intent = new Intent(MainActivity.this, GameActivity.class);
				break;
			case R.id.main_button_help:
				intent = new Intent(MainActivity.this, HelpActivity.class);
				break;
			case R.id.main_button_reference:
				intent = new Intent(MainActivity.this, ReferenceActivity.class);
				break;
		}
		if (intent != null){
			startActivity(intent);
		}
	}
}
