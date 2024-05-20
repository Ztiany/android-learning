package com.ztiany.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class StickySectionDecoration extends RecyclerView.ItemDecoration {

    private static final int DEFAULT_HEADER_HEIGHT = 20;
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_DIVIDER_HEIGHT = 1;

    private final GroupInfoCallback mCallback;

    private final TextPaint mTextPaint;
    private final Paint.FontMetrics mFontMetrics;
    private float mTextOffsetX;

    private final Paint mPaint;
    private int mHeaderHeight;//分组的高度
    private int mDividerHeight;//分割线的高度
    private int mHeaderColor = Color.GRAY;
    private int mDividerColor = Color.WHITE;

    private final GroupInfo mGroupInfo = new GroupInfo();

    StickySectionDecoration(@NonNull Context context, @NonNull GroupInfoCallback callback) {
        this.mCallback = callback;

        mDividerHeight = DEFAULT_DIVIDER_HEIGHT;
        mHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HEIGHT, context.getResources().getDisplayMetrics());
        float textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, context.getResources().getDisplayMetrics());

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(textSize);
        mFontMetrics = mTextPaint.getFontMetrics();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        mCallback.fillGroupInfo(mGroupInfo, position);
        //如果是组内的第一个则将间距撑开为一个Header的高度
        if (mGroupInfo.isFirstViewInGroup()) {
            outRect.top = mHeaderHeight;
        } else {//除了组内第一个Item外，其他Item需要设置分割线高度
            outRect.top = mDividerHeight;
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);//item在整个列表的位置
            mCallback.fillGroupInfo(mGroupInfo, index);//分组信息

            int left = parent.getPaddingLeft();//绘制区域左边边界
            int right = parent.getWidth() - parent.getPaddingRight();//绘制区域右边界

            //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View，它都需要绘制它对应的 StickyHeader。
            if (i == 0) {
                // 还要判断当前的 ItemView 是不是它组内的最后一个 View
                int top = parent.getPaddingTop();//第一个header高度始终是parent的paddingTop
                if (mGroupInfo.isLastViewInGroup()) {
                    int suggestTop = view.getBottom() - mHeaderHeight;
                    // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                    // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                    //否则将继续保持吸附在 parent 的顶部
                    if (suggestTop < top) {
                        top = suggestTop;
                    }
                }
                int bottom = top + mHeaderHeight;
                drawHeaderRect(c, mGroupInfo, left, top, right, bottom);
            } else {
                //只有组内的第一个ItemView之上才绘制
                if (mGroupInfo.isFirstViewInGroup()) {
                    int top = view.getTop() - mHeaderHeight;
                    int bottom = view.getTop();
                    drawHeaderRect(c, mGroupInfo, left, top, right, bottom);
                } else {
                    drawDivider(c, view, parent);
                }
            }
        }
    }

    private void drawDivider(Canvas c, View view, RecyclerView parent) {
        mPaint.setColor(mDividerColor);
        int top = view.getTop();
        int dividerTop = top - mDividerHeight;
        if (dividerTop > parent.getPaddingTop() + mHeaderHeight) {
            c.drawRect(view.getLeft(), dividerTop, view.getRight(), top, mPaint);
        }
    }

    private void drawHeaderRect(Canvas c, GroupInfo groupinfo, int left, int top, int right, int bottom) {
        //绘制Header
        mPaint.setColor(mHeaderColor);
        c.drawRect(left, top, right, bottom, mPaint);
        float titleX = left + mTextOffsetX;
        float v = top + (bottom - top) / 2F;
        float offset = (mFontMetrics.ascent + mFontMetrics.descent) / 2;
        //绘制Title
        c.drawText(groupinfo.mTitle, titleX, v - offset, mTextPaint);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 设置属性
    ///////////////////////////////////////////////////////////////////////////
    public void setTextOffsetX(float textOffsetX) {
        mTextOffsetX = textOffsetX;
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.getFontMetrics(mFontMetrics);
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
    }

    public void setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
    }

    public void setHeaderColor(int headerColor) {
        mHeaderColor = headerColor;
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 需要绘制的信息
    ///////////////////////////////////////////////////////////////////////////
    public interface GroupInfoCallback {
        void fillGroupInfo(GroupInfo groupInfo, int position);
    }

    public static class GroupInfo {

        //组号
        private int mGroupID;

        // Header 的 title
        private String mTitle;

        //ItemView 在组内的位置
        private int position;

        // 组的成员个数
        private int mGroupLength;

        public void setGroupID(int groupID) {
            this.mGroupID = groupID;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public boolean isFirstViewInGroup() {
            return position == 0;
        }

        public boolean isLastViewInGroup() {
            return position == mGroupLength - 1 && position >= 0;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setGroupLength(int groupLength) {
            this.mGroupLength = groupLength;
        }
    }

}
