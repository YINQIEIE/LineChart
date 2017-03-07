package com.yq.linechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yinqi on 2017/2/28.
 * 此view本身不可滑动，要把它放在一个可滑动的layout里使用
 */

public class NoScrollChartView extends BaseChartView {

    //圆形背景画笔
    private Paint bgPaint;

    private Paint mPaint;//折线连线画笔

    private int radius;//背景圆半径

    private Random random = new Random();//生成需要添加圆形背景的位置

    private List<Integer> bgPosList;//背景位置，测试用

    public NoScrollChartView(Context context) {
        this(context, null);
    }

    public NoScrollChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        rowNum = 50;

        colNum = 24;

        setBackgroundColor(Color.parseColor("#6633B5E5"));

        //随机生成要添加背景的位置
        bgPosList = new ArrayList<>();

        for (int i = 0; i < rowNum; i++) {

            bgPosList.add(random.nextInt(colNum));

        }

    }

    @Override
    protected void init() {

        super.init();

        initPaints();

    }

    /**
     * 初始化画笔
     */
    private void initPaints() {
        //背景画笔
        bgPaint = new Paint();

        bgPaint.setColor(Color.GREEN);

        bgPaint.setAntiAlias(true);

        bgPaint.setStyle(Paint.Style.FILL);

        //折线画笔
        mPaint = new Paint();

        mPaint.setColor(Color.RED);

        mPaint.setAntiAlias(true);

        mPaint.setStrokeWidth(3.0f);

        radius = Math.min(rowHeight, colWidth) / 2 - 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(colWidth * colNum, rowHeight * rowNum);

        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawText(canvas);

    }

    /**
     * 画号码和号码之间的连线
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        for (int i = 0; i < rowNum; i++) {//画文字

            //画文字
            baseLineY = rowHeight * i + rowHeight / 2 - (int) (metrics.ascent) / 2;

//            log(String.format("baseLineY = %d", baseLineY));

            for (int j = 0; j < colNum; j++) {//画第i行每一列的字符

                float textWid = textPaint.measureText(j + i + 1 + "");//测量字符宽度

                textPaint.setColor(Color.BLACK);//文字有背景时要变为白色，其他为黑色

                int bgPos = bgPosList.get(i);//需要添加背景的文字

                if (j == bgPos) {//先画圆，再画文字，否则圆遮挡文字

                    //先画线 第i行 第j列
                    if (i != rowNum - 1) {//画有背景位置之间的连线

                        int nextBgPos = bgPosList.get(i + 1);

                        canvas.drawLine(j * colWidth + colWidth / 2, rowHeight * i + rowHeight / 2, nextBgPos * colWidth + colWidth / 2, rowHeight * (i + 1) + rowHeight / 2, mPaint);

                    }

                    canvas.drawCircle(colWidth * bgPos + colWidth / 2, rowHeight * i + rowHeight / 2, radius, bgPaint);//背景圆

                    textPaint.setColor(Color.WHITE);

                }

                canvas.drawText(j + i + 1 + "", colWidth * j + colWidth / 2 - textWid / 2, baseLineY, textPaint);//号码

            }

        }
    }

}
