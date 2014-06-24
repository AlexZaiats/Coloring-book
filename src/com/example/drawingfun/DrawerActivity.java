package com.example.drawingfun;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kidsgames.start.ResourceId;
import com.vikinc.coloring.R;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial series: - Android
 * SDK: Create a Drawing App
 * 
 * Sue Smith August 2013
 * 
 */
public class DrawerActivity extends Activity implements OnClickListener {

	private DrawingView drawView;

	private ImageButton currPaint;
	private Button newBtn;

	// sizes
	private float mediumBrush;

	private String packName;
	private int idHelp;
	private ImageView imageViewHelp;
	private ImageView level_finished;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawView = (DrawingView) findViewById(R.id.drawing);

		if (getInches() < 6.5)
			mediumBrush = getResources().getInteger(R.integer.medium_size);
		else
			mediumBrush = getResources().getInteger(R.integer.large_size);

		drawView.setBrushSize(mediumBrush);
		drawView.setActivity(this);

		findViewById(R.id.first_color).setOnClickListener(this);
		findViewById(R.id.second_color).setOnClickListener(this);
		findViewById(R.id.third_color).setOnClickListener(this);
		findViewById(R.id.fourth_color).setOnClickListener(this);
		findViewById(R.id.fifth_color).setOnClickListener(this);
		findViewById(R.id.six_color).setOnClickListener(this);

		newBtn = (Button) findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);

		findViewById(R.id.home_btn).setOnClickListener(this);
		packName = getIntent().getStringExtra("pack");
		drawView.setPack(packName);

		int level = getIntent().getIntExtra("level", 1);

		idHelp = ResourceId.getResId("level" + level + "_color_" + packName,
				ResourceId.DRAWABLE, this);
		imageViewHelp = (ImageView) findViewById(R.id.help_image);
		imageViewHelp.setImageResource(idHelp);

		imageViewHelp.setVisibility(View.GONE);
		
		level_finished = (ImageView) findViewById(R.id.level_finished);
		level_finished.setVisibility(View.GONE);
		
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight(); // deprecated
		height = (height / 5) * 3;

		imageViewHelp.setLayoutParams(new RelativeLayout.LayoutParams(height,
				height));
		imageViewHelp.setVisibility(View.GONE);

		String json = ResourceId.getJson(this, packName);
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject levelObject = jsonObject.getJSONObject("level_" + level);
			JSONArray colors = levelObject.getJSONArray("colors");

			ArrayList<String> colorsName = new ArrayList<String>();

			int[] bitmaps = new int[colors.length() + 1];
			int id = ResourceId.getResId("stroke_level_" + level + "_"
					+ packName, ResourceId.DRAWABLE, this);
			bitmaps[0] = id;
			for (int i = 0; i < colors.length(); i++) {
				colorsName.add(colors.getString(i));
				setVisibleColor(i, colors.getString(i));

				id = ResourceId.getResId(
						"level" + level + "_" + colors.getString(i) + "_"
								+ packName, ResourceId.DRAWABLE, this);
				bitmaps[i + 1] = id;
			}
			drawView.setColors(colorsName);
			drawView.setBitmapArray(bitmaps);
			drawView.setLevel(level);

		} catch (Exception e) {

		}
	}

	public void setActiveColor(int pos, String color) {
		Log.d("Buble", "Active");
		int id = ResourceId.getResId(color, ResourceId.DRAWABLE, this);

		switch (pos) {
		case 0:
			((ImageView) findViewById(R.id.first_color)).setImageResource(id);
			((ImageView) findViewById(R.id.first_color))
					.setVisibility(View.VISIBLE);
			break;
		case 1:
			((ImageView) findViewById(R.id.second_color)).setImageResource(id);
			((ImageView) findViewById(R.id.second_color))
					.setVisibility(View.VISIBLE);
			break;
		case 2:
			((ImageView) findViewById(R.id.third_color)).setImageResource(id);
			((ImageView) findViewById(R.id.third_color))
					.setVisibility(View.VISIBLE);
			break;
		case 3:
			((ImageView) findViewById(R.id.fourth_color)).setImageResource(id);
			((ImageView) findViewById(R.id.fourth_color))
					.setVisibility(View.VISIBLE);
			break;
		case 4:
			((ImageView) findViewById(R.id.fifth_color)).setImageResource(id);
			((ImageView) findViewById(R.id.fifth_color))
					.setVisibility(View.VISIBLE);
			break;
		case 5:
			((ImageView) findViewById(R.id.six_color)).setImageResource(id);
			((ImageView) findViewById(R.id.six_color))
					.setVisibility(View.VISIBLE);
			break;
		case 6:
			((ImageView) findViewById(R.id.seven_color)).setImageResource(id);
			((ImageView) findViewById(R.id.seven_color))
					.setVisibility(View.VISIBLE);
			break;
		case 7:
			((ImageView) findViewById(R.id.eight_color)).setImageResource(id);
			((ImageView) findViewById(R.id.eight_color))
					.setVisibility(View.VISIBLE);
			break;
		}

	}

	public void setDefaultColor(ArrayList<String> colorsName) {
		int i = 0;
		for (String color : colorsName) {
			int id = ResourceId.getResId(color, ResourceId.DRAWABLE, this);
			switch (i) {
			case 0:
				((ImageView) findViewById(R.id.first_color))
						.setImageResource(id);
				break;
			case 1:
				((ImageView) findViewById(R.id.second_color))
						.setImageResource(id);
				break;
			case 2:
				((ImageView) findViewById(R.id.third_color))
						.setImageResource(id);
				break;
			case 3:
				((ImageView) findViewById(R.id.fourth_color))
						.setImageResource(id);
				break;
			case 4:
				((ImageView) findViewById(R.id.fifth_color))
						.setImageResource(id);
				break;
			case 5:
				((ImageView) findViewById(R.id.six_color)).setImageResource(id);
				break;
			case 6:
				((ImageView) findViewById(R.id.seven_color))
						.setImageResource(id);
				break;
			case 7:
				((ImageView) findViewById(R.id.eight_color))
						.setImageResource(id);
				break;
			}
			i++;
		}
	}

	private void setVisibleColor(int pos, String color) {
		int id = ResourceId.getResId(color, ResourceId.DRAWABLE, this);

		switch (pos) {
		case 0:
			((ImageView) findViewById(R.id.first_color)).setImageResource(id);
			((ImageView) findViewById(R.id.first_color))
					.setVisibility(View.VISIBLE);
			break;
		case 1:
			((ImageView) findViewById(R.id.second_color)).setImageResource(id);
			((ImageView) findViewById(R.id.second_color))
					.setVisibility(View.VISIBLE);
			break;
		case 2:
			((ImageView) findViewById(R.id.third_color)).setImageResource(id);
			((ImageView) findViewById(R.id.third_color))
					.setVisibility(View.VISIBLE);
			break;
		case 3:
			((ImageView) findViewById(R.id.fourth_color)).setImageResource(id);
			((ImageView) findViewById(R.id.fourth_color))
					.setVisibility(View.VISIBLE);
			break;
		case 4:
			((ImageView) findViewById(R.id.fifth_color)).setImageResource(id);
			((ImageView) findViewById(R.id.fifth_color))
					.setVisibility(View.VISIBLE);
			break;
		case 5:
			((ImageView) findViewById(R.id.six_color)).setImageResource(id);
			((ImageView) findViewById(R.id.six_color))
					.setVisibility(View.VISIBLE);
			break;
		case 6:
			((ImageView) findViewById(R.id.seven_color)).setImageResource(id);
			((ImageView) findViewById(R.id.seven_color))
					.setVisibility(View.VISIBLE);
			break;
		case 7:
			((ImageView) findViewById(R.id.eight_color)).setImageResource(id);
			((ImageView) findViewById(R.id.eight_color))
					.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void paintClicked(View view) {

		drawView.setBrushSize(drawView.getLastBrushSize());

		if (view != currPaint) {
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			// update ui
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaint = (ImageButton) view;
		}
	}

	AnimationListener listener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			imageViewHelp.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			imageViewHelp.setVisibility(View.GONE);
		}
	};

	AnimationListener finish_anim = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			level_finished.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			
		}
	};

	
	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.home_btn:
			onBackPressed();
			break;
		case R.id.new_btn:
			// drawView.startNew();
			Animation animFadein = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide);
			imageViewHelp.setVisibility(View.VISIBLE);
			animFadein.setAnimationListener(listener);

			imageViewHelp.startAnimation(animFadein);

			Log.d("Likers", "Anim");

			break;
		default:
			drawView.setBrushColor(view.getId());
			break;
		}

	}
	
	public void finish_anim()
	{
		Animation animFadein = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_finish);
		level_finished.setVisibility(View.VISIBLE);
		animFadein.setAnimationListener(finish_anim);

		level_finished.startAnimation(animFadein);
	}

	private double getInches() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		double screenInches = Math.sqrt(x + y);
		Log.d("debug", "Screen inches : " + screenInches);
		return screenInches;
	}
}
