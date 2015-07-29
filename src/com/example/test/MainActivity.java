package com.example.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private CalView[] cv = new CalView[42];
	private final int INITIAL_CONTENT = 1;
	private final int CHANGE_MONTH = 2;
	private final int CHANGE_DISPLAY = 3;
	private final int CHANGE_HEIGHT = 4;
	private final int SET_MONTH_MODE = 5;
	private final int SET_WEEK_MODE = 6;
	private final int GET_TASK_LIST = 7;
	HashMap<Integer, HashMap<String, Integer[]>> drawDetail;
	MyHandler handler = new MyHandler();
	MyListAdapter adapter;
	SimpleAdapter adapter1;
	private Animation anim,animNext,animLast;
	private MySQLiteHelper helper;
	private SQLiteDatabase db;
	TextView display;
	ListView taskList;
	int[] currentMonth;
	CalGroup wholeCalendar;
	LinearLayout c1, c2, c3, c4, c5, c6, c7;
	LinkedList<HashMap<String, Float>> moveRecord = new LinkedList<HashMap<String, Float>>();
	// 璅⊥鞈�澈
	//LinkedList<HashMap<String, String>> toDoList = new LinkedList<HashMap<String, String>>();
	LinkedList<HashMap<String, String>> toDoList = new LinkedList<HashMap<String, String>>();
	String[] TaskKey = { "_id", "dataTitle", "dataColor", "dateTimeStamp", "dateTimeStampEnd"};
	String[] TaskValue = { "1","Coding", "#E6CAFF","1436889600134","1437148800134" };//7.15-7.18
	String[] TaskValue2 = { "2","Meeting", "#FFBFFF","1436976000134","1437062400134"};//7.16-7.17
	String[] TaskValue3 = { "3", "Hiking", "#FF9797", "1437062400134","1437321600134"};//7.17-7.20
	HashMap<String, Integer> DateRecord = new HashMap<String, Integer>();
	private int displayYear;
	private int displayMonth;
	private int chosenYear;
	private int chosenMonth;
	private int chosenDay;
	private int chosenLine;
	private int gridHeight;
	private int daysBefore=0;
	private int daysAfter=0;
	
	private boolean isWeekMode= false;
	float maxSpeedX = 0;
	float maxSpeedY = 0;
	VelocityTracker slideSpeed;
	Message msg = new Message();
	String[] monthList = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
			"OCTOBER", "NOVEMBER", "DECEMBER" };
	String[] shortMonthList = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	int[] idPosition = { R.id.grid_1, R.id.grid_2, R.id.grid_3, R.id.grid_4, R.id.grid_5, R.id.grid_6, R.id.grid_7,
			R.id.grid_8, R.id.grid_9, R.id.grid_10, R.id.grid_11, R.id.grid_12, R.id.grid_13, R.id.grid_14,
			R.id.grid_15, R.id.grid_16, R.id.grid_17, R.id.grid_18, R.id.grid_19, R.id.grid_20, R.id.grid_21,
			R.id.grid_22, R.id.grid_23, R.id.grid_24, R.id.grid_25, R.id.grid_26, R.id.grid_27, R.id.grid_28,
			R.id.grid_29, R.id.grid_30, R.id.grid_31, R.id.grid_32, R.id.grid_33, R.id.grid_34, R.id.grid_35,
			R.id.grid_36, R.id.grid_37, R.id.grid_38, R.id.grid_39, R.id.grid_40, R.id.grid_41, R.id.grid_42 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Calendar today = Calendar.getInstance();
		display = (TextView) findViewById(R.id.diplay);
		taskList = (ListView) findViewById(R.id.taskList);
		wholeCalendar = (CalGroup) findViewById(R.id.wholeCalendar);
		c1 = (LinearLayout) findViewById(R.id.c1);
		c2 = (LinearLayout) findViewById(R.id.c2);
		c3 = (LinearLayout) findViewById(R.id.c3);
		c4 = (LinearLayout) findViewById(R.id.c4);
		c5 = (LinearLayout) findViewById(R.id.c5);
		c6 = (LinearLayout) findViewById(R.id.c6);
		//sample database of plannerdata
		helper =new MySQLiteHelper(this, "plannerdata", null, 1);
		db=helper.getReadableDatabase();
		animNext=AnimationUtils.loadAnimation(this, R.anim.next);
		animLast=AnimationUtils.loadAnimation(this, R.anim.last);
		anim=AnimationUtils.loadAnimation(this, R.anim.alpha);
		wholeCalendar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				HashMap<String, Float> location;

				float pointX = event.getX();
				float pointY = event.getY();
				int index = event.getActionIndex();
				int pointerId = event.getPointerId(index);

				
				slideSpeed = VelocityTracker.obtain();
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					Log.i("touch", "ParentMOVE");
					slideSpeed.addMovement(event);
					slideSpeed.computeCurrentVelocity(1000);
					if (Math.abs(VelocityTrackerCompat.getXVelocity(slideSpeed, pointerId)) > Math.abs(maxSpeedX)) {
						maxSpeedX = VelocityTrackerCompat.getXVelocity(slideSpeed, pointerId);
					}
					if (Math.abs(VelocityTrackerCompat.getYVelocity(slideSpeed, pointerId)) > Math.abs(maxSpeedY)) {
						maxSpeedY = VelocityTrackerCompat.getYVelocity(slideSpeed, pointerId);
					}
					//display.setText("X velocity: " + maxSpeedX + "\nY velocity: " + maxSpeedY);
					
					location = new HashMap<String, Float>();
					location.put("x", pointX);
					location.put("y", pointY);
					moveRecord.add(location);
					
					// msg.what=CHANGE_HEIGHT;
					// handler.sendMessage(msg);
					if(!moveRecord.isEmpty()){
						//display.setText("Y : " + pointY+"\n firstY:"+moveRecord.getFirst().get("y"));
						handler.sendEmptyMessage(CHANGE_HEIGHT);
						}
				
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.i("touch", "ParentUP");
					int moveX;
					int moveY;
					try{
					moveX= (int) (moveRecord.getFirst().get("x") - moveRecord.getLast().get("x"));
					moveY = (int) (moveRecord.getFirst().get("y") - moveRecord.getLast().get("y"));
					}catch(Exception e){
						return false;
					}
					if(Math.abs(maxSpeedX)<Math.abs(maxSpeedY)){
					
					if (isWeekMode==false) {
						if (moveY >= 2 * gridHeight||maxSpeedY<-2000) {
							handler.sendEmptyMessage(SET_WEEK_MODE);
							isWeekMode=true;
						} else {
							handler.sendEmptyMessage(SET_MONTH_MODE);
							
						}
						maxSpeedY=0;
					} else {
						if (moveY <=-2 * gridHeight||maxSpeedY>2000) {
							handler.sendEmptyMessage(SET_MONTH_MODE);
							isWeekMode=false;
						} else {
							handler.sendEmptyMessage(SET_WEEK_MODE);
						}
						maxSpeedY=0;
					}
					}else{
						
						if (maxSpeedX<-2000) {
							nextMonth(null);
							
						} else if(maxSpeedX>2000) {
							lastMonth(null);
							
						}
						maxSpeedX=0;	
					}
					slideSpeed.recycle();
					moveRecord=new LinkedList<HashMap<String,Float>>();

					return true;
				}
				return true;
			}
		});

		Message msg = new Message();

		taskList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				// Log.i("brad","OK"+index);
				loadEvent(index);

			}

		});

		// 璅⊥鞈�澈
		HashMap<String, String> l1 = new HashMap<String, String>();
		HashMap<String, String> l2 = new HashMap<String, String>();
		HashMap<String, String> l3 = new HashMap<String, String>();
		for (int i = 0; i < TaskKey.length; i++) {
			l1.put(TaskKey[i], TaskValue[i]);
			l2.put(TaskKey[i], TaskValue2[i]);
			l3.put(TaskKey[i], TaskValue3[i]);
		}
		toDoList.add(l1);
		toDoList.add(l2);
		toDoList.add(l3);

		// Log.i("test","year:"+today.get(Calendar.YEAR));
		// Log.i("test","month:"+today.get(Calendar.MONTH));
		// Log.i("test",""+today.get(Calendar.DAY_OF_MONTH));

		DateRecord.put("yearOfToday", today.get(Calendar.YEAR));
		DateRecord.put("monthOfToday", today.get(Calendar.MONTH));
		DateRecord.put("dayOfToday", today.get(Calendar.DAY_OF_MONTH));
		displayYear= today.get(Calendar.YEAR);
		displayMonth= today.get(Calendar.MONTH);
		chosenYear = today.get(Calendar.YEAR);
		chosenMonth = today.get(Calendar.MONTH);
		
		
		for (int i = 0; i < idPosition.length; i++) {
			cv[i] = (CalView) findViewById(idPosition[i]);
		}
		// adapter1 = new SimpleAdapter(this , toDoList,
		// R.layout.tasklist_layout , new String[]{"id","Syear"}, new
		// int[]{R.id.timeTag,R.id.titleTag});
		// taskList.setAdapter(adapter1);
		
		handler.sendEmptyMessage(INITIAL_CONTENT);

		handler.sendEmptyMessage(GET_TASK_LIST);

		// Toast.makeText(this, cv.viewW+":"+cv.viewH,
		// Toast.LENGTH_LONG).show();
	}

	private void loadEvent(int index) {
		Intent it;

		it = new Intent(this, EventPage.class);
		// startActivity(it);

		it.putExtra("id", toDoList.get(index).get("_id"));

		// startActivity(it);
		startActivityForResult(it, 0);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		gridHeight = cv[0].getHeight();
		Log.i("test", gridHeight + "Hout");
	}

	private class MyListAdapter extends ArrayAdapter {

		private Context mContext;
		private int id;
		private List<Object> items;
		private HashMap<String, String> map;

		public MyListAdapter(Context context, int textViewResourceId, List<Object> list) {
			super(context, textViewResourceId, list);
			mContext = context;
			id = textViewResourceId;
			items = list;

		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View mView = v;
			if (mView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mView = vi.inflate(id, null);
			}

			TextView timeTag = (TextView) mView.findViewById(R.id.timeTag);
			TextView titleTag = (TextView) mView.findViewById(R.id.titleTag);
			map = toDoList.get(position);
			long inmilli=Long.parseLong(map.get("dateTimeStamp"));
			Calendar cl = Calendar.getInstance();
			cl.setTimeInMillis(inmilli);
			cl.get(Calendar.DAY_OF_MONTH);
			String hour=String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
			String minute=String.valueOf(cl.get(Calendar.MINUTE));;
			if(cl.get(Calendar.HOUR_OF_DAY)<=9){
				hour="0"+String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
			}
			if(cl.get(Calendar.MINUTE)<=9){
				minute="0"+String.valueOf(cl.get(Calendar.MINUTE));
			}
			String timeText = shortMonthList[cl.get(Calendar.MONTH)] + " " + cl.get(Calendar.DAY_OF_MONTH) + "\n"
					+ hour + ":" + minute;
			String titleText = toDoList.get(position).get("dataTitle");
			if (items.get(position) != null) {
				timeTag.setTextColor(Color.WHITE);
				// text.setText(items.get(position));
				timeTag.setText(timeText);
				int color = Color.parseColor(toDoList.get(position).get("dataColor"));
				timeTag.setBackgroundColor(color);

				titleTag.setBackgroundColor(color);
				titleTag.setTextColor(Color.WHITE);
				titleTag.setText(titleText);
				titleTag.setBackgroundColor(color);

			}

			return mView;
		}

	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int move = 0;
			switch (msg.what) {
			case 1:
				createContent(DateRecord.get("yearOfToday"), DateRecord.get("monthOfToday"));
				setChosenDay(DateRecord.get("dayOfToday"));
				break;
			case 2:
				createContent(chosenYear, chosenMonth);

				break;
			case 3:
				if(chosenDay==DateRecord.get("dayOfToday")&&chosenMonth==DateRecord.get("monthOfToday")&&chosenYear==DateRecord.get("yearOfToday")){
					display.setTextColor(Color.parseColor("#FF8000"));
					display.setText(monthList[chosenMonth] + "  " + chosenYear+" - TODAY");	
				}else{
					display.setTextColor(Color.WHITE);
				display.setText(monthList[chosenMonth] + "  " + chosenYear);
				}
				break;
			case 4:

				if (!moveRecord.isEmpty()) {
					move = (int) (1.5 * (moveRecord.getFirst().get("y") - moveRecord.getLast().get("y")));
				}

				int line = move / gridHeight;
				int heightChange = move % gridHeight;
				int CalGroupHeight;
				int topPadding;
				if(isWeekMode==false){
				CalGroupHeight = gridHeight * 6 - gridHeight * line - heightChange;
				topPadding = gridHeight * 6 - CalGroupHeight;
				}else{
				CalGroupHeight = gridHeight - gridHeight * line - heightChange;	
				topPadding = gridHeight * 6 - CalGroupHeight;
				}
				CalGroup.LayoutParams params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT,
						CalGroupHeight);
				Log.i("touch", "move:" + move + "gridHeight*6:" + gridHeight * 6 + "line:" + line + "heightChange:"
						+ heightChange);
				switch (chosenLine) {
				case 0:
					if (CalGroupHeight >= gridHeight && CalGroupHeight <= 6 * gridHeight) {
						wholeCalendar.setLayoutParams(params2);
					} else if (CalGroupHeight < gridHeight) {
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
						wholeCalendar.setLayoutParams(params2);

					} else if (CalGroupHeight > 6 * gridHeight) {
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}

					break;
				case 1:
					if (CalGroupHeight >= 2 * gridHeight && CalGroupHeight <= 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						wholeCalendar.setLayoutParams(params2);
					} else if (CalGroupHeight < 2 * gridHeight) {
						topPadding = topPadding - 4 * gridHeight;
						if (topPadding <= gridHeight && topPadding >= 0) {
							wholeCalendar.setPadding(0, -topPadding, 0, 0);
							wholeCalendar.setLayoutParams(params2);
						} else if (topPadding > gridHeight) {
							wholeCalendar.setPadding(0, -gridHeight, 0, 0);
							params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
							wholeCalendar.setLayoutParams(params2);
						}

					} else if (CalGroupHeight > 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}

					break;
				case 2:
					if (CalGroupHeight >= 3 * gridHeight && CalGroupHeight <= 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						wholeCalendar.setLayoutParams(params2);
					} else if (CalGroupHeight < 3 * gridHeight) {
						topPadding = topPadding - 3 * gridHeight;
						if (topPadding <= 2 * gridHeight && topPadding >= 0) {
							wholeCalendar.setPadding(0, -topPadding, 0, 0);
							wholeCalendar.setLayoutParams(params2);
						} else if (topPadding > 2 * gridHeight) {
							wholeCalendar.setPadding(0, -2 * gridHeight, 0, 0);
							params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
							wholeCalendar.setLayoutParams(params2);
						}

					} else if (CalGroupHeight > 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}
					break;
				case 3:
					if (CalGroupHeight >= 4 * gridHeight && CalGroupHeight <= 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						wholeCalendar.setLayoutParams(params2);
					} else if (CalGroupHeight < 4 * gridHeight) {
						topPadding = topPadding - 2 * gridHeight;
						if (topPadding <= 3 * gridHeight && topPadding >= 0) {
							wholeCalendar.setPadding(0, -topPadding, 0, 0);
							wholeCalendar.setLayoutParams(params2);
						} else if (topPadding > 3 * gridHeight) {
							wholeCalendar.setPadding(0, -3 * gridHeight, 0, 0);
							params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
							wholeCalendar.setLayoutParams(params2);
						}

					} else if (CalGroupHeight > 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}
					break;
				case 4:
					if (CalGroupHeight >= 5 * gridHeight && CalGroupHeight <= 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						wholeCalendar.setLayoutParams(params2);
					} else if (CalGroupHeight < 5 * gridHeight) {
						topPadding = topPadding - gridHeight;
						if (topPadding <= 4 * gridHeight && topPadding >= 0) {
							wholeCalendar.setPadding(0, -topPadding, 0, 0);
							wholeCalendar.setLayoutParams(params2);
						} else if (topPadding > 4 * gridHeight) {
							wholeCalendar.setPadding(0, -4 * gridHeight, 0, 0);
							params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
							wholeCalendar.setLayoutParams(params2);
						}

					} else if (CalGroupHeight > 6 * gridHeight) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}
					break;
				case 5:
					if (topPadding <= 5 * gridHeight && topPadding >= 0) {
						wholeCalendar.setPadding(0, -topPadding, 0, 0);
						wholeCalendar.setLayoutParams(params2);
					} else if (topPadding > 5 * gridHeight) {
						wholeCalendar.setPadding(0, -5 * gridHeight, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, gridHeight);
						wholeCalendar.setLayoutParams(params2);
					} else if (topPadding < 0) {
						wholeCalendar.setPadding(0, 0, 0, 0);
						params2 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT, 6 * gridHeight);
						wholeCalendar.setLayoutParams(params2);
					}

					break;

				}

				break;
			case 5:
				CalGroup.LayoutParams params3 = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT,
						6 * gridHeight);
				wholeCalendar.setLayoutParams(params3);
				wholeCalendar.setPadding(0, 0, 0, 0);
				break;
			case 6:
				CalGroup.LayoutParams params = new CalGroup.LayoutParams(CalGroup.LayoutParams.MATCH_PARENT,
						gridHeight);
				if(chosenMonth!=displayMonth||chosenYear!=displayYear){
					createContent(chosenYear, chosenMonth);
					displayMonth=chosenMonth;
					displayYear=chosenYear;
					setChosenDay(chosenDay);
					Log.i("steve","cline:"+chosenLine+" chosenDay:"+chosenDay);
				}
				handler.sendEmptyMessage(GET_TASK_LIST);
				switch (chosenLine) {
				case 0:
					wholeCalendar.setPadding(0, 0, 0, 0);
					wholeCalendar.setLayoutParams(params);
					
					break;
				case 1:
					wholeCalendar.setPadding(0, -gridHeight, 0, 0);
					
					wholeCalendar.setLayoutParams(params);
					
					break;
				case 2:
					wholeCalendar.setPadding(0, -2 * gridHeight, 0, 0);
					wholeCalendar.setLayoutParams(params);
					break;
				case 3:
					wholeCalendar.setPadding(0, -3 * gridHeight, 0, 0);
					wholeCalendar.setLayoutParams(params);
					break;
				case 4:
					wholeCalendar.setPadding(0, -4 * gridHeight, 0, 0);
					wholeCalendar.setLayoutParams(params);
					break;
				case 5:
					wholeCalendar.setPadding(0, -5 * gridHeight, 0, 0);
					break;
				}
				break;
				
			case 7:
				Calendar getStamp=Calendar.getInstance();
				
				getStamp.set(chosenYear,chosenMonth,chosenDay,0,0,0);
				long chosenInMilli=((getStamp.getTimeInMillis()/1000)+1)*1000;
				
				//以上只是要抓chosendate的後一天
				
				query(chosenInMilli);
				sendEmptyMessage(CHANGE_DISPLAY);
				adapter = new MyListAdapter(MainActivity.this, R.layout.tasklist_layout, (List) toDoList);
				taskList.setAdapter(adapter);
				String selectedColor="#272727";
				for(int i =0;i<cv.length;i++){
					String color=cv[i].getWordColor();
					cv[i].setTextColor(Color.parseColor(color));
				}
				
				if(chosenMonth==displayMonth&&chosenYear==displayYear){
				int whitchGrid=daysBefore+chosenDay-1;
				cv[whitchGrid].setTextColor(Color.parseColor(selectedColor));
				}else if(chosenMonth>displayMonth&&chosenYear==displayYear||chosenYear>displayYear){
				int whitchGrid=42-daysAfter+chosenDay-1;
				cv[whitchGrid].setTextColor(Color.parseColor(selectedColor));
				}else if(chosenMonth<displayMonth&&chosenYear==displayYear||chosenYear<displayYear){
					Calendar cl=Calendar.getInstance();
					cl.set(chosenYear, chosenMonth, chosenDay);
					
					int max=cl.getActualMaximum(Calendar.DAY_OF_MONTH);
					
					int whitchGrid= chosenDay-(max-daysBefore)-1;
					cv[whitchGrid].setTextColor(Color.parseColor(selectedColor));
				}
				
				break;
				
			}
			
		}
	}
	
   public void query(long timeStamp){//timeStamp為隔日凌晨
	   
	   Log.i("brad", chosenYear+"."+chosenMonth+"."+chosenDay+"Query");
	   Cursor c=db.query("plannerDB", new String[]{"_id","dataTitle","dataColor","dateTimeStamp","dateTimeStampEnd"}, "(dateTimeStamp<? and dateTimeStampEnd>?) or (dateYear=? and dateMonth=? and dateDay=?)", new String[]{timeStamp+"",timeStamp+"",chosenYear+"",chosenMonth+"",chosenDay+""}, null, null, "dateTimeStamp asc");
	   toDoList.clear();
	   while(c.moveToNext()){
			String _id = c.getString(c.getColumnIndex("_id"));
			String dataTitle =c.getString(c.getColumnIndex("dataTitle"));
			String dataColor =c.getString(c.getColumnIndex("dataColor"));
			String dateTimeStamp =c.getString(c.getColumnIndex("dateTimeStamp"));
			String dateTimeStampEnd =c.getString(c.getColumnIndex("dateTimeStampEnd"));
			HashMap<String, String> attr=new HashMap<String, String>();
			attr.put("_id", _id);
			attr.put("dataTitle", dataTitle);
			attr.put("dataColor", dataColor);
			attr.put("dateTimeStamp", dateTimeStamp);
			attr.put("dateTimeStampEnd", dateTimeStampEnd);
			
				toDoList.add(attr);
			
		
		}
	   
   }
	
	public void home(View view){
		
		if(DateRecord.get("yearOfToday")>displayYear){
			wholeCalendar.startAnimation(animNext);
		}else if(DateRecord.get("yearOfToday")<displayYear){
			wholeCalendar.startAnimation(animLast);
		}else if(DateRecord.get("monthOfToday")==displayMonth){
			wholeCalendar.startAnimation(anim);
		}else if(DateRecord.get("monthOfToday")>displayMonth){
			wholeCalendar.startAnimation(animNext);
		}else if(DateRecord.get("monthOfToday")<displayMonth){
			wholeCalendar.startAnimation(animLast);
		}
		
		displayYear=chosenYear=DateRecord.get("yearOfToday");
		displayMonth=chosenMonth=DateRecord.get("monthOfToday");
		setChosenDay(DateRecord.get("dayOfToday"));
		handler.sendEmptyMessage(CHANGE_MONTH);
		if(isWeekMode){
			handler.sendEmptyMessage(SET_WEEK_MODE);
		}
		handler.sendEmptyMessage(GET_TASK_LIST);
		
		
		
	}
	

	private int[] getCalendarContent(int yyyy, int mm) {
		// int[] thisMonth =
		// {29,30,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,1,2};
		// return thisMonth;
		// yyyy= 2013;
		// mm= 4; // 0 as January - 11 as December
		// dd= 1; // with actual day number

		int dd = 1;
		int lastMm;
		int fWeekDayThisMonth;
		int lDayThisMonth;
		int lWeekDayThisMonth;
		int lDayLastMonth;

		int[] thisMonthAmount;
		int[] lastMonthAmount;
		int[] nextMonthAmount;
		int[] totalMonthArr;

		lastMm = mm - 1;

		Calendar thisMonth = Calendar.getInstance();
		thisMonth.set(yyyy, mm, dd);
		fWeekDayThisMonth = thisMonth.get(Calendar.DAY_OF_WEEK);
		lDayThisMonth = thisMonth.getActualMaximum(Calendar.DATE);
		Log.i("test", "fw" + fWeekDayThisMonth);
		Log.i("test", "ld" + lDayThisMonth);
		Log.i("test", "month" + thisMonth.get(Calendar.MONTH));
		thisMonth.set(yyyy, mm, lDayThisMonth);
		lWeekDayThisMonth = thisMonth.get(Calendar.DAY_OF_WEEK);

		Calendar lastMonth = Calendar.getInstance();
		lastMonth.set(yyyy, lastMm, dd);
		lDayLastMonth = lastMonth.getActualMaximum(Calendar.DATE);

		lastMonthAmount = new int[fWeekDayThisMonth - 1];
		int tempL = lDayLastMonth - fWeekDayThisMonth + 2;

		for (int i = 0; i < fWeekDayThisMonth - 1; i++) {
			lastMonthAmount[i] = tempL;
			tempL++;
			// Log.i("steve", String.valueOf(lastMonthAmount[i]));
		}

		thisMonthAmount = new int[lDayThisMonth];
		int countTemp = 1;
		for (int j = 0; j < lDayThisMonth; j++) {
			thisMonthAmount[j] = countTemp;
			countTemp++;
			// Log.i("steve", String.valueOf(thisMonthAmount[i]));
		}

		int tempN = 7 - lWeekDayThisMonth; // Sunday as 1 to Saturday as 7
		int checkTotal = tempN + lastMonthAmount.length + thisMonthAmount.length;
		if (checkTotal < 42 && checkTotal > 28) {
			tempN += 7;
		} else if (checkTotal <= 28) {
			lastMonthAmount = new int[fWeekDayThisMonth - 1 + 7];
			tempL = lDayLastMonth - fWeekDayThisMonth + 2 - 7;
			for (int i = 0; i < fWeekDayThisMonth - 1 + 7; i++) {
				lastMonthAmount[i] = tempL;
				tempL++;
				// Log.i("steve", String.valueOf(lastMonthAmount[i]));
			}
			tempN += 7;
		}
		nextMonthAmount = new int[tempN];

		for (int k = 0; k < tempN; k++) {
			nextMonthAmount[k] = k + 1;
			// Log.i("steve", String.valueOf(nextMonthAmount[k]));
		}

		int length = lastMonthAmount.length + thisMonthAmount.length + nextMonthAmount.length;
		int[] result = new int[length];
		System.arraycopy(lastMonthAmount, 0, result, 0, lastMonthAmount.length);
		System.arraycopy(thisMonthAmount, 0, result, lastMonthAmount.length, thisMonthAmount.length);
		System.arraycopy(nextMonthAmount, 0, result, lastMonthAmount.length + thisMonthAmount.length,
				nextMonthAmount.length);
		// System.arraycopy(嚙諉瘀蕭嚙罷嚙瘠嚙璀嚙稻嚙締嚙踝蕭嚙豬值，嚙諍迎蕭嚙罷嚙瘠嚙璀嚙稻嚙締嚙踝蕭嚙豬值，嚙複製嚙踝蕭嚙踝蕭);
		return result;

	}

	private void createContent(int yyyy, int mm) {
		currentMonth = getCalendarContent(yyyy, mm);
		daysBefore=0;
		daysAfter=0;
		
		for (int i = 0; i < idPosition.length; i++) {

			if (i < 8 && currentMonth[i] > 21) {
				String color="#E0E0E0";
				cv[i].setTextColor(Color.parseColor(color));
				cv[i].setWordColor(color);
				cv[i].setText(String.valueOf(currentMonth[i]));
				if(mm==0){
					cv[i].setDate(yyyy-1, 11, currentMonth[i], i / 7);
				}else{
				cv[i].setDate(yyyy, mm - 1, currentMonth[i], i / 7);
				}
				daysBefore++;
				
			} else if (i > 29 && currentMonth[i] < 13) {
				String color="#E0E0E0";
				cv[i].setTextColor(Color.parseColor(color));
				cv[i].setWordColor(color);
				cv[i].setText(String.valueOf(currentMonth[i]));
				if(mm==11){
					cv[i].setDate(yyyy+1, 0, currentMonth[i], i / 7);
				}else{
				cv[i].setDate(yyyy, mm + 1, currentMonth[i], i / 7);
				}
				daysAfter++;
			} else {
				String color="#9D9D9D";
				cv[i].setTextColor(Color.parseColor(color));
				cv[i].setWordColor(color);
				cv[i].setText(String.valueOf(currentMonth[i]));
				cv[i].setDate(yyyy, mm, currentMonth[i], i / 7);
				
			}
		}
		handler.sendEmptyMessage(CHANGE_DISPLAY);
	}
	private void setChosenDay(int day){
		
		chosenDay=day;
		
		for(int i =0;i<cv.length;i++){
			 if(((chosenMonth>displayMonth&&chosenYear==displayYear)||chosenYear>displayYear)&&(i > 29 &&cv[i].getDay() < 13)&&cv[i].getDay()==day){
				chosenLine=cv[i].getLineOfCalendar();
				
				break;
			}else if(((chosenMonth<displayMonth&&chosenYear==displayYear)||chosenYear<displayYear)&&(i < 8 && cv[i].getDay() > 21)&&cv[i].getDay()==day){
				chosenLine=cv[i].getLineOfCalendar();
				
				break;
			}else if(cv[i].getDay()==day&&chosenMonth==displayMonth&&chosenYear==displayYear){
				if(i < 8 && cv[i].getDay() > 21){
					
					continue;
				}else{
				chosenLine=cv[i].getLineOfCalendar();
				
				break;
				}
			}
			
		}
	}
	public void setChosenDate(View view) {
		
		chosenYear = ((CalView) view).getYear();
		chosenMonth = ((CalView) view).getMonth();
//		if(chosenYear!=displayYear||chosenMonth!=displayMonth){
//			chosenDay=((CalView) view).getDay();
//		}else{
		setChosenDay(((CalView) view).getDay());
		Log.i("steve", "line:"+((CalView) view).getLineOfCalendar());
		
		//query here
        handler.sendEmptyMessage(GET_TASK_LIST);
		
		Toast.makeText(this,
				"You've chosen:" + chosenYear + "." + (chosenMonth + 1) + "." + chosenDay + " Line" + (chosenLine + 1),
				Toast.LENGTH_SHORT).show();
	}

	public void lastMonth(View view) {
		if(isWeekMode==false){
		if (displayMonth >0) {
			displayMonth--;
			chosenMonth=displayMonth;
			
		} else {
			chosenMonth = 11;
			displayMonth=11;
			
			displayYear--;
			chosenYear=displayYear;
			
		}
		setChosenDay(1);
		handler.sendEmptyMessage(CHANGE_MONTH);
		
		}else{
			int minChosenLine=0;
			int switchWeek=5;
			int [] lastMonth;
			
			LinkedList<Integer> count=new LinkedList<Integer>();
			if(displayMonth==0){
				lastMonth = getCalendarContent(displayYear-1, 11);	
			}else{
			lastMonth = getCalendarContent(displayYear, displayMonth-1);
			}
			for(int i =0;i<lastMonth.length;i++){
				if(i > 29 && lastMonth[i] < 13){
					count.add(lastMonth[i]);
				}
			}
			if(daysBefore/7==1){
				 minChosenLine++;
			}else if(daysBefore/7==0&&daysBefore%7>3){
				if(chosenLine==0){
					switchWeek--;
				}else{
				 minChosenLine++;
				}
			}
			
			if(count.size()/7==1&&count.size()%7<=3){
				switchWeek-=1;
			}else if(count.size()/7==1&&count.size()%7>3){
				switchWeek-=2;
			}else if(count.size()/7==0&&count.size()%7>3){
				switchWeek-=1;
			}
			if(chosenLine>minChosenLine){
				chosenLine--;
				if(chosenLine==0){
					setChosenDay(1);	
				}else{
				int setDay=cv[chosenLine*7].getDay();
				setChosenDay(setDay);
				}
				handler.sendEmptyMessage(SET_WEEK_MODE);
			}else{
				if (displayMonth >0) {
					displayMonth--;
					chosenMonth=displayMonth;
					
					setChosenDay(1);
				} else {
					chosenMonth = 11;
					displayMonth=11;
					
					displayYear--;
					chosenYear=displayYear;
					setChosenDay(1);
				}
				int[] LastM=getCalendarContent(chosenYear, chosenMonth);

				int setDay=LastM[switchWeek*7];
				setChosenDay(setDay);
				
				
				handler.sendEmptyMessage(CHANGE_MONTH);
				chosenLine=switchWeek;
				
				handler.sendEmptyMessage(SET_WEEK_MODE);
			}
			
		}
		wholeCalendar.startAnimation(animLast);	
		handler.sendEmptyMessage(GET_TASK_LIST);
	}

	public void nextMonth(View view) {
		
		if(isWeekMode==false){
		
		if (displayMonth < 11) {
			
			displayMonth++;
			chosenMonth=displayMonth;
			

			
		} else {
			chosenMonth = 0;
			displayMonth=0;
			
			displayYear++;
			chosenYear=displayYear;
			
		}
		setChosenDay(1);
		handler.sendEmptyMessage(CHANGE_MONTH);
		
		
	}else{
		int weeks=5;//0 to 5
		int switchWeek=0;
		Calendar check=Calendar.getInstance();
		check.set(displayYear, 1, 1);
		if(displayMonth==0&&check.get(Calendar.DAY_OF_WEEK)==1){
			switchWeek+=1;
		}
		if(daysAfter/7==1&&daysAfter%7>3){
			if(chosenLine==4){
				switchWeek++;
				weeks-=1;
			}else{
			weeks-=2;
			}
		}else if(daysAfter/7==1&&(daysAfter%7<=3&&daysAfter%7>0)){
			weeks-=1;
			switchWeek+=1;
			
		}else if(daysAfter/7==1&&daysAfter%7==0){
			weeks-=1;
			
		}else if(daysAfter/7==0){
			if(chosenLine==5){
				switchWeek++;	
			}else{
			weeks-=1;
			}
		}
		if(chosenLine<weeks){
			chosenLine++;
			int setDay=cv[chosenLine*7].getDay();
			setChosenDay(setDay);
			handler.sendEmptyMessage(SET_WEEK_MODE);
		}else{
			if (displayMonth < 11) {
				displayMonth++;				
				chosenMonth=displayMonth;
				
							
			} else {
				chosenMonth = 0;
				displayMonth=0;
				displayYear++;
				chosenYear=displayYear;
				
			}
			if(switchWeek==0){
				setChosenDay(1);
				}else{
					chosenDay=daysAfter%7+1;
				}
			
			handler.sendEmptyMessage(CHANGE_MONTH);
			
			chosenLine=switchWeek;
			
			
			handler.sendEmptyMessage(SET_WEEK_MODE);
		}
	}
	wholeCalendar.startAnimation(animNext);	
	handler.sendEmptyMessage(GET_TASK_LIST);
}
}
