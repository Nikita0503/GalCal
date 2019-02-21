package com.mydomain.galcal;

/**
 * Created by Nikita on 21.02.2019.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.squareup.picasso.Transformation;

public class UnlockedTransformation implements Transformation {


    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        final Paint paint1 = new Paint();
        //paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        paint1.setStrokeWidth(10);
        paint1.setColor(Color.rgb(184, 233, 134));
        final Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        canvas.drawRect(1, 1, source.getWidth()-1, source.getHeight()-1, paint1);
        canvas.drawRect(11, 11, source.getWidth()-10, source.getHeight()-10, paint);

        if (source != output)
            source.recycle();

        return output;
    }

    @Override
    public String key() {
        return "circle";
    }
}
