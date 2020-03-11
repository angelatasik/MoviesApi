package com.example.android.movies;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

class MovieLoader extends AsyncTaskLoader<String> {

    private String mQueryString;


    public MovieLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public String loadInBackground() {
        return NetworkUtils.getMovieInfo(mQueryString);
    }
}

