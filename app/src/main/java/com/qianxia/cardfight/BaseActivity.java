package com.qianxia.cardfight;
import android.test.*;
import android.app.*;
import android.os.*;
import android.content.pm.*;

public class BaseActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionbar = getActionBar();
		if (actionbar != null){
			actionbar.hide();
		}
	}
	
	@Override
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}
}
