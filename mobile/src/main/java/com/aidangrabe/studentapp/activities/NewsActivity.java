package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aidangrabe.common.activities.FragContainerActivity;
import com.aidangrabe.studentapp.fragments.NewsFragment;

/**
 * Created by aidan on 04/02/15.
 * This Activity displays a list of news articles fetched by an ArticleFetcher
 */
public class NewsActivity extends FragContainerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("News");

    }

    @Override
    protected Fragment onCreateFragment() {
        return new NewsFragment();
    }
}
