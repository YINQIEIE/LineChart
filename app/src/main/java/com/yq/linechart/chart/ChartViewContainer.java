package com.yq.linechart.chart;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by yinqi on 2017/3/6.
 */

public class ChartViewContainer extends FrameLayout {

    protected String TAG = getClass().getSimpleName();

    protected int viewWidth;//控件宽

    protected int viewHeight;//控件高

    protected int largestDeltaX;//水平最大滑动距离

    protected int largestDeltaY;//竖直最大滑动距离

    //手指所在位置坐标
    private float lastX;

    private float lastY;

    private ValueAnimator animator;

    private Point endPoint;


    private ScrollChartView.ScrollByListener scrollByListener;//scrollby监听

    private ScrollChartView.ScrollToListener scrollToListener;//scrollTo监听

    public ChartViewContainer(Context context) {
        this(context, null);
    }

    public ChartViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getWidth();

        viewHeight = getHeight();

        largestDeltaX = Math.abs(viewWidth - getChildAt(0).getWidth());

        largestDeltaY = Math.abs(viewHeight - getChildAt(0).getHeight());

        Log.i(TAG, "onMeasure: largestDeltaX=" + largestDeltaX + "largestDeltaY=" + largestDeltaY);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (getScrollX() < 0 || getScrollX() > largestDeltaX || getScrollY() < 0 || getScrollY() > largestDeltaY)//划过边界恢复之前再次点击无效

                    return false;

                lastX = event.getX();

                lastY = event.getY();

                return true;

            case MotionEvent.ACTION_MOVE:

                return true;

            case MotionEvent.ACTION_UP:

                return false;

            default:
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
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

    }

    /**
     * 手指抬起时执行此方法，并根据条件执行相应动画，手指抬起未超过边界不执行
     */
    private void fingerUp() {

        if (getScrollX() < 0) {//左边界

            //new Point(0, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY())) 判断y的位置确定应该回到0，largestDeltaY还是不变
            endPoint = new Point(0, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY()));

            upAnimation();

        } else if (getScrollY() < 0) {//上边界

            endPoint = new Point(getScrollX() > largestDeltaX ? largestDeltaX : (getScrollX() < 0 ? 0 : getScrollX()), 0);

            upAnimation();

        } else if (getScrollX() > largestDeltaX) {//右边界

            endPoint = new Point(largestDeltaX, getScrollY() > largestDeltaY ? largestDeltaY : (getScrollY() < 0 ? 0 : getScrollY()));

            upAnimation();

        } else if (getScrollY() > largestDeltaY) {//下边界

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

        Log.i(TAG, "move: ");

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

    public void addScrollListener(ScrollChartView.ScrollByListener scrollByListener) {

        this.scrollByListener = scrollByListener;

    }

    /**
     * scrollTo调用时调用
     */
    public interface ScrollToListener {

        void onScrollTo(int deltaX, int deltaY);
    }

    public void addScrollToListener(ScrollChartView.ScrollToListener scrollToListener) {

        this.scrollToListener = scrollToListener;

    }
}
