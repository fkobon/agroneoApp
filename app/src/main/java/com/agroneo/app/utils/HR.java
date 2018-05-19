package com.agroneo.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.agroneo.app.oauth.AuthActivity;

public class HR extends View {
    public HR(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        setBackgroundColor(Color.parseColor("#EEEEEE"));
    }
}
