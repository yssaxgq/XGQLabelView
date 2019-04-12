package com.xgq.a123.xgqlabelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LabelView extends ViewGroup {

    private Context mContext;

    private int mTopBottomMargin;
    private int mLeftRightMargin;

    public LabelView(Context context) {
        super(context);
        mContext = context;
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.LabelView);
            mTopBottomMargin = array.getDimensionPixelOffset(R.styleable.LabelView_top_bottom_margin, 0);
            mLeftRightMargin = array.getDimensionPixelOffset(R.styleable.LabelView_left_right_margin, 0);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //此控件的宽度
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec - getPaddingLeft() - getPaddingRight());

        int contentHeight = 0;//内容高度总和
        int maxItemHeight = 0;//当前行最高子View的高
        int lineWidth = 0;//当前行的宽度
        int maxLineWidth = 0;//最宽一行的宽度

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);//很重要 不然子 View 出不来
            if (childView.getMeasuredWidth() + mLeftRightMargin + lineWidth > maxWidth) {//换行计算
                contentHeight += maxItemHeight;//累加高度
                maxLineWidth = Math.max(maxLineWidth, lineWidth);//更新最宽行
                maxItemHeight = 0;//因为换行  需要重置一行内最高item
                lineWidth = 0;//因为换行 重置一行宽度
            }
            lineWidth += childView.getMeasuredWidth() + mLeftRightMargin;//累加行宽
            //更新最宽的的item高度
            maxItemHeight = Math.max(maxItemHeight, childView.getMeasuredHeight() + mTopBottomMargin);
        }
        contentHeight += maxItemHeight + mTopBottomMargin;//累加高度 mTopBottomMargin 是为了给整个控件的底部留出空白
        maxLineWidth = Math.max(maxLineWidth, lineWidth) + mLeftRightMargin;//更新最宽行 mLeftRightMargin 是为了给整个控件的右边留出空白
        setMeasuredDimension(measureWidth(widthMeasureSpec, maxLineWidth),
                measureHeight(heightMeasureSpec, contentHeight));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int x = getPaddingLeft() + mLeftRightMargin;//子 view 的横向最初绘制点 x
        int y = getPaddingTop() + mTopBottomMargin;//子 view 的纵向最初绘制点 y

        int contentWidth = right - left;//控件宽度
        int maxItemHeight = 0;//每一行最大高度

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //如果加上子 view 的宽度超出控件宽度就换行
            if (contentWidth < x + childView.getMeasuredWidth() + mLeftRightMargin + getPaddingRight()) {
                x = getPaddingLeft() + mLeftRightMargin;//将绘制点 x 拉到最初的位置
                y += maxItemHeight;//因为是换行 所以累加绘制点 y
                maxItemHeight = 0;//统计新一行的子 view 最大高度
            }
            //绘制子 view 的位置
            childView.layout(x, y, x + childView.getMeasuredWidth(), y + childView.getMeasuredHeight());
            x += childView.getMeasuredWidth() + mLeftRightMargin;//累加这行子 view 的宽度和
            //更新一行内子 view 中的最大高度
            maxItemHeight = Math.max(maxItemHeight, childView.getMeasuredHeight() + mTopBottomMargin);
        }
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {//控件的宽度设置为 march_parent 或者精确值
            result = specSize;//控件宽度就为准确值
        } else {
            result = contentWidth + getPaddingLeft() + getPaddingRight();//控件宽度为计算结果的值
            if (specMode == MeasureSpec.AT_MOST) {//控件的宽度设置为 wrap_content
                result = Math.min(result, specSize);//控件宽度就为 计算值和 准确值中较小的那一个
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;
    }

    private int measureHeight(int measureSpec, int contentHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentHeight + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }

    public void addViewWithString(String content) {
        View view = getView(content);
        if (view != null) {
            addView(view);
        }
    }

    public void addViewWithStrings(String... contents) {
        for (int i = 0; i < contents.length; i++) {
            View view = getView(contents[i]);
            if (view != null) {
                addView(view);
            }
        }
    }

    private View getView(String content) {
        if (TextUtils.isEmpty(content))
            return null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.label_item, null);
        TextView textView = view.findViewById(R.id.tv_label);
        textView.setText(content);
        return view;
    }
}
