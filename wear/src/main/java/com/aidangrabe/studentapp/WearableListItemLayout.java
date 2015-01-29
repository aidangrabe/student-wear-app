package com.aidangrabe.studentapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WearableListItemLayout extends LinearLayout
        implements WearableListView.OnCenterProximityListener {

    private TextView mName;

    private final float mFadedTextAlpha;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);

        mFadedTextAlpha = 0.5f;
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mName.setAlpha(1f);
        setBackground(new ColorDrawable(getResources().getColor(R.color.highlight_color)));
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        mName.setAlpha(mFadedTextAlpha);
        setBackground(null);
    }
}
