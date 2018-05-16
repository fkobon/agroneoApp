package com.agroneo.app.ui.action;

import android.os.AsyncTask;

public abstract class ActionBarAsyncTask extends AsyncTask<Integer, Boolean, Boolean> implements ActionImpl {


    @Override
    protected Boolean doInBackground(Integer... params) {
        try {
            Thread.sleep(params[1] * 1000);
        } catch (Exception e) {

        }
        return params[0] > 0;
    }

    @Override
    protected void onPostExecute(Boolean show) {
        if (show) {
            showing();
        } else {
            hidding();
        }
    }
}
