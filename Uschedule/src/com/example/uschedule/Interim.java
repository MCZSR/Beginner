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
		// ���������һ��Ӧ�ó���İ���
				"com.pwp.activity",
				// ���������Ҫ������Activity
				"com.pwp.activity.CalendarActivity");
		// Intent intent= new Intent("chroya.foo");
		Intent intent = new Intent();
		// ���Ǹ������һ��������ʾ��apk1����ȥ��
//		Bundle bundle = new Bundle();
//		bundle.putString("arge1", "������ת�����ģ�����apk1");
//		intent.putExtras(bundle);
		intent.setComponent(componetName);
		startActivity(intent);
		return super.onTouchEvent(event);
	}
}
