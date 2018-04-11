package com.luiz.joao.nytimes_most_popular_articles;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joaoe on 10/04/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, List<Article> androidFlavors) {
        super(context, 0, androidFlavors);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final Article article = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        ImageView articlePic =  view.findViewById(R.id.iv_articlePic);
        Picasso.with(getContext()).load(article.getPictureURL()).into(articlePic);

        TextView articleTitle = view.findViewById(R.id.tv_article_title);
        articleTitle.setText(article.getTitle());

        TextView articleAbstract = view.findViewById(R.id.tv_article_abstract);
        articleAbstract.setText(article.getArticleAbstract());

        TextView publishDate = view.findViewById(R.id.tv_article_pub_date);
        publishDate.setText(getContext().getString(R.string.published_date) + " " + article.getPublished_date());

        return view;
    }

}
