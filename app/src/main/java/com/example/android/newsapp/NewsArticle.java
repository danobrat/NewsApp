package com.example.android.newsapp;

public class NewsArticle {

    /** Section the article is within the news site */
    public final String section_name;

    /** Title of the news article */
    public final String web_title;

    /** Date of the news article */
    public final int date;

    /**
     * Constructs a new news article.
     *
     * @param eventTitle is the title of the news article
     * @param eventTime is the section the article is within the news site
     * @param eventTsunamiAlert is the date of the news article
     */
    public NewsArticle (String eventName, String eventTitle, int eventDate) {
        section_name = eventName;
        web_title = eventTitle;
        date = eventDate;
    }
}