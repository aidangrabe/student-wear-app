package com.aidangrabe.common.news;

import android.content.Context;
import android.util.Log;

import com.aidangrabe.common.model.Article;
import com.aidangrabe.common.util.MyVolley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aidan on 04/02/15.
 * This class downloads Ucc's news page, parses it, and returns a List of Articles
 */
public class UccArticleFetcher implements ArticleFetcher {

    private static final String ALL_ARTICLES_URL = "http://www.ucc.ie/en/news/";

    private Context mContext;
    private List<Article> mArticles;
    private SimpleDateFormat mDateFormat;
    private Listener mListener;

    public UccArticleFetcher(Context context) {
        mContext = context;
    }

    @Override
    public void fetchArticles() {

        mDateFormat = new SimpleDateFormat("dd MMM yy");
        downloadArticlePage();

    }

    @Override
    public void fetchArticles(Listener callback) {
        setListener(callback);
        fetchArticles();
    }

    private void downloadArticlePage() {

        Log.d("D", "Downloading: " + ALL_ARTICLES_URL);
        MyVolley.getInstance(mContext).add(new StringRequest(ALL_ARTICLES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError();
                Log.e("E", "Error downloading: " + ALL_ARTICLES_URL);
            }
        }));

    }

    private void onError() {
        if (mListener != null) {
            // call the listener's callback with an empty list of Articles
            mListener.onArticlesReady(new ArrayList<Article>());
        }
    }

    private void parseResponse(String response) {

        Log.d("D", "parsing response..");
        ArrayList<Article> articles = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements articleElements = doc.select(".article");

        Log.d("D", "creating articles");
        for (Element element : articleElements) {
            Article article = createArticle(element);
            if (article != null) {
                articles.add(article);
            }
        }
        mArticles = articles;
        Log.d("D", "download/parse complete");
        Log.d("D", "found " + mArticles.size() + " articles");
        if (mListener != null) {
            mListener.onArticlesReady(articles);
        }

    }

    private Article createArticle(Element element) {

        Article article = null;

        try {
            Element linkElement = element.select("h2 > a").first();
            String title = linkElement.text();
            String link = linkElement.attr("href");
            String dateText = element.select(".date").first().text();
            Date date = mDateFormat.parse(dateText);
            article = new Article(title, link, date.getTime());
        } catch (Exception e) {
            Log.e("E", "Error parsing article:");
            e.printStackTrace();
        }

        return article;

    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }
}
