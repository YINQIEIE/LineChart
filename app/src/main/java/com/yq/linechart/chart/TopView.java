package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

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

        setBackgroundColor(Color.parseColor("#6633B5E5"));
    }

 /*   @Override
    public void draw(Canvas canvas) {

        for (int i = 0; i < colNum + 1; i++) {//竖线

            canvas.drawLine(colWidth * i, 2, colWidth * i, rowHeight + 2, linePaint);//竖线

        }

        canvas.drawLine(2, 2, colWidth * colNum, 2, linePaint);//横线

        canvas.drawLine(2, rowHeight, colWidth * rowNum, rowHeight, linePaint);//横线
    }*/

}
