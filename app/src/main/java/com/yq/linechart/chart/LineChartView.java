package com.yq.linechart.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yq.linechart.R;

import static android.content.ContentValues.TAG;

/**
 * Created by yinqi on 2017/3/1.
 */

public class LineChartView extends FrameLayout {

    private LeftCornerView leftCornerView;

    private TopView topView;//顶部自定义控件

    private LeftView left_View;//左边自定义控件

    private ChartViewContainer chartFrame;//走勢圖佈局

    private NoScrollChartView chartView;

    private int leftWid;//左边宽

    private int rowHeight;

    private int colWidth;

    public LineChartView(Context context) {

        this(context, null);

    }

    public LineChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.chart);

        leftWid = typedArray.getInt(R.styleable.chart_left_width, 60);//左边宽

        colWidth = typedArray.getInt(R.styleable.chart_col_width, 60);//列宽

        rowHeight = typedArray.getInt(R.styleable.chart_row_height, 60);//行高

        Log.i(TAG, "LineChartView: " + "leftWid>>>" + leftWid + "rowHeight>>>" + rowHeight + ">>>" + colWidth);

        init(context);

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

        chartFrame = (ChartViewContainer) v.findViewById(R.id.chart_container);

        chartView = (NoScrollChartView) v.findViewById(R.id.chartView);

        ViewGroup.LayoutParams lp = leftCornerView.getLayoutParams();

        lp.width = dp2px(leftWid);

        leftCornerView.setLayoutParams(lp);

        lp = left_View.getLayoutParams();

        lp.width = dp2px(leftWid);

        left_View.setLayoutParams(lp);

        setSize();

        chartFrame.addScrollListener(new ScrollChartView.ScrollByListener() {
            @Override
            public void onScroll(int deltaX, int deltaY) {

                Log.d(TAG, "onScroll:deltaX= " + deltaX);

                topView.scrollBy(deltaX, 0);

                left_View.scrollBy(0, deltaY);

            }
        });

        chartFrame.addScrollToListener(new ScrollChartView.ScrollToListener() {
            @Override
            public void onScrollTo(int deltaX, int deltaY) {

                topView.scrollTo(deltaX, 0);

                left_View.scrollTo(0, deltaY);
            }
        });

    }

    private void setSize() {

//        topView.setColWidth(dp2px(colWidth));
//
//        topView.setRowHeight(dp2px(rowHeight));
//
//        left_View.setRowHeight(dp2px(rowHeight));
//
//        left_View.setColWidth(dp2px(leftWid));
//
//        chartView.setColWidth(dp2px(colWidth));
//
//        chartView.setRowHeight(dp2px(rowHeight));

        leftCornerView.setSize(dp2px(leftWid), dp2px(rowHeight));

        topView.setSize(dp2px(colWidth), dp2px(rowHeight));

        left_View.setSize(dp2px(leftWid), dp2px(rowHeight));

        chartView.setSize(dp2px(colWidth), dp2px(rowHeight));

    }

    protected int dp2px(int dp) {

        return (int) (dp * getContext().getResources().getDisplayMetrics().density);

    }

}
