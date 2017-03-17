package com.yq.linechart.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yiqile.app.zhucaibao.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by yinqi on 2017/3/1.
 */

public class LineChartView extends FrameLayout {

    private LeftCornerView leftCornerView;

    private TopView topView;//顶部自定义控件

    private LeftView left_View;//左边自定义控件

    private ChartViewContainer chartViewContainer;//走勢圖佈局

    private NoScrollChartView chartView;

    private int leftWid;//左边宽

    private int rowHeight;//行高

    private int colWidth;//列宽

    private int colNum;//列数

    private boolean isAverage = false;//是否平均

    private List<String> list;//数据集合

    public LineChartView(Context context) {

        this(context, null);

    }

    public LineChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        initAttrs(context, attrs);

        init(context);

    }

    /**
     * 初始化属性设置
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.chart);

        leftWid = typedArray.getInt(R.styleable.chart_left_width, 60);//左边宽

        colWidth = typedArray.getInt(R.styleable.chart_col_width, 60);//列宽

        rowHeight = typedArray.getInt(R.styleable.chart_row_height, 60);//行高

        colNum = typedArray.getInt(R.styleable.chart_col_number, 30);//列数

        isAverage = typedArray.getBoolean(R.styleable.chart_average_col_width, false);//是否使走势图数据部分平分屏幕宽度

        typedArray.recycle();
    }

    /**
     * @param context 上下文
     *                layout_runchartviw1使用本身可滑动的chartview
     */
    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_linechart, null);

        this.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        assignViews(view);
    }


    private void assignViews(View v) {

        leftCornerView = (LeftCornerView) v.findViewById(R.id.left_corner);

        topView = (TopView) v.findViewById(R.id.top_view);

        left_View = (LeftView) v.findViewById(R.id.left_View);

        chartViewContainer = (ChartViewContainer) v.findViewById(R.id.chart_container);

        chartView = (NoScrollChartView) v.findViewById(R.id.chartView);

        addListener();

        setSize();

    }

    /**
     * 添加滑动监听
     * {@link #topView} 、{@link #left_View}要随{@link #chartViewContainer} 一起滑动
     */
    private void addListener() {

        chartViewContainer.addScrollListener(new ScrollChartView.ScrollByListener() {
            @Override
            public void onScroll(int deltaX, int deltaY) {

                Log.d(TAG, "onScroll:deltaX= " + deltaX);

                topView.scrollBy(deltaX, 0);

                left_View.scrollBy(0, deltaY);

            }
        });

        chartViewContainer.addScrollToListener(new ScrollChartView.ScrollToListener() {
            @Override
            public void onScrollTo(int deltaX, int deltaY) {

                topView.scrollTo(deltaX, 0);

                left_View.scrollTo(0, deltaY);
            }
        });
    }

    /**
     * 根据xml中的属性设置各个view大小
     */
    private void setSize() {

        //将行高、列宽转化为像素
        leftWid = dp2px(leftWid);

        rowHeight = dp2px(rowHeight);

        colWidth = dp2px(colWidth);

        ViewGroup.LayoutParams lp = leftCornerView.getLayoutParams();

        lp.width = leftWid;

        lp.height = rowHeight;

        leftCornerView.setLayoutParams(lp);

        lp = left_View.getLayoutParams();

        lp.width = leftWid;

        left_View.setLayoutParams(lp);

        setChildSize();

    }

    /**
     * 设置各个部分size
     */
    private void setChildSize() {

        if (isAverage) colWidth = (getScreenWidth() - leftWid) / colNum;

        leftCornerView.setSize(leftWid, rowHeight);

        topView.setSize(colWidth, rowHeight, colNum);

        left_View.setSize(leftWid, rowHeight);

        chartView.setSize(colWidth, rowHeight, colNum);
    }

    /**
     * 设置列数发生变化
     *
     * @param colNum
     */
    public void setColNum(int colNum, boolean average) {

        this.colNum = colNum;

        //以下适用于动态切换列数 如列数由35变为12
        /*if (average) colWidth = (getScreenWidth() - leftWid) / colNum;

        leftCornerView.setSize(leftWid, rowHeight);

        topView.setSize(colWidth, rowHeight, colNum);

        left_View.setSize(leftWid, rowHeight);

        chartView.setSize(colWidth, rowHeight, colNum);

        chartViewContainer.setLargestDeltaX(isAverage ? 0 : (colWidth * colNum - chartViewContainer.getMeasuredWidth()));

        chartViewContainer.scrollTo(0, 0);

        topView.scrollTo(0, 0);

        left_View.scrollTo(0, 0);*/
    }

    /**
     * 设置是否平分屏幕
     *
     * @param average
     */
    public void setAverage(boolean average) {

        isAverage = average;

        setChildSize();

    }


    /**
     * 设置绘制数据源
     *
     * @param issueList 期次数据集合
     * @param missList  号码遗漏数据集合
     */
    public void setData(List<String> issueList, List<List<String>> missList) {

        allScrollToStart();

        Log.i(TAG, "trend_setData: size=" + issueList.size() + ">>" + ">>" + leftWid + isAverage + ">>" + colNum + ">>" + colWidth + "》》" + chartViewContainer.getMeasuredHeight());

        this.list = new ArrayList<>();

        this.list.addAll(issueList);

        chartViewContainer.setLargestDeltaX(isAverage ? 0 : (colWidth * colNum - chartViewContainer.getMeasuredWidth()));

        chartViewContainer.setLargestDeltaY(rowHeight * issueList.size() - chartViewContainer.getMeasuredHeight());

        left_View.notifyDataChanged(issueList);

        chartView.notifyDataSetChanged(missList);

    }

    /**
     * 全部滑到开始位置，在重新设置数据时调用
     *
     * @see {@link LineChartView#setData(List, List)}
     */
    private void allScrollToStart() {

        topView.scrollTo(0, 0);

        left_View.scrollTo(0, 0);

        chartViewContainer.scrollTo(0, 0);

    }

    /**
     * 设置顶部标题，由于顶部开奖号码和顶部标题数字一样，所以要同时把数据设置给chartview
     * 设置{@link #topView}字体颜色
     *
     * @param content
     * @see {@link #setData(List, List)}前调用
     */
    public void setTopContent(List<String> content) {

        topView.setList(content);

        chartView.setList(content);

    }

    /**
     * 设置绘制开奖号码的背景圆颜色
     *
     * @param color
     * @see {@link #setData(List, List)}前调用
     */
    public void setCircleColor(@ColorInt int color) {

        chartView.setCircleColor(color);

    }

    /**
     * 设置{@link #left_View}字体颜色
     *
     * @param size
     * @see {@link #setData(List, List)}前调用
     */
    public void setLeftTextSize(int size) {

        left_View.setTextSize(size);

    }

    /**
     * 设置{@link #topView}字体颜色
     *
     * @param color
     * @see {@link #setData(List, List)}前调用
     */
    public void setTopTextColor(int color) {

        topView.setTextColor(color);

    }

    /*
     *************************************************
     *
     * 两个工具方法
     *
     * ***********************************************
     */
    private int dp2px(int dp) {

        return (int) (dp * getContext().getResources().getDisplayMetrics().density);

    }

    private int getScreenWidth() {

        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

}
