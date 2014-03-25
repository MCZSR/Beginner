package com.example.uschedule;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class Interim extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ComponentName componetName = new ComponentName(
		// 这个是另外一个应用程序的包名
				"com.pwp.activity",
				// 这个参数是要启动的Activity
				"com.pwp.activity.CalendarActivity");
		// Intent intent= new Intent("chroya.foo");
		Intent intent = new Intent();
		// 我们给他添加一个参数表示从apk1传过去的
//		Bundle bundle = new Bundle();
//		bundle.putString("arge1", "这是跳转过来的！来自apk1");
//		intent.putExtras(bundle);
		intent.setComponent(componetName);
		startActivity(intent);
		return super.onTouchEvent(event);
	}
}
