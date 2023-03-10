package com.example.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class PaintView extends View {

    public int BRUSH_STYLE = 20;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    public int currentColor;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private ArrayList<FingerPath> pathspiral = new ArrayList<>();
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private DisplayMetrics metrics;
    int height = metrics.heightPixels;
    int width = metrics.widthPixels;

    public PaintView(Context context) {
        super(null);

    }
    public PaintView(Context context, AttributeSet attrs){      // nasatavenie paint
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }   // end PaintView

    public void init(DisplayMetrics metrics){       // velkosť plochy, displej
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_STYLE;

    }   // end init

    public void clear(){        // vymaze nakreslene
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        invalidate();
    }   // end clear

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for(FingerPath  fp : pathspiral) {  //  Spirala
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.path, mPaint);
        }
        for(FingerPath  fp : paths){    // Ruka
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap,0,0, mBitmapPaint);
        canvas.restore();
    }   // end onDrawn

    public void touchStart(float x, float y){
    mPath = new Path();
    FingerPath fp = new FingerPath(currentColor, strokeWidth, mPath);
    paths.add(fp);

    mPath.reset();
    mPath.moveTo(x,y);
    mX = x;
    mY = y;
    }   // end touchStart

    public void touchMove(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }   // end touchMove

    public void touchUp() {
        mPath.lineTo(mX, mY);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }   // end onTouchEvent

    public void touchStartSpiral(float x, float y){
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, strokeWidth, mPath);
        pathspiral.add(fp);

        mPath.reset();
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }   //end touchStartSpiral

    public void touchMoveSpiral(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }   // end touchMoveSpiral

    public void touchUpSpiral() {
        mPath.lineTo(mX, mY);
    }

    public boolean onTouchEventSpiral(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartSpiral(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMoveSpiral(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUpSpiral();
                invalidate();
                break;
        }
        return true;
    }   //end onTouchEventSpiral

    public void compare(){
    }
}   // end PaintView
