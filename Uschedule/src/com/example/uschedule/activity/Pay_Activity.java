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
		// �ı��������ݣ���־���Ǵ�activity��ת������
		TextView texView = (TextView) findViewById(R.id.others_look);
		texView.setText("����������activity");
		Intent intent = getIntent();
		String value = intent.getStringExtra("arge1");
		if (value != null && !value.equals("")) {
			texView.setText(value);// ���ｫ��ʾ��������ת�����ģ�����apk1��
		} else {
			System.out.println("�յĲ���");
		}
	}

}
