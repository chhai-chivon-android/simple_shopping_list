package com.gosiewski.shoppinglist.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    public abstract boolean onSingleTapConfirmed(MotionEvent e);
    public abstract void onLongPress(MotionEvent e);
}
