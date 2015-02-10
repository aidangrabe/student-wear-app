package com.aidangrabe.common.news;

import com.aidangrabe.common.model.Article;

import java.util.List;

/**
 * Created by aidan on 04/02/15.
 * An implementation of ArticleFetcher should download and parse data into a list of
 * Articles
 */
public interface ArticleFetcher {

    /**
     * A listener for ArticleFetcher events
     */
    public interface Listener {
        /**
         * Called when the Articles have been downloaded and are ready to be used
         * @param articles the articles that were downloaded
         */
        public void onArticlesReady(List<Article> articles);
    }

    /**
     * Start downloading the articles
     */
    public void fetchArticles();

    /**
     * Fetch the articles and call the given listener
     * @param callback the callback to be notified when the download is complete
     */
    public void fetchArticles(Listener callback);

    /**
     * Cancel the download
     */
    public void cancel();

}
