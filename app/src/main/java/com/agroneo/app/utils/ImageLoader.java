package com.agroneo.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageLoader {

    public static void setRound(String url, View imageView, int dpRessource) {
        Picasso.get()
                .load(url)
                .resizeDimen(dpRessource, dpRessource)
                .transform(new CircleTransform())
                .into(ImageView.class.cast(imageView));

    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
            source.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
