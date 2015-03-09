package com.aidangrabe.studentapp.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.aidangrabe.common.activities.FragContainerActivity;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.NewsArticleFragment;

/**
 * Created by aidan on 05/02/15.
 *
 */
public class NewsArticleActivity extends FragContainerActivity {

    public static final String EXTRA_ARTICLE_URL = "article_url";
    public static final String EXTRA_ARTICLE_TITLE = "article_title";
    public static final String EXTRA_COLOR = "color";
    public static final String EXTRA_STATUSBAR_COLOR = "statusBarColor";

    private String mUrl;
    private NewsArticleFragment mFrag;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mUrl = inState == null
                ? extras.getString(EXTRA_ARTICLE_URL)
                : inState.getString(EXTRA_ARTICLE_URL);

        String title = extras.getString(EXTRA_ARTICLE_TITLE);
        getSupportActionBar().setTitle(title);

        // get the bar colours from the extras
        int color = extras.getInt(EXTRA_COLOR, getResources().getColor(R.color.primary));
        int statusBarColor = extras.getInt(EXTRA_STATUSBAR_COLOR, getResources().getColor(R.color.primary_dark));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(statusBarColor);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // back button pressed
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXTRA_ARTICLE_URL, mUrl);

    }

    @Override
    protected Bundle onCreateFragmentArgs() {
        return NewsArticleFragment.makeArgs(mUrl);
    }

    @Override
    protected Fragment onCreateFragment() {
        mFrag =  new NewsArticleFragment();
        return mFrag;
    }


}
