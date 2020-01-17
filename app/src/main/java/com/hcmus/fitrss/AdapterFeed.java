package com.hcmus.fitrss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hcmus.fitrss.databinding.FeedItemBinding;
import com.hcmus.fitrss.feed.FeedItem;
import com.hcmus.fitrss.utils.UtilsTextView;

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

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.FeedViewHolder> {
    private Context context;
    private ArrayList<FeedItem> items = new ArrayList<>();
    private LayoutInflater inflater;
    private OnItemClickedListener onItemClickedListener;
    //private static RequestQueue queue;

    class FeedViewHolder extends RecyclerView.ViewHolder {
        private FeedItemBinding binding;

        public FeedViewHolder(@NonNull FeedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterFeed(Context context) {
        this.context = context;
        //queue = Volley.newRequestQueue(context);
        //queue.start();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        FeedItemBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.feed_item, parent, false);
        return new FeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final @NonNull FeedViewHolder holder, final int position) {
        FeedItem item = items.get(position);
        UtilsTextView.setText(holder.binding.itemTitle, item.getTitle());
        UtilsTextView.setText(holder.binding.itemLink, item.getLink());
        UtilsTextView.setText(holder.binding.itemPublicDate, item.getPublicDate());
        //UtilsTextView.setText(holder.binding.itemDescription, item.getDescription());
        holder.binding.itemDescription.setVisibility(View.GONE);
        holder.binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null)
                    onItemClickedListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void refresh() {
        new DownloadRssFeed().execute();
    }

    public class DownloadRssFeed extends AsyncTask<String, Void, ArrayList<FeedItem>> {
        private ProgressDialog dialog;

        public DownloadRssFeed(){
            dialog = new ProgressDialog(context);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Please wait\nReading RSS feed ...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected ArrayList<FeedItem> doInBackground(String... params) {
            ArrayList<FeedItem> newsList = new ArrayList<>();
            this.dialog.setMessage("Please wait\nReading RSS feed");
            try {
                URL url = new URL("https://www.fit.hcmus.edu.vn/vn/feed.aspx");
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
                    newsList.clear();
                    NodeList itemNodes = treeElements.getElementsByTagName("item");
                    if ((itemNodes != null) && (itemNodes.getLength() > 0)) {
                        for (int i = 0; i < itemNodes.getLength(); i++) {
                            Element entry = (Element) itemNodes.item(i);
                            newsList.add(FeedItem.Converter.from(entry));
                        }
                    }
                }
                httpConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newsList;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedItem> result) {
            super.onPostExecute(result);
            items = result;
            notifyDataSetChanged();
            dialog.dismiss();
        }
    }
}
