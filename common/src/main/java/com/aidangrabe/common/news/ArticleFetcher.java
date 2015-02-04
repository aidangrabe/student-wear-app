package com.aidangrabe.common.news;

import com.aidangrabe.common.model.Article;

import java.util.List;

/**
 * Created by aidan on 04/02/15.
 * An implementation of ArticleFetcher should download and parse data into a list of
 * Articles
 */
public interface ArticleFetcher {

    public List<Article> fetchArticles();

}
