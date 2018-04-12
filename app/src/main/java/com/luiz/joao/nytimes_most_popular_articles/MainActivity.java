package com.luiz.joao.nytimes_most_popular_articles;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.luiz.joao.nytimes_most_popular_articles.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<ArrayList<Article>> {
    private static ArticleAdapter articleAdapter;

    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    private static final int API_SEARCH_LOADER = 42;

    private static ArrayList<Article> articlesList = new ArrayList<>();
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private int viewPage = 0;
    private boolean loading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtils.setKey(getString(R.string.api_key));
        articleAdapter = new ArticleAdapter(this, articlesList);
        final ListView listView = findViewById(R.id.articles_list);
        
        listView.setAdapter(articleAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen >= totalItemCount) {
                    if (searchView != null && !loading) {
                        String searchQuery = searchView.getQuery().toString();
                        viewPage++;
                        doQuery(searchQuery);
                    }
                }
            }
        });

        getLoaderManager().initLoader(API_SEARCH_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        articlesList.clear();
        articleAdapter.clear();
        viewPage = 0;
        doQuery(newText);
        return false;
    }

    private void doQuery(String newText) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, newText);
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(API_SEARCH_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(API_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(API_SEARCH_LOADER, queryBundle, this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Article>>(this) {

            @Override
            protected void onStartLoading() {
                loading = true;
                forceLoad();
            }

            @Override
            public ArrayList<Article> loadInBackground() {
                if (args != null) {
                    String query = args.getString(SEARCH_QUERY_URL_EXTRA);
                    if (query != null && query.length() > 0) {
                        return NetworkUtils.loadArticles(query, viewPage);
                    }
                }
                return NetworkUtils.loadPopularArticles(viewPage);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        if (data != null) {
            articleAdapter.addAll(data);
            articleAdapter.notifyDataSetChanged();
        }
        loading = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        loading = false;
    }
}
