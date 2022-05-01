package com.sagr.newsreader.utils;


import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.sagr.newsreader.adapter.NewsAdapter;
import com.sagr.newsreader.models.NewsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetNews extends AsyncTask<Void, Void, ArrayList<NewsItem>> {
    public static final String TAG ="TAG";
    private ArrayList<NewsItem> news;

    private NewsAdapter newsAdapter;

    public GetNews(NewsAdapter newsAdapter) {
        this.newsAdapter = newsAdapter;
    }

    @Override
    protected ArrayList<NewsItem> doInBackground(Void ... voids) {
        Log.d(TAG, "doInBackground: started");
        InputStream inputStream = getInputStream();
        news = new ArrayList<>();
        if (null != inputStream) {
            XmlParser parser = new XmlParser();
            try {
                news = parser.initXmlParser(inputStream);
                return news;
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsItem> newsItems) {
        super.onPostExecute(newsItems);
        Log.d(TAG, "onPostExecute: started");
        newsAdapter.setNews(news);
    }

    private InputStream getInputStream() {
        try {
            URL url = new URL("https://rss.nytimes.com/services/xml/rss/nyt/World.xml");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    }