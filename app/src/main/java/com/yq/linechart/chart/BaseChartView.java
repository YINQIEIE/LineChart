package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinqi on 2017/2/28.
 */

public class BaseChartView extends View {

    protected String TAG = getClass().getSimpleName();

    /**
     * 行和列参数
     */
    protected int rowNum = 0;//行数

    protected int colNum = 0;//列数

    protected int rowHeight = 0;//行高

    protected int colWidth = 0;//列宽

    /**
     * 画笔
     */
    protected Paint linePaint;//表格线的画笔

    protected Paint textPaint;//文字的画笔

    protected int viewWidth;//控件宽

    protected int viewHeight;//控件高

    protected int largestDeltaX;//水平最大滑动距离

    protected int largestDeltaY;//竖直最大滑动距离

    //文字相关
    protected Paint.FontMetrics metrics;

    protected int baseLineY = 0;

    protected DecimalFormat df = new DecimalFormat("00");

    protected Paint rectPaint;

    protected List<String> list = new ArrayList<>();//数据

    public BaseChartView(Context context) {
        this(context, null);
    }

    public BaseChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        init();

    }

    /**
     * 初始化线和字的画笔以及行高列宽
     */
    protected void init() {

        //初始化线的画笔
        linePaint = new Paint();

        linePaint.setColor(0x66333333);

        linePaint.setStrokeWidth(1.0f);

        linePaint.setAntiAlias(true);

        linePaint.setTextAlign(Paint.Align.LEFT);

        //初始化字画笔
        textPaint = new Paint();

        textPaint.setColor(Color.BLACK);

        textPaint.setStrokeWidth(2.0f);

        textPaint.setTextSize(dp2px(16));

        textPaint.setAntiAlias(true);

        metrics = textPaint.getFontMetrics();


        //背景画笔
        rectPaint = new Paint();

        rectPaint.setAntiAlias(true);

        rectPaint.setStyle(Paint.Style.FILL);

//        initSize();

    }

    /**
     * 初始化行高列宽
     */
    protected void initSize() {
        //初始化行高列宽
        rowHeight = dp2px(rowHeight);

        colWidth = dp2px(colWidth);

        log(String.format("rowHeight=%d;colWidth=%d", rowHeight, colWidth));
    }

    public void setSize(int colWidth, int rowHeight) {

        this.colWidth = colWidth;

        this.rowHeight = rowHeight;

        setMeasuredDimension(colWidth * colNum, Math.min(rowHeight * rowNum, getMeasuredHeight()));

        invalidate();

    }

    public void setTextColor(int color) {

        textPaint.setColor(color);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();

        viewHeight = getMeasuredHeight();

        largestDeltaX = colWidth * colNum - viewWidth;

        largestDeltaY = rowHeight * rowNum - viewHeight;

        log(String.format("largestDeltaX=%d;largestDeltaY=%d", largestDeltaX, largestDeltaY));

    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (rowHeight == 0 || colWidth == 0) return;

        if (rowNum == 0 || colNum == 0) return;

        drawRect(canvas);

        for (int i = 0; i < colNum + 1; i++) {//竖线

            canvas.drawLine(colWidth * i, 0, colWidth * i, rowHeight * rowNum, linePaint);//竖线

        }

        for (int i = 0; i < rowNum + 1; i++) {//横线

            canvas.drawLine(0, rowHeight * i, colWidth * colNum, rowHeight * i, linePaint);//横线

        }

    }

    protected void drawRect(Canvas canvas) {

        Rect rect = new Rect(0, 0, colWidth * colNum, rowNum * rowHeight);

        canvas.drawRect(rect, rectPaint);

    }

    protected int dp2px(int dp) {

        return (int) (dp * getContext().getResources().getDisplayMetrics().density);

    }

    protected void log(String msg) {

        Log.i(TAG, msg);
    }

    public void notifyDataChanged(List<String> list) {

        this.list.clear();

        this.list.addAll(list);

        rowNum = list.size();

        invalidate();

    }

}
