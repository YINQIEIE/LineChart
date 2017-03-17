package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinqi on 2017/2/28.
 * 此view本身不可滑动，要把它放在一个可滑动的layout里使用
 */

public class NoScrollChartView extends BaseChartView {

    //圆形背景画笔
    private Paint bgPaint;

    private Paint mPaint;//折线连线画笔

    private int radius;//背景圆半径

    private List<List<String>> missList = new ArrayList<>();

    public NoScrollChartView(Context context) {
        this(context, null);
    }

    public NoScrollChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 50;

        colNum = 24;

    }

    @Override
    protected void init() {

        super.init();

        rectPaint.setColor(Color.parseColor("#FFF2F2F2"));

        initPaints();

    }

    /**
     * 初始化画笔
     */
    private void initPaints() {

        textPaint.setColor(Color.parseColor("#FF999999"));

        //背景画笔
        bgPaint = new Paint();

        bgPaint.setColor(Color.parseColor("#FFF64646"));

        bgPaint.setAntiAlias(true);

        bgPaint.setStyle(Paint.Style.FILL);

        //折线画笔
        mPaint = new Paint();

        mPaint.setColor(Color.RED);

        mPaint.setAntiAlias(true);

        mPaint.setStrokeWidth(3.0f);

        radius = Math.min(rowHeight, colWidth) / 2 - 2;
    }

    public void setSize(int colWidth, int rowHeight, int colNum) {

        this.colNum = colNum;

        radius = Math.min(rowHeight, colWidth) / 2 - 2;

        setSize(colWidth, rowHeight);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(colWidth * colNum, rowHeight * rowNum);

        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (rowHeight == 0 || colWidth == 0) return;

        super.onDraw(canvas);

        drawText(canvas);

    }


    /**
     * 画号码和号码之间的连线
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        if (missList.isEmpty()) return;

        String text;

        for (int i = 0; i < rowNum; i++) {//画文字

            //画文字
            baseLineY = rowHeight * i + rowHeight / 2 - (int) (metrics.ascent) / 2;

            for (int j = 0; j < colNum; j++) {//画第i行每一列的字符

                text = missList.get(i).get(j);

                float textWid = textPaint.measureText(text);//测量字符宽度

                textPaint.setColor(Color.parseColor("#FF999999"));//文字有背景时要变为白色，其他为黑色

                if ("0".equals(text)) {//先画圆，再画文字，否则圆遮挡文字

//                    //先画线 第i行 第j列
//                    if (i != rowNum - 1) {//画有背景位置之间的连线
//
//                        int nextBgPos = bgPosList.get(i + 1);
//
//                        canvas.drawLine(j * colWidth + colWidth / 2, rowHeight * i + rowHeight / 2, nextBgPos * colWidth + colWidth / 2, rowHeight * (i + 1) + rowHeight / 2, mPaint);
//
//                    }

                    canvas.drawCircle(colWidth * j + colWidth / 2, rowHeight * i + rowHeight / 2, radius, bgPaint);//背景圆

                    textPaint.setColor(Color.WHITE);

                    if (!list.isEmpty())

                        text = list.get(j);

                    textWid = textPaint.measureText(text);

                }

                canvas.drawText(text, colWidth * j + colWidth / 2 - textWid / 2, baseLineY, textPaint);//号码

//                Log.i(TAG, "drawText: for" + i + ">>>" + j + ">>>" + text);
            }

        }
    }

    public void notifyDataSetChanged(List<List<String>> missList) {

        this.missList.clear();

        this.missList.addAll(missList);

        rowNum = missList.size();

        requestLayout();//由于baseChartView里rownum初始化为50，会导致最多只能绘制50行，调用此方法重新设置大小

        setMeasuredDimension(getMeasuredWidth(), rowHeight * rowNum);

        invalidate();

    }

    public void setCircleColor(int color) {

        bgPaint.setColor(color);

    }

    public void setList(List<String> list) {

        this.list.addAll(list);

    }

}
