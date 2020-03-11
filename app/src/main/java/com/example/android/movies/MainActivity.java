package com.example.android.movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private EditText mMovieInput;
    private TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieInput = (EditText)findViewById(R.id.movieInput);
        mTitleText = (TextView)findViewById(R.id.titleText);


        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    public void searchMovies(View view) {

        // Get the search string from the input field.
        String queryString = mMovieInput.getText().toString();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            mTitleText.setText(R.string.loading);
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
        else {
            if (queryString.length() == 0) {
                mTitleText.setText(R.string.no_search_term);
            } else {
                mTitleText.setText(R.string.no_network);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, args.getString("queryString"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray itemsArray = jsonObject.getJSONArray("Search");

            int i = 0;
            String title = null;
            String t = "";

            while (i < itemsArray.length() || (title == null)) {

                JSONObject movie = itemsArray.getJSONObject(i);

                try {
                    title = movie.getString("Title");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                t = t + "\n" + title;
                i++;

            }
            if (t != "") {
                mTitleText.setText(t);
            } else {
                mTitleText.setText(R.string.no_results);
            }

        } catch (Exception e){
            mTitleText.setText(R.string.no_results);
            e.printStackTrace();
        }


    }
    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}
