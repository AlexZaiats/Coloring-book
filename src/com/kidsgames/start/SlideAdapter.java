package com.kidsgames.start;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kidsgames.start.DrawerSliderActivity.DetailOnPageChangeListener;
import com.vikinc.coloring.R;

public class SlideAdapter extends PagerAdapter {

	  private DetailOnPageChangeListener pageListener;

	  private Activity activity;
	  private int[] ids;
	  private SharedPreferences prefs;
	  private List<Integer> levelsList;
	  int height;
	  private String packName;
	  
	  public SlideAdapter(DetailOnPageChangeListener pageListener , ViewPager pager, Context context, Activity activity, String json,String packName)
	  {
		  
		  Display display = activity.getWindowManager().getDefaultDisplay(); 
		  height = display.getHeight();  // deprecated
		  height = (height/5)*3;
		  
		    prefs = context.getSharedPreferences("com.bublecat.drawer", Activity.MODE_PRIVATE);
			String levels = prefs.getString(packName, null);
			levelsList = new ArrayList<Integer>();
			if (levels != null)
			{
				String[] everyLevel = levels.split(",");
				for (int i = 0 ; i < everyLevel.length ; i++)
				{
					int lv = Integer.parseInt(everyLevel[i]);
					levelsList.add(lv);
				}
			}
		  this.pageListener = pageListener;
		  this.activity = activity;
		  this.packName = packName;
	  }

	  @Override
	  public int getItemPosition (Object object)
	  {
	      return POSITION_NONE;
	  }
	  
	  public void setIds(int[] ids)
	  {
		  this.ids = ids;
		  notifyDataSetChanged();
	  }
	  
	  @Override
	  public int getCount ()
	  {
	    return (ids.length/2);
	  }

	  @Override
	  public boolean isViewFromObject (View view, Object object)
	  {
	    return view == (View)object;
	  }

	  @Override
	  public Object instantiateItem (ViewGroup container, int position)
	  {
		  View view = null ;
			try{
				int i = position*2;
				 view = activity.getLayoutInflater().inflate(R.layout.page_item, null);
				 if (!levelsList.contains(i+1))
					 ((ImageView)view.findViewById(R.id.image_first)).setImageResource(ids[i]);
				 else
					 ((ImageView)view.findViewById(R.id.image_first)).setImageResource(ResourceId.getResId("level"+(i+1)+"_color_"+packName, ResourceId.DRAWABLE, activity));
				 
				 if (!levelsList.contains(i+2))
					 ((ImageView)view.findViewById(R.id.image_second)).setImageResource(ids[i+1]);
				 else
					 ((ImageView)view.findViewById(R.id.image_second)).setImageResource(ResourceId.getResId("level"+(i+2)+"_color_"+packName, ResourceId.DRAWABLE, activity));
				 

				
				((ImageView)view.findViewById(R.id.image_first)).setOnClickListener((DrawerSliderActivity)activity);
				((ImageView)view.findViewById(R.id.image_second)).setOnClickListener((DrawerSliderActivity)activity);
				


			}
			catch(Exception e)
			{
				Log.d("Buble" , "Exception " + e);
			}
			  ((ViewPager) container).addView(view);

				ImageView first = (ImageView)view.findViewById(R.id.image_first);
				ImageView second = (ImageView)view.findViewById(R.id.image_second);
				
				first.setLayoutParams(new LinearLayout.LayoutParams(height, height));
				second.setLayoutParams(new LinearLayout.LayoutParams(height, height));
				
		        return view;
	  }
	  
	  @Override
	  public void destroyItem (ViewGroup container, int position, Object object)
	  {
		  View view = (View)object;
		  ((ViewPager) container).removeView(view);
		  view = null;
	  }
}

