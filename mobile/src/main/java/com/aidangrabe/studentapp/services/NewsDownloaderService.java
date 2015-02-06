package com.aidangrabe.studentapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.aidangrabe.common.model.Article;
import com.aidangrabe.common.news.ArticleFetcher;
import com.aidangrabe.common.news.UccArticleFetcher;
import com.aidangrabe.common.util.MyVolley;
import com.aidangrabe.studentapp.activities.NewsActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

/**
 * Created by aidan on 05/02/15.
 * This service downloads news articles and posts a Notification if new articles are found
 */
public class NewsDownloaderService extends Service implements ArticleFetcher.Listener {

    private ArticleFetcher mArticleFetcher;

    @Override
    public void onCreate() {
        super.onCreate();

        downloadArticles();

    }

    private void downloadArticles() {

        mArticleFetcher = new UccArticleFetcher(this);
        mArticleFetcher.fetchArticles(this);

    }

    private void showNotification(Article article) {
        int notificationId = 001;

        Intent viewIntent = new Intent(this, NewsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("This is longer text that has a lot more information than the other smaller text");

        Notification notif = new NotificationCompat.Builder(this)
                .setContentTitle(article.getTitle())
                .setContentText(article.getImageUrl())
                .setSmallIcon(com.aidangrabe.common.R.drawable.ic_launcher)
                .setLargeIcon(article.getImage())
                .setStyle(bigStyle)
                .setContentIntent(pendingIntent)
                .extend(new NotificationCompat.WearableExtender().setBackground(article.getImage()))
                .build();
        notif.flags = notif.flags | Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notif);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onArticlesReady(List<Article> articles) {

        if (articles.size() != 0) {

            Article newArticle = articles.get(0);
            downloadArticleImage(newArticle);

        } else {
            // stop the service
            stopSelf();
        }

    }

    private void downloadArticleImage(final Article article) {

        MyVolley.getInstance(this).add(new ImageRequest(article.getImageUrl(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                article.setImage(response);
                showNotification(article);
                stopSelf();
            }
        }, 320, 320, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopSelf();
            }
        }));

    }

}
