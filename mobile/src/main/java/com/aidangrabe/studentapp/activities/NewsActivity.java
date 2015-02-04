package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.model.Article;
import com.aidangrabe.common.news.ArticleFetcher;
import com.aidangrabe.common.news.UccArticleFetcher;
import com.aidangrabe.studentapp.R;

import java.util.List;

/**
 * Created by aidan on 04/02/15.
 * This Activity displays a list of news articles fetched by an ArticleFetcher
 */
public class NewsActivity extends ActionBarActivity {

    private ListView mListView;
    private ArrayAdapter<Article> mAdapter;
    private ArticleFetcher mArticleFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        setTitle("News");

        mArticleFetcher = new UccArticleFetcher(this);
        mArticleFetcher.fetchArticles(new ArticleFetcher.Listener() {
            @Override
            public void onArticlesReady(List<Article> articles) {
                refreshList(articles);
            }
        });


        setupListView();

    }

    private void refreshList(List<Article> articles) {

        mAdapter.clear();
        mAdapter.addAll(articles);
        mAdapter.notifyDataSetChanged();

    }

    /**
     * Get the ListView and set it up with it's Adapter
     */
    private void setupListView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<Article>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Article article = getItem(position);
                ((TextView) view.findViewById(android.R.id.text1)).setText(article.getTitle());
                return view;
            }
        };
        mListView.setAdapter(mAdapter);
    }


}
