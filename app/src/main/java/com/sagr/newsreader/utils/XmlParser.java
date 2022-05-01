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

    public List initXmlParser(InputStream inputStream) throws XmlPullParserException, IOException {
        List entries = new ArrayList();
        Log.d(TAG, "initXmlParser: started ");
        XmlPullParser xmlPullParser =  Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
        xmlPullParser.setInput(inputStream,null);
        xmlPullParser.nextTag();
        return readFeed(xmlPullParser);

    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG,null,"rss");
        while (parser.next() !=XmlPullParser.END_TAG){
            if(parser.getEventType()!= XmlPullParser.START_TAG){
                continue;
            }
            parser.require(XmlPullParser.START_TAG,null,"channel");
            while (parser.next() != XmlPullParser.END_TAG){
                if(parser.getEventType()!= XmlPullParser.START_TAG){
                    continue;
                }
                String name = parser.getName();

                if(name.equals("item")){
                    if (name.equals("entry")) {
                        entries.add(readEntry(parser));
                    } else {
                        skip(parser);
                    }
                }
            }


        }
        return  entries;
    }


    private NewsItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String description= null;
        String link = null;
        String date= null;
        String imageUrl = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readContent(parser,"title");
            } else if (name.equals("pubdate")) {
                date = readContent(parser,"pubdate");
            } else if (name.equals("link")) {
                link = readContent(parser,"link");
            }
            else if(name.equals("media:content")){
                imageUrl = readImageUrl(parser);
            }
            else if(name.equals("description")){
                description = readContent(parser,"description");
            }
            else {
                skip(parser);
            }
        }
        return new NewsItem(title,description, link,date,imageUrl);
    }


    private String readContent(XmlPullParser parser , String tagName) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tagName);
        return content;
    }




    // Processes link tags in the feed.
    private String readImageUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageUrl = "";
        parser.require(XmlPullParser.START_TAG, null, "media:content");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");

        return imageUrl;
    }



    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
