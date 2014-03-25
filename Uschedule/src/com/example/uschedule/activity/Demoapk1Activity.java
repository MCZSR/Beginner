package com.example.uschedule.activity;

import com.example.uschedule.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Demoapk1Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ComponentName componetName = new ComponentName(
		// 这个是另外一个应用程序的包名
				"com.pwp",
				// 这个参数是要启动的Activity
				"com.pwp.activity.CalendarActivity");
		// Intent intent= new Intent("chroya.foo");
		Intent intent = new Intent();
		// 我们给他添加一个参数表示从apk1传过去的
		Bundle bundle = new Bundle();
		bundle.putString("arge1", "这是跳转过来的！来自apk1");
		intent.putExtras(bundle);
		intent.setComponent(componetName);
		startActivity(intent);
		return super.onTouchEvent(event);
	}
}
