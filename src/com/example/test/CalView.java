package com.example.test;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CalView extends TextView{
    Paint paint =new Paint();
    Canvas canvas;
    private float viewW;
    private float viewH;
    private float widthOfLine;
	private int year;
	private int month;
	private int day;
	private int lineOfCalendar;
	private String textColor;
	private HashMap<String, Integer[]> drawSpec;
	private String[] oneDayTasks;
	public CalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setTextSize(25);//
		setGravity(Gravity.CENTER);
		setClickable(true);
		//setTextColor(Color.parseColor("#9d9d9d"));
		//setHeight(130);
//		XmlPullParser xp = Resources.getSystem().getXml(R.drawable.round_corner);
//		Drawable db;
//		try {
//			db = Drawable.createFromXml(getResources(), xp);
//			setBackground(db);
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		setBackgroundColor(Color.WHITE);
//		setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas=canvas;
		viewW=canvas.getWidth();
		viewH= canvas.getHeight();
		widthOfLine=viewH/18;
		paint.setColor(Color.GRAY);
		paint.setTextSize(50);
		paint.setTextAlign(Paint.Align.CENTER);
		
		paint.setAlpha(100);
		
		paint.setStrokeWidth(widthOfLine);
		//canvas.drawCircle(50, 50, 50, paint);
		//canvas.drawLine(0, viewH-widthOfLine/2, viewW/2, viewH-widthOfLine/2, paint);
		viewW = getWidth();
		viewH =getHeight();
		//Log.i("test", viewW+":"+viewH);
		
//		canvas.drawText(String.valueOf(8), viewW/2f,80, paint);
//		canvas.drawCircle(0, 0, 20, paint);
	}
	

public void draw(String[] tasks,HashMap<String, Integer[]> detail){
	drawSpec = detail;
	oneDayTasks = tasks;
	
}
public void setWordColor(String color){
	textColor=color;
	
}
	
	
public void setDate(int yyyy,int mm,int dd,int lineOfCalendar){
	
	year=yyyy;
	month=mm;
	day=dd;
	this.lineOfCalendar=lineOfCalendar;
}
public int getYear(){
	return year;
}
public int getMonth(){
	return month;
}
public int getDay(){
	return day;
}
public int getLineOfCalendar(){
	return lineOfCalendar;
}
public String getWordColor(){
	return textColor;
}
}