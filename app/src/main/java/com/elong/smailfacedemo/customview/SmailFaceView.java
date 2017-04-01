package com.elong.smailfacedemo.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.elong.smailfacedemo.R;

/**
 * Created by user on 17/3/30.
 */

public class SmailFaceView extends View {
    private static final String TAG = "SmailFaceView";
    private static final int DEF_VIEW_WIDTH = 300;
    private static final int DEF_VIEW_HEIGHT = 200;
    private static final float FACE_HEIGHT_SCAL = 3F / 4;


    private int smailBgColor;
    private int smailConerRadio;
    private int smailFaceColor;
    private int smailFaceContainerColor;
    private int smailFaceEyeColor;

    private Paint bgPaint;
    private Paint facePaint;
    private Paint faceContainerPaint;
    private Paint eyePaint;
    private Paint transPaint;

    private int viewWith;
    private int viewHeight;
    private RectF bgRect;
    private Point centerPoint;
    private Point leftEyePoint;
    private Point rightEyePoint;
    private RectF ovalReactF;
    private RectF mouthReactF;
    private float currentDegree;

    public SmailFaceView(Context context) {
        this(context, null);
    }

    public SmailFaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmailFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmailFaceView, 0, R.style.def_smail_face_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SmailFaceView_smail_bg_color:
                    smailBgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SmailFaceView_smail_bg_radio:
                    smailConerRadio = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.SmailFaceView_smail_face_color:
                    smailFaceColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SmailFaceView_smail_face_container_color:
                    smailFaceContainerColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SmailFaceView_smail_face_eys_color:
                    smailFaceEyeColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }
        }
        typedArray.recycle();
        bgPaint = fillPaint(smailBgColor, Paint.Style.FILL);
        faceContainerPaint = fillPaint(smailFaceContainerColor, Paint.Style.FILL);
        facePaint = fillPaint(smailFaceColor, Paint.Style.FILL);
        eyePaint = fillPaint(smailFaceEyeColor, Paint.Style.FILL);
        transPaint = fillPaint(ContextCompat.getColor(context, android.R.color.transparent), Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_VIEW_WIDTH, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_VIEW_HEIGHT, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWith = w;
        bgRect = new RectF(0, 0, viewWith, viewHeight);
        centerPoint = new Point(viewWith / 2, viewHeight / 2);
        float top = (viewHeight - viewHeight * FACE_HEIGHT_SCAL) / 2;
        float left = (viewWith - FACE_HEIGHT_SCAL * viewHeight) / 2;
        float right = viewWith - left;
        float bottom = viewHeight - top;
        ovalReactF = new RectF(left, top, right, bottom);
        int leftEyeX = (int) (centerPoint.x - ovalReactF.width() / 8);
        int leftEyeY = centerPoint.y;
        int rightEyeX = (int) (centerPoint.x + ovalReactF.width() / 8);
        int rightEyeY = centerPoint.y;
        leftEyePoint = new Point(leftEyeX, leftEyeY);
        rightEyePoint = new Point(rightEyeX, rightEyeY);
        int mL = leftEyeX + 40;
        int mT = centerPoint.y + 50;
        int mR = rightEyeX - 40;
        int mB = centerPoint.y + 50 + 50;
        mouthReactF = new RectF(mL, mT, mR, mB);
        rotateFaceContainer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSmailBG(canvas);
        canvas.save();
        canvas.rotate(currentDegree, centerPoint.x, centerPoint.y);
        drawFaceContainer(canvas);
        canvas.restore();
        drawFace(canvas);
        drawEyes(canvas);
        drawMouth(canvas);
    }

    private void drawMouth(Canvas canvas) {
        canvas.drawArc(mouthReactF, 0, currentDegree, true, eyePaint);
    }

    private void drawEyes(Canvas canvas) {
        canvas.drawCircle(leftEyePoint.x, leftEyePoint.y, 16, eyePaint);
        canvas.drawCircle(rightEyePoint.x, rightEyePoint.y, 16, eyePaint);
    }

    private void drawFace(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, ovalReactF.width() / 4, facePaint);
    }

    private void drawFaceContainer(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            canvas.drawArc(ovalReactF, 45 * i, 10, true, transPaint);
            canvas.drawArc(ovalReactF, 45 * i + 10, 35, true, faceContainerPaint);
        }
    }


    private void drawSmailBG(Canvas canvas) {
        canvas.drawRoundRect(bgRect, smailConerRadio, smailConerRadio, bgPaint);
    }

    private Paint fillPaint(int paintColor, Paint.Style style) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(style);
        return paint;
    }

    private void rotateFaceContainer() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.F);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentF = (float) animation.getAnimatedValue();
                currentDegree = 360 * currentF;
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
