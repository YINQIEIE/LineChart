package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by yinqi on 2017/2/28.
 */

public class LeftView extends BaseChartView {

    public LeftView(Context context) {
        super(context, null);
    }

    public LeftView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 50;

        colNum = 1;

        setBackgroundColor(Color.parseColor("#6633B5E5"));

    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < rowNum + 1; i++) {//横线

            canvas.drawLine(0, rowHeight * i, colWidth, rowHeight * i, linePaint);

        }

        canvas.drawLine(1, 0, 1, rowHeight * rowNum, linePaint);//竖线

        canvas.drawLine(colWidth, 2, colWidth, rowHeight * rowNum + 2, linePaint);//竖线

    }

}
