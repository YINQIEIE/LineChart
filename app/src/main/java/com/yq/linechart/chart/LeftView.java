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
        this(context, null);
    }

    public LeftView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 50;

        colNum = 1;

        setPaintColor();

    }

    /**
     * 初始化矩形画笔
     */
    private void setPaintColor() {

        rectPaint.setColor(Color.TRANSPARENT);

        textPaint.setColor(Color.parseColor("#000000"));

    }

    public void setTextSize(int size) {

        textPaint.setTextSize(dp2px(size));

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawText(canvas);

    }

    /**
     * 画期次
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        if (list.isEmpty()) return;

        //画文字
        baseLineY = rowHeight / 2 - (int) (metrics.ascent) / 2;

        for (int i = 0; i < rowNum; i++) {//画第i行每一列的字符

            float textWid = textPaint.measureText(list.get(i));//测量字符宽度

            canvas.drawText(list.get(i), colWidth / 2 - textWid / 2, i * rowHeight + baseLineY, textPaint);//号码

        }

    }

}
