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
 * 日历显示activity
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
	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";

	private ScheduleDAO dao = null;
	
	public MainActivity() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

		dao = new ScheduleDAO(this);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序

		setContentView(R.layout.all_main);

//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title); // 注意顺序

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
				
		// 跳转到今天 的按钮
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int xMonth = jumpMonth;
						int xYear = jumpYear;
						int gvFlag = 0;
						jumpMonth = 0;
						jumpYear = 0;
						addGridView(); // 添加一个gridView
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
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
			// 像左滑动
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

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
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

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
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * menu.add(0, menu.FIRST, menu.FIRST, "今天"); menu.add(0, menu.FIRST+1,
		 * menu.FIRST+1, "跳转"); menu.add(0, menu.FIRST+2, menu.FIRST+2, "日程");
		 * menu.add(0, menu.FIRST+3, menu.FIRST+3, "日期转换");
		 * super.onCreateOptionsMenu(menu);
		 */
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		System.out.println(item);
		switch (item.getItemId()) {
		case Menu.FIRST:
		case R.id.today:
			// 跳转到今天
			int xMonth = jumpMonth;
			int xYear = jumpYear;
			int gvFlag = 0;
			jumpMonth = 0;
			jumpYear = 0;
			addGridView(); // 添加一个gridView
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
						// 不在查询范围内
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("错误日期")
								.setMessage("跳转日期范围(1901/1/1-2049/12/31)")
								.setPositiveButton("确认", null).show();
					} else {
						int gvFlag = 0;
						addGridView(); // 添加一个gridView
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
						// 跳转之后将跳转之后的日期设置为当期日期
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
			// // 我们给他添加一个参数表示从apk1传过去的
			// // Bundle bundle = new Bundle();
			// // bundle.putString("arge1", "这是跳转过来的！来自apk1");
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

	// 添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.top_day);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(")
				.append(calV.getCyclical()).append("年)");
		view.setText(textDate);
		view.setTextColor(Color.BLACK);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	// 添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// 取得屏幕的宽度和高度
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
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
//		gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return MainActivity.this.gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position && position <= endPosition) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					// String scheduleLunarDay =
					// calV.getDateByClickItem(position).split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					String week = "";

					// 通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
					String[] scheduleIDs = dao.getScheduleByTagDate(
							Integer.parseInt(scheduleYear),
							Integer.parseInt(scheduleMonth),
							Integer.parseInt(scheduleDay));
					if (scheduleIDs != null && scheduleIDs.length > 0) {
						// 跳转到显示这一天的所有日程信息界面
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								ScheduleInfoView.class);
						intent.putExtra("scheduleID", scheduleIDs);
						startActivity(intent);

					} else {
						// 直接跳转到需要添加日程的界面

						// 得到这一天是星期几
						switch (position % 7) {
						case 0:
							week = "星期日";
							break;
						case 1:
							week = "星期一";
							break;
						case 2:
							week = "星期二";
							break;
						case 3:
							week = "星期三";
							break;
						case 4:
							week = "星期四";
							break;
						case 5:
							week = "星期五";
							break;
						case 6:
							week = "星期六";
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