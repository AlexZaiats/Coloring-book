package com.example.drawingfun;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.Point;

class QuickFill
{
    private Bitmap _bmp;
    private int _oldColor;
    private int _newColor;
    private int _bmpWidth;
    private int _bmpHeight;

    public QuickFill(Bitmap bmp, int oldColor, int newColor)
    {
        _bmp = bmp;
        _oldColor = oldColor;
        _newColor = newColor;
        _bmpWidth = bmp.getWidth();
        _bmpHeight = bmp.getHeight();
    }

    public void fill(int x, int y)
    {
        // TODO : remove before release.
        //Debug.startMethodTracing("FloodFill");

        // 1. Set Q to the empty queue.
        Queue<Point> queue = new LinkedList<Point>();
        int[] scanLine = new int[_bmpWidth];

        // 2. If the color of node is not equal to target-color, return.
        if (_bmp.getPixel(x, y) == _oldColor)
        {
            // 3. Add node to Q.
            queue.add(new Point(x, y));

            // 4. For each element n of Q.
            while (!queue.isEmpty())
            {
                // 5. If the color of n is equal to target-color.
                Point n = queue.poll();
                if (_bmp.getPixel(n.x, n.y) == _oldColor)
                {
                    // 6. Set w and e equal to n.
                    int wx = n.x;
                    int ex = n.x + 1;

                    _bmp.getPixels(scanLine, 0, _bmpWidth, 0, n.y, _bmpWidth, 1);

                    // 7. Move w to the west until the color of the node to the
                    // west of w no longer matches target-color.
                    while (wx >= 0 && scanLine[wx] == _oldColor)
                    {
                        scanLine[wx] = _newColor;
                        wx--;
                    }

                    // 8. Move e to the east until the color of the node to the
                    // east of e no longer matches target-color.
                    while (scanLine[ex] == _oldColor && ex < _bmpWidth - 1)
                    {
                        scanLine[ex] = _newColor;
                        ex++;
                    }

                    // 9. Set the color of nodes between w and e to
                    // replacement-color.
                    int length = ex - wx - 1;
                    if (length > 0)
                    {
                        _bmp.setPixels(scanLine, wx + 1, _bmpWidth, wx + 1, n.y, length, 1);
                    }

                    // 10. For each node n between w and e.
                    for (int ix = wx + 1; ix < ex; ix++)
                    {
                        // 11. If the color of the node to the north of n is
                        // target-color, add that node to Q.
                        if (n.y - 1 >= 0 && _bmp.getPixel(ix, n.y - 1) == _oldColor)
                        {
                            queue.add(new Point(ix, n.y - 1));
                        }

                        // 12. If the color of the node to the south of n is
                        // target-color, add that node to Q.
                        if (n.y + 1 < _bmpHeight && _bmp.getPixel(ix, n.y + 1) == _oldColor)
                        {
                            queue.add(new Point(ix, n.y + 1));
                        }
                    }
                }
            }
        }

        // TODO : remove before release.
        // Debug.startMethodTracing();
    }
}