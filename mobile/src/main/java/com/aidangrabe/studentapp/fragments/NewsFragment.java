package com.aidangrabe.studentapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidangrabe.common.model.Article;
import com.aidangrabe.common.news.ArticleFetcher;
import com.aidangrabe.common.news.UccArticleFetcher;
import com.aidangrabe.common.util.MyVolley;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.NewsArticleActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aidan on 05/02/15.
 * This Fragment displays a list of Articles that start the NewsArticleActivity
 * when clicked.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SimpleDateFormat mDateFormat;
    private Date mDate;
    private List<Article> mArticles;
    private RecyclerView.Adapter<Adapter.ViewHolder> mAdapter;
    private ArticleFetcher mArticleFetcher;
    private ViewGroup mContainerView;
    private View mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDateFormat = new SimpleDateFormat("MMM dd yy");

        mArticles = new ArrayList<>();
        mDate = new Date();

        mArticleFetcher = new UccArticleFetcher(getActivity());
        mArticleFetcher.fetchArticles(new ArticleFetcher.Listener() {
            @Override
            public void onArticlesReady(List<Article> articles) {
                mArticles = articles;
                mAdapter.notifyDataSetChanged();
                setLoading(false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mContainerView = container;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        setupLoadingView();

        return view;

    }

    private void setupLoadingView() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mLoadingView = inflater.inflate(R.layout.loading_view, mContainerView, false);
        mContainerView.addView(mLoadingView);
        setLoading(true);

    }

    private void setLoading(boolean loading) {
        mRecyclerView.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        mLoadingView.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {

        int position = mRecyclerView.getChildPosition(v);
        Article article = mArticles.get(position);

        Intent intent = new Intent(getActivity(), NewsArticleActivity.class);
        intent.putExtra(NewsArticleActivity.EXTRA_ARTICLE_URL, article.getLink());
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // cancel the download
        mArticleFetcher.cancel();

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_item_news, parent, false);
            view.setOnClickListener(NewsFragment.this);

            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Article article = mArticles.get(position);

            mDate.setTime(article.getPublishTime());

            holder.titleTextView.setText(article.getTitle());
            holder.summaryTextView.setText(mDateFormat.format(mDate));

            if (article.getImage() == null) {
                downloadImage(article, holder.imageView);
                holder.imageView.setVisibility(View.INVISIBLE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageBitmap(article.getImage());
            }

        }

        @Override
        public int getItemCount() {
            return mArticles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView titleTextView, summaryTextView;
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);

                titleTextView = (TextView) itemView.findViewById(R.id.title_text);
                summaryTextView = (TextView) itemView.findViewById(R.id.summary_text);
                imageView = (ImageView) itemView.findViewById(R.id.image);

            }

        }

    }

    private void downloadImage(final Article article, ImageView imageView) {

        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);

        MyVolley.getInstance(getActivity()).add(new ImageRequest(article.getImageUrl(), new Response.Listener<Bitmap>() {
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
