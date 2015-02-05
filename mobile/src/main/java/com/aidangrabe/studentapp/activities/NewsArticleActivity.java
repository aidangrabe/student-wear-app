package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aidangrabe.common.activities.FragContainerActivity;
import com.aidangrabe.studentapp.fragments.NewsArticleFragment;

/**
 * Created by aidan on 05/02/15.
 *
 */
public class NewsArticleActivity extends FragContainerActivity {

    public static final String EXTRA_ARTICLE_URL = "article_url";

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

        mUrl = inState == null
                ? extras.getString(EXTRA_ARTICLE_URL)
                : inState.getString(EXTRA_ARTICLE_URL);

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
