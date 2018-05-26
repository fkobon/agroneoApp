package com.agroneo.app.utils.adapter;

import android.view.View;

public interface ListAdapterImpl<T> {
    void showView(View view, T dao, boolean isLast);
}
