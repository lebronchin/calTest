package com.example.test;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class EventPage extends Activity {
	private TextView tv;
	Intent back;
	LinkedList<HashMap<String,String>> toDoList = new LinkedList<HashMap<String,String>>();
	String[] TaskKey = {"id","color","Syear","Smonth","Sday","Shour","Sminute","Eyear","Emonth","Eday","Ehour","Eminute","description"};
	String[] TaskValue ={"0001","#E6CAFF","2015","7","25","18","50","2015","8","1","16","00","Coding"};
	String[] TaskValue2 ={"0002","#FFBFFF","2015","6","10","10","30","2015","7","2","17","00","Go to supermarcket"};
	String[] TaskValue3 ={"0003","#FF9797","2015","5","5","6","10","2015","6","3","18","00","Go hiking"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_page);
		
		tv = (TextView)findViewById(R.id.detail);
		HashMap<String,String> l1 = new HashMap<String,String>();
		HashMap<String,String> l2 = new HashMap<String,String>();
		HashMap<String,String> l3 = new HashMap<String,String>();
		for(int i=0;i<TaskKey.length;i++){
			l1.put(TaskKey[i], TaskValue[i]);	
			l2.put(TaskKey[i], TaskValue2[i]);
			l3.put(TaskKey[i], TaskValue3[i]);
		}
		toDoList.add(l1);
		toDoList.add(l2);
		toDoList.add(l3);
		Intent it = getIntent();
		
		String mode = it.getStringExtra("status");
		String id = it.getStringExtra("_id");
		
//		enum e {0001,0002,0003}
//		switch(e){
//		
//		}
		
		if(mode.equals("new")){
			tv.setText("Add a new event~");
		}else{
			tv.setText("Edit the event with the id:"+id);
		}
				
		
//		back.putExtra("data", "222");
//		setResult(111, back);
		
		
	}
	public void done(View view){
		
		finish();
	}
	@Override
	public void finish() {
		back = new  Intent(this, MainActivity.class);
		startActivity(back);
		super.finish();
	}
}

