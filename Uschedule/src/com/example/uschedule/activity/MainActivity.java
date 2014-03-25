package com.example.uschedule.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.uschedule.R;
import com.mc.activity.CalendarConvert;
import com.mc.activity.CalendarView;
import com.mc.activity.ScheduleAll;
import com.mc.activity.ScheduleInfoView;
import com.mc.activity.ScheduleTypeView;
import com.mc.activity.ScheduleView;
import com.mc.borderText.BorderText;
import com.mc.dao.ScheduleDAO;

/**
 * ������ʾactivity
 * 
 * @author MC
 * 
 */
public class MainActivity extends FragmentActivity implements OnGestureListener {

	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0; // ÿ�λ��������ӻ��ȥһ����,Ĭ��Ϊ0������ʾ��ǰ�£�
	private static int jumpYear = 0; // ������Խһ�꣬�����ӻ��߼�ȥһ��,Ĭ��Ϊ0(����ǰ��)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";

	private ScheduleDAO dao = null;
	
	public MainActivity() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // ��������
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

		dao = new ScheduleDAO(this);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // ע��˳��

		setContentView(R.layout.all_main);

//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title); // ע��˳��

		gestureDetector = new GestureDetector(this);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.removeAllViews();
		calV = new CalendarView(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);
		// flipper.addView(gridView);
		flipper.addView(gridView, 0);
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
				
		// ��ת������ �İ�ť
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int xMonth = jumpMonth;
						int xYear = jumpYear;
						int gvFlag = 0;
						jumpMonth = 0;
						jumpYear = 0;
						addGridView(); // ���һ��gridView
						year_c = Integer.parseInt(currentDate.split("-")[0]);
						month_c = Integer.parseInt(currentDate.split("-")[1]);
						day_c = Integer.parseInt(currentDate.split("-")[2]);
						calV = new CalendarView(MainActivity.this, getResources(), jumpMonth, jumpYear,
								year_c, month_c, day_c);
						gridView.setAdapter(calV);
						addTextToTopTextView(topText);
						gvFlag++;
						flipper.addView(gridView, gvFlag);
						if (xMonth == 0 && xYear == 0) {
							// nothing to do
						} else if ((xYear == 0 && xMonth > 0) || xYear > 0) {
							MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
									R.anim.push_left_in));
							MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
									R.anim.push_left_out));
							MainActivity.this.flipper.showNext();
						} else {
							MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
									R.anim.push_right_in));
							MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
									R.anim.push_right_out));
							MainActivity.this.flipper.showPrevious(); 
						}
						flipper.removeViewAt(0);					}
				});
	}

	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0; // ÿ�����gridview��viewflipper��ʱ���ı��
		if (e1.getX() - e2.getX() > 120) {
			// ���󻬶�
			addGridView(); // ���һ��gridView
			jumpMonth++; // ��һ����

			calV = new CalendarView(this, getResources(), jumpMonth, jumpYear,
					year_c, month_c, day_c);
			gridView.setAdapter(calV);
			// flipper.addView(gridView);
			addTextToTopTextView(topText);
			gvFlag++;
			flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			// ���һ���
			addGridView(); // ���һ��gridView
			jumpMonth--; // ��һ����

			calV = new CalendarView(this, getResources(), jumpMonth, jumpYear,
					year_c, month_c, day_c);
			gridView.setAdapter(calV);
			gvFlag++;
			addTextToTopTextView(topText);
			// flipper.addView(gridView);
			flipper.addView(gridView, gvFlag);

			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * menu.add(0, menu.FIRST, menu.FIRST, "����"); menu.add(0, menu.FIRST+1,
		 * menu.FIRST+1, "��ת"); menu.add(0, menu.FIRST+2, menu.FIRST+2, "�ճ�");
		 * menu.add(0, menu.FIRST+3, menu.FIRST+3, "����ת��");
		 * super.onCreateOptionsMenu(menu);
		 */
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * ѡ��˵�
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		System.out.println(item);
		switch (item.getItemId()) {
		case Menu.FIRST:
		case R.id.today:
			// ��ת������
			int xMonth = jumpMonth;
			int xYear = jumpYear;
			int gvFlag = 0;
			jumpMonth = 0;
			jumpYear = 0;
			addGridView(); // ���һ��gridView
			year_c = Integer.parseInt(currentDate.split("-")[0]);
			month_c = Integer.parseInt(currentDate.split("-")[1]);
			day_c = Integer.parseInt(currentDate.split("-")[2]);
			calV = new CalendarView(this, getResources(), jumpMonth, jumpYear,
					year_c, month_c, day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			gvFlag++;
			flipper.addView(gridView, gvFlag);
			if (xMonth == 0 && xYear == 0) {
				// nothing to do
			} else if ((xYear == 0 && xMonth > 0) || xYear > 0) {
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				this.flipper.showNext();
			} else {
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				this.flipper.showPrevious();
			}
			flipper.removeViewAt(0);
			break;
		case Menu.FIRST + 1:
		case R.id.jump_to:

			new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// 1901-1-1 ----> 2049-12-31
					if (year < 1901 || year > 2049) {
						// ���ڲ�ѯ��Χ��
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("��������")
								.setMessage("��ת���ڷ�Χ(1901/1/1-2049/12/31)")
								.setPositiveButton("ȷ��", null).show();
					} else {
						int gvFlag = 0;
						addGridView(); // ���һ��gridView
						calV = new CalendarView(MainActivity.this,
								MainActivity.this.getResources(), year,
								monthOfYear + 1, dayOfMonth);
						gridView.setAdapter(calV);
						addTextToTopTextView(topText);
						gvFlag++;
						flipper.addView(gridView, gvFlag);
						if (year == year_c && monthOfYear + 1 == month_c) {
							// nothing to do
						}
						if ((year == year_c && monthOfYear + 1 > month_c)
								|| year > year_c) {
							MainActivity.this.flipper
									.setInAnimation(AnimationUtils
											.loadAnimation(MainActivity.this,
													R.anim.push_left_in));
							MainActivity.this.flipper
									.setOutAnimation(AnimationUtils
											.loadAnimation(MainActivity.this,
													R.anim.push_left_out));
							MainActivity.this.flipper.showNext();
						} else {
							MainActivity.this.flipper
									.setInAnimation(AnimationUtils
											.loadAnimation(MainActivity.this,
													R.anim.push_right_in));
							MainActivity.this.flipper
									.setOutAnimation(AnimationUtils
											.loadAnimation(MainActivity.this,
													R.anim.push_right_out));
							MainActivity.this.flipper.showPrevious();
						}
						flipper.removeViewAt(0);
						// ��ת֮����ת֮�����������Ϊ��������
						year_c = year;
						month_c = monthOfYear + 1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			}, year_c, month_c - 1, day_c).show();
			break;
		case Menu.FIRST + 2:
		case R.id.schedule:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ScheduleAll.class);
			startActivity(intent);
			break;
		case Menu.FIRST + 3:
		case R.id.solar_lunar:
			Intent intent1 = new Intent();
			intent1.setClass(MainActivity.this, CalendarConvert.class);
			intent1.putExtra("date", new int[] { year_c, month_c, day_c });
			startActivity(intent1);

			// ComponentName componetName = new ComponentName("com.mc.activity",
			// "com.mc.activity.CalendarConvert");
			// // Intent intent= new Intent("chroya.foo");
			// Intent calendarModify = new Intent();
			// // ���Ǹ������һ��������ʾ��apk1����ȥ��
			// // Bundle bundle = new Bundle();
			// // bundle.putString("arge1", "������ת�����ģ�����apk1");
			// // intent.putExtras(bundle);
			// calendarModify.putExtra("date", new int[]{year_c,month_c,day_c});
			// calendarModify.setComponent(componetName);
			// startActivity(calendarModify);

			break;
		case R.id.action_settings:
			Intent settings = new Intent();
			settings.setClass(MainActivity.this, SettingsActivity.class);
			startActivity(settings);
			break;
		case R.id.personal_settings:
			Intent perSettings = new Intent();
			perSettings.setClass(MainActivity.this, LoginActivity.class);
			startActivity(perSettings);
			break;
		case R.id.others_look:
			Intent test = new Intent();
			test.setClass(MainActivity.this,
					com.example.uschedule.Interim.class);
			startActivity(test);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// ���ͷ������� �����µ���Ϣ
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.top_day);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("��")
				.append(calV.getShowMonth()).append("��").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("��").append(calV.getLeapMonth()).append("��")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("��").append("(")
				.append(calV.getCyclical()).append("��)");
		view.setText(textDate);
		view.setTextColor(Color.BLACK);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	// ���gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// ȡ����Ļ�Ŀ�Ⱥ͸߶�
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();

		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);

		if (Width == 480 && Height == 800) {
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // ȥ��gridView�߿�
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
//		gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
			// ��gridview�еĴ����¼��ش���gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return MainActivity.this.gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView�е�ÿһ��item�ĵ���¼�
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// ����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position && position <= endPosition) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // ��һ�������
					// String scheduleLunarDay =
					// calV.getDateByClickItem(position).split("\\.")[1];
					// //��һ�������
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					String week = "";

					// ͨ�����ڲ�ѯ��һ���Ƿ񱻱�ǣ����������ճ̾Ͳ�ѯ������������ճ���Ϣ
					String[] scheduleIDs = dao.getScheduleByTagDate(
							Integer.parseInt(scheduleYear),
							Integer.parseInt(scheduleMonth),
							Integer.parseInt(scheduleDay));
					if (scheduleIDs != null && scheduleIDs.length > 0) {
						// ��ת����ʾ��һ��������ճ���Ϣ����
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								ScheduleInfoView.class);
						intent.putExtra("scheduleID", scheduleIDs);
						startActivity(intent);

					} else {
						// ֱ����ת����Ҫ����ճ̵Ľ���

						// �õ���һ�������ڼ�
						switch (position % 7) {
						case 0:
							week = "������";
							break;
						case 1:
							week = "����һ";
							break;
						case 2:
							week = "���ڶ�";
							break;
						case 3:
							week = "������";
							break;
						case 4:
							week = "������";
							break;
						case 5:
							week = "������";
							break;
						case 6:
							week = "������";
							break;
						}

						ArrayList<String> scheduleDate = new ArrayList<String>();
						scheduleDate.add(scheduleYear);
						scheduleDate.add(scheduleMonth);
						scheduleDate.add(scheduleDay);
						scheduleDate.add(week);
						// scheduleDate.add(scheduleLunarDay);

						Intent intent = new Intent();
						intent.putStringArrayListExtra("scheduleDate",
								scheduleDate);
						intent.setClass(MainActivity.this, ScheduleView.class);
						startActivity(intent);
					}
				}
			}
		});
		gridView.setLayoutParams(params);

	}

}