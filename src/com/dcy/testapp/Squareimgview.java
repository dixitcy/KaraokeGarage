package com.dcy.testapp;
//Image Resizing
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Squareimgview extends ImageView{

	public Squareimgview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 public Squareimgview(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    public Squareimgview(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }

	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
	    }

}
