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
		// ���������һ��Ӧ�ó���İ���
				"com.pwp",
				// ���������Ҫ������Activity
				"com.pwp.activity.CalendarActivity");
		// Intent intent= new Intent("chroya.foo");
		Intent intent = new Intent();
		// ���Ǹ������һ��������ʾ��apk1����ȥ��
		Bundle bundle = new Bundle();
		bundle.putString("arge1", "������ת�����ģ�����apk1");
		intent.putExtras(bundle);
		intent.setComponent(componetName);
		startActivity(intent);
		return super.onTouchEvent(event);
	}
}
