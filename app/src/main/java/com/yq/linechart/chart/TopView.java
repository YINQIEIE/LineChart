package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by yinqi on 2017/2/28.
 */

public class TopView extends BaseChartView {

    public TopView(Context context) {
        this(context, null);
    }

    public TopView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 1;

        colNum = 24;

        setPaintColor();

        setBackgroundColor(Color.TRANSPARENT);

    }

    /**
     * 设置画笔颜色
     */
    private void setPaintColor() {

        rectPaint.setColor(Color.TRANSPARENT);

    }

    public void setSize(int colWidth, int rowHeight, int colNum) {

        this.colNum = colNum;

        setSize(colWidth, rowHeight);

    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);//画线

        drawText(canvas);

    }

    /**
     * 画号码和号码之间的连线
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        if (list.isEmpty()) return;

        //画文字
        baseLineY = rowHeight / 2 - (int) (metrics.ascent) / 2;

        for (int i = 0; i < colNum; i++) {//画第i列的字符

            float textWid = textPaint.measureText(list.get(i));//测量字符宽度

            canvas.drawText(list.get(i), colWidth * i + colWidth / 2 - textWid / 2, baseLineY, textPaint);//号码

        }

    }

    public void setList(List<String> content) {

        list.addAll(content);

        invalidate();

    }

//    /**
//     * 画矩形
//     *
//     * @param canvas
//     */
//    private void drawRect(Canvas canvas) {
//
//        Rect rect = new Rect(0, 0, colWidth * colNum, rowHeight);
//
//        canvas.drawRect(rect, rectPaint);
//
//    }

}
