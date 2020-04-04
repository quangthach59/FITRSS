package com.hcmus.fitrss.ui.news;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hcmus.fitrss.AppConfig;
import com.hcmus.fitrss.model.FeedItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FeedItem>> model = new MutableLiveData<>(new ArrayList<>());

    public NewsViewModel() {
        fetch();
    }

    public LiveData<ArrayList<FeedItem>> getModel() {
        return this.model;
    }

    public ArrayList<FeedItem> getItems() {
        return this.model.getValue() != null ?
                this.model.getValue() : new ArrayList<>();
    }

    public void setData(ArrayList<FeedItem> items) {
        this.model.setValue(items);
    }

    public void fetch() {
        setData(new ArrayList<>());
        new NewsAsyncFeedLoader(this).execute();
    }

    public static class NewsAsyncFeedLoader extends AsyncTask<String, FeedItem, ArrayList<FeedItem>> {
        private NewsViewModel model;

        public NewsAsyncFeedLoader(NewsViewModel viewModel) {
            this.model = viewModel;
        }

        @Override
        protected ArrayList<FeedItem> doInBackground(String... strings) {
            ArrayList<FeedItem> newItems = new ArrayList<>();
            try {
                URL url = new URL(AppConfig.RSS_FEED_ADDRESS);
                URLConnection connection;
                connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document dom = db.parse(in);
                    Element treeElements = dom.getDocumentElement();
                    newItems.clear();
                    NodeList itemNodes = treeElements.getElementsByTagName("item");
                    if ((itemNodes != null) && (itemNodes.getLength() > 0)) {
                        for (int i = 0; i < itemNodes.getLength(); i++) {
                            Element entry = (Element) itemNodes.item(i);
                            newItems.add(FeedItem.Converter.from(entry));
                        }
                    }
                }
                httpConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newItems;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedItem> feedItems) {
            super.onPostExecute(feedItems);
            this.model.setData(feedItems);
        }
    }
}
