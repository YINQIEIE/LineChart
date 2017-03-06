package com.yq.linechart.chart;

import android.content.Context;
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

    private TopView topView;//顶部自定义控件

    private LeftView left_View;//左边自定义控件

    private ChartViewContainer chartFrame;//走勢圖佈局

    public LineChartView(Context context) {

        super(context, null);

    }

    public LineChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

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

        topView = (TopView) v.findViewById(R.id.top_view);

        left_View = (LeftView) v.findViewById(R.id.left_View);

        chartFrame = (ChartViewContainer) v.findViewById(R.id.chart_container);

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

}
