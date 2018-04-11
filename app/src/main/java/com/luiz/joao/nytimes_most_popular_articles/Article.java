package com.luiz.joao.nytimes_most_popular_articles;

/**
 * Created by Joaoe on 09/04/2018.
 */

public class Article {

    private String title;
    private String articleAbstract;
    private String pictureURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }


    public String getPublished_date() {
        return published_date;
    }

    private String published_date;

    public Article(String title, String articleAbstract, String published_date, String pictureURL) {
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.published_date = published_date;
        this.pictureURL = pictureURL;
    }

    public String getPictureURL() {
        return pictureURL;
    }

}

