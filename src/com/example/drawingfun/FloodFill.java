package com.example.drawingfun;

import java.util.Deque;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

public class FloodFill {
	  public static void floodFill(Bitmap image, Point node, int targetColor, int replacementColor) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int target = targetColor;
	    int replacement = replacementColor;
	    if (target != replacement) {
	      Deque<Point> queue = new LinkedList<Point>();
	      do {
	        int x = node.x;
	        int y = node.y;
	        while (x > 0 && image.getPixel(x - 1, y) == target) {
	          x--;
	        }
	        boolean spanUp = false;
	        boolean spanDown = false;
	        while (x < width && image.getPixel(x, y) == target) {
	          image.setPixel(x, y, replacement);
	          if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
	            queue.add(new Point(x, y - 1));
	            spanUp = true;
	          } else if (spanUp && y > 0 && image.getPixel(x, y - 1) != target) {
	            spanUp = false;
	          }
	          if (!spanDown && y < height - 1 && image.getPixel(x, y + 1) == target) {
	            queue.add(new Point(x, y + 1));
	            spanDown = true;
	          } else if (spanDown && y < height - 1 && image.getPixel(x, y + 1) != target) {
	            spanDown = false;
	          }
	          x++;
	        }
	      } while ((node = queue.pollFirst()) != null);
	    }
	  }
	}
