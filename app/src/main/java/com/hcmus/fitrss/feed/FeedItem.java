package com.hcmus.fitrss.feed;

import com.hcmus.fitrss.config.AppConfig;
import com.hcmus.fitrss.utils.UtilsTime;

import org.w3c.dom.Element;

import java.util.Date;

public class FeedItem {
    private String title, link, description;
    private Date publicDate;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private FeedItem item = new FeedItem();

        public Builder setTitle(String title) {
            item.title = title;
            return this;
        }

        public Builder setLink(String link) {
            item.link = link;
            return this;
        }

        public Builder setPublicDate(Date publicDate) {
            item.publicDate = publicDate;
            return this;
        }

        public Builder setDescription(String description) {
            item.description = description;
            return this;
        }

        public FeedItem build() {
            return item;
        }
    }

    public static class Converter {
        public static FeedItem from(Element element) throws Exception {
            String title = element.getElementsByTagName("title").item(0)
                    .getFirstChild().getNodeValue();
            String link = element.getElementsByTagName("link").item(0)
                    .getFirstChild().getNodeValue();
            String pubDate = element.getElementsByTagName("pubDate").item(0)
                    .getFirstChild().getNodeValue();
            Date dateTime = UtilsTime.parse(pubDate, AppConfig.RSS_DATE_TIME_FORMAT);
            String description = element.getElementsByTagName("description").item(0)
                    .getLastChild().getNodeValue();
            return new FeedItem.Builder()
                    .setTitle(title)
                    .setLink(link)
                    .setPublicDate(dateTime)
                    .setDescription(description)
                    .build();
        }
    }
}
