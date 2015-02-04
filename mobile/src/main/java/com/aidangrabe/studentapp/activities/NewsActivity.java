package com.aidangrabe.studentapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.model.Article;
import com.aidangrabe.common.news.ArticleFetcher;
import com.aidangrabe.common.news.UccArticleFetcher;
import com.aidangrabe.common.util.MyVolley;
import com.aidangrabe.studentapp.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aidan on 04/02/15.
 * This Activity displays a list of news articles fetched by an ArticleFetcher
 */
public class NewsActivity extends ActionBarActivity {

    private ListView mListView;
    private ArrayAdapter<Article> mAdapter;
    private ArticleFetcher mArticleFetcher;
    private SimpleDateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        setTitle("News");

        mDateFormat = new SimpleDateFormat("MMM dd yy");

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
                if (convertView == null) {
                    convertView = LayoutInflater.from(NewsActivity.this).inflate(R.layout.list_item_article, parent, false);
                }
                Article article = getItem(position);
                Date date = new Date(article.getPublishTime());
                ((TextView) convertView.findViewById(R.id.title_text)).setText(article.getTitle());
                ((TextView) convertView.findViewById(R.id.date_text)).setText(mDateFormat.format(date));
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
                if (article.getImage() == null) {
                    imageView.setVisibility(View.INVISIBLE);
                    downloadImage(article, imageView);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(article.getImage());
                }
                return convertView;
            }
        };
        mListView.setAdapter(mAdapter);
    }

    private void downloadImage(final Article article, ImageView imageView) {

        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(imageView);

        MyVolley.getInstance(this).add(new ImageRequest(article.getImageUrl(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImageView imageV = imageViewWeakReference.get();
                if (imageV != null) {
                    imageV.setImageBitmap(response);
                    Animation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setInterpolator(new DecelerateInterpolator(3f));
                    alphaAnimation.setDuration(1000);
                    imageV.startAnimation(alphaAnimation);
                    imageV.setVisibility(View.VISIBLE);
                    article.setImage(response);
                }
            }
        }, 320, 320, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));

    }

}
