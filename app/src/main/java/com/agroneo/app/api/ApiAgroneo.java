package com.agroneo.app.api;

import android.content.Context;
import android.os.AsyncTask;

import com.agroneo.app.R;
import com.agroneo.app.utils.Json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public abstract class ApiAgroneo extends AsyncTask<String, String, Json> implements ApiImpl {
    private String api;

    public ApiAgroneo(Context context) {
        api = context.getString(R.string.api_url);
    }

    public AsyncTask<String, String, Json> doGet(String url) {
        return execute(url, "GET");
    }

    public AsyncTask<String, String, Json> doPost(String url, Json data) {
        return execute(url, "POST", data.toString());
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
            result(response);
        } else {
            error();
        }
    }

    @Override
    public void error() {
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
