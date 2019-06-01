package com.example.myride.customLayoutclass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class    RoundedImageView extends ImageView {


    private float radius = 25.0f;
    private Path path;
    private RectF rect;

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radii=this.getHeight()/2;

        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
            path.addRoundRect(rect, radii, radii, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
