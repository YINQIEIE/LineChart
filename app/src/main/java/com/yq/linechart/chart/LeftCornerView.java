package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by yinqi on 2017/3/6.
 */

public class LeftCornerView extends BaseChartView {

    public LeftCornerView(Context context) {
        super(context);
    }

    public LeftCornerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#6633B5E5"));
        rowNum = 1;
        colNum = 1;
    }
}
