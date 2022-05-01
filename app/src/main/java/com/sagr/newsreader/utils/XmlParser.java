package com.sagr.newsreader.utils;


import android.util.Log;
import android.util.Xml;

import com.sagr.newsreader.models.NewsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static final String TAG = "Xml Parser";
    private final  String ns = null;
    private ArrayList<NewsItem> newsItemArrayList;


    public ArrayList<NewsItem>  initXmlParser(InputStream inputStream) throws XmlPullParserException, IOException {
        newsItemArrayList = new ArrayList<>();
        Log.d(TAG, "initXMLPullParser: started");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.next();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            parser.require(XmlPullParser.START_TAG, null, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals("item")){
                    parser.require(XmlPullParser.START_TAG, null, "item");

                    String title = "";
                    String description = "";
                    String link = "";
                    String date = "";
                    String imageUrl = "";

                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        String tagName = parser.getName();
                        switch (tagName) {
                            case "title":
                                title = getContent(parser, "title");
                                break;
                            case "description":
                                description = getContent(parser, "description");
                                break;
                            case "link":
                                link = getContent(parser, "link");
                                break;
                            case "pubDate":
                                date = getContent(parser, "pubDate");
                                break;
                            case "media:content":

                                imageUrl = readLink(parser);
                            default:
                                skipTag(parser);
                                break;
                        }
                    }

                    NewsItem item = new NewsItem(title, description, link, date,imageUrl);
                    newsItemArrayList.add(item);
                }else {
                    skipTag(parser);
                }
            }
        }
        return newsItemArrayList;
    }

    private String getContent (XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        String content = "";
        parser.require(XmlPullParser.START_TAG, null, tagName);

        if (parser.next() == XmlPullParser.TEXT) {
            content = parser.getText();
            parser.next();
        }

        return content;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "media:content");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "url");

        return relType;
    }
    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int number = 1;

        while (number != 0) {
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    number++;
                    break;
                case XmlPullParser.END_TAG:
                    number--;
                    break;
                default:
                    break;
            }
        }

    }

}
