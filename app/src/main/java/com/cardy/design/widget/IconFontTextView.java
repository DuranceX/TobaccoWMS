package com.cardy.design.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.cardy.design.R;

public class IconFontTextView extends AppCompatTextView {
    public IconFontTextView(@NonNull Context context) {
        super(context);
        Init(context);
    }

    public IconFontTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public IconFontTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    public void Init(Context context){
        Typeface icon = Typeface.createFromAsset(context.getAssets(),"iconfont.ttf");
        setTypeface(icon);
    }
}
