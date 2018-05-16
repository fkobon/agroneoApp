package com.agroneo.app.ui.action;

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
}
