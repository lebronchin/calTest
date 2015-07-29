package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class CalGroup extends LinearLayout{
	float originX=0;
	float originY=0;
	float moveX;
	float moveY;
	Paint paint;
	public CalGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		
		// TODO Auto-generated method stub
		//Log.i("brad", "InterceptTouch");
		if(ev.getAction()==MotionEvent.ACTION_UP){
			Log.i("touch", "InterceptUP");
			
			return false;
			
		}
		else if(ev.getAction()==MotionEvent.ACTION_DOWN){
			Log.i("touch", "InterceptDOWN");
			originX=ev.getAxisValue(MotionEvent.AXIS_X);
			originY=ev.getAxisValue(MotionEvent.AXIS_Y);
			return false;
			
		}else if(ev.getAction()==MotionEvent.ACTION_MOVE){
			Log.i("touch", "InterceptMOVE");
			moveX=ev.getAxisValue(MotionEvent.AXIS_X)- originX;
			moveY=ev.getAxisValue(MotionEvent.AXIS_Y)- originY;
			//Log.i("touch", moveX+":"+moveY);
			if(Math.abs(moveX)>40||Math.abs(moveY)>40){
				return true;
			}else{
				return false;	
			}
			
			
		}
		
		
		return false;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(4);
		canvas.drawCircle(100, 100, 100, paint);
	}
	


	

}