package com.agroneo.app.api;

import android.os.AsyncTask;

import com.agroneo.app.utils.Json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public abstract class Api extends AsyncTask<String, String, Json> implements ApiResponse {
    private String api = "https://api.agroneo.com";

    public static Api build(final ApiResponse api) {

        return new Api() {

            @Override
            public void apiResult(Json response) {
                api.apiResult(response);
            }

            @Override
            public void apiError() {
                api.apiError();
            }
        };
    }

    public void doGet(String url) {
        execute(url, "GET");
    }

    public void doPost(String url, Json data) {
        execute(url, "POST", data.toString());
    }

    @Override
    protected Json doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            connection = (HttpURLConnection) new URL(api + "/" + params[0]).openConnection();
            connection.setRequestMethod(params[1]);

            if (params.length > 2) {
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write("data=" + URLEncoder.encode(params[2], "UTF-8"));
                writer.flush();
                writer.close();

            }
            connection.connect();


            InputStream inputStream = null;
            try {
                inputStream = connection.getInputStream();
            } catch (Exception e) {
                inputStream = connection.getErrorStream();
            }

            if (inputStream == null) {
                return null;
            }

            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return new Json(buffer.toString());
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Json response) {
        if (response != null) {
            apiResult(response);
        } else {
            apiError();
        }
    }

    @Override
    public void apiError() {
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

}
