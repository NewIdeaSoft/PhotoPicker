package com.nisoft.photopicker.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mDividerHeight;
    private int mDividerWidth;
    private Paint mPaint;

    public GridItemDecoration(int color) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
//            int index = parent.getChildAdapterPosition(view);
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//            int column = layoutManager.getSpanCount();
            float dividerTop = view.getTop() - mDividerHeight;
            float dividerLeft = view.getRight() - mDividerWidth;
            float dividerRight = view.getRight();
            float dividerBottom = view.getTop();
//            if (index < column-1){
//                dividerLeft = view.getRight()-mDividerWidth;
//            }else if (index==column -1){
//
//            }else{
//                if ((index+1)%column == 0){
//                    dividerTop = dividerTop - mDividerHeight;
//                }else{
//                    dividerTop = dividerTop - mDividerHeight;
//                    dividerLeft = dividerRight-mDividerWidth;
//                }
//            }
            c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int column = layoutManager.getSpanCount();
        int position = parent.getChildAdapterPosition(view);
        if (position < column - 1) {
            outRect.top = 0;
            outRect.right = 8;
            mDividerWidth = 8;
            mDividerHeight = 0;
        }else if(position == column - 1){
            outRect.top = 0;
            outRect.right = 0;
            mDividerHeight = 0;
            mDividerWidth = 0;
        } else if (position >= column) {
            if ((position + 1) % column > 0) {
                outRect.right = 8;
                mDividerWidth = 8;
                outRect.top = 8;
                mDividerHeight = 8;
            } else {
                outRect.top = 8;
                outRect.right = 0;
                mDividerHeight = 8;
                mDividerWidth = 0;
            }
        }
    }
}
