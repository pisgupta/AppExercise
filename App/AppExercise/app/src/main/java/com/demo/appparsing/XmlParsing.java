package com.demo.appparsing;

import android.util.Log;

import com.demo.appmodel.AppModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Pankaj on 6/22/2015.
 */
public class XmlParsing {
    private ArrayList<AppModel> pathlist;
    private AppModel model;
    private String TAG = "XmlParsing";

    /**
     * This Method return image icon and image path url
     * @param is
     * @return
     */
    public ArrayList<AppModel> parseData(InputStream is) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(is, null);
            parseXMLAndStoreIt(myparser);
            is.close();

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return pathlist;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        pathlist = new ArrayList<AppModel>();
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("flickr:buddyicon")) {
                            Log.e(TAG, "icon path" + text);
                            model = new AppModel();
                            model.setIconpath(text);
                        } else if (name.equals("link")) {
                            String attributeValue = myParser.getAttributeValue(1);
                            if (attributeValue.equalsIgnoreCase("image/jpeg")) {
                                String imagePathLink = myParser.getAttributeValue(null, "href");
                                model.setImagepath(imagePathLink);
                                Log.e(TAG, " image path " + imagePathLink);
                                pathlist.add(model);
                            }
                        } else {
                        }
                        break;
                }
                event = myParser.next();}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
