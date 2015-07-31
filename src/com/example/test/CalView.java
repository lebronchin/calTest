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
import android.util.Log;
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
	private int day=0;
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
		setTextColor(Color.parseColor("#5B5B5B"));
		//setHeight(130);

		
		
		setBackgroundColor(Color.parseColor("#B2b2b2"));

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas=canvas;
		viewW=canvas.getWidth();
		viewH= canvas.getHeight();
		widthOfLine=viewH/18;
		setText(day+"");
		paint.setColor(Color.YELLOW);
		paint.setTextSize(50);
		paint.setTextAlign(Paint.Align.CENTER);
		
		//paint.setAlpha(50);
		
		paint.setStrokeWidth(widthOfLine);
		canvas.drawCircle(20, 40, 10, paint);
	
		paint.setColor(Color.BLUE);
		//paint.setAlpha(100);
		canvas.drawCircle(50, 40, 10, paint);
		//canvas.drawLine(0, viewH-widthOfLine/2, viewW/2, viewH-widthOfLine/2, paint);
		Log.i("brad", day+"");
		
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