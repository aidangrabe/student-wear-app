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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        mUrl = extras.getString(EXTRA_ARTICLE_URL);
        mFrag.setArguments(NewsArticleFragment.makeArgs(mUrl));

    }

    @Override
    protected Fragment onCreateFragment() {

        mFrag = new NewsArticleFragment();
        return mFrag;

    }

}
