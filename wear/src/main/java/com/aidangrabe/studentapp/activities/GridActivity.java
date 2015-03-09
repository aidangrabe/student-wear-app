package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aidangrabe.studentapp.R;


/**
 * Created by aidan on 29/10/14.
 *
 */
public abstract class GridActivity extends Activity {

    protected int mCols, mRows;
    protected GridButton[][] mGrid;

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onButtonClicked((GridButton) v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGrid = new GridButton[mCols][mRows];
        setContentView(R.layout.activity_grid);
        setupView((LinearLayout) findViewById(R.id.grid_layout));
    }

    private void setupView(LinearLayout rootView) {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

        for (int y = 0; y < mRows; y++) {
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            LinearLayout rowLayout = new LinearLayout(getApplicationContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowParams);
            for (int x = 0; x < mCols; x++) {

                GridButton button = createButton(x, y);
                button.setOnClickListener(mButtonClickListener);
                button.setLayoutParams(buttonParams);
                onButtonCreated(button);

                mGrid[x][y] = button;
                rowLayout.addView(button);
            }
            rootView.addView(rowLayout);
        }
    }

    public GridButton getButton(int x, int y) {
        try {
            return mGrid[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    protected abstract void onButtonCreated(GridButton button);

    protected abstract void onButtonClicked(GridButton button);

    protected GridButton createButton(int x, int y) {
        return new GridButton(getApplicationContext(), x, y);
    }

    public static class GridButton extends Button {
        private int x, y;

        public GridButton(Context context, int x, int y) {
            super(context);
            this.x = x;
            this.y = y;
        }

        public Pair<Integer, Integer> getPosition() {
            return new Pair<Integer, Integer>(x, y);
        }

        public int getGridX() {
            return x;
        }

        public int getGridY() {
            return y;
        }

    }

}
