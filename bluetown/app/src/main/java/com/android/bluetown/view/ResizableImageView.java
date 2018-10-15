package com.android.bluetown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.utils.DisplayUtils;

import java.lang.reflect.Field;

/**
 * Created by Dafen on 2018/7/3.
 */

public class ResizableImageView extends ImageView {
    private static final String TAG="ResizableImageView";
    private Path path1 = new Path();
    private Path path2 = new Path();
    private Region re1 = new Region();
    private Region re2 = new Region();
    private int textSize;
    private int textColor;
    private String parkingA="A区",parkingB="B区";
    private  float screenWidth;
    private float screenHeight;
    private float scaleX,scaleY;
    private static final int AREA_PARKING_A = 1;
    private static final int AREA_PARKING_B = 2;
    private static final int AREA_PARKING_NO = 3;
    private TextPaint mTextPaint1;
    private TextPaint mTextPaint2;
    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        scaleX = screenWidth /720;
        scaleY = screenHeight/1520;

        textColor = Color.WHITE;
        textSize = DisplayUtils.dip2px(context,14);

        mTextPaint1 = new TextPaint();
//        mTextPaint1.setColor(textColor);
//        mTextPaint1.setTextSize(textSize);
//        mTextPaint1.setStyle(Paint.Style.FILL);
//        mTextPaint1.setAntiAlias(true);


        mTextPaint2 = new TextPaint();
//        mTextPaint2.setColor(textColor);
//        mTextPaint2.setTextSize(textSize);
//        mTextPaint2.setStyle(Paint.Style.FILL);
//        mTextPaint2.setAntiAlias(true);
    }

    public void setAreaName(String parkingA1,String parkingB1){
        this.parkingA = new StringBuilder().append(parkingA+parkingA1).toString();
        this.parkingB = new StringBuilder().append(parkingB+parkingB1).toString();
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            Drawable d = getDrawable();
            if (d != null) {
                // ceil not round - avoid thin vertical gaps along the left/right edges
                int width = MeasureSpec.getSize(widthMeasureSpec);
                //高度根据使得图片的宽度充满屏幕计算而得
                int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
                setMeasuredDimension(width, height);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"("+x+","+y+")");

                boolean isAreaB = re1.contains((int) event.getX(), (int) event.getY());
                boolean isAreaA = re2.contains((int) event.getX(), (int) event.getY());
                if (isAreaA){
                    if (onSingleTapListener!=null){
                        onSingleTapListener.onSingleTap(AREA_PARKING_A);
                    }
                }
                if (isAreaB){
                    if (onSingleTapListener!=null){
                        onSingleTapListener.onSingleTap(AREA_PARKING_B);
                    }
                }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path1.moveTo(90*scaleX,360*scaleX);
        path1.lineTo(444*scaleX,563*scaleX);
        path1.lineTo(346*scaleX,733*scaleX);
        path1.lineTo(186*scaleX,646*scaleX);
        path1.quadTo(107*scaleX,613*scaleX,52*scaleX,592*scaleX);
        path1.quadTo(94*scaleX,604*scaleX,52*scaleX,592*scaleX);
        path1.close();

        RectF r = new RectF();
        path1.computeBounds(r,true);
        re1.setPath(path1,new Region((int) r.left,(int)r.top,(int)r.right,(int)r.bottom));
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLACK);
        //设置抗锯齿。
        paint.setAntiAlias(true);
        canvas.drawPath(path1, paint);

        path2.moveTo(444*scaleX,563*scaleX);
        path2.lineTo(720*scaleX,710*scaleX);
        path2.lineTo(582*scaleX,877*scaleX);
        path2.lineTo(346*scaleX,733*scaleX);
        path2.close();

        RectF r1 = new RectF();
        path2.computeBounds(r1,true);
        re2.setPath(path2,new Region((int) r1.left,(int)r1.top,(int)r1.right,(int)r1.bottom));
        Paint paint1 = new Paint();
        paint1.setColor(Color.TRANSPARENT);
//        paint1.setColor(Color.YELLOW);
        paint1.setStrokeWidth(3);
        paint1.setStyle(Paint.Style.STROKE);
        //设置抗锯齿。
        paint.setAntiAlias(true);
        canvas.drawPath(path2, paint1);

        drawAparking(canvas);
        drawBparking(canvas);
    }

    private void drawAparking(Canvas canvas) {
        setTextColorUseReflection(Color.BLACK,mTextPaint1);
        mTextPaint1.setStrokeWidth(6);  // 描边宽度
        mTextPaint1.setTextSize(textSize);
        mTextPaint1.setStyle(Paint.Style.FILL_AND_STROKE); //描边种类
        mTextPaint1.setFakeBoldText(true); // 外层text采用粗体
        mTextPaint1.setShadowLayer(1, 0, 0, 0); //字体的阴影效果，可以忽略
        setTextColorUseReflection(Color.WHITE,mTextPaint1);
        mTextPaint1.setStrokeWidth(0);
        mTextPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint1.setFakeBoldText(true);
        mTextPaint1.setShadowLayer(0, 0, 0, 0);
        canvas.drawText(parkingA,(346+720-mTextPaint1.measureText(parkingA))/2*scaleX,(733+710)/2*scaleX,mTextPaint1);
    }

    private void drawBparking(Canvas canvas){
        setTextColorUseReflection(Color.BLACK,mTextPaint2);
        mTextPaint2.setStrokeWidth(6);  // 描边宽度
        mTextPaint2.setTextSize(textSize);
        mTextPaint2.setStyle(Paint.Style.FILL_AND_STROKE); //描边种类
        mTextPaint2.setFakeBoldText(true); // 外层text采用粗体
        mTextPaint2.setShadowLayer(1, 0, 0, 0); //字体的阴影效果，可以忽略
        setTextColorUseReflection(Color.WHITE,mTextPaint2);
        mTextPaint2.setStrokeWidth(0);
        mTextPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint2.setFakeBoldText(true);
        mTextPaint2.setShadowLayer(0, 0, 0, 0);
        mTextPaint2.setFakeBoldText(true);
        canvas.drawText(parkingB,((444+52-mTextPaint2.measureText(parkingB))/2)*scaleX,(563+592)/2*scaleX,mTextPaint2);
    }

    private OnSingleTapListener onSingleTapListener;
    public void setOnSingleTap(OnSingleTapListener onSingleTap){
        this.onSingleTapListener = onSingleTap;
    }

    public interface OnSingleTapListener{
        void onSingleTap(int type);
    }

    private void setTextColorUseReflection(int color,TextPaint m_TextPaint) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set("color",color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }
}
