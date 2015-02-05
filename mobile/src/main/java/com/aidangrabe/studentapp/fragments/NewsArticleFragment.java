package com.aidangrabe.studentapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.aidangrabe.common.news.UccArticleFetcher;
import com.aidangrabe.common.util.MyVolley;
import com.aidangrabe.studentapp.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by aidan on 05/02/15.
 *
 */
public class NewsArticleFragment extends Fragment {

    private static final String ARG_ARTICLE_URL = "article_url";
    private String mHtmlContent = "";

    public static Bundle makeArgs(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE_URL, url);
        return args;
    }

    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_article, container, false);

        mWebView = (WebView) view.findViewById(R.id.web_view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString(ARG_ARTICLE_URL);

            if (needsToDownload()) {
                MyVolley.getInstance(getActivity()).add(new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Element content = doc.select("#content").first();
                        updateWebView(content.html());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error loading article", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }));
            } else {
                Log.d("D", "Using previously downloaded data");
                updateWebView();
            }
        }

    }

    // returns true if we need to download the Article's content
    private boolean needsToDownload() {
        return mHtmlContent.length() == 0;
    }

    private void updateWebView() {
        mWebView.loadDataWithBaseURL(UccArticleFetcher.BASE_URL, makeHtmlContent(mHtmlContent), "text/html", "utf-8", null);
    }

    private void updateWebView(String html) {
        mHtmlContent = html;
        updateWebView();
    }

    private String makeHtmlContent(String html) {
        return String.format("<html><head><style>img {max-width: 100%%;}</style></head><body>%s</body></html>", html);
    }

}
