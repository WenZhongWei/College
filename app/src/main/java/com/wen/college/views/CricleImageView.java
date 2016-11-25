package com.wen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wen.college.R;

/**
 * Created by Administrator on 2016/8/22.
 * 自定义圆形图片
 */

public class CricleImageView extends ImageView {

    private int width, heigth;
    private int inStroke, outStroke;//内外边框
    private int size;
    private Paint p;

    public CricleImageView(Context context) {
        super(context);
        init(null);
    }

    public CricleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CricleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CricleImageView);
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int index = a.getIndex(i);
                switch (index) {
                    case R.styleable.CricleImageView_cimg_inStrokeWidth:
                        inStroke = a.getDimensionPixelSize(index, 0);
                        break;
                    case R.styleable.CricleImageView_cimg_outStrokeWidth:
                        outStroke = a.getDimensionPixelSize(index, 0);
                        break;
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        heigth = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap srcBmp = ((BitmapDrawable) drawable).getBitmap();
            if (srcBmp != null) {
                srcBmp = createCricleBitmap(srcBmp);
                Rect src = new Rect(0, 0, size, size);
                Rect dst = new Rect((width - size) / 2, (heigth - size) / 2, (width + size) / 2, (heigth + size) / 2);
                canvas.drawBitmap(srcBmp, src, dst, null);
                p = new Paint();
                p.setColor(Color.WHITE);
                p.setAntiAlias(true);
                p.setStrokeWidth(inStroke);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(width / 2, heigth / 2, (size + inStroke) / 2, p);
                p.setARGB(150, 255, 255, 255);
                canvas.drawCircle(width / 2, heigth / 2, (size + 2 * inStroke + outStroke) / 2, p);
            } else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }

    }

    //画圆的方法
    private Bitmap createCricleBitmap(Bitmap srcBmp) {
        size = (width > heigth ? heigth : width) - 2 * (inStroke + outStroke);
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(size / 2, size / 2, size / 2, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect src = new Rect(0, 0, srcBmp.getWidth(), srcBmp.getHeight());
        Rect dst = new Rect(0, 0, size, size);
        canvas.drawBitmap(srcBmp, src, dst, p);
        return output;
    }

    public int getInStroke() {
        return inStroke;
    }

    public void setInStroke(int inStroke) {
        this.inStroke = inStroke;
    }

    public int getOutStroke() {
        return outStroke;
    }

    public void setOutStroke(int outStroke) {
        this.outStroke = outStroke;
    }
}
