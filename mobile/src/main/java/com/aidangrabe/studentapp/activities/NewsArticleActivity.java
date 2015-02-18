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

        int color = extras.getInt(EXTRA_COLOR, getResources().getColor(R.color.primary));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
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
