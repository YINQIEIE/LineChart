package com.yq.linechart.chart;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yinqi on 2017/2/28.
 * <p>
 * 此view本身可滑动，但是滑动效果不是很流畅，使用
 *
 * @see NoScrollChartView 效果比较好
 */

public class ScrollChartView extends BaseChartView {

    //手指所在位置坐标
    private float lastX;

    private float lastY;

    private ValueAnimator animator;//滑动过猛手指抬起时回让视图到正常范围的过度动画

    private ScrollByListener scrollByListener;//scrollby监听

    private ScrollToListener scrollToListener;//scrollTo监听

    //圆形背景画笔
    private Paint bgPaint;

    private Paint mPaint;//折线连线画笔

    private int radius;//背景圆半径

    private Random random = new Random();//生成需要添加圆形背景的位置

    private List<Integer> bgPosList;//背景位置，测试用

    private Point endPoint;//滑过边界时需要回到的位置坐标点

    public ScrollChartView(Context context) {
        super(context, null);
    }

    public ScrollChartView(Context context, AttributeSet attrs) {

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

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (getScrollX() < 0 || getScrollX() > largestDeltaX || getScrollY() < 0 || getScrollY() > largestDeltaY)//划过边界恢复之前再次点击无效

                    return false;

                lastX = event.getX();

                lastY = event.getY();

                return true;

            case MotionEvent.ACTION_MOVE:

                move(event);

                return true;

            case MotionEvent.ACTION_UP:

                fingerUp();

                return false;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }*/

    /**
     * 手指抬起时执行此方法，并根据条件执行相应动画，手指抬起未超过边界不执行
     */
    private void fingerUp() {

        if (getScrollX() < 0) {//左边界

            log("左边界");

            //new Point(0, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY())) 判断y的位置确定应该回到0，largestDeltaY还是不变
            endPoint = new Point(0, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY()));

            upAnimation();

        } else if (getScrollY() < 0) {//上边界

            log("上边界");

            endPoint = new Point(getScrollX() > largestDeltaX ? largestDeltaX : (getScrollX() < 0 ? 0 : getScrollX()), 0);

            upAnimation();

        } else if (getScrollX() > largestDeltaX) {//右边界

            log("右边界");

            endPoint = new Point(largestDeltaX, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY()));

            upAnimation();

        } else if (getScrollY() > largestDeltaY) {//下边界

            log("下边界");

            endPoint = new Point(getScrollX() > largestDeltaX ? largestDeltaX : (getScrollX() < 0 ? 0 : getScrollX()), largestDeltaY);

            upAnimation();

        }
    }

    /**
     * move执行方法
     *
     * @param event
     */
    private void move(MotionEvent event) {

        int diffX = (int) (lastX - event.getX());

        int diffY = (int) (lastY - event.getY());

        if ((getScrollX() <= 0 && diffX <= 0) || (getScrollX() >= largestDeltaX && diffX > 0))

            diffX = 0;

        if ((getScrollY() <= 0 && diffY <= 0) || (getScrollY() >= largestDeltaY && diffY > 0))

            diffY = 0;

        scrollBy(diffX, diffY);

        if (null != scrollByListener) scrollByListener.onScroll(diffX, diffY);

        Log.i(TAG, "getScrollX=" + getScrollX() + "  diffX=" + diffX + "   getScrollY=" + getScrollY() + "  diffY=" + diffY);

        lastX = event.getX();

        lastY = event.getY();
    }

    /**
     * 手指抬起越过边界时执行
     */
    private void upAnimation() {

        animator = ValueAnimator.ofObject(new PointEvaluator(), new Point(getScrollX(), getScrollY()), endPoint);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                Point point = (Point) valueAnimator.getAnimatedValue();

                scrollTo((int) point.getX(), (int) point.getY());

                observeScrollTo((int) point.getX(), (int) point.getY());

            }
        });

        animator.start();

    }

    /**
     * 猛滑越界回弹监听
     *
     * @param x
     * @param y
     */
    private void observeScrollTo(int x, int y) {

        if (null != scrollToListener) scrollToListener.onScrollTo(x, y);
    }

    /**
     * 坐标类
     */
    private class Point {

        private float x;

        private float y;

        public Point(float x, float y) {

            this.x = x;

            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

    /**
     * 估值器类
     */
    class PointEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startPoint, Point endPoint) {

            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());

            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

            return new Point(x, y);
        }

    }

    /**
     * scrollBy调用时调用
     */
    public interface ScrollByListener {

        void onScroll(int deltaX, int deltaY);
    }

    public void addScrollListener(ScrollByListener scrollByListener) {

        this.scrollByListener = scrollByListener;

    }

    /**
     * scrollTo调用时调用
     */
    public interface ScrollToListener {

        void onScrollTo(int deltaX, int deltaY);
    }

    public void addScrollToListener(ScrollToListener scrollToListener) {

        this.scrollToListener = scrollToListener;

    }

}
