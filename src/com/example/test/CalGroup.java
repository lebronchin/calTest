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
	Paint paint;
	public CalGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//Log.i("brad", "InterceptTouch");
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			Log.i("touch", "InterceptDOWN");
			
			return false;
			
		}else if(ev.getAction()==MotionEvent.ACTION_MOVE){
			Log.i("touch", "InterceptMOVE");
			return true;
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