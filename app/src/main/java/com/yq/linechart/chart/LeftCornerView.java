package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by yinqi on 2017/3/6.
 */

public class LeftCornerView extends BaseChartView {

    public LeftCornerView(Context context) {
        this(context, null);
    }

    public LeftCornerView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 1;

        colNum = 1;

        rectPaint.setColor(Color.TRANSPARENT);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float textWid = textPaint.measureText("期号");

        baseLineY = rowHeight / 2 - (int) (metrics.ascent) / 2;

        canvas.drawText("期号", viewWidth / 2 - textWid / 2, baseLineY, textPaint);
    }
}
