package com.dcy.kandg;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Sqrdimg extends ImageView {

	public Sqrdimg(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 public Sqrdimg(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    public Sqrdimg(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }

	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
	    }


}
