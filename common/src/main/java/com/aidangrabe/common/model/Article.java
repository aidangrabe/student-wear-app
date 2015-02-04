package com.aidangrabe.common.model;

import com.orm.SugarRecord;

/**
 * Created by aidan on 04/02/15.
 * This class represents a news article. An article should have a title, date
 * and link to the article's body
 */
public class Article extends SugarRecord<Article> {

    private String title, link, imageUrl;
    private long publishTime;

    // needed for Sugar ORM
    public Article() {}

    public Article(String title, String link, long publishTime) {
        this.title = title;
        this.link = link;
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
