package com.example.drawingfun;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.vikinc.coloring.R;

public class DrawingView extends View {

	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private int paintColor = 0xFF660000;
	private Canvas drawCanvas;

	private Bitmap canvasBitmap;

	private float brushSize, lastBrushSize;
	boolean test = false;
	private List<Path> pathList;
	private Rect rect;
	private String color;

	private Bitmap animal;
	private Bitmap partImage;

	private ArrayList<String> colorsName;
	private ArrayList<String> constColors;
	private float tolerance = (float) 0.09;
	private Context context;
	private int[] bitmapArray = null;
	private int level = 0;
	private DrawerActivity activity;
	private String packName;

	private int w, h;
	private int screenW, screenH;

	private boolean isFinished;
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
		this.context = context;

		pathList = new ArrayList<Path>();

		rect = new Rect();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		this.screenW = display.getWidth(); // deprecated
		this.screenH = display.getHeight(); // deprecated

	}

	private void setupDrawing() {

		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		if (bitmapArray != null) {
			
	
			animal = BitmapFactory.decodeResource(getResources(),
					bitmapArray[0]);
			
			double difference = (double) animal.getHeight()
					/ (double) animal.getWidth();

			double multyply = 0.95;
			
			if (w < 600)
				multyply = 0.9;
			
			int tmpH = (int) (screenH * multyply);
			int tmpW = (int) (tmpH / difference);
			animal = Bitmap.createScaledBitmap(animal, tmpW, tmpH, true);

			this.w = tmpW;
			this.h = tmpH;

			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[1]);
			partImage = Bitmap.createScaledBitmap(partImage, tmpW, tmpH, false);
			color = constColors.get(0);
			Shader mShader1 = new BitmapShader(partImage,
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);

			activity.setActiveColor(0, color + "_s");

			canvasBitmap = Bitmap.createBitmap(animal.getWidth(),
					animal.getHeight(), Bitmap.Config.ARGB_8888);
			getLayoutParams().height = animal.getHeight();
			getLayoutParams().width = animal.getWidth();
			drawCanvas = new Canvas(canvasBitmap);
			super.onSizeChanged(animal.getWidth(), animal.getHeight(), oldw,
					oldh);
		} else
			super.onSizeChanged(w, h, oldw, oldh);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.getClipBounds(rect);
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		if (drawPath != null) {
			canvas.drawPath(drawPath, drawPaint);
		}
		canvas.drawBitmap(animal, 0, 0, canvasPaint);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:

			if (drawPath == null)
				drawPath = new Path();

			drawPath.moveTo(touchX, touchY);

			break;
		case MotionEvent.ACTION_MOVE:
			if (drawPath == null) {
				drawPath = new Path();
				drawPath.moveTo(touchX, touchY);
			}
			if (drawPath != null)
				drawPath.lineTo(touchX, touchY);

			break;
		case MotionEvent.ACTION_UP:

			if (drawPath != null) {

				drawPath.lineTo(touchX, touchY);
				drawCanvas.drawPath(drawPath, drawPaint);
				Path p = new Path(drawPath);

				pathList.add(p);

				drawPath.reset();
			}

			boolean result = compare(canvasBitmap, partImage);
			if (result) {
				if (colorsName.contains(color))
					colorsName.remove(color);
			}

			if (colorsName.size() == 0) {
			//	Toast.makeText(context, context.getString(R.string.level_finished), Toast.LENGTH_LONG).show();
				if (!isFinished)
				{
					activity.finish_anim();
					isFinished = true;
				}
				
				SharedPreferences prefs = context.getSharedPreferences(
						"com.bublecat.drawer", Activity.MODE_PRIVATE);

				String levels = prefs.getString(packName, null);
				if (levels == null) {
					levels = "" + level;
				} else {
					levels += "," + level;
				}
				prefs.edit().putString(packName, levels).commit();
			}

			break;
		default:
			return false;
		}

		// redraw
		invalidate();
		return true;

	}

	private boolean compare(Bitmap canvas, Bitmap main) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		int pixelCount = width * height;
		int[] pixels1 = new int[pixelCount];
		int[] pixels2 = new int[pixelCount];

		canvas.getPixels(pixels1, 0, width, 0, 0, width, height);
		main.getPixels(pixels2, 0, width, 0, 0, width, height);
		int count = 0;
		int color = 0;
		for (int i = 0; i < pixelCount; i++) {
			if (pixels2[i] != Color.TRANSPARENT) {
				color++;
				if (pixels1[i] == Color.TRANSPARENT) {
					count++;
				}
			}

		}
		float percents = (float) count / (float) color;

		if (percents < tolerance)
			return true;
		else
			return false;

	}

	public void setLevel(int level) {
		this.level = level;
	}

	// update color
	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	// set brush size
	public void setBrushSize(float newSize) {
		float pixelAmount = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, newSize, getResources()
						.getDisplayMetrics());
		brushSize = pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	public void setColors(ArrayList<String> colorsName) {
		this.colorsName = colorsName;
		this.constColors = (ArrayList<String>) colorsName.clone();
	}

	public void setBitmapArray(int[] bitmapArray) {
		this.bitmapArray = bitmapArray;

		requestLayout();
	}

	public void setActivity(DrawerActivity act) {
		this.activity = act;
	}

	public void setLastBrushSize(float lastSize) {
		lastBrushSize = lastSize;
	}

	public float getLastBrushSize() {
		return lastBrushSize;
	}

	public void setBrushColor(int colorId) {
		
		switch (colorId) {
		case R.id.first_color:
			color = constColors.get(0);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[1]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			Shader mShader1 = new BitmapShader(partImage,
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(0, color + "_s");
			break;
		case R.id.second_color:
			color = constColors.get(1);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[2]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(1, color + "_s");
			break;
		case R.id.third_color:
			color = constColors.get(2);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[3]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(2, color + "_s");
			break;
		case R.id.fourth_color:
			color = constColors.get(3);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[4]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(3, color + "_s");
			break;
		case R.id.fifth_color:
			color = constColors.get(4);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[5]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(4, color + "_s");
			break;
		case R.id.six_color:
			color = constColors.get(5);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[6]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(5, color + "_s");
			break;
		case R.id.seven_color:
			color = constColors.get(6);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[7]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(6, color + "_s");
			break;
		case R.id.eight_color:
			color = constColors.get(7);
			partImage.recycle();
			partImage = BitmapFactory.decodeResource(getResources(),
					bitmapArray[8]);
			partImage = Bitmap.createScaledBitmap(partImage, w, h, false);
			mShader1 = new BitmapShader(partImage, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			drawPaint.setShader(mShader1);
			activity.setDefaultColor(constColors);
			activity.setActiveColor(7, color + "_s");
			break;
		}
	}

	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		drawCanvas.drawBitmap(animal, 0, 0, drawPaint);
		invalidate();
	}

	public void setPack(String packName) {
		this.packName = packName;
	}

}
