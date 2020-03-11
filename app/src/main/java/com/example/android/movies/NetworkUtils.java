package com.example.android.movies;


import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NetworkUtils {

    private static final String MOVIE_BASE_URL = "http://www.omdbapi.com/?apikey=4ca02acf";
    private static final String QUERY_PARAM = "s";


    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    static String getMovieInfo(String queryString){


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJSONString = null;


        try {

            Uri builtURI = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .build();

            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();


            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            movieJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return movieJSONString;


    }
}
