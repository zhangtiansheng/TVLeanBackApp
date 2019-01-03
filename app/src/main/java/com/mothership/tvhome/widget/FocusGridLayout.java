package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wangwei on 3/15/16.
 */
public class FocusGridLayout extends GridLayout {
    public FocusGridLayout(Context context) {
        this(context, null, 0);
    }

    public FocusGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }
    @Override
    public void requestChildFocus(View child, View focused)
    {
        super.requestChildFocus(child, focused);
        invalidate();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i)
    {
        View focus = getFocusedChild();
        if(null == focus)
        {
            return i;
        }
        else
        {
            int focusIdx = indexOfChild(focus);
            if(i < focusIdx)
            {
                return  i;
            }
            else if(i < childCount - 1)
            {
                return focusIdx + childCount - 1 - i;
            }
            else
            {
                return focusIdx;
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {

        int index;
        int increment;
        int end;
        int count = getChildCount();
        if((direction & FOCUS_UP) != 0) {
            Rect better = new Rect();
            getChildAt(0).getFocusedRect(better);
            this.offsetDescendantRectToMyCoords(getChildAt(0), better);
            int pos = 0;
            for (int i = 1; i != count; i += 1) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    Rect rect = new Rect();
                    getChildAt(i).getFocusedRect(rect);
                    this.offsetDescendantRectToMyCoords(getChildAt(i), rect);
                    if(rect.bottom>better.bottom){
                        better = rect;
                        pos = i;
                    }
                }
            }
            if(pos<count){
                if (getChildAt(pos).requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }

        }else{
            if ((direction & FOCUS_FORWARD) != 0) {
                index = 0;
                increment = 1;
                end = count;

            } else {
                index = count - 1;
                increment = -1;
                end = -1;
            }

            for (int i = index; i != end; i += increment) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    if (child.requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(hasFocus()){
            super.addFocusables(views,direction,focusableMode);
        }else{
            if (isFocusable()) {
                views.add(this);
            }
        }
        return ;
    }
}
