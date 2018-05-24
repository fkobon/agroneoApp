package com.agroneo.app.utils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;

import com.agroneo.app.R;

public class RatioImageView extends AppCompatImageView {

    private float ratio;

    public RatioImageView(Context context, float ratio) {
        super(context);
        this.ratio = ratio;

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setImageResource(R.drawable.loading_anim);

    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int newWidth = getMeasuredWidth();
        int newHeight = (int) (newWidth * ratio);

        setMeasuredDimension(newWidth, newHeight);
    }

}