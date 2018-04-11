package com.luiz.joao.nytimes_most_popular_articles.utils;

import com.luiz.joao.nytimes_most_popular_articles.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Joaoe on 09/04/2018.
 */

public class JsonUtils {

    private static String mediaKey = "media";
    private static String abstractKey = "abstract";
    private static String publishedDateKey = "published_date";
    private static String titleKey = "title";
    private static String imageBaseURL = "https://static01.nyt.com/";

    public static ArrayList<Article> getInfoFromSearchJSON(String articlesJsonStr)
            throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject jsonObject = new JSONObject(articlesJsonStr);
        if (jsonObject.has(OWM_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray ArticlesArray = jsonObject.getJSONObject("response").getJSONArray("docs");
        ArrayList<Article> ArticlesList = new ArrayList<>();

        for (int i = 0; i < ArticlesArray.length(); i++) {
            JSONObject ArticleJson = ArticlesArray.getJSONObject(i);
            String articleAbstract = ArticleJson.getString("snippet");
            String date = ArticleJson.getString("pub_date");
            int t = date.indexOf("T");
            String publishedDate = date.substring(0, t);
            String title = ArticleJson.getJSONObject("headline").getString("main");
            JSONArray mediaArray = ArticleJson.getJSONArray("multimedia");
            String pictureUrl = null;
            if (mediaArray != null && mediaArray.length() > 0) {
                JSONObject mediaObject = (JSONObject) mediaArray.get(0);
                pictureUrl = imageBaseURL + mediaObject.getString("url");
            }

            Article Article = new Article(title, articleAbstract, publishedDate, pictureUrl);
            ArticlesList.add(Article);
        }

        return ArticlesList;
    }

    public static ArrayList<Article> getInfoFromPopularJSON(String articlesJsonStr)
            throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject jsonObject = new JSONObject(articlesJsonStr);
        if (jsonObject.has(OWM_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray ArticlesArray = jsonObject.getJSONArray("results");
        ArrayList<Article> articlesList = new ArrayList<>();

        for (int i = 0; i < ArticlesArray.length(); i++) {
            JSONObject articleJson = ArticlesArray.getJSONObject(i);
            String articleAbstract = articleJson.getString(abstractKey);
            String publishedDate = articleJson.getString(publishedDateKey);
            String title = articleJson.getString(titleKey);
            JSONArray mediaArray = articleJson.getJSONArray(mediaKey);
            String pictureUrl = null;
            if (mediaArray != null || mediaArray.length() > 0) {
                JSONObject mediaObject = (JSONObject) mediaArray.get(0);
                pictureUrl = ((JSONObject) mediaObject.getJSONArray("media-metadata").get(0)).getString("url");
            }

            Article article = new Article(title, articleAbstract, publishedDate, pictureUrl);
            articlesList.add(article);
        }

        return articlesList;
    }

}
