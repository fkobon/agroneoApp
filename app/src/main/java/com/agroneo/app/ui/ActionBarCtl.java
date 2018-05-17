package com.agroneo.app.ui;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;

public class ActionBarCtl {

    private ActionBar actionBar;
    private ActionBarAsyncTask ctrl;

    public ActionBarCtl(ActionBar actionBar) {
        this.actionBar = actionBar;
    }


    public void hide(int seconds) {

        cancel();

        ctrl.execute(0, seconds);
    }

    public void hide() {
        cancel();
        actionBar.hide();
    }

    public void show() {
        cancel();
        try {
            actionBar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(int seconds) {
        cancel();
        ctrl.execute(1, seconds);
    }

    private void cancel() {
        if (ctrl != null) {
            ctrl.cancel(true);
        }
        ctrl = new ActionBarAsyncTask() {
            @Override
            public void showing() {
                show();
            }

            @Override
            public void hidding() {
                hide();
            }
        };
    }

    public int getHeight() {
        return actionBar.getHeight();
    }

    public abstract static class ActionBarAsyncTask extends AsyncTask<Integer, Boolean, Boolean> implements ActionImpl {


        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                Thread.sleep(params[1] * 1000);
            } catch (Exception e) {
                return null;
            }
            return params[0] > 0;
        }

        @Override
        protected void onPostExecute(Boolean show) {
            if (show == null) {
                //canceled
                return;
            }
            if (show) {
                showing();
            } else {
                hidding();
            }
        }
    }

    public interface ActionImpl {
        void showing();
        void hidding();
    }
}
