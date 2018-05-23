package com.agroneo.app.utils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;

public class RatioImageView extends AppCompatImageView {

    public RatioImageView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setAdjustViewBounds(true);
    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable != null) {
            Rect bound = drawable.getBounds();
            setRatio(((float) (bound.bottom - bound.top)) / ((float) (bound.right - bound.left)));
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setRatio(((float) bm.getWidth()) / ((float) bm.getHeight()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setRatio(float ratio) {
        requestLayout();
        getLayoutParams().height = (int) (getMeasuredWidth() * ratio);
    }

}