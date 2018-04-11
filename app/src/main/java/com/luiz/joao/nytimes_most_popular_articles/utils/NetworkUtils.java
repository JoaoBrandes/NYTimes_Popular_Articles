package com.luiz.joao.nytimes_most_popular_articles.utils;

import android.net.Uri;
import android.util.Log;

import com.luiz.joao.nytimes_most_popular_articles.Article;
import com.luiz.joao.nytimes_most_popular_articles.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joaoe on 09/04/2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    private static final String popularBaseUrl = "https://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json";
    private static final String searchBaseUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static String key = null;

    private static final String keyParam = "api_key";
    private static final String queryKey = "q";
    private static final String filterKey = "fl";
    private static final String sortKey = "sort";

    public static ArrayList<Article> loadArticles(String filter, int page) {

        ArrayList<Article> Articles = fetchArticles(filter, page);
        return Articles;
    }

    public static ArrayList<Article> loadPopularArticles(int page) {
        ArrayList<Article> Articles = fetchArticles(null, page);
        return Articles;
    }

    public static void setKey(String api_key) {
        key = api_key;
    }

    private static Uri buildSearchURL(String filter, int page) {
        Uri.Builder builder = Uri.parse(searchBaseUrl).buildUpon()
                .appendQueryParameter(keyParam, key)
                .appendQueryParameter(queryKey, filter)
                .appendQueryParameter(filterKey, "multimedia, pub_date, headline, snippet")
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter(sortKey, "newest");

        return builder.build();
    }

    private static Uri buildPopularURL(int page) {
        Uri.Builder builder = Uri.parse(popularBaseUrl).buildUpon()
                .appendQueryParameter(keyParam, key)
                .appendQueryParameter("offset", String.valueOf(page * 10));
        return builder.build();
    }

    private static URL buildURL(String filter, int page) {
        Uri builtUri;
        if (filter != null) {
            builtUri = buildSearchURL(filter, page);
        }   else {
            builtUri = buildPopularURL(page);
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static ArrayList<Article> fetchArticles(String filter, int page) {
        URL requestUrl = NetworkUtils.buildURL(filter, page);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            if (filter == null) {
                ArrayList<Article> ArticlesInfoFromJSON = JsonUtils.getInfoFromPopularJSON(jsonWeatherResponse);
                return ArticlesInfoFromJSON;
            } else {
                ArrayList<Article> ArticlesInfoFromJSON = JsonUtils.getInfoFromSearchJSON(jsonWeatherResponse);
                return ArticlesInfoFromJSON;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
