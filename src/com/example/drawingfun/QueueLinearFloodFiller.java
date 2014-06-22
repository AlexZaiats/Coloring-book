package com.example.drawingfun;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class QueueLinearFloodFiller
{
	protected Bitmap             	image      = null;
	protected int[]              	tolerance  = new int[] {0,0,0};
	protected int                	width      = 0;
	protected int                	height     = 0;
	protected int[]              	pixels     = null;
	protected int                 	fillColor  = 0;
	protected int[]             	startColor = new int[] {0,0,0};
	protected boolean[]         	pixelsChecked;
	protected Queue<FloodFillRange> ranges;
	private int target ;
	private Bitmap bitmap;
	
  //Construct using an image and a copy will be made to fill into,
  //Construct with BufferedImage and flood fill will write directly to provided BufferedImage
	public QueueLinearFloodFiller(Bitmap img)
  {
  	copyImage(img);
  }

	public QueueLinearFloodFiller(Bitmap img, int targetColor, int newColor)
  {
  	useImage(img);
      
  	setFillColor(newColor);
  	setTargetColor(targetColor);
  }

	public void setTargetColor(int targetColor)
  {
		target = targetColor;
  	startColor[0] = Color.red(targetColor);
  	startColor[1] = Color.green(targetColor);
  	startColor[2] = Color.blue(targetColor);
  }

	public int getFillColor()
  {
  	return fillColor;
  }
  
	public void setFillColor(int value)
  {
  	fillColor = value;
  }
  
	public int[] getTolerance()
  {
  	return tolerance;
  }
  
	public void setTolerance(int[] value)
  {
  	tolerance = value;
  }
  
	public void setTolerance(int value)
  {
  	tolerance = new int[] {value, value, value};
  }
  
	public Bitmap getImage()
  {
  	return image;
  }

	public void copyImage(Bitmap img)
  {
      //Copy data from provided Image to a BufferedImage to write flood fill to, use getImage to retrieve
      //cache data in member variables to decrease overhead of property calls
  	width  = img.getWidth();
  	height = img.getHeight();
      
  	image  = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
  	image  = Bitmap.createBitmap(img);
      
  	pixels = new int[width * height];
      
  	image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
  }

	public void useImage(Bitmap img)
  {
      //Use a pre-existing provided BufferedImage and write directly to it
      //cache data in member variables to decrease overhead of property calls
  	width  = img.getWidth();
  	height = img.getHeight();
  	image  = img;

  	pixels = new int[width * height];

  	image.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
  }
  
	protected void prepare()
  {
      //Called before starting flood-fill
  	pixelsChecked = new boolean[pixels.length];
  	ranges        = new LinkedList<FloodFillRange>();
  }


	public void setBitmap (Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}
	
	public void floodFill(int x, int y)
  {
      //Setup 
  	prepare();

  	LinearFill(x,  y);

  
  	FloodFillRange range;
      
  	while (ranges.size() > 0)
      {
    
      	range = ranges.remove();

      	int downPxIdx = (width * (range.Y + 1)) + range.startX;
      	int upPxIdx   = (width * (range.Y - 1)) + range.startX;
      	int upY       = range.Y - 1;//so we can pass the y coord by ref
      	int downY     = range.Y + 1;
          
      	for (int i = range.startX; i <= range.endX; i++)
          {
       
              //if we're not above the top of the bitmap and the pixel above this one is within the color tolerance
          	if (range.Y > 0 && (!pixelsChecked[upPxIdx]) && CheckPixel(upPxIdx))
              {
              	LinearFill( i,  upY);
              }
              
  
              //if we're not below the bottom of the bitmap and the pixel below this one is within the color tolerance
          	if (range.Y < (height - 1) && (!pixelsChecked[downPxIdx]) && CheckPixel(downPxIdx))
              {
              	LinearFill( i,  downY);
              }
              
          	downPxIdx++;
          	upPxIdx++;
          }
      }
      
 // 	image.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);
  }

	protected void LinearFill(int x, int y)
  {

  	int lFillLoc = x; //the location to check/fill on the left
  	int pxIdx    = (width * y) + x;
      
  	while (true)
      {
   
      	pixels[pxIdx] = fillColor;

      	pixelsChecked[pxIdx] = true;
          

      	lFillLoc--;     //de-increment counter
      	pxIdx--;        //de-increment pixel index
          

      	if (lFillLoc < 0 || (pixelsChecked[pxIdx]) || !CheckPixel(pxIdx))
          {
          	break;
          }
      }
      
  	lFillLoc++;


  	int rFillLoc = x; //the location to check/fill on the left
      
  	pxIdx = (width * y) + x;
      
  	while (true)
      {

      	pixels[pxIdx] = fillColor;
          

      	pixelsChecked[pxIdx] = true;
          

      	rFillLoc++;     //increment counter
      	pxIdx++;        //increment pixel index
          
  
      	if (rFillLoc >= width || pixelsChecked[pxIdx] || !CheckPixel(pxIdx))
          {
          	break;
          }
      }
      
  	rFillLoc--;

      //add range to queue
  	FloodFillRange r = new FloodFillRange(lFillLoc, rFillLoc, y);
      
  	ranges.offer(r);
  }

  //Sees if a pixel is within the color tolerance range.
	protected boolean CheckPixel(int px)
  {
  	int red   = (pixels[px] >>> 16) & 0xff;
  	int green = (pixels[px] >>> 8) & 0xff;
  	int blue  = pixels[px] & 0xff;
      if (red != 0 && green != 0 && blue != 00)
    	  Log.d("Buble" , "Try");
   //   318767104
      // http://www.mathsisfun.com/hexadecimal-decimal-colors.html
      if (px == Color.TRANSPARENT)
    	  Log.d("Likers" , "Transparent");
      
  	return (red   >= (startColor[0] - tolerance[0]) && red   <= (startColor[0] + tolerance[0]) &&
              green >= (startColor[1] - tolerance[1]) && green <= (startColor[1] + tolerance[1]) &&
              blue  >= (startColor[2] - tolerance[2]) && blue  <= (startColor[2] + tolerance[2]));
  }


	protected class FloodFillRange
  {
  	public int startX;
  	public int endX;
  	public int Y;

  	public FloodFillRange(int startX, int endX, int y)
      {
          this.startX = startX;
          this.endX   = endX;
          this.Y      = y;
      }
  }    
}