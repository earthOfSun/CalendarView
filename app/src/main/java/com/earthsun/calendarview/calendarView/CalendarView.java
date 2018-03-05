package com.earthsun.calendarview.calendarView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.earthsun.calendarview.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CalendarView extends View {


    private int viewWidth;
    private int viewHeight;
    //是否可以切换月份
    private boolean isScroll;
    private int signBg;
    private int noSignBg;
    private int signFg;
    private int noSignFg;
    private int weekBg;
    private int weekFg;
    private float daySize;
    private float weekSize;
    private int minColu;
    private int minRow;
    private Paint weekPaint;
    private Paint weekBgPaint;
    private Paint dayPaint;
    private int ovalPadding;
    private String[] week = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private Paint signOvalPaint;


    private List<Integer> signDays;
    private Paint ovalPaint;
private Context mContext;

    private long time;
    private int ovalRadius;
    private int ovalPaddingLong;
    private RectF rectF;
    private Paint otherMonthDayPaint;
    private boolean needOval;
    //已签到状态
    private Drawable signBitmap;
    //当天未签到状态背景图
    private Drawable onDayBitmap;
    private int weekHeight;

    public CalendarView(Context context) {
        super(context);
        mContext = context;
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        for (int i = 0; i < typedArray.length(); i++) {
            int typedArrayIndex = typedArray.getIndex(i);
            switch (typedArrayIndex) {
                case R.styleable.CalendarView_isScroll:
                    isScroll = typedArray.getBoolean(R.styleable.CalendarView_isScroll, false);
                    break;
                case R.styleable.CalendarView_signBg:
                    signBg = typedArray.getColor(R.styleable.CalendarView_signBg, Color.RED);
                    break;
                case R.styleable.CalendarView_noSignBg:
                    noSignBg = typedArray.getColor(R.styleable.CalendarView_noSignBg, Color.WHITE);
                    break;
                case R.styleable.CalendarView_signFg:
                    signFg = typedArray.getColor(R.styleable.CalendarView_signFg, Color.WHITE);
                    break;
                case R.styleable.CalendarView_noSignFg:
                    noSignFg = typedArray.getColor(R.styleable.CalendarView_noSignFg, Color.BLACK);
                    break;
                case R.styleable.CalendarView_weekBg:
                    weekBg = typedArray.getColor(R.styleable.CalendarView_weekBg, Color.WHITE);
                    break;
                case R.styleable.CalendarView_weekFg:
                    weekFg = typedArray.getColor(R.styleable.CalendarView_weekFg, Color.BLACK);
                    break;
                case R.styleable.CalendarView_daySize:
                    daySize = typedArray.getDimension(R.styleable.CalendarView_daySize, 20);
                    break;
                case R.styleable.CalendarView_weekSize:
                    weekSize = typedArray.getDimension(R.styleable.CalendarView_weekSize, 20);
                    break;
                case R.styleable.CalendarView_needOval:
                    needOval = typedArray.getBoolean(R.styleable.CalendarView_needOval, false);
                    break;
                case R.styleable.CalendarView_signBitmap:
                    signBitmap = typedArray.getDrawable(R.styleable.CalendarView_signBitmap);
                    break;
                case R.styleable.CalendarView_onDayBitmap:
                    onDayBitmap = typedArray.getDrawable(R.styleable.CalendarView_onDayBitmap);
                    break;
                case R.styleable.CalendarView_weekHeight:
                    weekHeight = (int) typedArray.getDimension(R.styleable.CalendarView_weekHeight, 100);
                    break;
            }
        }
        typedArray.recycle();

        weekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekPaint.setTextSize(weekSize);
        weekPaint.setColor(weekFg);

        weekBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekBgPaint.setColor(weekBg);

        dayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setColor(noSignFg);
        dayPaint.setTextSize(daySize);

        otherMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        otherMonthDayPaint.setColor(Color.GRAY);
        otherMonthDayPaint.setTextSize(daySize);

        signOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        signOvalPaint.setColor(signFg);

        ovalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ovalPaint.setColor(weekBg);
        ovalPaint.setStyle(Paint.Style.STROKE);

//        signBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        minColu = viewWidth / 7;
        minRow = (viewHeight -weekHeight) / 6;
        if (minRow < minColu) {
            ovalRadius = minRow;
            ovalPaddingLong = (minColu - ovalRadius) / 2;
        } else if (minColu < minRow) {
            ovalRadius = minColu;
            ovalPaddingLong = (minRow - ovalRadius) / 2;
        } else ovalRadius = minColu;
        //短的边使用
        ovalPadding = ovalRadius / 10;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWeekBg(canvas);
        drawSplitLine(canvas);
        drawWeek(canvas);
        drawDays(canvas);
    }

    /**
     * 绘制日期
     *
     * @param canvas
     */
    private void drawDays(Canvas canvas) {
        DateHandle dateHandle;
        if (time != 0) {
            dateHandle = new DateHandle(time);
        } else dateHandle = new DateHandle(System.currentTimeMillis());

        List<SignDate> lastMonthDays = null;
        List<SignDate> nextMonthDays = null;
        List<SignDate> data = dateHandle.getData(signDays);
        for (SignDate signDate : data) {

            if (signDate.getDay() == 1) {
                lastMonthDays = dateHandle.getLastMonthDays(signDate.getColuIndex());
            }

            if (signDate.getDay() == data.size()) {
                nextMonthDays = dateHandle.getNextMonthDays(signDate.getColuIndex(),signDate.getRowIndex());
            }

            drawDates(canvas, signDate, dayPaint);
        }
        if (lastMonthDays != null)
            for (SignDate signDate : lastMonthDays) {
                drawDates(canvas, signDate, otherMonthDayPaint);
            }
        if (nextMonthDays != null)
            for (SignDate signDate : nextMonthDays) {
                drawDates(canvas, signDate, otherMonthDayPaint);
            }
    }

    /**
     * 真正绘制不同状态日期地方
     * @param canvas
     * @param signDate
     * @param paint
     */
    private void drawDates(Canvas canvas, SignDate signDate, Paint paint) {
        Paint.FontMetricsInt fontMetricsInt = dayPaint.getFontMetricsInt();
        int deltaTextCellY = (fontMetricsInt.bottom + fontMetricsInt.top) / 2;
        int dayY = (signDate.getRowIndex() + 1) * minRow + ovalPadding + ovalRadius / 2;
//            int dayY = (signDate.getY() + 1) * minRow + minRow / 2 + deltaTextCellY + 2 * ovalPadding;

        if (ovalPaddingLong == 0) {
            rectF = new RectF(signDate.getColuIndex() * minColu + ovalPadding, (signDate.getRowIndex() + 1) * minRow + ovalPadding
                    , (signDate.getColuIndex() + 1) * minColu - ovalPadding, (signDate.getRowIndex() + 2) * minRow - ovalPadding);
        } else if (minRow < minColu) {
            rectF = new RectF(signDate.getColuIndex() * minColu + ovalPaddingLong + ovalPadding, (signDate.getRowIndex() + 1) * minRow + ovalPadding
                    , (signDate.getColuIndex() + 1) * minColu - ovalPaddingLong - ovalPadding, (signDate.getRowIndex() + 1) * minRow + ovalRadius - ovalPadding);

        } else {
            rectF = new RectF(signDate.getColuIndex() * minColu + ovalPadding, (signDate.getRowIndex() + 1) * minRow + ovalPaddingLong + ovalPadding
                    , (signDate.getColuIndex()) * minColu + ovalRadius - ovalPadding, (signDate.getRowIndex() + 2) * minRow - ovalPaddingLong - ovalPadding);
//                dayY = (signDate.getY() + 1) * minRow + minRow / 2 + deltaTextCellY + ovalPaddingLong;
            dayY = (signDate.getRowIndex() + 1) * minRow + ovalPaddingLong + ovalPadding + ovalRadius / 2;

        }
        if (signDate.getDateType() == DateType.SIGNED) {


            int height = signBitmap.getIntrinsicHeight();
            int intrinsicWidth = signBitmap.getIntrinsicWidth();

            if (minColu < intrinsicWidth){
                intrinsicWidth = minColu;
            }
            if (minRow < height){
                height = minRow;
            }

            Rect rect = new Rect((signDate.getColuIndex()) * minColu+(minColu - intrinsicWidth)/2
                    ,(signDate.getRowIndex() + 1) * minRow+(minRow -height)/2
                    ,(signDate.getColuIndex()+1) * minColu-(minColu - intrinsicWidth)/2
                    ,(signDate.getRowIndex() + 2) * minRow-(minRow -height)/2);
            signBitmap.setBounds(rect);
            signBitmap.draw(canvas);

//            canvas.drawBitmap(signBitmap ,rect ,
//                   rectF,weekPaint);
//            canvas.drawText(String.valueOf(signDate.getDay()), (signDate.getColuIndex()) * minColu + (minColu / 2 - 10),
//                    dayY, paint);
        } else if (signDate.getDateType() == DateType.UNSIGNED){

            canvas.drawText(String.valueOf(signDate.getDay()), (signDate.getColuIndex()) * minColu + (minColu  - daySize )/ 2,
                    dayY, paint);
        }else if (signDate.getDateType() == DateType.WAITING){
            int height = onDayBitmap.getIntrinsicHeight();
            int intrinsicWidth = onDayBitmap.getIntrinsicWidth();

            if (minColu < intrinsicWidth){
                intrinsicWidth = minColu;
            }
            if (minRow < height){
                height = minRow;
            }


            Rect rect = new Rect((signDate.getColuIndex()) * minColu +5+(minColu - intrinsicWidth)/2
                    ,(signDate.getRowIndex() + 1) * minRow+5+(minRow -height)/2
                    ,(signDate.getColuIndex()+1) * minColu-5-(minColu - intrinsicWidth)/2
                    ,(signDate.getRowIndex() + 2) * minRow-5 -(minRow -height)/2);
            onDayBitmap.setBounds(rect);
            onDayBitmap.draw(canvas);
            canvas.drawText(String.valueOf(signDate.getDay()), (signDate.getColuIndex()) * minColu + (minColu  - daySize )/ 2,
                    dayY, paint);
        }

        if (needOval)  canvas.drawOval(rectF, signOvalPaint);//画圆
    }

    /**
     * 绘制星期数
     *
     * @param canvas
     */

    private void drawWeek(Canvas canvas) {
        for (int i = 0; i < week.length; i++) {
            canvas.drawText(week[i], (i) * minColu + (minColu / 2 - weekSize / 2), weekHeight / 2 + weekSize / 2, weekPaint);
        }
    }
    /**
     * 绘制星期数
     *
     * @param canvas
     */

    private void drawSplitLine(Canvas canvas) {
        for (int i = 1; i < week.length; i++) {
            canvas.drawLine( (i) * minColu, 0,(i) * minColu,weekHeight, weekPaint);
        }
    }
    /**
     * 绘制星期背景
     *
     * @param canvas
     */
    private void drawWeekBg(Canvas canvas) {
        Rect rect = new Rect(0, 0, viewWidth, weekHeight);
        canvas.drawRect(rect, weekBgPaint);
    }

    /**
     * 设置已签到日期
     *
     * @param signDays
     */
    public void setSignDays(List<Integer> signDays) {
        this.signDays = signDays;
        invalidate(0, viewHeight - minRow, viewWidth, viewHeight);
//        invalidate();
    }

    public void setTime(long time) {
        if (time != 0 && time > 0)
            this.time = time;
        else this.time = System.currentTimeMillis();
        invalidate(0, viewHeight - minRow, viewWidth, viewHeight);
//        invalidate();
    }

    private int dp2px(float dpValue){
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue*density+0.5f);
    }
}
