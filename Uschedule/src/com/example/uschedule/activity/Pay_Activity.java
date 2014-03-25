package com.example.uschedule.activity;

import com.example.uschedule.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Pay_Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 改变文字内容，标志这是从activity跳转过来的
		TextView texView = (TextView) findViewById(R.id.others_look);
		texView.setText("我来自其他activity");
		Intent intent = getIntent();
		String value = intent.getStringExtra("arge1");
		if (value != null && !value.equals("")) {
			texView.setText(value);// 这里将显示“这是跳转过来的！来自apk1”
		} else {
			System.out.println("空的参数");
		}
	}

}
