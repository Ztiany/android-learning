package com.ztiany.view.custom.custom_viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

/**
 * 练习onMeasure的写法
 *
 * 需求：实现一个自定义布局：里面所有的子view按照正方形的样式排列，允许定义多行多列
 */
public class SquareEnhanceLayout extends ViewGroup {

    private static final String TAG = SquareEnhanceLayout.class.getSimpleName();

    private int childMeasureState;

    public SquareEnhanceLayout(Context context) {
        this(context, null);
    }

    public SquareEnhanceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareEnhanceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareEnhanceLayout);
        mOrientation = array.getInt(R.styleable.SquareEnhanceLayout_sel_orientation, 0);
        mMaxColumn = array.getInteger(R.styleable.SquareEnhanceLayout_sel_column, 1);
        mMaxRow = array.getInteger(R.styleable.SquareEnhanceLayout_sel_row, 1);
        array.recycle();
    }

    private int mOrientation;//排列方向
    public final int ORIENTATION_VERTICAL = 0;//横向
    public final int ORIENTATION_HORIZONTAL = 1;//纵向

    public static final int DEFAULT_MAX_ROW = Integer.MAX_VALUE, DEFAULT_MAX_COLUMN = Integer.MAX_VALUE;
    public int mMaxRow = DEFAULT_MAX_ROW;//最大行数
    public int mMaxColumn = DEFAULT_MAX_COLUMN;//最大列数

    private void init() {
        // 初始化最大行列数
        mMaxRow = mMaxColumn = 1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 声明临时变量存储父容器的期望值
        int parentDesireWidth = 0;
        int parentDesireHeight = 0;
        //获取子view的个数
        int count = getChildCount();

        View child;
        if (count > 0) {//有孩子采取测量

            // 声明两个一维数组存储子元素宽高数据
            int[] childWidths = new int[getChildCount()];
            int[] childHeights = new int[getChildCount()];


            for (int i = 0; i < count; i++) {
                child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {//如果view不可见，就不进行测量了
                    continue;
                }

                CustomLayoutParams customLayoutParams = (CustomLayoutParams) child.getLayoutParams();
                //对子view进行测量 并且考虑布局margin
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                //获取子view测量后的宽高指中的最大宽高值（这里需要子view都是按照正方形显示的）
                int childMaxSize = Math.max(child.getMeasuredHeight(), child.getMeasuredWidth());
                //取最大值，让子view重新测量
                int childMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxSize, MeasureSpec.EXACTLY);
                child.measure(childMeasureSpec, childMeasureSpec);

                //考量外边距计算子元素实际宽高并将数据存入数组
                childWidths[i] = child.getMeasuredWidth() + customLayoutParams.leftMargin + customLayoutParams.rightMargin;
                childHeights[i] = child.getMeasuredHeight() + customLayoutParams.topMargin + customLayoutParams.bottomMargin;

                // 合并子元素的测量状态，跟着系统控件写即可
                childMeasureState = combineMeasuredStates(childMeasureState, child.getMeasuredState());

            }


            // 声明临时变量存储行/列宽高
            int indexMultiWidth = 0, indexMultiHeight = 0;

            //父view根据孩子的测量结果，来计算自己的大小
            if (mOrientation == ORIENTATION_HORIZONTAL) {//横
                //如果子view的个数大于最大列数，说明需要换行
                if (count > mMaxColumn) {

                    int row = count / mMaxColumn;//行数
                    int remainder = count & mMaxColumn;//余数
                    // 声明临时变量存储子元素宽高数组下标值
                    int index = 0;

                    for (int x = 0; x < row; x++) {

                        for (int y = 0; y < mMaxColumn; y++) {//为什么是 x<row y<mMaxColumn  因为是横布局，整体一行一行来测量，一行中一个一个来测量
                            //单行累加宽度
                            indexMultiWidth += childWidths[index];
                            //高度取最大值
                            indexMultiHeight = Math.max(indexMultiHeight, childHeights[index]);
                            index++;
                        }
                        //一行遍历完毕后 高度累加 宽度取最大值
                        parentDesireHeight += indexMultiHeight;
                        parentDesireWidth = Math.max(parentDesireWidth, indexMultiWidth);
                        //一行遍历完毕 重置
                        indexMultiHeight = 0;
                        indexMultiWidth = 0;
                    }
                        /*
                     * 如果有余数表示有子元素未能占据一行
                     */
                    if (remainder > 0) {
                        for (int i = count - remainder; i < count; i++) {
                            indexMultiHeight = Math.max(indexMultiHeight, childHeights[i]);
                            indexMultiWidth += childWidths[i];
                        }
                        //最后一行遍历完毕后 高度累加 宽度取最大值
                        parentDesireHeight += indexMultiHeight;
                        parentDesireWidth = Math.max(parentDesireWidth, indexMultiWidth);
                    }

                } else {
                    //没有列数限制，就是一行
                    for (int x = 0; x < count; x++) {
                        //横向的布局 横向累加，要考虑子view的margin
                        parentDesireWidth += childWidths[x];
                        parentDesireHeight = Math.max(parentDesireHeight, childHeights[x]);
                    }
                }

            } else if (mOrientation == ORIENTATION_VERTICAL) {//纵向
                // TODO: 17.5.7
            }

            //最后要考虑自身的padding
            parentDesireWidth += getPaddingLeft() + getPaddingRight();
            parentDesireHeight += getPaddingTop() + getPaddingBottom();
            //尝试比较父容器期望值与Android建议的最小值大小并取较大值
            parentDesireWidth = Math.max(getSuggestedMinimumWidth(), parentDesireWidth);
            parentDesireHeight = Math.max(getSuggestedMinimumHeight(), parentDesireHeight);

        }

   /*
       这个resolveSize是View提供用来获取最合理size的一个工具方法
       
       具体实现在API 11由resolveSizeAndState处理，这个方法多了一个childMeasuredState参数，
       而上面例子我们在具体测量时也引入了一个childMeasureState临时变量的计算，那么这个值的作用是什么呢？说到这里不得不提API 11后引入的几个标识位：

        MEASURED_HEIGHT_STATE_SHIFT         测量高度状态遮罩
        MEASURED_SIZE_MASK                            测量尺寸遮罩
        MEASURED_STATE_MASK                         测量状态遮罩
        MEASURED_STATE_TOO_SMALL              表示规定的size小于期望的size

        childMeasuredState这个值由View.getMeasuredState()这个方法返回，一个布局通过View.combineMeasuredStates()这个方法来统计其子元素的测量状态。
        在大多数情况下你可以简单地只传递0作为参数值，而子元素状态值目前的作用只是用来告诉父容器在对其进行测量得出的测量值比它自身想要的尺寸要小，
        如果有必要的话一个对话框将会根据这个原因来重新校正它的尺寸。
        这里我们还是就按照谷歌官方的建议依葫芦画瓢。
        
        不过在看一些系统控件的写法时，很多操作(比如combineMeasuredStates)都使用了ViewCompat中的方法代替系统SDK提供的方法，
        已达到最好的兼容性，所以还是建议使用ViewCompat中的方法。
        
         */
        setMeasuredDimension(resolveSizeAndState(parentDesireWidth, widthMeasureSpec, childMeasureState),
                resolveSizeAndState(parentDesireHeight, heightMeasureSpec, childMeasureState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            Log.d(TAG, String.format(" int l = %d, int t = %d, int  = %d, int b = %d", l, t, r, b));

            int count = getChildCount();
            int indexPoint = 1;//标识到了第几行或第几列
            int indexWidth = 0;//存储临时行的宽度
            int indexHeight = 0;//存储列的高度
            // 声明临时变量存储行/列临时宽高
            int tempHeight = 0, tempWidth = 0;

            CustomLayoutParams clp = null;
            View child = null;

            for (int i = 0; i < count; i++) {
                child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                clp = (CustomLayoutParams) child.getLayoutParams();
                if (mOrientation == ORIENTATION_HORIZONTAL) {

                    if (count > mMaxColumn) {//多行
                        if (i < mMaxColumn * indexPoint) {
                            child.layout(
                                    getPaddingLeft() + indexWidth + clp.leftMargin,
                                    getPaddingTop() + indexHeight + clp.topMargin,
                                    getPaddingLeft() + indexWidth + clp.leftMargin + child.getMeasuredWidth(),
                                    getPaddingTop() + indexHeight + clp.topMargin + child.getMeasuredHeight()
                            );
                            tempHeight = Math.max(tempHeight, clp.topMargin + clp.bottomMargin + child.getMeasuredHeight());
                            indexWidth += child.getMeasuredWidth() + clp.leftMargin + clp.rightMargin;
                            if (i + 1 >= mMaxColumn * indexPoint) {
                                indexWidth = 0;
                                indexHeight += tempHeight;
                                indexPoint++;
                            }

                        }


                    } else {//单行

                        child.layout(
                                getPaddingLeft() + indexWidth + clp.leftMargin,
                                getPaddingTop() + clp.topMargin,
                                getPaddingLeft() + indexWidth + clp.leftMargin + child.getMeasuredWidth(),
                                getPaddingTop() + clp.topMargin + child.getMeasuredHeight()
                        );
                        indexWidth += child.getMeasuredWidth() + clp.leftMargin + clp.rightMargin;
                    }

                } else if (mOrientation == ORIENTATION_VERTICAL) {
                    // TODO: 17.5.7
                }
            }
        }
    }

    /**
     * 一致地返回false，其作用是告诉framework我们当前的布局不是一个滚动的布局
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    //实现跟LayoutParams相关的方法

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }

    @SuppressWarnings("all")
    public static class CustomLayoutParams extends MarginLayoutParams {

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CustomLayoutParams(int width, int height) {
            super(width, height);
        }

        public CustomLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
