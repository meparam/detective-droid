package com.michaelcarrano.detectivedroid.views;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.michaelcarrano.detectivedroid.R;
import com.michaelcarrano.detectivedroid.utils.BusUtil;

/**
 * Created by mcarrano on 11/29/14.
 */
public class SlidingPanel extends SlidingPaneLayout implements SlidingPaneLayout.PanelSlideListener {

    private boolean isOpened = false;
    private float actiondown = 0;
    private int menuWidthClosed;
    private int menuWidthExpand;

    public SlidingPanel(Context context) {
        super(context);
        setPanelSlideListener(this);
    }

    public SlidingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setPanelSlideListener(this);
    }

    public SlidingPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setPanelSlideListener(this);
    }

    private void init() {
        menuWidthClosed = getResources().getDimensionPixelOffset(R.dimen.menu_width_closed);
        menuWidthExpand = getResources().getDimensionPixelOffset(R.dimen.menu_width_expand);
        setSliderFadeColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            actiondown = ev.getX();
            return super.onInterceptTouchEvent(ev);
        }

        return (ev.getAction() == MotionEvent.ACTION_MOVE &&
                ((isOpened && actiondown > ev.getX() && actiondown > menuWidthExpand) ||
                        (!isOpened && actiondown < ev.getX() && actiondown < menuWidthClosed)));
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
    }

    @Override
    public void onPanelOpened(View panel) {
        isOpened = true;
        BusUtil.getInstance().post(new SlidingPanelStateChanged(true));
    }

    @Override
    public void onPanelClosed(View panel) {
        isOpened = false;
        BusUtil.getInstance().post(new SlidingPanelStateChanged(false));
    }

    public class SlidingPanelStateChanged {
        private final boolean state;

        public SlidingPanelStateChanged(boolean state) {
            this.state = state;
        }

        public boolean isPanelOpen() {
            return state;
        }
    }

}
