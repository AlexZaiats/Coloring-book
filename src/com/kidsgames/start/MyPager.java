package com.kidsgames.start;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

public class MyPager extends ViewPager {

	public MyPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		  postInitViewPager();
    }

    private FixedSpeedScroller mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    
   @Override
public void setCurrentItem(int item, boolean smoothScroll) {
	// TODO Auto-generated method stub
	super.setCurrentItem(item, smoothScroll);
}
    private void postInitViewPager() {
        try {
             Class<?> viewpager = ViewPager.class;
                Field scroller = viewpager.getDeclaredField("mScroller");
                scroller.setAccessible(true);
                Field interpolator = viewpager.getDeclaredField("sInterpolator");
                interpolator.setAccessible(true);

                mScroller = new FixedSpeedScroller(getContext(),
                        (Interpolator) interpolator.get(null));
                scroller.set(this, mScroller);
        } catch (Exception e) {
            Log.e("MyPager", e.getMessage());
        }
    }
    

    public void setScrollDurationFactor(double scrollFactor) {
    	
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    

    /**
     * Set the factor by which the duration will change
     */
    
}