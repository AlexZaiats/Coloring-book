package com.kidsgames.start;


import android.annotation.SuppressLint;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 1;
    private double mScrollFactor = 10;
    
    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

 
	@SuppressLint("NewApi")
	public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }



    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
    
    public void setScrollDurationFactor(double scrollFactor) {
        mScrollFactor = scrollFactor;
    }
    
    
    @Override
    public void startScroll(int startX, int startY, int dx, int dy,
            int duration) {
    
    	int newDur = duration;
    	int scrollFactor = 2;
    	
    	if (duration == 200)
    	{
    		newDur = 200;
    		scrollFactor = 5;
    	}
        super.startScroll(startX, startY, dx, dy,
                (int) (newDur * scrollFactor));
    }
}